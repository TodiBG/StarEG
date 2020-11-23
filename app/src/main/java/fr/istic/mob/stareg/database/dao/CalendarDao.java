package fr.istic.mob.stareg.database.dao ;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.stareg.database.StarContract;
import fr.istic.mob.stareg.database.modeles.CalendarEntity;

/**
 * @Version 1.0
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */
@Dao
public interface CalendarDao {
    /**
     * select all in the Calendar table
     * @return
     */
    @Query("Select * from " + StarContract.Calendar.CONTENT_PATH)
    List<CalendarEntity> getCalendarList();


    @Query("Select * from " + StarContract.Calendar.CONTENT_PATH)
    Cursor getCalendarListCursor();



    @Insert
    void insertAll(ArrayList<CalendarEntity> calendarEntities);

    @Query("DELETE FROM " + StarContract.Calendar.CONTENT_PATH)
    void deleteAll();
}
