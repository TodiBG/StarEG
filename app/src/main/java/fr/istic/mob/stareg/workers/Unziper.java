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

    DownloadProgress downloadProgress;
    private String zipFilePath;
    private String LocationOfTheTarget;
    private Context context;

    public Unziper(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.zipFilePath = context.getExternalFilesDir(null) + "/starTimetables";
        this.LocationOfTheTarget = context.getExternalFilesDir(null) + "/starTimetables";
    }

    @NonNull
    @Override
    public Result doWork() {

        unzip(zipFilePath, LocationOfTheTarget);

        return Result.success();
    }

    /**
     * @param zipFilePath  the path to zip file
     * @param LocationOfTheTarget the location of the zip file
     */
    private void unzip(String zipFilePath, String LocationOfTheTarget) {
        int zipSize;
        int unzipProgress = 0;

        createDir(LocationOfTheTarget);

        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            zipSize = zipFile.size();
            downloadProgress = new DownloadProgress(context, context.getString(R.string.timetables_unzip_status), zipSize, false);
            downloadProgress.getNotifiationManager().notify(1, downloadProgress.getBuilder().build());
            FileInputStream fin = new FileInputStream(zipFilePath);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    createDir(ze.getName());
                } else {
                    unzipProgress++;
                    showProgress(zipSize, unzipProgress);
                    FileOutputStream fout = new FileOutputStream(LocationOfTheTarget + ze.getName());
                    streamCopy(zin, fout);

                    zin.closeEntry();
                    fout.close();

                }
            }
            zin.close();
            downloadProgress.getNotifiationManager().cancel(1);
            startDbLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To start the db loading process
     */
    private void startDbLoading() {
        OneTimeWorkRequest saveRequest =
                new OneTimeWorkRequest.Builder(DbFiller.class)
                        .build();
        WorkManager.getInstance(getApplicationContext())
                .enqueue(saveRequest);
    }

    /**
     * Makes a copy of the {@link InputStream} into {@link OutputStream}.
     * This enables to read and write in larger chunks which makes the unzipping process quicker.
     *
     * @param in  the {@link InputStream}
     * @param out the {@link OutputStream}
     * @throws IOException
     */
    private void streamCopy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[32 * 1024]; // play with sizes..
        int readCount;
        while ((readCount = in.read(buffer)) != -1) {
            out.write(buffer, 0, readCount);
        }
    }

    /**
     * Creates a directory at the targeted location
     *
     * @param targetPath the path to create the file
     */
    private void createDir(String targetPath) {
        File file = new File(targetPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * Updates the progress in the progress bar
     */
    private void showProgress(int zipSize, int unzipProgress) {
        downloadProgress.getBuilder().setProgress(zipSize, unzipProgress, false);
        downloadProgress.getNotifiationManager().notify(1, downloadProgress.getBuilder().build());
    }
}
