//package main.java;
//
//import com.github.sh0nk.matplotlib4j.Plot;
//import com.github.sh0nk.matplotlib4j.PythonConfig;
//import com.github.sh0nk.matplotlib4j.PythonExecutionException;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//public class main {
//
//    static Plot plt = Plot.create();
//    static List<Integer> epochs = new ArrayList<>();
//    static List<Double> losses = new ArrayList<>();
//
//    public static void main(String[] args) throws IOException, PythonExecutionException {
//
//        int numTrials = 1;
//        plt = Plot.create(PythonConfig.pythonBinPathConfig("/usr/bin/python3"));
//        plt.xlabel("Epoch");
//        plt.ylabel("RMSE");
//        plt.title("RMSE vs. Number of Epochs with Different Nums of Hidden Neuron");
//
////        double[] learningRates = {0.005, 0.001, 0.01, 0.1,0.2}; // 0.0001
////        int[] numHidden = {5,10,25,50}; // 200
//        double[] momentum = {0.1, 0.5, 0.9}; //0.9
//        LookUpTable lut = new LookUpTable(5,5,5,5,5);
//        lut.load("out/production/part3/main/java/MyOwnRobot.data/LUT-fire.txt");
//        for (int m: numHidden) {
//            train(lut, 0.001, m, 0.0, numTrials);
//        }
////        }
//        plt.legend();
//        plt.savefig("RMSE-hidden.png");
//        plt.show();
//    }
//
//    public static void train(LookUpTable lut, double lr, int numHidden, double momentum, int numTrials) {
//        double[] minMaxQ = findMinMaxQ(lut.LUT);
//        for (int i = 0; i < numTrials; i++) {
//            epochs.clear();
//            losses.clear();
//            NeuralNet nn = new NeuralNet(5,numHidden,1,lr, momentum, false);
//            nn.initializeWeights();
//            int maxEpochs = 200;
//            int epoch = 0;
//            double rmsError = 1.0;
//            for (; epoch < maxEpochs; epoch++) {
//                double totalSquaredError = 0;
//                int totalInputs = 0;
//                for (int a = 0; a < 5; a++) {
//                    for (int b = 0; b < 5; b++) {
//                        for (int c = 0; c < 5; c++) {
//                            for (int d = 0; d < 5; d++) {
//                                for (int e = 0; e < 5; e++) {
//                                    double[] input = quantizedToNoneQuantized(a, b, c, d, e);
//                                    double qValue = lut.outputFor(new double[]{a, b, c, d, e});
////                                    double normalizedQ = 2 * (qValue - minMaxQ[0]) / (minMaxQ[1] - minMaxQ[0]) - 1;
//                                    totalSquaredError += Math.pow(nn.train(input, qValue),2);
//                                    totalInputs++;
//                                }
//                            }
//                        }
//                    }
//                }
////                System.out.println(totalSquaredError);
//                rmsError = Math.sqrt(totalSquaredError / totalInputs);
////                if (rmsError < errorThreshold) {
////                    break;
////                }
//                epochs.add(epoch);
//                losses.add(rmsError);
////                if (epoch % 5 == 0) {
////                    map.put(epoch, rmsError);
////                    List<Integer> epochs = new ArrayList<>();
////                    List<Double> losses = new ArrayList<>();
////                }
//            }
//            System.out.println("Trial " + i + ": Current error is " + rmsError + ", Epoch: " + epoch);
//            plt.plot().add(epochs, losses).label("Hidden Neuron = " + numHidden);
//        }
//    }
//
//
//    public static double[] quantizedToNoneQuantized(int a, int b, int c, int d, int e) {
//        Map<Integer, Double> normalizedEnergy = new HashMap<>(){{
//            put(0, 0.0);
//            put(1, 20.0);
//            put(2, 40.0);
//            put(3, 60.0);
//            put(4, 80.0);
//        }};
//
//        Map<Integer, Double> normalizedDist = new HashMap<>(){{
//            put(0, 100.0);
//            put(1, 300.0);
//            put(2, 500.0);
//            put(3, 700.0);
//            put(4, 900.0);
//        }};
//
//        return new double[]{normalizedEnergy.get(a), normalizedEnergy.get(b), normalizedDist.get(c), normalizedDist.get(d), e};
//    }
//
//    public static double[] findMinMaxQ(double[][][][][] lut) {
//        double minQ = Double.POSITIVE_INFINITY;
//        double maxQ = Double.NEGATIVE_INFINITY;
//        for (int a = 0; a < 5; a++) {
//            for (int b = 0; b < 5; b++) {
//                for (int c = 0; c < 5; c++) {
//                    for (int d = 0; d < 5; d++) {
//                        for (int e = 0; e < 5; e++) {
//                            minQ = Math.min(minQ, lut[a][b][c][d][e]);
//                            maxQ = Math.max(maxQ, lut[a][b][c][d][e]);
//                        }
//                    }
//                }
//            }
//        }
//        return new double[]{minQ, maxQ};
//    }
//}



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
    static LookUpTable lut= new LookUpTable(5, 5, 5, 5, 5);
    static Plot plt2 = Plot.create();

    public static void main(String[] args) throws PythonExecutionException, IOException {


//        lut.load("out/production/part3/MyOwnRobot.data/LUT.txt");
        lut.load("out/production/part3/main/java/MyOwnRobot.data/LUT-fire.txt");

        // hidden neuron
//        hiddenNeuronPlot(5,0.001,0.0);
//        hiddenNeuronPlot(10,0.001,0.0);
//        hiddenNeuronPlot(15,0.001,0.0);
//        hiddenNeuronPlot(20,0.001,0.0);

        // learning rate
        hiddenNeuronPlot(10,0.001,0.0);
        hiddenNeuronPlot(10,0.005,0.0);
        hiddenNeuronPlot(10,0.01,0.0);
        hiddenNeuronPlot(10,0.2,0.0);
        hiddenNeuronPlot(10,0.5,0.0);



        plt2.xlabel("Number of epochs");
        plt2.ylabel("RMS error");
        plt2.title("RMS error vs. number of epochs");
        plt2.legend();
        plt2.savefig("RMSE-lr.png");
        plt2.show();
//        System.out.printf("Average of epoch for bipolar is %d\n", totalEpoch/interations);

    }

    private static void hiddenNeuronPlot(int numOfNeuron, double lr, double momentum){
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
//        double[] minMaxQ = findMinMaxQ(lut.LUT);

        NeuralNet nn = new NeuralNet(5, numOfNeuron, 1, lr, momentum, false);
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
//                                double[] InputData = new double[]{a,b,c,d,e};
                                double[] NormalizedData = normalizeX(a,b,c,d,e);
                                double value = lut.outputFor(new double[]{a,b,c,d,e});
//                                double normalizedValue = 2 * (value - minMaxQ[0]) / (minMaxQ[1] - minMaxQ[0]) - 1;
                                double error = nn.train(NormalizedData, value);
                                errorSum += Math.pow(error, 2);  // E =  SUM(C-y)^2
                                iteration++;
                            }
                        }
                    }
                }
            }
            epoch++;
            avgErrorSum = Math.sqrt(errorSum/iteration);
//            System.out.println(errorSum);
            errorList.add(avgErrorSum);
            epochList.add(epoch);
//            System.out.printf("error value is %f\n", avgErrorSum);
        } while (epoch < 1000);
//        totalEpoch += epoch;
        plt2.plot().add(epochList, errorList).label("learning rate = " + lr);
    }


    private static double[] normalizeX(int energy1, int energy2, int dist1, int dist2, int action){
        Map<Integer, Double> normalizedEnergy = new HashMap<>(){{
            put(0, 0.0);
            put(1, 20.0);
            put(2, 40.0);
            put(3, 60.0);
            put(4, 80.0);
        }};

        Map<Integer, Double> normalizedDist = new HashMap<>(){{
            put(0, 100.0);
            put(1, 300.0);
            put(2, 500.0);
            put(3, 700.0);
            put(4, 900.0);
        }};

//        Map<Integer, Double> normalizedAction = new HashMap<Integer, Double>(){{
//            put(0, 0.0);
//            put(1, 0.25);
//            put(2, 0.5);
//            put(3, 0.75);
//            put(4, 1.0);
//        }};

        return new double[]{
                normalizedEnergy.get(energy1),
                normalizedEnergy.get(energy2),
                normalizedDist.get(dist1),
                normalizedDist.get(dist2),
                action
//                normalizedAction.get(action)
        };
    }
}