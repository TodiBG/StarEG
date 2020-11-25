package fr.istic.mob.stareg.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import androidx.core.app.NotificationCompat;

import fr.istic.mob.stareg.R;

/**
 * Has to manage the downloading progress bar
 *  @Author Bonaventure Gbehe - Rebecca Ehua
 */
public class ProgressBar {

    NotificationCompat.Builder builder;
    private Context context;
    private Handler handler;
    private NotificationManager manager;
    private String title;
    private String desc;

    public ProgressBar(Context context, String title, int maxProgress) {
        notify(context, title, maxProgress);
        this.title = title;

        System.out.println("new Progress bar");
    }


    public NotificationCompat.Builder getBuilder() {
        return this.builder;
    }

    public NotificationManager getNotifiationManager() {
        return this.manager;
    }

    public void notify(final Context context, String title, int maxProgress) {
        System.out.println("Progress bar Notification");
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final String channelName = "task_name";
        final String channelId = "task_channel";
        builder = new NotificationCompat.Builder(context, channelId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }

        builder.setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true)
                .setProgress(maxProgress, 0, false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        manager.notify(1, builder.build());

    }


}
