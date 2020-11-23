package fr.istic.mob.stareg.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.stareg.database.StarContract;
import fr.istic.mob.stareg.database.modeles.RouteEntity;

/**
 * @Version 1.0
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */
@Dao
public interface RouteDao {

    @Query("Select * from " + StarContract.BusRoutes.CONTENT_PATH)
    List<RouteEntity> getRouteList();

    /**
     * Select distinct busroute._id, busroute.route_short_name, busroute.route_long_name, busroute.route_color, busroute.route_text_color
     * from busroute
     * order by busroute._id;
     *
     * @return a Cursor which list the result from the database
     */
    @Query("Select distinct " +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + ", " +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns._ID + ", " +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns.COLOR + ", " +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR + ", " +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns.LONG_NAME +
            " from " + StarContract.BusRoutes.CONTENT_PATH +
            " order by " + StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns._ID)
    Cursor getRouteListCursor();

    @Query("SELECT DISTINCT " +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns._ID + "," +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + "," +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns.COLOR + "," +
            StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR +
            " FROM " + StarContract.BusRoutes.CONTENT_PATH + "," + StarContract.Trips.CONTENT_PATH + "," + StarContract.Stops.CONTENT_PATH + "," + StarContract.StopTimes.CONTENT_PATH +
            " WHERE " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns.NAME + "= :stop_name" +
            " AND " + StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns._ID + "= " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID +
            " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + "= " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID +
            " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + "= " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID +
            " ORDER BY " + StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns._ID)
    Cursor getRoutesForStop(String stop_name);

    @Insert
    void insertAll(ArrayList<RouteEntity> dataEntities);

    @Query("DELETE FROM " + StarContract.BusRoutes.CONTENT_PATH)
    void deleteAll();
}
