/**
 * This class provides convenience methods for saving and retrieving {@code Movement} objects
 * from the database.
 * */

package com.andela.movit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andela.movit.models.Movement;
import com.andela.movit.models.Visit;
import com.andela.movit.utilities.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbRepo extends SQLiteOpenHelper {

    public static final String TABLE = "activities";

    public static final String ID = "_id";

    public static final String ACT_NAME = "act_name";

    public static final String PLC_NAME = "plc_name";

    public static final String TIMESTAMP = "timestamp";

    public static final String LATITUDE = "latitude";

    public static final String LONGITUDE = "longitude";

    public static final String DURATION = "duration";

    private static final String DATABASE = "movements.db";

    private static final int VERSION = 1;

    private static final String CREATE = "CREATE TABLE "
            + TABLE + " ("
            + ID + " INTEGER PRIMARY KEY, "
            + ACT_NAME + " TEXT, "
            + PLC_NAME + " TEXT, "
            + TIMESTAMP + " BIGINT, "
            + LATITUDE + " FLOAT, "
            + LONGITUDE + " FLOAT, "
            + DURATION + " BIGINT);";

    private static final String GET_BY_DATE = "SELECT * FROM "
            + TABLE + " WHERE "
            + TIMESTAMP + " > foo AND "
            + TIMESTAMP + " < bar "
            + "ORDER BY " + TIMESTAMP + " DESC;";

    private static final String GET_LOCATIONS = "SELECT "
            + PLC_NAME + ", SUM ("
            + DURATION + ") FROM "
            + TABLE + " WHERE "
            + ACT_NAME + " = 'Standing Still' GROUP BY "
            + PLC_NAME + ";";

    private static final String GET_BY_LOCATION = "SELECT * FROM "
            + TABLE + " WHERE "
            + PLC_NAME + " = foo "
            + "ORDER BY " + TIMESTAMP + " DESC;";

    private SQLiteDatabase db;

    public DbRepo(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void initializeDatabase() {
        if (db == null) {
            db = this.getWritableDatabase();
        }
    }

    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    /**
     * Saves a {@code Movement} object in the database.
     * @param movement the Movement object to write to the database
     * @return the id of the row created.
     * */

    public long addMovement(Movement movement) {
        initializeDatabase();
        ContentValues values = getWriteContents(movement);
        long newRowId = db.insert(TABLE, null, values);
        return newRowId;
    }

    /**
     * Fetches {@code Movement} objects from the database that were made on the given date.
     * @param date the date when the movement(s) were made.
     * @return a list of Movement objects.
     **/

    public List<Movement> getMovementsByDate(Date date) {
        initializeDatabase();
        String[] ranges = getDateRanges(date);
        String query = GET_BY_DATE.replace("foo", ranges[0]).replace("bar", ranges[1]);
        return extractMovementsFromCursor(runQuery(query));
    }

    /**
     * Fetches a list of all the places logged in the database and the total duration spent there,
     * wrapped in {@code Visit} objects.
     * @return a list of {@code Visit} objects.
     * */

    public List<Visit> getVisits() {
        initializeDatabase();
        Cursor cursor = runQuery(GET_LOCATIONS);
        List<Visit> visits = new ArrayList<>();
        while (cursor.moveToNext()) {
            visits.add(getVisitFromCursor(cursor));
        }
        return visits;
    }

    private Visit getVisitFromCursor(Cursor cursor) {
        Visit visit = new Visit();
        visit.setPlaceName(cursor.getString(0));
        visit.setDuration(cursor.getLong(1));
        return visit;
    }

    /**
     * Fetches a list of {@code Movement} objects that were made at a particular location.
     * @param placeName the place where the movements were made.
     * @return a list of Movement objects.
     * */

    public List<Movement> getMovementsByLocation(String placeName) {
        initializeDatabase();
        String query = GET_BY_LOCATION.replace("foo", "'" + placeName + "'");
        return extractMovementsFromCursor(runQuery(query));
    }

    private Cursor runQuery(String query) {
        return db.rawQuery(query, null);
    }

    private List<Movement> extractMovementsFromCursor(Cursor cursor) {
        List<Movement> movements = new ArrayList<>();
        while (cursor.moveToNext()) {
            movements.add(getMovementFromCursor(cursor));
        }
        cursor.close();
        return movements;
    }

    private Movement getMovementFromCursor(Cursor cursor) {
        Movement movement = new Movement();
        movement.setActivityName(cursor.getString(1));
        movement.setPlaceName(cursor.getString(2));
        movement.setTimeStamp(cursor.getLong(3));
        movement.setLatitude(cursor.getDouble(4));
        movement.setLongitude(cursor.getDouble(5));
        movement.setDuration(cursor.getLong(6));
        return movement;
    }

    private String[] getDateRanges(Date date) {
        int[] dateValues = Utility.getDateValues(date);
        int year = dateValues[0];
        int month = dateValues[1];
        int day = dateValues[2];
        Date date1 = Utility.generateDate(year, month, day);
        Date date2 = Utility.generateDate(year, month, day + 1);
        return new String[] {getDateAsLongString(date1), getDateAsLongString(date2)};
    }

    private String getDateAsLongString(Date date) {
        return Long.toString(date.getTime());
    }

    private ContentValues getWriteContents(Movement movement) {
        ContentValues values = new ContentValues();
        values.put(PLC_NAME, movement.getPlaceName());
        values.put(ACT_NAME, movement.getActivityName());
        values.put(TIMESTAMP, movement.getTimeStamp());
        values.put(LATITUDE, movement.getLatitude());
        values.put(LONGITUDE, movement.getLongitude());
        values.put(DURATION, movement.getDuration());
        return values;
    }
}
