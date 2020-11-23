package fr.istic.mob.stareg.database.modeles;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import fr.istic.mob.stareg.database.StarContract;

/**
 * stop table .
 *
 * @Version 1.0
 * @Author Bonaventure Gbehe - Rebecca Ehua
 */
@Entity(tableName = StarContract.Stops.CONTENT_PATH)
public class StopEntity {

    @NonNull
    @PrimaryKey
    private String _id;

    @ColumnInfo(name = StarContract.Stops.StopColumns.NAME)
    private String stop_name;

    @ColumnInfo(name = StarContract.Stops.StopColumns.DESCRIPTION)
    private String stop_desc;

    @ColumnInfo(name = StarContract.Stops.StopColumns.LATITUDE)
    private String stop_lat;

    @ColumnInfo(name = StarContract.Stops.StopColumns.LONGITUDE)
    private String stop_lon;

    @ColumnInfo(name = StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING)
    private String wheelchair_boarding;

    /**
     * Constructor.
     *
     * @param _id                 stop's identifier
     * @param stop_name           stop's name
     * @param stop_desc           stop's description
     * @param stop_lat            stop's latitude
     * @param stop_lon            stop's longitude
     * @param wheelchair_boarding 0 if false, 1 otherwise
     */
    public StopEntity(@NonNull String _id, String stop_name, String stop_desc, String stop_lat, String stop_lon, String wheelchair_boarding) {
        this._id = _id;
        this.stop_name = stop_name;
        this.stop_desc = stop_desc;
        this.stop_lat = stop_lat;
        this.stop_lon = stop_lon;
        this.wheelchair_boarding = wheelchair_boarding;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String id) {
        this._id = _id;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    public String getStop_desc() {
        return stop_desc;
    }

    public void setStop_desc(String stop_desc) {
        this.stop_desc = stop_desc;
    }

    public String getStop_lat() {
        return stop_lat;
    }

    public void setStop_lat(String stop_lat) {
        this.stop_lat = stop_lat;
    }

    public String getStop_lon() {
        return stop_lon;
    }

    public void setStop_lon(String stop_lon) {
        this.stop_lon = stop_lon;
    }

    public String getWheelchair_boarding() {
        return wheelchair_boarding;
    }

    public void setWheelchair_boarding(String wheelchair_boarding) {
        this.wheelchair_boarding = wheelchair_boarding;
    }
}
