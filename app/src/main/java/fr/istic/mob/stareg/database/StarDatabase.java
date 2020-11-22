package fr.istic.mob.stareg.database;



import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.istic.mob.stareg.database.modeles.CalendarEntity;
import fr.istic.mob.stareg.database.modeles.RouteEntity;
import fr.istic.mob.stareg.database.modeles.StopEntity;
import fr.istic.mob.stareg.database.modeles.StopTimeEntity;
import fr.istic.mob.stareg.database.modeles.TripEntity;


@Database(entities = {RouteEntity.class, TripEntity.class, StopEntity.class, StopTimeEntity.class, CalendarEntity.class}, version = 1, exportSchema = false)
public abstract class StarDatabase extends RoomDatabase {

    public static final String DB_NAME = "starge";
    private static StarDatabase instance;

    public static synchronized StarDatabase getInstance(Context theContext) {
        // singleton pattern
        if (instance == null) {
            instance = Room.databaseBuilder(theContext.getApplicationContext(), StarDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static void clear() {
        instance = null;
    }

    //public abstract RouteDao routeDao();

    //public abstract TripDao tripDao();

    //public abstract StopDao stopDao();

    //public abstract StopTimeDao stopTimeDao();

    //public abstract CalendarDao calendarDao();

    @Override
    public void clearAllTables() {
    }
}

