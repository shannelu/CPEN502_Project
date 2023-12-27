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
//        plt.title("RMSE vs. Number of Epochs with Different Number of Hidden Neurons"); //Number of Hidden Neurons
//
////        double[] learningRates = {0.0001, 0.0005,0.001, 0.005,0.1}; // 0.001
////        double[] learningRates = {0.001, 0.005, 0.01};
//        int[] numHidden = {10,20,30,50}; //20
////        double[] momentum = {0.0, 0.01, 0.1,0.5,0.9}; //0.9
//        LookUpTable lut = new LookUpTable(5,5,5,5,5);
//        lut.load("out/production/part3/main/java/MyOwnRobot.data/LUT-fire.txt");
//        for (int m: numHidden) {
//            train(lut,0.001,m,0.9, numTrials);
//
//        }
////        }
//        plt.legend();
//        plt.savefig("./figures/RMSE-lr.png");
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
////                                    double[] scaledX = intToBinaryArray(e);
////                                    double[] input = new double[]{
////                                            quantizedToNoneQuantized.get(a),
////                                            quantizedToNoneQuantized.get(b),
////                                            quantizedToNoneQuantized.get(c),
////                                            quantizedToNoneQuantized.get(d),
////                                            scaledX[0],
////                                            scaledX[1],
////                                            scaledX[2],
////                                            scaledX[3],
////                                            scaledX[4]
////                                    };
//                                    double[] input = new double[]{
//                                            quantizedToNoneQuantized.get(a),
//                                            quantizedToNoneQuantized.get(b),
//                                            quantizedToNoneQuantized.get(c),
//                                            quantizedToNoneQuantized.get(d),
//                                            e
//                                    };
//
//                                    double qValue = (lut.outputFor(new double[]{a, b, c, d, e}));
////                                    double normalizedQ = 2 * (qValue - minMaxQ[0]) / (minMaxQ[1] - minMaxQ[0]) - 1;
////                                    double normalizedQ = (qValue - minMaxQ[0]) / (minMaxQ[1] - minMaxQ[0]);
//                                    totalSquaredError += Math.pow(nn.train(input, qValue),2);
//                                    totalInputs++;
//                                }
//                            }
//                        }
//                    }
//                }
//                rmsError = Math.sqrt(totalSquaredError / totalInputs);
////                if (rmsError < errorThreshold) {
////                    break;
////                }
//                epochs.add(epoch);
//                losses.add(rmsError);
//            }
//            System.out.println("Trial " + i + ": Current error is " + rmsError + ", Epoch: " + epoch);
//            plt.plot().add(epochs, losses).label("numHidden =" + numHidden);
//        }
//    }
//
//
//    static Map<Integer, Double> quantizedToNoneQuantized = new HashMap<Integer, Double>(){{
//        put(0, 0.2);
//        put(1, 0.4);
//        put(2, 0.6);
//        put(3, 0.8);
//        put(4, 1.0);
//    }};
//
////    public static double[] quantizedToNoneQuantized(int a, int b, int c, int d, int e) {
////        Random random = new Random();
////        // Generate a random number:
//////        double rD1 = random.nextInt(101); // 0 - 100
//////        double rD2 = random.nextInt(201)+100;  // 100-300
//////        double rD3 = random.nextInt(201)+300; // 300-500
//////        double rD4 = random.nextInt(201)+500; //500-700
//////        double rD5 = random.nextInt(301)+700; // 700-1000
////        Map<Integer, Double> normalizedEnergy = new HashMap<>(){{
//////            put(0, 0.0);
//////            put(1, 20.0);
//////            put(2, 40.0);
//////            put(3, 60.0);
//////            put(4, 80.0);
////            put(0, 0.2);
////            put(1, 0.4);
////            put(2, 0.6);
////            put(3, 0.8);
////            put(4, 1.0);
////        }};
////
////        Map<Integer, Double> normalizedDist = new HashMap<>(){{
//////            put(0, rD1);
//////            put(1, rD2);
//////            put(2, rD3);
//////            put(3, rD4);
//////            put(4, rD5);
////            put(0, 0.2);
////            put(1, 0.4);
////            put(2, 0.6);
////            put(3, 0.8);
////            put(4, 1.0);
////        }};
////
////        return new double[]{normalizedEnergy.get(a), normalizedEnergy.get(b), normalizedDist.get(c), normalizedDist.get(d), e};
////    }
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

// *************************************************************************************************
// *************************************************************************************************


package main.java;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static main.java.Utils.IntToBiArray;

public class main {


    static ArrayList<Double> errorList = new ArrayList<Double>();
    static ArrayList<Integer> epochList = new ArrayList<Integer>();
    static LookUpTable lut= new LookUpTable(5, 5, 5, 5, 5);
    static Plot plt2 = Plot.create();

    public static void main(String[] args) throws PythonExecutionException, IOException {

        lut.load("out/production/part3/main/java/MyOwnRobot.data/LUT-fire.txt");

        // learning rate 0.001
        hiddenNeuronPlot(20,0.0001,0.9);
        hiddenNeuronPlot(20,0.0005,0.9);
        hiddenNeuronPlot(20,0.001,0.9);
        hiddenNeuronPlot(20,0.005,0.9);
        hiddenNeuronPlot(20,0.1,0.9);

//        // number of neuron 20
//        hiddenNeuronPlot(10,0.001,0.9);
//        hiddenNeuronPlot(20,0.001,0.9);
//        hiddenNeuronPlot(30,0.001,0.9);
//        hiddenNeuronPlot(50,0.001,0.9);

        // momentum
//        hiddenNeuronPlot(10,0.001,0.0);
//        hiddenNeuronPlot(20,0.001,0.01);
//        hiddenNeuronPlot(30,0.001,0.1);
//        hiddenNeuronPlot(30,0.001,0.5);
//        hiddenNeuronPlot(50,0.001,0.9);


        plt2.xlabel("Number of epochs");
        plt2.ylabel("RMS error");
        plt2.title("RMS error vs. number of epochs");
        plt2.legend();
        plt2.savefig("./figures/RMSE-neuron.png");
        plt2.show();

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


        NeuralNet nn = new NeuralNet(9, numOfNeuron, 1, lr, momentum, false);
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
                                double[] ExpandedX = IntToBiArray(e);
                                double[] input = new double[]{
                                        normalizeX.get(a),
                                        normalizeX.get(b),
                                        normalizeX.get(c),
                                        normalizeX.get(d),
                                        ExpandedX[0],
                                        ExpandedX[1],
                                        ExpandedX[2],
                                        ExpandedX[3],
                                        ExpandedX[4]
                                };

                                double qValue = lut.outputFor(new double[]{a,b,c,d,e});
//                                double normalizedValue = 2 * (qValue - minMaxQ[0]) / (minMaxQ[1] - minMaxQ[0]) - 1;
                                double error = nn.train(input, qValue);
                                errorSum += Math.pow(error, 2);  // E =  SUM(C-y)^2
                                iteration++;
                            }
                        }
                    }
                }
            }
            epoch++;
            avgErrorSum = Math.sqrt(errorSum/iteration);
            errorList.add(avgErrorSum);
            epochList.add(epoch);
        } while (epoch < 1000);
        plt2.plot().add(epochList, errorList).label("NumOfNeurons = " + numOfNeuron);
    }


    static Map<Integer, Double> normalizeX = new HashMap<>(){{
        put(0, 0.2);
        put(1, 0.4);
        put(2, 0.6);
        put(3, 0.8);
        put(4, 1.0);
    }};
}