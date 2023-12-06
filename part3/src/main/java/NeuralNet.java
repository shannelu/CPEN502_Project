package main.java;

import java.io.File;
import java.io.IOException;

public class NeuralNet implements NeuralNetInterface {

    private int NumInputs = 5; //TODO
    private int NumHidden = 10;
    private int NumOutputs = 1;
    private double learningRate = 0.1;
    private double momentum = 0.0;
    double error_threshold = 0.05;

    private boolean binary = true; // true for binary, false for bipolar





    //weights matrix for each layer
    double[][] w1 = new double[NumInputs+1][NumHidden+1]; // add 1 for bias
    double[][] w2 = new double[NumHidden+1][NumOutputs];

    double[][] w1Delta = new double[NumInputs+1][NumHidden+1];
    double[][] w2Delta = new double[NumHidden+1][NumOutputs];
    double[][] savedlastDeltaWeight1 = new double[NumInputs+1][NumHidden+1];
    double[][] savedlastDeltaWeight2 = new double[NumHidden+1][NumOutputs];


    double[] inputLayer = new double[NumInputs + 1];
    double[] hiddenLayer = new double[NumHidden + 1];
    double[] outputLayer = new double[NumOutputs];
    double[] outputDelta = new double[NumOutputs];  //error signal
    double[] hiddenDelta = new double[NumHidden+1];
    double[] Error = new double[NumOutputs];

    public NeuralNet(int argNumInputs, int argNumHidden, int argNumOutputs, double argLearningRate,
                     double argMomentumTerm, boolean argbinary){
        this.NumInputs = argNumInputs;
        this.NumHidden = argNumHidden;
        this.NumOutputs = argNumOutputs;
        this.learningRate = argLearningRate;
        this.momentum = argMomentumTerm;
        this.binary = argbinary; //true for binary

        //weights matrix for each layer
        w1 = new double[this.NumInputs+1][this.NumHidden+1]; // add 1 for bias
        w2 = new double[this.NumHidden+1][this.NumOutputs];

        w1Delta = new double[this.NumInputs+1][this.NumHidden+1];
        w2Delta = new double[this.NumHidden+1][this.NumOutputs];
        savedlastDeltaWeight1 = new double[this.NumInputs+1][this.NumHidden+1];
        savedlastDeltaWeight2 = new double[this.NumHidden+1][this.NumOutputs];


        inputLayer = new double[this.NumInputs + 1];
        hiddenLayer = new double[this.NumHidden + 1];
        outputLayer = new double[this.NumOutputs];
        outputDelta = new double[this.NumOutputs];  //error signal
        hiddenDelta = new double[this.NumHidden+1];
        Error = new double[this.NumOutputs];
    }


    @Override
    public double sigmoid(double x){
        return 2 / (1+Math.exp(-x)) - 1;
    }

    @Override
    public double customSigmoid(double x){
        //for binary representation
        return 1/(1 + Math.exp(-x));
    }

    @Override
    public void initializeWeights(){
        // Initialize weights to random values in the range -0.5 to +0.5
        for(int i=0; i< w1.length; i++){
            for(int j=0; j< w1[0].length; j++){
                w1[i][j] = Math.random() - 0.5;
                savedlastDeltaWeight1[i][j] = 0;
                w1Delta[i][j] = 0;
            }
        }

        for(int i=0; i< w2.length; i++){
            for(int j=0; j< w2[0].length; j++){
                w2[i][j] = Math.random() - 0.5;
                savedlastDeltaWeight2[i][j] = 0;
                w2Delta[i][j] = 0;
            }
        }
    }


    @Override
    public void zeroWeights(){
//         Initialize weights to random values to 0
        for(int i=0; i< w1.length; i++){
            for(int j=0; j< w1[0].length; j++){
                w1[i][j] = 0;
            }
        }

        for(int i=0; i< w2.length; i++){
            for(int j=0; j< w2[0].length; j++){
                w2[i][j] = 0;
            }
        }
    }

    public void forwardPropagation(double[] input){
        for(int i=0; i<input.length; i++){
            inputLayer[i] = input[i];
        }
        inputLayer[input.length] = 1; // add bias term



        for(int j=0; j<NumHidden; j++){
            hiddenLayer[j] = 0;
            for(int i=0; i<NumInputs+1; i++){
                hiddenLayer[j] += w1[i][j] * inputLayer[i];
            }
            if(binary) { // binary
                hiddenLayer[j] = customSigmoid(hiddenLayer[j]);
            } else { // bipolar
                hiddenLayer[j] = sigmoid(hiddenLayer[j]);
            }
        }
        hiddenLayer[NumHidden] = 1;

        for(int j=0; j<NumOutputs; j++){
            outputLayer[j] = 0;
            for(int i=0; i<NumHidden+1; i++) {
                outputLayer[j] += w2[i][j] * hiddenLayer[i];
            }
            if (binary) { //binary
                outputLayer[j] = customSigmoid(outputLayer[j]);
            } else{ //bipolar
                outputLayer[j] = sigmoid(outputLayer[j]);
            }
        }
    }

    public void backPropagation(){
        //compute error signal when y is an output unit
        for(int i=0; i<NumOutputs; i++){
            //TODO
            outputDelta[i] = 0;
            if(binary){ //binary
                outputDelta[i] = outputLayer[i] * (1 - outputLayer[i]) * Error[i];
            } else { //bipolar : derivative is different!
                outputDelta[i] = (1 + outputLayer[i]) * (1 - outputLayer[i]) / 2 * Error[i];
            }
        }

        // Update weights for hidden to output layer first
        for (int j = 0; j < NumOutputs; j++) {
            for(int k=0; k<NumHidden+1; k++) {
//                w2Delta[k][j] = momentum * w2Delta[k][j] + learningRate * outputDelta[j] * hiddenLayer[k] ;
                w2Delta[k][j] = momentum * savedlastDeltaWeight2[k][j] + learningRate * outputDelta[j] * hiddenLayer[k];
                w2[k][j] += w2Delta[k][j];
                savedlastDeltaWeight2[k][j] = w2Delta[k][j];
            }
        }

        //compute error signal when y is a hidden unit
        for(int k = 0; k<NumHidden; k++){
            hiddenDelta[k] = 0;
            for(int j=0; j<NumOutputs; j++){
                if(binary){
                    hiddenDelta[k] += hiddenLayer[k] * (1 - hiddenLayer[k]) * outputDelta[j] * w2[k][j];
                } else {
                    hiddenDelta[k] +=  (1 + hiddenLayer[k]) * (1 - hiddenLayer[k]) / 2  * outputDelta[j] * w2[k][j];
                }
            }
        }

        // Update weights for input to hidden layer
        for(int k=0; k<NumHidden; k++) {
            for(int i=0; i< NumInputs+1; i++) {
//                w1Delta[i][k]  = momentum * w1Delta[i][k] + learningRate * hiddenDelta[k] * inputLayer[i];
                w1Delta[i][k]  = momentum * savedlastDeltaWeight1[i][k] + learningRate * hiddenDelta[k] * inputLayer[i];
                w1[i][k] += w1Delta[i][k];
                savedlastDeltaWeight1[i][k] = w1Delta[i][k];
            }
        }


    }

    @Override
    public double outputFor(double [] X){
        // TODO: not used for part 1a
        if(Error[0] < error_threshold){
            return outputLayer[0];
        } else{
            System.out.println("The neural net is not trained well yet.\n");
            return 0.0;
        }
    }

    @Override
    // train the neutral net for one dataset
    public double train(double [] X, double argValue){
        forwardPropagation(X);
        for(int i=0; i<NumOutputs; i++){ // NumOutputs is 1 in this example
            Error[i] = argValue - outputLayer[i];
        }
        backPropagation();
        return Error[0]; // hardcode as the number of output is 1
//        int epoch = 0;
//        double errorSum;
//        do{
//            errorSum = 0;
//            for(int j=0; j<X.length; j++){
//                forwardPropagation(X);
//                for(int i=0; i<NumOutputs; i++){ // NumOutputs is 1 in this example
//                    Error[i] = argValue - outputLayer[i];
//                    errorSum += Math.pow(Error[i], 2) / 2;  // E = 1/2 * SUM(C-y)^2
//                }
//                backPropagation();
//                epoch++;
////            return Error[0]; // hardcode as the number of output is 1
//            }
//        } while(errorSum > error_threshold);
//        System.out.printf("error sum value is %d\n", epoch);
//        return errorSum;
    }


    @Override
    public void save(File argFile){
        // TODO: no need for part 1a
    }

    @Override
    public void load(String argFileName) throws IOException{
        // TODO: no need for part 1a
    }


}