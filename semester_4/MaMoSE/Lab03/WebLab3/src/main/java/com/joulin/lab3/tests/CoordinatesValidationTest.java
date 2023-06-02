package com.joulin.lab3.tests;

import com.joulin.lab3.beans.Coordinates;
import com.joulin.lab3.utils.CoordinatesValidation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinatesValidationTest {
    private static final Vector<Coordinates> coordinatesVector = new Vector<>();
    private static final Vector<Boolean> correctAns = new Vector<>();
    private static final String localisationFilePath = "src/main/java/com/joulin/lab3/tests/coordinatesValidation.loc";
    private static final int NUM_TESTS = 14;

    @BeforeAll
    public static void setUp() throws FileNotFoundException {
        File loc = new File(localisationFilePath);
        Scanner scanner = new Scanner(loc);
        for (int i = 0; i < NUM_TESTS && scanner.hasNext(); i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double r = scanner.nextDouble();
            coordinatesVector.add(new Coordinates(x, y, r));
            boolean correct = scanner.nextBoolean();
            correctAns.add(correct);
        }
    }

    @Test
    public void testNullCoordinates(){
        assertFalse(CoordinatesValidation.validate(null));
    }

    @Test
    public void testCoordinatesValidation_0() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_1() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_2() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_3() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_4() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_5() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_6() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_7() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_8() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_9() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_10() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_11() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_12() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }

    @Test
    public void testCoordinatesValidation_13() {
        int cur_test = Integer.parseInt(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
        if (coordinatesVector.size() > cur_test)
            assertEquals(CoordinatesValidation.validate(coordinatesVector.get(cur_test)), correctAns.get(cur_test));
    }
}
