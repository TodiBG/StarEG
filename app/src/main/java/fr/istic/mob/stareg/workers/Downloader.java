package fr.istic.mob.stareg.workers;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;


import fr.istic.mob.stareg.MainActivity;
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
    private ProgressBar progressBar;
    private static final String PROGRESS = "PROGRESS";



    public Downloader(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        System.out.println("Downloader");
        this.context = context;
        this.workerParams = workerParams;
        context.registerReceiver(downloadFinished, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        progressBar = new ProgressBar(context, context.getString(R.string.download_state), 100);
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
    }



    @NonNull
    @Override
    public Result doWork() {
        downloadFile(workerParams.getInputData().getString("uri"));

        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean download_ok = false;
                int downloadedData = 0;
                int downloadProgress = 0 ;
                int totalData = 0;
                while (!download_ok) {

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor != null && cursor.moveToFirst()) {

                        downloadedData = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                        totalData = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            download_ok = true;
                        }
                    }

                    System.out.println("downloadProgress =  (int) ((downloadedData * 100) / totalData);");
                     downloadProgress =  (int) ((downloadedData * 100) / totalData);

                    Downloader.this.progressBar.getBuilder().setProgress(100, downloadProgress, false);
                    Downloader.this.progressBar.getNotifiationManager().notify(1, Downloader.this.progressBar.getBuilder().build());

                    setProgressAsync(new Data.Builder().putInt(PROGRESS, downloadProgress).build());
                    cursor.close();
                }
                progressBar.getNotifiationManager().cancel(1);
                //context.unregisterReceiver(downloadFinished);
            }

        }).start();
        return Result.success();
    }

    private void downloadFile(String uri) {
        System.out.println("Downloading file ... ");
        File file = new File(context.getExternalFilesDir(null), "starAPItables");
        if (uri != null) {
            System.out.println("uri != null ");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverRoaming(true)
                    .setAllowedOverMetered(true);
            downloadManager = (DownloadManager) getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
            downloadId = downloadManager.enqueue(request);
        }
    }


    private BroadcastReceiver downloadFinished = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("downloadFinished ++++++++++++ ");
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) {
                OneTimeWorkRequest saveRequest = new OneTimeWorkRequest.Builder(Unziper.class)
                        .build();
                WorkManager.getInstance(getApplicationContext()).enqueue(saveRequest);

                MainActivity.download_info.setVisibility(View.VISIBLE);
            }
        }
    };


}
