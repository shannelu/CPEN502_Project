package main.java.ece.cpen502;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;

public class main {

    static ArrayList<Double> errorList = new ArrayList<Double>();
    static ArrayList<Integer> epochList = new ArrayList<Integer>();

    public static void main(String[] args) throws PythonExecutionException, IOException {

        double error_threshold = 0.05;
        int interations = 200;
        double errorSum;
        int epoch;
        int totalEpoch;


        // binary representation
        double binaryInputData[][] = {{0,0},{1,0},{0,1},{1,1}};
        double binaryExpectedOutput[][] = {{0},{1},{1},{0}};

        // bipolar representation
        double bipolarInputData[][] = {{-1,-1},{1,-1},{-1,1},{1,1}};
        double bipolarExpectedOutput[][] = {{-1},{1},{1},{-1}};


        NeuralNet binary = new NeuralNet(2,4,1,0.2,0.0, true);
        NeuralNet bipolar = new NeuralNet(2,4,1,0.2,0.0,false);

//        System.out.println("Binary representation with momentum = 0.0");
//         // binary representation
//        errorList.clear();
//        epochList.clear();
//        Plot plt1 = Plot.create();
//        totalEpoch=0;
//
//        // Train for 200 times(Binary representation)
//        for(int j=0; j<interations;j++){
//            binary.initializeWeights();
//            epoch=0;
//            errorList.clear();
//            epochList.clear();
//            do{
//                errorSum = 0;
//                for(int i=0; i<binaryInputData.length; i++){
//                    double error = binary.train(binaryInputData[i], binaryExpectedOutput[i][0]);
//                    errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUM(C-y)^2
//                }
//                epoch++;
//                errorList.add(errorSum);
//                epochList.add(epoch);
//            } while (error_threshold < errorSum);
//            totalEpoch +=epoch;
//            if( j%20 == 0){
//                plt1.plot().add(epochList, errorList).label("Trial" + j);
//            }
//        }
//        plt1.xlabel("Number of epochs");
//        plt1.ylabel("Total error");
//        plt1.title("Total error vs. numbers of epoch with binary representation");
//        plt1.legend();
//        plt1.savefig("binary.png");
//        plt1.show();
//        System.out.printf("Average of epoch for binary is %d\n", totalEpoch/interations);



        System.out.println("Bipolar representation with momentum = 0.0");
        // bipolar representation
        errorList.clear();
        epochList.clear();
        Plot plt2 = Plot.create();
        totalEpoch=0;

        // Train for 200 times(Bipolar representation)
        for(int j=0; j<interations;j++){
            bipolar.initializeWeights();
            epoch=0;
            errorList.clear();
            epochList.clear();
            do{
                errorSum = 0;
                for(int i=0; i<bipolarInputData.length; i++){
                    double error = bipolar.train(bipolarInputData[i], bipolarExpectedOutput[i][0]);
                    errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUM(C-y)^2
                }
                epoch++;
                errorList.add(errorSum);
                epochList.add(epoch);
            } while (error_threshold < errorSum);
            totalEpoch +=epoch;
            if( j%20 == 0){
                plt2.plot().add(epochList, errorList).label("Trial" + j);
            }
        }
        plt2.xlabel("Number of epochs");
        plt2.ylabel("Total error");
        plt2.title("Total error vs. number of epochs with bipolar representation");
        plt2.legend();
        plt2.savefig("bipolar.png");
        plt2.show();
        System.out.printf("Average of epoch for bipolar is %d\n", totalEpoch/interations);

        NeuralNet binary_momentum = new NeuralNet(2, 4, 1, 0.2, 0.9, true);
        NeuralNet bipolar_momentum = new NeuralNet(2,4,1,0.2,0.9,false);


//        // Binary representation with momentum = 0.9
//        System.out.println("Binary representation with momentum = 0.9");
//        // binary representation
//        errorList.clear();
//        epochList.clear();
//        Plot plt3 = Plot.create();
//        totalEpoch=0;
//
//        //Train for 200 times(Binary with momentum representation)
//        for(int j=0; j<interations;j++){
//            binary_momentum.initializeWeights();
//            epoch=0;
//            errorList.clear();
//            epochList.clear();
//            do{
//                errorSum = 0;
//                for(int i=0; i<binaryInputData.length; i++){
//                    double error = binary_momentum.train(binaryInputData[i], binaryExpectedOutput[i][0]);
//                    errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUM(C-y)^2
//                }
//                epoch++;
//                errorList.add(errorSum);
//                epochList.add(epoch);
//        } while (error_threshold < errorSum);
//            totalEpoch +=epoch;
//            if( j%20 == 0){
//                plt3.plot().add(epochList, errorList).label("Trial" + j);
//            }
//        }
//        plt3.xlabel("Number of epochs");
//        plt3.ylabel("Total error");
//        plt3.title("Total error vs. number of epochs in binary representation with momentum");
//        plt3.legend();
//        plt3.savefig("binary_momentum.png");
//        plt3.show();
//        System.out.printf("Average of epoch for binary_momentum is %d\n", totalEpoch/interations);
//
//
//        // Bipolar representation with momentum = 0.9
//        System.out.println("Binary representation with momentum = 0.9");
//        // binary representation
//        errorList.clear();
//        epochList.clear();
//        Plot plt4 = Plot.create();
//        totalEpoch=0;
////
//        // Train for 200 times(bipolar with momentum representation)
//        for(int j=0; j<interations;j++){
//            bipolar_momentum.initializeWeights();
//            epoch=0;
//            errorList.clear();
//            epochList.clear();
//        do{
//            errorSum = 0;
//            for(int i=0; i<bipolarInputData.length; i++){
//                double error = bipolar_momentum.train(bipolarInputData[i], bipolarExpectedOutput[i][0]);
//                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUM(C-y)^2
//            }
//            epoch++;
//            errorList.add(errorSum);
//            epochList.add(epoch);
//        } while (error_threshold < errorSum);
//            totalEpoch +=epoch;
//            if( j%20 == 0){
//                plt4.plot().add(epochList, errorList).label("Trial" + j);
//            }
//        }
//        plt4.plot().add(epochList,errorList);
//        plt4.xlabel("Number of epochs");
//        plt4.ylabel("Total error");
//        plt4.title("Total error vs. number of epochs in bipolar representation with momentum");
//        plt4.legend();
//        plt4.savefig("bipolar_momentum.png");
//        plt4.show();
//        System.out.printf("Average of epoch for bipolar_momentum is %d\n", totalEpoch/interations);
    }
}
