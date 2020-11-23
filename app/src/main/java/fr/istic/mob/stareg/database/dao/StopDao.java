package fr.istic.mob.stareg.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.stareg.database.StarContract;
import fr.istic.mob.stareg.database.modeles.StopEntity;

/**
 * @Version 1.0
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */
@Dao
public interface StopDao {

    @Query("Select * from " + StarContract.Stops.CONTENT_PATH)
    List<StopEntity> getStopList();

    @Query("Select * from " + StarContract.Stops.CONTENT_PATH)
    Cursor getStopListCursor();


    /**
     * A big join request which returns all the stops by routes and the headsign of the trip.
     *
     * select distinct stop.stop_name, stop.stop_id, trip.trip_headsign, trip.direction_id
     * from stop, stoptime, trip
     * where trip._id = stoptime.trip_id
     * and stoptime.stop_id = stop._id
     * and trip.route_id = :route_id
     * and trip.direction_id = :direction_id
     * order by stoptime.departure_time;
     *
     * @param routeId     id of the chosen route
     * @param directionId 0 or 1
     * @return a cursor
     */
    @Query("SELECT DISTINCT " +
            StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns.NAME + ", " +
            StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID + ", " +
            StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.HEADSIGN + ", " +
            StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID +
            " FROM " + StarContract.Stops.CONTENT_PATH + ", " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Trips.CONTENT_PATH +
            " WHERE " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + " = " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID +
            " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID +
            " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId" +
            " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = :directionId" +
            " ORDER BY " + StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME)
    Cursor getStopsByLines(String routeId, String directionId);

    @Query("SELECT DISTINCT " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns.NAME +
            " FROM " + StarContract.Stops.CONTENT_PATH +
            " WHERE " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns.NAME + " LIKE :char_sequence || '%' ORDER BY " + StarContract.Stops.StopColumns.NAME + " ASC")
    Cursor getSearchedStops(String char_sequence);

    @Insert
    void insertAll(ArrayList<StopEntity> stopEntities);

    @Query("DELETE FROM " + StarContract.Stops.CONTENT_PATH)
    void deleteAll();
}
