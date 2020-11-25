package fr.istic.mob.stareg.workers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import fr.istic.mob.stareg.database.modeles.CalendarEntity;
import fr.istic.mob.stareg.database.modeles.RouteEntity;
import fr.istic.mob.stareg.database.modeles.StopEntity;
import fr.istic.mob.stareg.database.modeles.StopTimeEntity;
import fr.istic.mob.stareg.database.modeles.TripEntity;


/**
 * Has to read data from file and create entities.
 *
 */
public class FilesReader {

    private String path;

    public FilesReader(String path) {
        this.path = path;
    }

    public ArrayList<?> readEntitiesFromFile(String fileName, Class type) {
        ArrayList<Object> entities = new ArrayList<>();
        try {
            File f = new File(path + File.separator + File.separator + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line = reader.readLine();

            while (line != null) {
                String[] fields = line.split(",");
                if (type.equals(TripEntity.class)) {
                    TripEntity tripEntity = TripEntity.createTripEntity(fields);
                    entities.add(tripEntity);
                } else if (type.equals(RouteEntity.class)) {
                    RouteEntity routeEntity = RouteEntity.createRouteEntity(fields);
                    entities.add(routeEntity);
                } else   if (type.equals(StopEntity.class)) {
                    StopEntity stopEntity = StopEntity.createStopEntity(fields);
                    entities.add(stopEntity);
                } else if (type.equals(CalendarEntity.class)) {
                    CalendarEntity calendarEntity = CalendarEntity.createCalendarEntity(fields);
                    entities.add(calendarEntity);
                } else if (type.equals(StopTimeEntity.class)) {
                    StopTimeEntity stopTimeEntity = StopTimeEntity.createStopTimeEntity(fields);
                    entities.add(stopTimeEntity);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }


}
