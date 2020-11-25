package fr.istic.mob.stareg.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.istic.mob.stareg.database.dao.CalendarDao;
import fr.istic.mob.stareg.database.dao.RouteDao;
import fr.istic.mob.stareg.database.dao.StopDao;
import fr.istic.mob.stareg.database.dao.StopTimeDao;
import fr.istic.mob.stareg.database.dao.TripDao;
import fr.istic.mob.stareg.database.modeles.CalendarEntity;
import fr.istic.mob.stareg.database.modeles.RouteEntity;
import fr.istic.mob.stareg.database.modeles.StopEntity;
import fr.istic.mob.stareg.database.modeles.StopTimeEntity;
import fr.istic.mob.stareg.database.modeles.TripEntity;

/**
 * The database
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */
@androidx.room.Database(entities = {RouteEntity.class, TripEntity.class, StopEntity.class, StopTimeEntity.class, CalendarEntity.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public static final String DB_NAME = "stareg";
    private static Database instance;

    public static synchronized Database getInstance(Context theContext) {

        if (instance == null) {
            instance = Room.databaseBuilder(theContext.getApplicationContext(), Database.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static void clear() {
        instance = null;
    }

    public abstract TripDao tripDao();
    public abstract StopDao stopDao();
    public abstract RouteDao routeDao();
    public abstract StopTimeDao stopTimeDao();
    public abstract CalendarDao calendarDao();

    @Override
    public void clearAllTables() {
    }
}

