package fr.istic.mob.stareg;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import androidx.room.RoomSQLiteQuery;


import fr.istic.mob.stareg.database.Database;
import fr.istic.mob.stareg.database.StarContract;
import fr.istic.mob.stareg.database.dao.CalendarDao;
import fr.istic.mob.stareg.database.dao.RouteDao;
import fr.istic.mob.stareg.database.dao.StopDao;
import fr.istic.mob.stareg.database.dao.StopTimeDao;
import fr.istic.mob.stareg.database.dao.TripDao;

 /**
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */
public class Provider extends ContentProvider {

    private static final int QUERY_ROUTES = 10;
    private static final int QUERY_STOPS = 20;
    private static final int QUERY_TRIPS = 30;
    private static final int QUERY_STOP_TIMES = 40;
    private static final int QUERY_CALENDAR = 50;
    private static final int QUERY_ROUTES_DETAILS = 60;
    private static final int QUERY_SEARCHED_STOPS = 70;
    private static final int QUERY_ROUTES_FOR_STOP = 80;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH, QUERY_ROUTES);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Trips.CONTENT_PATH, QUERY_TRIPS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Stops.CONTENT_PATH, QUERY_STOPS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.StopTimes.CONTENT_PATH, QUERY_STOP_TIMES);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Calendar.CONTENT_PATH, QUERY_CALENDAR);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.RouteDetails.CONTENT_PATH, QUERY_ROUTES_DETAILS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.SearchedStops.CONTENT_PATH, QUERY_SEARCHED_STOPS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.RoutesForStop.CONTENT_PATH, QUERY_ROUTES_FOR_STOP);
    }

    private Database database;

    @Override
    public boolean onCreate() {
        database = Room.databaseBuilder(getContext(), Database.class, Database.DB_NAME).build();
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor result = null;


        System.out.println(uri);
        int uriMatcher = URI_MATCHER.match(uri);
        if (uriMatcher == QUERY_ROUTES) {
            RouteDao routeDao1 = Database.getInstance(getContext()).routeDao();
            result = routeDao1.getRouteListCursor();
        } else if (uriMatcher == QUERY_TRIPS) {
            TripDao tripDao = Database.getInstance(getContext()).tripDao();
            result = tripDao.getTripsListCursor(selectionArgs[0]);
        } else if (uriMatcher == QUERY_STOPS) {
            StopDao stopDao = Database.getInstance(getContext()).stopDao();
            result = stopDao.getStopsByLines(selectionArgs[0], selectionArgs[1]);
        } else if (uriMatcher == QUERY_STOP_TIMES) {
            if (selectionArgs.length > 2 && !selectionArgs[3].isEmpty()) {
                String dayOfWeek = selectionArgs[3];
                StopTimeDao stopTimeDao = Database.getInstance(getContext()).stopTimeDao();
                if (dayOfWeek.equals(StarContract.Calendar.CalendarColumns.MONDAY)) {
                    result = stopTimeDao.getStopTimeCursorMonday(selectionArgs[0], selectionArgs[1], selectionArgs[2], selectionArgs[4]);
                } else if (dayOfWeek.equals(StarContract.Calendar.CalendarColumns.TUESDAY)) {
                    result = stopTimeDao.getStopTimeCursorTuesday(selectionArgs[0], selectionArgs[1], selectionArgs[2], selectionArgs[4]);
                } else if (dayOfWeek.equals(StarContract.Calendar.CalendarColumns.WEDNESDAY)) {
                    result = stopTimeDao.getStopTimeCursorWenesday(selectionArgs[0], selectionArgs[1], selectionArgs[2], selectionArgs[4]);
                } else if (dayOfWeek.equals(StarContract.Calendar.CalendarColumns.THURSDAY)) {
                    result = stopTimeDao.getStopTimeCursorThursday(selectionArgs[0], selectionArgs[1], selectionArgs[2], selectionArgs[4]);
                } else if (dayOfWeek.equals(StarContract.Calendar.CalendarColumns.FRIDAY)) {
                    result = stopTimeDao.getStopTimeCursorFriday(selectionArgs[0], selectionArgs[1], selectionArgs[2], selectionArgs[4]);
                } else if (dayOfWeek.equals(StarContract.Calendar.CalendarColumns.SATURDAY)) {
                    result = stopTimeDao.getStopTimeCursorSaturday(selectionArgs[0], selectionArgs[1], selectionArgs[2], selectionArgs[4]);
                } else if (dayOfWeek.equals(StarContract.Calendar.CalendarColumns.SUNDAY)) {
                    result = stopTimeDao.getStopTimeCursorSunday(selectionArgs[0], selectionArgs[1], selectionArgs[2], selectionArgs[4]);
                }
            }
        } else if (uriMatcher == QUERY_CALENDAR) {
            CalendarDao calendarDao = Database.getInstance(getContext()).calendarDao();
            result = calendarDao.getCalendarListCursor();
        } else if (uriMatcher == QUERY_ROUTES_DETAILS) {
            StopTimeDao stopTimeDao = Database.getInstance(getContext()).stopTimeDao();
            result = stopTimeDao.getRouteDetail(selectionArgs[0], selectionArgs[1]);
        }
        else if (uriMatcher == QUERY_SEARCHED_STOPS) {
            StopDao stopDao = Database.getInstance(getContext()).stopDao();
            result = stopDao.getSearchedStops(selectionArgs[0]);
        }
        else if (uriMatcher == QUERY_ROUTES_FOR_STOP) {
            RouteDao routeDao2 = Database.getInstance(getContext()).routeDao();
            result = routeDao2.getRoutesForStop(selectionArgs[0]);
        }

        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type = null;
        int uriMatcher = URI_MATCHER.match(uri);
        if (uriMatcher == QUERY_ROUTES) {
            type = StarContract.BusRoutes.CONTENT_ITEM_TYPE;
        } else if (uriMatcher == QUERY_TRIPS) {
            type = StarContract.Trips.CONTENT_ITEM_TYPE;
        } else if (uriMatcher == QUERY_STOPS) {
            type = StarContract.Stops.CONTENT_ITEM_TYPE;
        } else if (uriMatcher == QUERY_STOP_TIMES) {
            type = StarContract.StopTimes.CONTENT_ITEM_TYPE;
        } else if (uriMatcher == QUERY_CALENDAR) {
            type = StarContract.Calendar.CONTENT_ITEM_TYPE;
        } else if (uriMatcher == QUERY_ROUTES_DETAILS) {
            type = StarContract.RouteDetails.CONTENT_ITEM_TYPE;
        } else if(uriMatcher == QUERY_SEARCHED_STOPS) {
            type = StarContract.SearchedStops.CONTENT_ITEM_TYPE;
        }
        else if(uriMatcher == QUERY_ROUTES_FOR_STOP) {
            type = StarContract.RoutesForStop.CONTENT_ITEM_TYPE;
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
