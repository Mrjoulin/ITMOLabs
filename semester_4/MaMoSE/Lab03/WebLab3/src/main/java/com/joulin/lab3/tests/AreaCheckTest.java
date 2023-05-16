package com.joulin.lab3.tests;

import com.joulin.lab3.beans.Coordinates;
import com.joulin.lab3.utils.AreaCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import com.joulin.lab3.utils.CoordinatesValidation;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class AreaCheckTest {
    private static final AreaCheck areaCheck = new AreaCheck();
    private static final Vector<Coordinates> areaCheckCoordinates = new Vector<>();
    private static final Vector<Boolean> correctAns = new Vector<>();
    private static final String localisationFilePath = "src/main/java/com/joulin/lab3/tests/areaCheck.loc";
    private static final int NUM_TESTS = 8;

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        File loc = new File(localisationFilePath);
        Scanner scanner = new Scanner(loc);
        for (int i = 0; i < NUM_TESTS && scanner.hasNext(); i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double r = scanner.nextDouble();
            areaCheckCoordinates.add(new Coordinates(x, y, r));
            boolean correct = scanner.nextBoolean();
            correctAns.add(correct);
        }
    }

    @Test
    public void testNullCoordinates(){
        assertFalse(areaCheck.isHit(null));
    }

    @Test
    public void testAreaCheck_0() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (areaCheckCoordinates.size() > cur_test)
            assertEquals(areaCheck.isHit(areaCheckCoordinates.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testAreaCheck_1() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (areaCheckCoordinates.size() > cur_test)
            assertEquals(areaCheck.isHit(areaCheckCoordinates.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testAreaCheck_2() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (areaCheckCoordinates.size() > cur_test)
            assertEquals(areaCheck.isHit(areaCheckCoordinates.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testAreaCheck_3() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (areaCheckCoordinates.size() > cur_test)
            assertEquals(areaCheck.isHit(areaCheckCoordinates.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testAreaCheck_4() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (areaCheckCoordinates.size() > cur_test)
            assertEquals(areaCheck.isHit(areaCheckCoordinates.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testAreaCheck_5() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (areaCheckCoordinates.size() > cur_test)
            assertEquals(areaCheck.isHit(areaCheckCoordinates.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testAreaCheck_6() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (areaCheckCoordinates.size() > cur_test)
            assertEquals(areaCheck.isHit(areaCheckCoordinates.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testAreaCheck_7() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (areaCheckCoordinates.size() > cur_test)
            assertEquals(areaCheck.isHit(areaCheckCoordinates.get(cur_test)), correctAns.get(cur_test));
    }
}
