package fr.istic.mob.stareg.database.modeles;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import fr.istic.mob.stareg.database.StarContract;

/**
 * calendar Table.
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */


@Entity(tableName = StarContract.Calendar.CONTENT_PATH)
public class CalendarEntity {

    @NonNull
    @PrimaryKey
    private String _id;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.MONDAY)
    private String monday;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.TUESDAY)
    private String tuesday;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.WEDNESDAY)
    private String wednesday;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.THURSDAY)
    private String thursday;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.FRIDAY)
    private String friday;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.SATURDAY)
    private String saturday;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.SUNDAY)
    private String sunday;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.START_DATE)
    private String start_date;

    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.END_DATE)
    private String end_date;
    
    
    public CalendarEntity(@NonNull String _id, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday, String start_date, String end_date) {
        this._id = _id;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public static CalendarEntity createCalendarEntity(String[] tab) {
        String service_id = tab[0].replace("\"", "");
        String monday = tab[1].replace("\"", "");
        String tuesday = tab[2].replace("\"", "");
        String wednesday = tab[3].replace("\"", "");
        String thursday = tab[4].replace("\"", "");
        String friday = tab[5].replace("\"", "");
        String saturday = tab[6].replace("\"", "");
        String sunday = tab[7].replace("\"", "");
        String start_date = tab[8].replace("\"", "");
        String end_date = tab[9].replace("\"", "");

        return new CalendarEntity(service_id, monday, tuesday, wednesday, thursday, friday, saturday, sunday, start_date, end_date);
    }
}






