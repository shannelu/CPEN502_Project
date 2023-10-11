package main.java.ece.cpen502;

import java.io.File;
import java.io.IOException;

public class NeuralNet implements NeuralNetInterface {

    private int NumInputs = 2;
    private int NumHidden = 4;
    private int NumOutputs = 1;
    private double learningRate = 0.2;
    private double momentum = 0.0;


    private boolean dataRep = true; // true for binary, false for bipolar
    private double error_threshold = 0.05;
    double errorSum = 0;


    //weights matrix for each layer
    double[][] w1 = new double[NumInputs+1][NumHidden]; // add 1 for bias
    double[][] w2 = new double[NumHidden+1][NumOutputs];

    double[][] w1Delta = new double[NumInputs+1][NumHidden];
    double[][] w2Delta = new double[NumHidden+1][NumOutputs];


    double[] inputLayer = new double[NumInputs + 1];
    double[] hiddenLayer = new double[NumHidden + 1];
    double[] outputLayer = new double[NumOutputs];
    double[] outputDelta = new double[NumOutputs];  //error signal
    double[] hiddenDelta = new double[NumHidden];
    double[] Error = new double[NumOutputs];



    public NeuralNet(int argNumInputs, int argNumHidden, int argNumOutputs, double argLearningRate,
                     double argMomentumTerm, boolean argdataRep){
        this.NumInputs = argNumInputs;
        this.NumHidden = argNumHidden;
        this.NumOutputs = argNumOutputs;
        this.learningRate = argLearningRate;
        this.momentum = argMomentumTerm;
        this.dataRep = argdataRep; //true for binary
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
            }
        }

        for(int i=0; i< w2.length; i++){
            for(int j=0; j< w2[0].length; j++){
                w2[i][j] = Math.random() - 0.5;
            }
        }
    }


    @Override
    public void zeroWeights(){
        // Initialize weights to random values to 0
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
            for(int i=0; i<NumInputs+1; i++){
                hiddenLayer[j] += w1[i][j] * inputLayer[i];
            }
            if(dataRep) { // binary
                hiddenLayer[j] = customSigmoid(hiddenLayer[j]);
            } else { // bipolar
                hiddenLayer[j] = sigmoid(hiddenLayer[j]);
            }

        }
        hiddenLayer[NumHidden] = 1;

        for(int j=0; j<NumOutputs; j++){
            for(int i=0; i<NumHidden+1; i++) {
                outputLayer[j] += w2[i][j] * hiddenLayer[i];
            }
            if (dataRep) { //binary
                outputLayer[j] = customSigmoid(outputLayer[j]);
            } else{ //bipolar
                outputLayer[j] = sigmoid(outputLayer[j]);
            }
        }
    }

    public void backPropagation(){
        //compute error signal when y is an output unit
        for(int i=0; i<NumOutputs; i++){
            if(dataRep){ //binary
//                outputDelta[i] = customSigmoid(outputLayer[i]) * (1 - customSigmoid(outputLayer[i])) * Error[i];
                outputDelta[i] = outputLayer[i] * (1 - outputLayer[i]) * Error[i];
            } else { //bipolar : derivative is different!
                //TODO
                outputDelta[i] = (1 + outputLayer[i]) * (1 - outputLayer[i]) / 2 * Error[i];
            }
        }


//         System.out.println(NumHidden);
        for(int k=0; k<NumHidden+1; k++) {
            for (int j = 0; j < NumOutputs; j++) {
                // same comment as above for choosing w2Delta[i][j]
                w2Delta[k][j] = momentum * w2Delta[k][j] + learningRate * outputDelta[j] * hiddenLayer[k] ;
                w2[k][j] += w2Delta[k][j];
            }
        }

        //compute error signal when y is a hidden unit
        for(int k=0; k<NumHidden; k++){
            hiddenDelta[k] = 0;
            for(int j=0; j<NumOutputs; j++){
                if(dataRep){
                    hiddenDelta[k] += hiddenLayer[k] * (1 - hiddenLayer[k]) * outputDelta[j] * w2[k][j];
                } else {
                    //TODO: different derivative :)
                    hiddenDelta[k] +=  (1 + hiddenLayer[k]) * (1 - hiddenLayer[k]) / 2  * outputDelta[j] * w2[k][j];
                }
            }
        }

        // update weight
        for(int i=0; i< NumInputs+1; i++) {
            for(int k=0; k<NumHidden; k++) {
                // For simplicity, using the same w1Delta[i][j] instead of the previous weight change
                // also we have asked after the lecture because we are more focusing on the direction that the weight change
                // shows us instead of the exact value of weight change, therefore, I think my simplification here is valid.
                w1Delta[i][k]  = momentum * w1Delta[i][k] + learningRate * hiddenDelta[k] * inputLayer[i];
                w1[i][k] += w1Delta[i][k];
            }
        }


    }

    @Override
    public double outputFor(double [] X){
        // TODO
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
