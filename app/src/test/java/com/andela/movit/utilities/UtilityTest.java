package com.andela.movit.utilities;

import com.andela.movit.models.Movement;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class UtilityTest {

    @Test
    public void testGetCoordsString() throws Exception {
        Movement movement = new Movement();
        movement.setLongitude(1.0005);
        movement.setLatitude(1.0005);
        assertEquals("1.0005, 1.0005", Utility.getCoordsString(movement));
    }

    @Test
    public void testGetDateValues() throws Exception {
        Date date = Utility.generateDate(2016, 4, 20);
        int[] values = Utility.getDateValues(date);
        assertEquals(values[2], 20);
    }

    @Test
    public void testGetDateString() throws Exception {
        Date date = Utility.generateDate(2016, 3, 20);
        assertEquals("Wed, Apr 20, 2016", Utility.getDateString(date));
    }

    @Test
    public void testGetDurationString() throws Exception {
        long millis = 90000;
        assertEquals("00:01:30", Utility.getDurationString(millis));
    }
}