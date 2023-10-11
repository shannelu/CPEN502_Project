package main.java.ece.cpen502;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;


public class main {
    static ArrayList<Double> errorList = new ArrayList<Double>();
    static ArrayList<Integer> epochList = new ArrayList<Integer>();

    public static void main(String[] args) throws PythonExecutionException, IOException {

        int epoch = 0;
        double error_threshold = 0.05;
        double errorSum;


        // binary representation
        double binaryInputData[][] = {{0,0},{1,0},{0,1},{1,1}};
        double binaryExpectedOutput[][] = {{0},{1},{1},{0}};

        // bipolar representation
        double bipolarInputData[][] = {{-1,-1},{1,-1},{-1,1},{1,1}};
        double bipolarExpectedOutput[][] = {{-1},{1},{1},{-1}};


        NeuralNet binary = new NeuralNet(2,4,1,0.2,0.0, true);
        NeuralNet bipolar = new NeuralNet(2,4,1,0.2,0.0,false);


////         binary representation
//        binary.initializeWeights();
//        do{
//            errorSum = 0;
//            for(int i=0; i<binaryInputData.length; i++){
//                double error = binary.train(binaryInputData[i], binaryExpectedOutput[i][0]);
//                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUM(C-y)^2
//            }
//            epoch++;
//            errorList.add(errorSum);
//            epochList.add(epoch);
////            System.out.printf("%f and %d\n",errorSum, epoch);
//        } while (error_threshold <= errorSum);
//        DoubleExporter();
//
//
//
//        // draw a figure errorSum verse epoch
//        Plot plt1 = Plot.create();
//        plt1.plot().add(epochList,errorList);
//        plt1.xlabel("Number of epochs");
//        plt1.ylabel("Total error");
//        plt1.title("Total error vs. numbers of epoch with binary representation");
//        plt1.show();

//        // bipolar representation
//        bipolar.initializeWeights();
//        do{
//            errorSum = 0;
//            for(int i=0; i<bipolarInputData.length; i++){
//                double error = bipolar.train(bipolarInputData[i], bipolarExpectedOutput[i][0]);
//                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUM(C-y)^2
//            }
//            epoch++;
//            errorList.add(errorSum);
//            epochList.add(epoch);
//            System.out.printf("%f and %d\n",errorSum, epoch);
//        } while (error_threshold <= errorSum);
//        DoubleExporter();
//
//        // draw a figure errorSum verse epoch
//        Plot plt2 = Plot.create();
//        plt2.plot().add(epochList,errorList);
//        plt2.xlabel("Number of epochs");
//        plt2.ylabel("Total error");
//        plt2.title("Total error vs. number of epochs with bipolar representation");
//        plt2.show();
//
//
        NeuralNet binary_momentum = new NeuralNet(2, 4, 1, 0.2, 0.9, true);
        NeuralNet bipolar_momentum = new NeuralNet(2,4,1,0.2,0.9,false);
//
//        // Binary representation with momentum = 0.9
//        binary_momentum.initializeWeights();
//        do{
//            errorSum = 0;
//            for(int i=0; i<binaryInputData.length; i++){
//                double error = binary_momentum.train(binaryInputData[i], binaryExpectedOutput[i][0]);
//                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUM(C-y)^2
//            }
//            epoch++;
//            errorList.add(errorSum);
//            epochList.add(epoch);
//            System.out.printf("%f and %d\n",errorSum, epoch);
//        } while (error_threshold <= errorSum);
//        DoubleExporter();
//
//        // draw a figure errorSum verse epoch
//        Plot plt3 = Plot.create();
//        plt3.plot().add(epochList,errorList);
//        plt3.xlabel("Number of epochs");
//        plt3.ylabel("Total error");
//        plt3.title("Total error vs. number of epochs in binary representation with momentum");
//        plt3.show();

        bipolar_momentum.initializeWeights();
        do{
            errorSum = 0;
            for(int i=0; i<bipolarInputData.length; i++){
                double error = bipolar_momentum.train(bipolarInputData[i], bipolarExpectedOutput[i][0]);
                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUM(C-y)^2
            }
            epoch++;
            errorList.add(errorSum);
            epochList.add(epoch);
            System.out.printf("%f and %d\n",errorSum, epoch);
        } while (error_threshold <= errorSum);
        DoubleExporter();

        // draw a figure errorSum verse epoch
        Plot plt4 = Plot.create();
        plt4.plot().add(epochList,errorList);
        plt4.xlabel("Number of epochs");
        plt4.ylabel("Total error");
        plt4.title("Total error vs. number of epochs in bipolar representation with momentum");
        plt4.show();
    }

    private static void DoubleExporter() {
        try {
            java.io.FileWriter writer = new java.io.FileWriter("totalError.txt");
            System.out.println("Exporting to file:");
            for (double value : errorList) {
                writer.write(value + "\n");
            }
            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}
