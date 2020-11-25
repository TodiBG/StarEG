package fr.istic.mob.stareg.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import fr.istic.mob.stareg.R;
/**
 * Has to unzip downloaded data
 *  @Author Bonaventure Gbehe - Rebecca Ehua
 */

public class Unziper extends Worker {

    ProgressBar progressBar;
    private String zipFilePath;
    private String LocationOfTheTarget;
    private Context context;

    public Unziper(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.zipFilePath = context.getExternalFilesDir(null) + "/starAPItables";
        this.LocationOfTheTarget = context.getExternalFilesDir(null) + "/starAPItables";
    }

    @NonNull
    @Override
    public Result doWork() {
        unzip(zipFilePath, LocationOfTheTarget);
        return Result.success();
    }


    private void unzip(String zipFilePath, String LocationOfTheTarget) {
        int total;
        int progress = 0;

        makeDirectory(LocationOfTheTarget);

        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            total = zipFile.size();
            progressBar = new ProgressBar(context, context.getString(R.string.timetables_unzip_status), total);
            progressBar.getNotifiationManager().notify(1, progressBar.getBuilder().build());
            FileInputStream fin = new FileInputStream(zipFilePath);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    makeDirectory(ze.getName());
                } else {
                    progress++;
                    showProgress(total, progress);
                    FileOutputStream file = new FileOutputStream(LocationOfTheTarget + ze.getName());

                    byte[] buffer = new byte[32 * 1024]; // play with sizes..
                    int readCount;
                        while ((readCount = zin.read(buffer)) != -1) {
                            file.write(buffer, 0, readCount);
                        }
                    zin.closeEntry();
                    file.close();
                }
            }
            zin.close();
            progressBar.getNotifiationManager().cancel(1);
            OneTimeWorkRequest saveRequest = new OneTimeWorkRequest.Builder(DbFiller.class).build();
            WorkManager.getInstance(getApplicationContext()).enqueue(saveRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void makeDirectory(String targetPath) {
        File file = new File(targetPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }


    /**
     * Updates the progress in the progress bar
     */
    private void showProgress(int zipSize, int unzipProgress) {
        progressBar.getBuilder().setProgress(zipSize, unzipProgress, false);
        progressBar.getNotifiationManager().notify(1, progressBar.getBuilder().build());
    }
}
