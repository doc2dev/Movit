package com.andela.movit.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.andela.movit.Movit;
import com.andela.movit.models.Movement;
import com.andela.movit.models.Visit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import static com.andela.movit.data.DbRepo.*;

@RunWith(AndroidJUnit4.class)
public class DbRepoTest {

    private SQLiteDatabase movementDb;

    private DbRepo repo;

    @Before
    public void setup() throws Exception {
        repo = new DbRepo(Movit.getApp());
        movementDb = repo.getReadableDatabase();
    }

    @Test
    public void testWriteToDb() throws Exception {
        long newRowId = repo.addMovement(getTestMovement());
        assertTrue(newRowId > 0);
        String[] projection = {ACT_NAME};
        String selection = ACT_NAME + " LIKE ?";
        String[] selectionArgs = {"Idling"};
        Cursor cursor = movementDb.query(
                TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        assertTrue(cursor.moveToNext());
        String activityName = cursor.getString(0);
        assertEquals("Idling", activityName);
        cursor.close();
    }

    @Test
    public void testGetByDate() {
        List<Movement> movements = repo.getMovementsByDate(new Date());
        assertTrue(movements.size() > 0);
    }

    @Test
    public void testGetLocations() {
        List<Visit> visits = repo.getVisits();
        assertTrue(visits.size() > 0);
    }

    @Test
    public void testGetByPlaceName() {
        List<Movement> movements = repo.getMovementsByLocation("Ngong Forest Rd, Nairobi, Kenya");
        assertTrue(movements.size() > 0);
    }

    private Movement getTestMovement() {
        Movement mv = new Movement();
        mv.setActivityName("Idling");
        mv.setPlaceName("Test Place");
        mv.setLongitude(0.0005d);
        mv.setLongitude(0.004d);
        mv.setTimeStamp(System.currentTimeMillis());
        return mv;
    }

}