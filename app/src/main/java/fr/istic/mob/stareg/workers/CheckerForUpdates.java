package fr.istic.mob.stareg.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.istic.mob.stareg.MainActivity;
import fr.istic.mob.stareg.R;
import fr.istic.mob.stareg.others.Constants;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * This class checks every 20 minutes if an new version of the timetables is available
 *  @Author Bonaventure Gbehe - Rebecca Ehua
 */
public class CheckerForUpdates extends Worker {

    private String oldData;
    private JSONObject json;
    private String zipUri;
    private SharedPreferences prefs;


    /**
     * Constructor of {@link CheckerForUpdates} class
     * @param context the app context
     * @param params the params of the the worker
     */
    public CheckerForUpdates(Context context, WorkerParameters params) {
        super(context, params);
        prefs = getApplicationContext().getSharedPreferences("fr.istic.mob.stareg", Context.MODE_PRIVATE);
        oldData = prefs.getString("oldData", null);
        json = new JSONObject();
        prefs.edit().putBoolean("newTimetablesAvailable", false).apply();

        System.out.println("Worker is working  ....");

    }

    @Override
    public Result doWork() {
        Result result;
        try {
            json = getJsonObjectFromUrl(Constants.URL);
            JSONArray jsonArray = json.optJSONArray("records");

            if (!json.toString().equals(oldData)) {
                try {
                    Date currentDate = new Date();
                    Date beginDateFirstZip = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).parse(jsonArray.getJSONObject(0).getJSONObject("fields").getString("debutvalidite"));
                    Date endDateFirstZip = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).parse(jsonArray.getJSONObject(0).getJSONObject("fields").getString("finvalidite"));
                    Date beginDateSecondZip = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).parse(jsonArray.getJSONObject(1).getJSONObject("fields").getString("debutvalidite"));
                    Date endDateSecondZip = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).parse(jsonArray.getJSONObject(1).getJSONObject("fields").getString("finvalidite"));
                    zipUri = jsonArray.getJSONObject(0).getJSONObject("fields").getString("url");
                    if ((currentDate.equals(beginDateFirstZip) || currentDate.after(beginDateFirstZip)) && (currentDate.equals(endDateFirstZip) || currentDate.before(endDateFirstZip))) {
                        zipUri = jsonArray.getJSONObject(0).getJSONObject("fields").getString("url");
                    } else if ((currentDate.equals(beginDateSecondZip) || currentDate.after(beginDateSecondZip)) && (currentDate.equals(endDateSecondZip) || currentDate.before(endDateSecondZip))) {
                        zipUri = jsonArray.getJSONObject(1).getJSONObject("fields").getString("url");
                    }

                    if (oldData != null) {
                        System.out.println("oldData is not null  ....");
                        oldData = json.toString();
                        prefs.edit().putString("oldData", oldData).apply();
                        showNotification(getApplicationContext().getString(R.string.app_name), getApplicationContext().getString(R.string.new_timetables_notification_desc));
                        prefs.edit().putBoolean("newTimetablesAvailable", true).apply();
                    } else {
                        System.out.println("oldData is null  ....");
                        oldData = json.toString();
                        prefs.edit().putString("oldData", oldData).apply();
                        prefs.edit().putBoolean("newTimetablesAvailable", true).apply();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("zipUri", zipUri);
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }
                    prefs.edit().putString("zipUri", zipUri).apply();

                } catch (Exception e) { e.printStackTrace();}
            } else {
                prefs.edit().putBoolean("newTimetablesAvailable", false).apply();
                zipUri = prefs.getString("zipUri", null);
                System.out.println("json.toString().equals(oldData)");
            }
            result = Result.success();
        } catch (JSONException e) {
            System.out.println("yyyyyyyyyyyyyyyyyyyy");
            e.printStackTrace();
            result = Result.failure();
        }

        return result;

    }

    /**
     * This methode returns a json object from a given url.
     * @param givenUrl the url to the json
     */
    private JSONObject getJsonObjectFromUrl(String givenUrl) throws JSONException {
        HttpURLConnection urlConnection;
        String line;
        StringBuilder jsonString = new StringBuilder();

        try {
            URL url = new URL(givenUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return new JSONObject(jsonString.toString());
    }

    /**
     * notifies users about available new timetables  and invites him to download it to update app.
     * On clicks of the notification message, the app will download the new timetables.
     * @param NootificationTitle the notification tile
     * @param message the notification description
     */
    private void showNotification(String NootificationTitle, String message) {

        System.out.println("Notification");

        System.out.println("zipUri : "+zipUri);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "task_channel";
        String channelName = "task_name";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(NootificationTitle)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Intent downloadIntent = new Intent(getApplicationContext(), MainActivity.class);
        downloadIntent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        downloadIntent.putExtra("zipUri", zipUri);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntent(downloadIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        manager.notify(1, builder.build());

    }
}
