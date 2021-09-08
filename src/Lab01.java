import static java.lang.Math.*;

public class Lab01 {
    public static void main(String[] args) {
        // Step 1
        long[] arrayA = gen_a_array();

        // Step 2
        float[] arrayX = gen_x_array();

        // Step 3
        double[][] arrayE = gen_e_array(arrayA, arrayX);

        // Step 4
        printMatrix(arrayE);
    }

    private static long[] gen_a_array() {
        // Given in the task
        int aStart = 6;
        int aEnd = 22;

        int aLen = (aEnd - aStart + 2) / 2;
        long[] a = new long[aLen];

        // Step 2 in a loop to get only even numbers
        for (int val = aStart; val <= aEnd; val += 2) {
            int index = (val - aStart) / 2;
            a[index] = val;
        }

        return a;
    }

    private static float[] gen_x_array() {
        // Given in the task
        float xStart = -5.0F;
        float xEnd = 5.0F;
        int xLen = 20;
        float[] arrayX = new float[xLen];

        // Get random numbers and write them to array
        for (int i = 0; i < xLen; i++) arrayX[i] = get_random(xStart, xEnd);

        return arrayX;
    }

    private static double[][] gen_e_array(long[] arrayA, float[] arrayX) {
        double[][] arrayE = new double[arrayA.length][arrayX.length];

        // Values for conditions (given in the task)
        int first_condition = 14;
        int[] second_condition = {8, 18, 20, 22};

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
                System.out.printf("%.4f ", val);
            }
            System.out.println(); // New line
        }
    }
}
