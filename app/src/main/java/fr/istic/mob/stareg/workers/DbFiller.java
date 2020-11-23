package fr.istic.mob.stareg.workers;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;

import fr.istic.mob.stareg.R;
import fr.istic.mob.stareg.database.StarDatabase;
import fr.istic.mob.stareg.database.modeles.CalendarEntity;
import fr.istic.mob.stareg.database.modeles.RouteEntity;
import fr.istic.mob.stareg.database.modeles.StopEntity;
import fr.istic.mob.stareg.database.modeles.StopTimeEntity;
import fr.istic.mob.stareg.database.modeles.TripEntity;
import fr.istic.mob.stareg.others.Constants;

public class DbFiller extends Worker {

    private Context context;
    private DownloadProgress downloadProgress;

    public DbFiller(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        downloadProgress = new DownloadProgress(context, context.getString(R.string.db_filling_status), 100, false);
    }

    @NonNull
    @Override
    public Result doWork() {
        downloadProgress.getBuilder().setProgress(100, 100, true);
        downloadProgress.getNotifiationManager().notify(1, downloadProgress.getBuilder().build());
        clearDatabase();
        fillDatabase();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        return Result.success();
    }

    /**
     * Clears the database of the previous data
     */
    private void clearDatabase() {
        StarDatabase.getInstance(context).routeDao().deleteAll();
    }

    /**
     * Has to fill the database with the new unziped data
     *  @Author Bonaventure Gbehe - Rebecca Ehua
     */
    private void fillDatabase() {

        FilesReader filesReader = new FilesReader(context.getExternalFilesDir(null).toString());

        ArrayList<RouteEntity> routeEntities = (ArrayList<RouteEntity>) filesReader.readEntitiesFromFile(Constants.ROUTES_FILE, RouteEntity.class);
        StarDatabase.getInstance(context).routeDao().insertAll(routeEntities);

        ArrayList<TripEntity> tripEntities = (ArrayList<TripEntity>) filesReader.readEntitiesFromFile(Constants.TRIPS_FILE, TripEntity.class);
        StarDatabase.getInstance(context).tripDao().insertAll(tripEntities);

        ArrayList<CalendarEntity> calendarEntities = (ArrayList<CalendarEntity>) filesReader.readEntitiesFromFile(Constants.CALENDAR_FILE, CalendarEntity.class);
        StarDatabase.getInstance(context).calendarDao().insertAll(calendarEntities);

        ArrayList<StopEntity> stopEntities = (ArrayList<StopEntity>) filesReader.readEntitiesFromFile(Constants.STOPS_FILE, StopEntity.class);
        StarDatabase.getInstance(context).stopDao().insertAll(stopEntities);

        ArrayList<StopTimeEntity> stopTimeEntities = (ArrayList<StopTimeEntity>) filesReader.readEntitiesFromFile(Constants.STOP_TIME_FILE, StopTimeEntity.class);
        StarDatabase.getInstance(context).stopTimeDao().insertAll(stopTimeEntities);
    }

}