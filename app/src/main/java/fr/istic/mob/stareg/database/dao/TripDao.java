package fr.istic.mob.stareg.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.stareg.database.StarContract;
import fr.istic.mob.stareg.database.modeles.TripEntity;

/**
 * @Version 1.0
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */
@Dao
public interface TripDao {
    /**
     *  Select all element in TripEntity table
     */
    @Query("Select * from " + StarContract.Trips.CONTENT_PATH)
    List<TripEntity> getTripsList();

    /**
     * A big join request which returns
     * select distinct trip.trip_headsign, trip.direction_id, trip.route_id
     * from trip
     * where trip.route_id = :route_id;
     *
     * @param routeId id of the chosen route
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.Trips.TripColumns.HEADSIGN + ", "
            + StarContract.Trips.TripColumns.DIRECTION_ID + ", "
            + StarContract.Trips.TripColumns.ROUTE_ID +
            " FROM " + StarContract.Trips.CONTENT_PATH +
            " WHERE " + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId")
    Cursor getTripsListCursor(String routeId);

    /**
     * multiple insertion
     * @param tripEntities
     */
    @Insert
    void insertAll(ArrayList<TripEntity> tripEntities);

    /**
     * deletion
     */
    @Query("DELETE FROM " + StarContract.Trips.CONTENT_PATH)
    void deleteAll();
}
