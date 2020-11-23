package fr.istic.mob.stareg.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.stareg.database.StarContract;
import fr.istic.mob.stareg.database.modeles.StopTimeEntity;

/**
 * @Version 1.0
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */
@Dao
public interface StopTimeDao {

    @Query("Select * from " + StarContract.StopTimes.CONTENT_PATH)
    List<StopTimeEntity> getStopTimeList();

    /**
     * A big join request which returns all the stops times for the chosen route, in the chosen direction,
     * at the chosen stop, from the chosen time until the end of the day.
     *
     * select distinct stoptime.arrival_time, trip._id
     * from stoptime, trip, calendar
     * where stoptime.trip_id = trip._id
     * and trip.service_id = calendar._id
     * and trip.route_id = :route_id
     * and stoptime.stop_id = :stop_id
     * and trip.direction_id = direction_id
     * and calendar.monday = 1
     * and stoptime.arrival_time > : arrival_time
     * group by stoptime.arrival_time
     * order by stoptime.arrival_time;
     *
     * @param routeId     id of the chosen route
     * @param stopId      id of the chosen stop
     * @param directionId the chosen direction
     * @param arrivalTime chosen time
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.HEADSIGN
            + " FROM " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Trips.CONTENT_PATH + ", " + StarContract.Calendar.CONTENT_PATH
            + " WHERE " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.SERVICE_ID + "=" + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = :stopId"
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = :directionId"
            + " AND " + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.MONDAY + " = 1"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > :arrivalTime"
            + " GROUP BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME
            + " ORDER BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
        Cursor getStopTimeCursorMonday(String routeId, String stopId, String directionId, String arrivalTime);

    /**
     * A big join request which returns all the stops times
     * for the chosen route, in the chosen direction,
     * at the chosen stop, from the chosen time until
     * the end of the day.
     * <p>
     * select distinct stoptime.arrival_time, trip._id
     * from stoptime, trip, calendar
     * where stoptime.trip_id = trip._id
     * and trip.service_id = calendar._id
     * and trip.route_id = :route_id
     * and stoptime.stop_id = :stop_id
     * and trip.direction_id = direction_id
     * and calendar.tuesday = 1
     * and stoptime.arrival_time > : arrival_time
     * group by stoptime.arrival_time
     * order by stoptime.arrival_time;
     *
     * @param routeId     id of the chosen route
     * @param stopId      id of the chosen stop
     * @param directionId the chosen direction
     * @param arrivalTime chosen time
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.HEADSIGN + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.START_DATE + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.END_DATE
            + " FROM " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Trips.CONTENT_PATH + ", " + StarContract.Calendar.CONTENT_PATH
            + " WHERE " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.SERVICE_ID + "=" + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = :stopId"
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = :directionId"
            + " AND " + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.TUESDAY + " = 1"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > :arrivalTime"
            + " GROUP BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME
            + " ORDER BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    Cursor getStopTimeCursorTuesday(String routeId, String stopId, String directionId, String arrivalTime);

    /**
     * A big join request which returns all the stops times
     * for the chosen route, in the chosen direction,
     * at the chosen stop, from the chosen time until
     * the end of the day.
     * <p>
     * select distinct stoptime.arrival_time, trip._id
     * from stoptime, trip, calendar
     * where stoptime.trip_id = trip._id
     * and trip.service_id = calendar._id
     * and trip.route_id = :route_id
     * and stoptime.stop_id = :stop_id
     * and trip.direction_id = direction_id
     * and calendar.wenesday = 1
     * and stoptime.arrival_time > : arrival_time
     * group by stoptime.arrival_time
     * order by stoptime.arrival_time;
     *
     * @param routeId     id of the chosen route
     * @param stopId      id of the chosen stop
     * @param directionId the chosen direction
     * @param arrivalTime chosen time
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.HEADSIGN + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.START_DATE + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.END_DATE
            + " FROM " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Trips.CONTENT_PATH + ", " + StarContract.Calendar.CONTENT_PATH
            + " WHERE " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.SERVICE_ID + "=" + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = :stopId"
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = :directionId"
            + " AND " + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.WEDNESDAY + " = 1"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > :arrivalTime"
            + " GROUP BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME
            + " ORDER BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    Cursor getStopTimeCursorWenesday(String routeId, String stopId, String directionId, String arrivalTime);

    /**
     * A big join request which returns all the stops times
     * for the chosen route, in the chosen direction,
     * at the chosen stop, from the chosen time until
     * the end of the day.
     * <p>
     * select distinct stoptime.arrival_time, trip._id
     * from stoptime, trip, calendar
     * where stoptime.trip_id = trip._id
     * and trip.service_id = calendar._id
     * and trip.route_id = :route_id
     * and stoptime.stop_id = :stop_id
     * and trip.direction_id = direction_id
     * and calendar.thursday = 1
     * and stoptime.arrival_time > : arrival_time
     * group by stoptime.arrival_time
     * order by stoptime.arrival_time;
     *
     * @param routeId     id of the chosen route
     * @param stopId      id of the chosen stop
     * @param directionId the chosen direction
     * @param arrivalTime chosen time
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.HEADSIGN + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.START_DATE + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.END_DATE
            + " FROM " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Trips.CONTENT_PATH + ", " + StarContract.Calendar.CONTENT_PATH
            + " WHERE " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.SERVICE_ID + "=" + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = :stopId"
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = :directionId"
            + " AND " + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.THURSDAY + " = 1"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > :arrivalTime"
            + " GROUP BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME
            + " ORDER BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    Cursor getStopTimeCursorThursday(String routeId, String stopId, String directionId, String arrivalTime);

    /**
     * A big join request which returns all the stops times
     * for the chosen route, in the chosen direction,
     * at the chosen stop, from the chosen time until
     * the end of the day.
     * <p>
     * select distinct stoptime.arrival_time, trip._id
     * from stoptime, trip, calendar
     * where stoptime.trip_id = trip._id
     * and trip.service_id = calendar._id
     * and trip.route_id = :route_id
     * and stoptime.stop_id = :stop_id
     * and trip.direction_id = direction_id
     * and calendar.friday = 1
     * and stoptime.arrival_time > : arrival_time
     * group by stoptime.arrival_time
     * order by stoptime.arrival_time;
     *
     * @param routeId     id of the chosen route
     * @param stopId      id of the chosen stop
     * @param directionId the chosen direction
     * @param arrivalTime chosen time
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.HEADSIGN + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.START_DATE + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.END_DATE
            + " FROM " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Trips.CONTENT_PATH + ", " + StarContract.Calendar.CONTENT_PATH
            + " WHERE " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.SERVICE_ID + "=" + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = :stopId"
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = :directionId"
            + " AND " + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.FRIDAY + " = 1"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > :arrivalTime"
            + " GROUP BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME
            + " ORDER BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    Cursor getStopTimeCursorFriday(String routeId, String stopId, String directionId, String arrivalTime);

    /**
     * A big join request which returns all the stops times for the chosen route, in the chosen direction,
     * at the chosen stop, from the chosen time until he end of the day.
     *
     * select distinct stoptime.arrival_time, trip._id
     * from stoptime, trip, calendar
     * where stoptime.trip_id = trip._id
     * and trip.service_id = calendar._id
     * and trip.route_id = :route_id
     * and stoptime.stop_id = :stop_id
     * and trip.direction_id = direction_id
     * and calendar.saturday = 1
     * and stoptime.arrival_time > : arrival_time
     * group by stoptime.arrival_time
     * order by stoptime.arrival_time;
     *
     * @param routeId     id of the chosen route
     * @param stopId      id of the chosen stop
     * @param directionId the chosen direction
     * @param arrivalTime chosen time
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.HEADSIGN + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.START_DATE + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.END_DATE
            + " FROM " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Trips.CONTENT_PATH + ", " + StarContract.Calendar.CONTENT_PATH
            + " WHERE " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.SERVICE_ID + "=" + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = :stopId"
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = :directionId"
            + " AND " + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.SATURDAY + " = 1"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > :arrivalTime"
            + " GROUP BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME
            + " ORDER BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    Cursor getStopTimeCursorSaturday(String routeId, String stopId, String directionId, String arrivalTime);

    /**
     * A big join request which returns all the stops times for the chosen route, in the chosen direction,
     * at the chosen stop, from the chosen time until the end of the day.
     *
     * select distinct stoptime.arrival_time, trip._id
     * from stoptime, trip, calendar
     * where stoptime.trip_id = trip._id
     * and trip.service_id = calendar._id
     * and trip.route_id = :route_id
     * and stoptime.stop_id = :stop_id
     * and trip.direction_id = direction_id
     * and calendar.sunday = 1
     * and stoptime.arrival_time > : arrival_time
     * group by stoptime.arrival_time
     * order by stoptime.arrival_time;
     *
     * @param routeId     id of the chosen route
     * @param stopId      id of the chosen stop
     * @param directionId the chosen direction
     * @param arrivalTime chosen time
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + ", "
            + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.HEADSIGN + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.START_DATE + ", "
            + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.END_DATE
            + " FROM " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Trips.CONTENT_PATH + ", " + StarContract.Calendar.CONTENT_PATH
            + " WHERE " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.SERVICE_ID + "=" + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns._ID
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.ROUTE_ID + " = :routeId"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = :stopId"
            + " AND " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = :directionId"
            + " AND " + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns.SUNDAY + " = 1"
            + " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > :arrivalTime"
            + " GROUP BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME
            + " ORDER BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    Cursor getStopTimeCursorSunday(String routeId, String stopId, String directionId, String arrivalTime);

    /**
     * A big join request which returns
     * select distinct stop.stop_name, stoptime.arrival_time
     * from stop, stoptime
     * where stop._id = stoptime.stop_id
     * and stoptime.trip_id = :tripId
     * and stoptime.arrival_time > :arrivalTime
     * order by stoptime.arrival_time;
     *
     * @param tripId      trip's id
     * @param arrivalTime
     * @return a Cursor which list the result from the database
     */
    @Query("SELECT DISTINCT "
            + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns.NAME + ", "
            + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME +
            " FROM " + StarContract.StopTimes.CONTENT_PATH + ", " + StarContract.Stops.CONTENT_PATH +
            " WHERE " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID + "=" + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.STOP_ID +
            " AND " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = :tripId" +
            " AND " + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > :arrivalTime" +
            " ORDER BY " + StarContract.StopTimes.CONTENT_PATH + "." + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    Cursor getRouteDetail(String tripId, String arrivalTime);

    @Insert
    void insertAll(ArrayList<StopTimeEntity> stopTimeEntities);

    @Query("DELETE FROM " + StarContract.StopTimes.CONTENT_PATH)
    void deleteAll();
}
