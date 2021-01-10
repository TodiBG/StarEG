package fr.istic.mob.stareg.workers;

import android.app.NotificationManager;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;

import fr.istic.mob.stareg.MainActivity;
import fr.istic.mob.stareg.R;
import fr.istic.mob.stareg.database.Database;
import fr.istic.mob.stareg.database.modeles.CalendarEntity;
import fr.istic.mob.stareg.database.modeles.RouteEntity;
import fr.istic.mob.stareg.database.modeles.StopEntity;
import fr.istic.mob.stareg.database.modeles.StopTimeEntity;
import fr.istic.mob.stareg.database.modeles.TripEntity;
import fr.istic.mob.stareg.others.Constants;

/**
 *  Has to fill database"
 *  *  @Author Bonaventure Gbehe - Rebecca Ehua
 */

public class DbFiller extends Worker {

    private Context context;
    private ProgressBar progressBar;

    public DbFiller(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        progressBar = new ProgressBar(context, context.getString(R.string.db_filling_status), 100);
    }

    @NonNull
    @Override
    public Result doWork() {
        progressBar.getBuilder().setProgress(100, 100, true);
        progressBar.getNotifiationManager().notify(1, progressBar.getBuilder().build());
        //Delete all data in the database of the previous data before inserting new data
        Database.getInstance(context).routeDao().deleteAll();
        fillDatabase();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        return Result.success();
    }
    private void clerDatabase() {

    }

    private void fillDatabase() {

        FilesReader filesReader = new FilesReader(context.getExternalFilesDir(null).toString());

        ArrayList<RouteEntity> routeEntities = (ArrayList<RouteEntity>) filesReader.getEntitiesFromFile(Constants.ROUTES_FILE, RouteEntity.class);
        Database.getInstance(context).routeDao().insertAll(routeEntities);

        ArrayList<TripEntity> tripEntities = (ArrayList<TripEntity>) filesReader.getEntitiesFromFile(Constants.TRIPS_FILE, TripEntity.class);
        Database.getInstance(context).tripDao().insertAll(tripEntities);

        ArrayList<CalendarEntity> calendarEntities = (ArrayList<CalendarEntity>) filesReader.getEntitiesFromFile(Constants.CALENDAR_FILE, CalendarEntity.class);
        Database.getInstance(context).calendarDao().insertAll(calendarEntities);

        ArrayList<StopEntity> stopEntities = (ArrayList<StopEntity>) filesReader.getEntitiesFromFile(Constants.STOPS_FILE, StopEntity.class);
        Database.getInstance(context).stopDao().insertAll(stopEntities);

        ArrayList<StopTimeEntity> stopTimeEntities = (ArrayList<StopTimeEntity>) filesReader.getEntitiesFromFile(Constants.STOP_TIME_FILE, StopTimeEntity.class);
        Database.getInstance(context).stopTimeDao().insertAll(stopTimeEntities);

       ;

    }

}
