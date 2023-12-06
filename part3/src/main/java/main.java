package main.java;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class main {


    static ArrayList<Double> errorList = new ArrayList<Double>();
    static ArrayList<Integer> epochList = new ArrayList<Integer>();
    static LookUpTable lut= new LookUpTable(
            5,
            5,
            5,
            5,
            5
    );
    static Plot plt2 = Plot.create();

    public static void main(String[] args) throws PythonExecutionException, IOException {


//        lut.load("out/production/part3/MyOwnRobot.data/LUT.txt");
        lut.load("out/production/part3/main/java/MyOwnRobot.data/LUT-corner.txt");


        hiddenNeuronPlot(5);
        hiddenNeuronPlot(10);
        hiddenNeuronPlot(15);
        hiddenNeuronPlot(20);


        plt2.xlabel("Number of epochs");
        plt2.ylabel("RMS error");
        plt2.title("RMS error vs. number of epochs");
        plt2.legend();
        plt2.savefig("RMSE.png");
        plt2.show();
//        System.out.printf("Average of epoch for bipolar is %d\n", totalEpoch/interations);

    }

    private static void hiddenNeuronPlot(int numOfNeuron){
        // bipolar representation
        errorList.clear();
        epochList.clear();

        double errorSum;
        double avgErrorSum;
        int epoch;

        int EnergyLength = 5;
        int DistantLength = 5;
        int ActionSize = 5;

        int totalEpoch = 0;

        NeuralNet nn = new NeuralNet(5, numOfNeuron, 1, 0.1, 0.0, false);
        nn.initializeWeights();
        epoch = 0;
        errorList.clear();
        epochList.clear();

        do {
            int iteration = 0;
            errorSum = 0;
            for (int a = 0; a < EnergyLength; a++) {
                for (int b = 0; b < EnergyLength; b++) {
                    for (int c = 0; c < DistantLength; c++) {
                        for (int d = 0; d < DistantLength; d++) {
                            for (int e = 0; e < ActionSize; e++) {
                                double[] InputData = new double[]{a,b,c,d,e};
//                                double[] NormalizedData = normalizeX(a,b,c,d,e);
                                double error = nn.train(InputData, lut.LUT[a][b][c][d][e]);
                                errorSum += Math.pow(error, 2);  // E =  SUM(C-y)^2
                                iteration++;
                            }
                        }
                    }
                }
            }
            epoch++;
            avgErrorSum = Math.pow(errorSum/iteration, 0.5);
            errorList.add(avgErrorSum);
            epochList.add(epoch);
            System.out.printf("error value is %f\n", avgErrorSum);
        } while (epoch < 1000);
        totalEpoch += epoch;
        plt2.plot().add(epochList, errorList).label("Hidden Neuron =" + numOfNeuron);

    }



    private static double[] normalizeX(int energy1, int energy2, int dist1, int dist2, int action){
        Map<Integer, Double> normalizedE = new HashMap<Integer, Double>(){{
            put(0, 0.0);
            put(1, 0.2);
            put(2, 0.4);
            put(3, 0.6);
            put(4, 1.0);
        }};

//        Map<Integer, Double> normalizedD = new HashMap<Integer, Double>(){{
//            put(0, 0.05);
//            put(1, 0.25);
//            put(2, 0.5);
//            put(3, 0.75);
//            put(4, 1.0);
//        }};
//
//        Map<Integer, Double> normalizedA = new HashMap<Integer, Double>(){{
//            put(0, 0.0);
//            put(1, 0.25);
//            put(2, 0.5);
//            put(3, 0.75);
//            put(4, 1.0);
//        }};

        return new double[]{
                normalizedE.get(energy1),
                normalizedE.get(energy2),
                normalizedE.get(dist1),
                normalizedE.get(dist2),
                normalizedE.get(action)
        };
    }
}
