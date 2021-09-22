import static java.lang.Math.*;

public class Lab01 {
    public static void main(String[] args) {
        // Constants given in the task
        final int aStart = 6;
        final int aEnd = 22;
        final float xStart = -5.0F;
        final float xEnd = 5.0F;
        final int xLen = 20;
        // Values for conditions (given in the task)
        final int first_condition = 14;
        final int[] second_condition = {8, 18, 20, 22};

        // Step 1
        long[] arrayA = gen_a_array(aStart, aEnd);

        // Step 2
        float[] arrayX = gen_x_array(xStart, xEnd, xLen);

        // Step 3
        double[][] arrayE = gen_e_array(arrayA, arrayX, first_condition, second_condition);

        // Step 4
        printMatrix(arrayE);
    }

    private static long[] gen_a_array(int aStart, int aEnd) {
        /*
            Count array length for only EVEN numbers
            (aStart & aEnd) % 2 gives 1 if aStart and aEnd are odd else gives zero
            This is necessary for a correct calculation of aLen
        */
        int aLen = (aEnd - aStart + 2 - (aStart & aEnd) % 2) / 2;
        long[] arrayA = new long[aLen];

        for (int i = 0; i < aLen; i++) {
            /*
                We calculate all even numbers in the desired interval,
                adding the shift - the first even number in the interval
            */
            arrayA[i] = i * 2 + aStart + aStart % 2;
        }

        return arrayA;
    }

    private static float[] gen_x_array(float xStart, float xEnd, int xLen) {
        float[] arrayX = new float[xLen];

        for (int i = 0; i < xLen; i++) {
            // Get random number and write it to array
            arrayX[i] = get_random(xStart, xEnd);
        }

        return arrayX;
    }

    private static double[][] gen_e_array(
            long[] arrayA,
            float[] arrayX,
            int first_condition,
            int[] second_condition
        ) {

        double[][] arrayE = new double[arrayA.length][arrayX.length];

        // Go throw array
        for (int i = 0; i < arrayA.length; i++) {
            for (int j = 0; j < arrayX.length; j++) {
                float x = arrayX[j];

                // Check conditions
                if (arrayA[i] == first_condition) {
                    arrayE[i][j] = first_terrible_formula(x);
                } else if (contains(second_condition, (int) arrayA[i])) {
                    arrayE[i][j] = second_terrible_formula(x);
                } else {
                    arrayE[i][j] = third_terrible_formula(x);
                }
            }
        }

        return arrayE;
    }


    private static float get_random(float start, float end) {
        return (float) (random() * (end - start) + start);
    }

    public static boolean contains(int[] array, int v) {
        for (int i : array) if (i == v) return true;
        return false;
    }

    private static double first_terrible_formula(float x) {
        return exp(sin(0.75 * (x - 0.25)));
    }

    private static double second_terrible_formula(float x) {
        return tan(asin(pow(x * E + 1, 2)));
    }

    private static double third_terrible_formula(float x) {
        // I know that variable names are terrible, but it is impossible to explain what they store
        double _1 = pow(cos(x) / (1 - pow((1 - x) / 3, 2)), 3);
        double _2 = pow(exp(x) * (x / (0.5 - x) + 1), atan(x * E + 1)) - 1;
        double _3 = pow(_2 / cos(exp(x)), _1) - 0.5;
        double _4 = pow(PI * (log(exp(x)) - 1), exp(x) / 0.25);

        return _3 / _4;
    }

    private static void printMatrix(double[][] arr) {
        for (double[] line : arr) {
            for (double val : line) {
                // 7 - number spaces for digit, .4 - number of digits after dot
                System.out.printf("%7.4f ", val);
            }
            System.out.println(); // New line
        }
    }
}
