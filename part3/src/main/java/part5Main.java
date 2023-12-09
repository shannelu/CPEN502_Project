//package main.java;
//
//import com.github.sh0nk.matplotlib4j.Plot;
//import com.github.sh0nk.matplotlib4j.PythonExecutionException;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class part5Main {
//
//
//    static ArrayList<Double> WinrateList = new ArrayList<Double>();
//    static ArrayList<Integer> epochList = new ArrayList<Integer>();
//    static LookUpTable lut = new LookUpTable(5, 5, 5, 5, 5);
//    static Plot plt2 = Plot.create();
//
//    public static void main(String[] args) throws PythonExecutionException, IOException {
//
//
////        lut.load("out/production/part3/MyOwnRobot.data/LUT.txt");
////        lut.load("out/production/part3/main/java/MyOwnRobot.data/LUT-fire.txt");
//
//
//
//        plt2.xlabel("Number of epochs");
//        plt2.ylabel("RMS error");
//        plt2.title("RMS error vs. number of epochs");
//        plt2.legend();
//        plt2.savefig("RMSE-lr.png");
//        plt2.show();
////        System.out.printf("Average of epoch for bipolar is %d\n", totalEpoch/interations);
//
//    }
//
//    private static void hiddenNeuronPlot(int numOfNeuron, double lr, double momentum) {
//        // bipolar representation
//        WinrateList.clear();
//        epochList.clear();
//
////        double errorSum;
////        double avgErrorSum;
//        int epoch;
//
//        int EnergyLength = 5;
//        int DistantLength = 5;
//        int ActionSize = 5;
//
//        int totalEpoch = 0;
////        double[] minMaxQ = findMinMaxQ(lut.LUT);
//
//        NeuralNet nn = new NeuralNet(5, numOfNeuron, 1, lr, momentum, false);
//        nn.initializeWeights();
//        epoch = 0;
//        WinrateList.clear();
//        epochList.clear();
//
//        do {
//            int iteration = 0;
////            errorSum = 0;
//            for (int a = 0; a < EnergyLength; a++) {
//                for (int b = 0; b < EnergyLength; b++) {
//                    for (int c = 0; c < DistantLength; c++) {
//                        for (int d = 0; d < DistantLength; d++) {
//                            for (int e = 0; e < ActionSize; e++) {
////                                double[] InputData = new double[]{a,b,c,d,e};
//                                double[] NormalizedData = normalizeX(a, b, c, d, e);
//                                double value = lut.outputFor(new double[]{a, b, c, d, e});
////                                double normalizedValue = 2 * (value - minMaxQ[0]) / (minMaxQ[1] - minMaxQ[0]) - 1;
//                                double error = nn.train(NormalizedData, value);
//                                errorSum += Math.pow(error, 2);  // E =  SUM(C-y)^2
//                                iteration++;
//                            }
//                        }
//                    }
//                }
//            }
//            epoch++;
//            avgErrorSum = Math.sqrt(errorSum / iteration);
////            System.out.println(errorSum);
//            WinrateList.add(avgErrorSum);
//            epochList.add(epoch);
////            System.out.printf("error value is %f\n", avgErrorSum);
//        } while (epoch < 1000);
////        totalEpoch += epoch;
//        plt2.plot().add(epochList, errorList).label("learning rate = " + lr);
//    }
//
//
//    private static double[] normalizeX(int energy1, int energy2, int dist1, int dist2, int action) {
//        Map<Integer, Double> normalizedEnergy = new HashMap<>() {{
//            put(0, 0.0);
//            put(1, 20.0);
//            put(2, 40.0);
//            put(3, 60.0);
//            put(4, 80.0);
//        }};
//
//        Map<Integer, Double> normalizedDist = new HashMap<>() {{
//            put(0, 100.0);
//            put(1, 300.0);
//            put(2, 500.0);
//            put(3, 700.0);
//            put(4, 900.0);
//        }};
//
////        Map<Integer, Double> normalizedAction = new HashMap<Integer, Double>(){{
////            put(0, 0.0);
////            put(1, 0.25);
////            put(2, 0.5);
////            put(3, 0.75);
////            put(4, 1.0);
////        }};
//
//        return new double[]{
//                normalizedEnergy.get(energy1),
//                normalizedEnergy.get(energy2),
//                normalizedDist.get(dist1),
//                normalizedDist.get(dist2),
//                action
////                normalizedAction.get(action)
//        };
//    }
//}