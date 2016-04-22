package com.andela.movit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andela.movit.models.Movement;

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

    public long addMovement(Movement movement) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getWriteContents(movement);
        long newRowId = db.insert(TABLE, null, values);
 //       db.close();
        return newRowId;
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
