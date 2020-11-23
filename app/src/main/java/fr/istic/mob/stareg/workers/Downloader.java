package fr.istic.mob.stareg.workers;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;


import fr.istic.mob.stareg.R;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Has to  downloaded data
 *  @Author Bonaventure Gbehe - Rebecca Ehua
 */
public class Downloader extends Worker {

    private Context context;
    private WorkerParameters workerParams;
    private DownloadManager downloadManager;
    private long downloadId;
    private DownloadProgress downloadProgress;
    private NotificationManager manager;


    public Downloader(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        System.out.println("Downloader");
        this.context = context;
        this.workerParams = workerParams;
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downloadProgress = new DownloadProgress(context, context.getString(R.string.timetables_download_status), 100, true);
    }

    /**
      Executes the file unzipping process end the of the download process.
     */
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadId == id) {
                OneTimeWorkRequest saveRequest = new OneTimeWorkRequest.Builder(Unziper.class)
                                .build();
                WorkManager.getInstance(getApplicationContext())
                        .enqueue(saveRequest);
            }
        }
    };




    @NonNull
    @Override
    public Result doWork() {
        downloadFile(workerParams.getInputData().getString("uri"));
        showProgress();

        return Result.success();
    }

    private void downloadFile(String uri) {
        File file = new File(context.getExternalFilesDir(null), "starTimetables");
        if (uri != null) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                    .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
            downloadManager = (DownloadManager) getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
            downloadId = downloadManager.enqueue(request);
        }
    }

    private void showProgress() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean downloading = true;
                int bytesDownloaded = 0;
                int bytesTotal = 0;
                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(q);
                    if (cursor != null && cursor.moveToFirst()) {
                        bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;
                        }
                    }

                    final double downloadProgress = (int) ((bytesDownloaded * 100l) / bytesTotal);

                    Downloader.this.downloadProgress.getBuilder().setProgress(100, (int) downloadProgress, false);
                    Downloader.this.downloadProgress.getNotifiationManager().notify(1, Downloader.this.downloadProgress.getBuilder().build());

                    cursor.close();
                }
                downloadProgress.getNotifiationManager().cancel(1);
                context.unregisterReceiver(onDownloadComplete);
            }

        }).start();
    }

}
