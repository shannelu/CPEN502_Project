package main.java;

public class Utils {

    public static double[] downScaleVector(double[] vector) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        // Find the min and max values of the vector
        for (double value : vector) {
            if (value < min) min = value;
            if (value > max) max = value;
        }
        // Scale vector to the range [0, 1]
        double[] scaledVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            scaledVector[i] = (vector[i] - min) / (max - min);
        }
        return scaledVector;
    }


    public static int[] oneHotVectorFor(String[] categories, String category) {
        int[] oneHotVector = new int[categories.length];
        for (int i = 0; i < categories.length; i++) {
            if (category.equals(categories[i])) {
                oneHotVector[i] = 1;
                break;
            }
        }
        return oneHotVector;
    }

}
