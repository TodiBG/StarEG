package fr.istic.mob.stareg.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.istic.mob.stareg.MainActivity;
import fr.istic.mob.stareg.R;
import fr.istic.mob.stareg.others.Constants;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * This class observes Star API to download the new available json data
 *  @Author Bonaventure Gbehe - Rebecca Ehua
 */
public class StarAPIObserver extends Worker {

    private String oldData;
    private JSONObject starJson;
    private String zipUri;
    private SharedPreferences prefs;


    public StarAPIObserver(Context context, WorkerParameters params) {
        super(context, params);
        prefs = getApplicationContext().getSharedPreferences("fr.istic.mob.stareg", Context.MODE_PRIVATE);
        oldData = prefs.getString("oldData", null);
        starJson = new JSONObject();
        prefs.edit().putBoolean("newDataAvailable", false).apply();

    }

    @Override
    public Result doWork() {
        System.out.println("Worker is working  ....");
        Result result;
        try {
            starJson = getJson(Constants.URL);
            JSONArray jsonArray = starJson.optJSONArray("records");
            if (!starJson.toString().equals(oldData)) {

                    long currentDate = new Date().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date stringDate1Start = dateFormat.parse(jsonArray.getJSONObject(0).getJSONObject("fields").getString("debutvalidite"));
                    Date stringDate1End  = dateFormat.parse(jsonArray.getJSONObject(0).getJSONObject("fields").getString("finvalidite") );

                    Date stringDate2Start  = dateFormat.parse(jsonArray.getJSONObject(1).getJSONObject("fields").getString("debutvalidite")) ;
                    Date stringDate2End  = dateFormat.parse(jsonArray.getJSONObject(1).getJSONObject("fields").getString("finvalidite")) ;

                    System.out.println("!starJson.toString().equals(oldData) == in == "+!starJson.toString().equals(oldData));

                    long date1Start = stringDate1Start.getTime();
                    long date1End = stringDate1End.getTime();
                    long date2Start = stringDate2Start.getTime();
                    long date2End = stringDate2End.getTime();

                    zipUri = jsonArray.getJSONObject(0).getJSONObject("fields").getString("url");

                    if (date1Start <= currentDate && currentDate <= date1End  ){
                        zipUri = jsonArray.getJSONObject(0).getJSONObject("fields").getString("url");
                    }else if (date2Start <= currentDate && currentDate <= date2End  ){
                        zipUri = jsonArray.getJSONObject(1).getJSONObject("fields").getString("url");
                    }else{
                        //Toast.makeText(MainActivity.class, MainActivity.JSON_NOT_FOUND,Toast.LENGTH_SHORT) ;
                    }

                } catch (ParseException e) { e.printStackTrace(); }

                if (oldData == null) {
                        System.out.println("oldData is null  ....");
                        oldData = starJson.toString();
                        prefs.edit().putString("oldData", oldData).apply();
                        prefs.edit().putBoolean("newDataAvailable", true).apply();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("zipUri", zipUri);
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }
                else {
                    System.out.println("oldData is not null  ....");
                    oldData = starJson.toString();
                    prefs.edit().putString("oldData", oldData).apply();
                    notify(getApplicationContext().getString(R.string.app_name), getApplicationContext().getString(R.string.new_timetables_notification_desc));
                    prefs.edit().putBoolean("newDataAvailable", true).apply();
                }
                    prefs.edit().putString("zipUri", zipUri).apply();



            } else {
                prefs.edit().putBoolean("newDataAvailable", false).apply();
                zipUri = prefs.getString("zipUri", null);
            }
            result = Result.success();
        } catch (JSONException e) {
            System.out.println("zzzzzzzzzzzzz");
            e.printStackTrace();
            result = Result.failure();
        }

        return result;

    }

    /**
     * This methode returns a json object from a given url.
     * @param givenUrl the url to the json
     */
    private JSONObject getJson(String givenUrl) throws JSONException {
        HttpURLConnection urlConnection;
        String line = "";
        StringBuilder jsonString = new StringBuilder();

        try {
            URL url = new URL(givenUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();
        } catch (Exception e) { e.printStackTrace(); }

        return new JSONObject(jsonString.toString());
    }



    private void notify(String NootificationTitle, String message) {
        System.out.println("Notification");
        String canalId = "task_channel";
        String canallName = "task_name";
        System.out.println("zipUri : "+zipUri);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(canalId, canallName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), canalId)
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
