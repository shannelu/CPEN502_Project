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


    public static double[] oneHotVectorFor(double[] X) {
        double[] onehotX = new double[9];
        double previousMyEnergy = X[0];
        double previousEnemyEnergy = X[1];
        double previousDistanceToCenter = X[2];
        double previousDistanceToEnemy = X[3];
        double previousAction = X[4];

        onehotX[0] = previousMyEnergy / 100;
        onehotX[1] = previousEnemyEnergy / 100;
        onehotX[2] = previousDistanceToCenter / 1000;
        onehotX[3] = previousDistanceToEnemy / 1000;
        for (int i = 4; i < 9; i++) {
            onehotX[i] = (i - 4 == previousAction) ? 1 : 0;
        }
        return onehotX;
    }

}