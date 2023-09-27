package ece.cpen502;

public class Backpropagation implements NeuralNetInterface {

    int input = 2;
    int hidden = 4;
    int output = 1;
    double learningRate = 0.2;
    double momentum = 0.0;

    //weights matrix for each layer
    double[][] w1 = new double[input][hidden];
    double[][] w2 = new double[hidden][output];



    @Override
    public double sigmoid(double x){
        return 0.0;
    }

    @Override
    public double customSigmoid(double x){
        return 0.0;
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
        return;
    }
}
