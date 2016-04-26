package com.andela.movit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andela.movit.models.Movement;
import com.andela.movit.utilities.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovementRepo extends SQLiteOpenHelper {

    public static final String TABLE = "activities";

    public static final String ID = "_id";

    public static final String ACT_NAME = "act_name";

    public static final String PLC_NAME = "plc_name";

    public static final String TIMESTAMP = "timestamp";

    public static final String LATITUDE = "latitude";

    public static final String LONGITUDE = "longitude";

    private static final String DATABASE = "movements.db";

    private static final int VERSION = 1;

    private static final String CREATE = "CREATE TABLE "
            + TABLE + " ("
            + ID + " INTEGER PRIMARY KEY, "
            + ACT_NAME + " TEXT, "
            + PLC_NAME + " TEXT, "
            + TIMESTAMP + " BIGINT, "
            + LATITUDE + " FLOAT, "
            + LONGITUDE + " FLOAT);";

    private static final String GET_BY_DATE = "SELECT * FROM "
            + TABLE + " WHERE "
            + TIMESTAMP + " > foo AND "
            + TIMESTAMP + " < bar "
            + "ORDER BY " + TIMESTAMP + " DESC;";

    private SQLiteDatabase db;

    public MovementRepo(Context context) {
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

    public long addMovement(Movement movement) {
        initializeDatabase();
        ContentValues values = getWriteContents(movement);
        long newRowId = db.insert(TABLE, null, values);
        return newRowId;
    }

    public List<Movement> getMovementsByDate(Date date) {
        initializeDatabase();
        String[] ranges = getDateRanges(date);
        String query = GET_BY_DATE.replace("foo", ranges[0]).replace("bar", ranges[1]);
        Cursor cursor = db.rawQuery(query, null);
        return extractMovementsFromCursor(cursor);
    }

    public List<Movement> getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE + ";";
        Cursor cursor = db.rawQuery(query, null);
        return extractMovementsFromCursor(cursor);
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
        return values;
    }
}
