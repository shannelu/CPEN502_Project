package ece.cpen502;

public class main {
    public static void main(String[] args){

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

        // binary representation
        binary.initializeWeights();
        do{
            errorSum = 0;
            for(int i=0; i<binaryInputData.length; i++){
                double error = binary.train(binaryInputData[i], binaryExpectedOutput[i][0]);
                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUMu7(C-y)^2
            }
            epoch++;
            System.out.printf("%f and %d\n",errorSum, epoch);
        } while (error_threshold <= errorSum);

        // bipolar representation
        // TODO
        bipolar.initializeWeights();
        do{
            errorSum = 0;
            for(int i=0; i<bipolarInputData.length; i++){
                double error = bipolar.train(bipolarInputData[i], bipolarExpectedOutput[i][0]);
                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUMu7(C-y)^2
            }
            epoch++;
            System.out.printf("%f and %d\n",errorSum, epoch);
        } while (error_threshold <= errorSum);



        NeuralNet binary_momentum = new NeuralNet(2, 4, 1, 0.2, 0.9, true);
        NeuralNet bipolar_momentum = new NeuralNet(2,4,1,0.2,0.0,false);

        // Binary representation with momentum = 0.9
        binary_momentum.initializeWeights();
        do{
            errorSum = 0;
            for(int i=0; i<binaryInputData.length; i++){
                double error = binary_momentum.train(binaryInputData[i], binaryExpectedOutput[i][0]);
                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUMu7(C-y)^2
            }
            epoch++;
            System.out.printf("%f and %d\n",errorSum, epoch);
        } while (error_threshold <= errorSum);


        // TODO
//        bipolar.initializeWeights();
//        do{
//            errorSum = 0;
//            for(int i=0; i<bipolarInputData.length; i++){
//                double error = bipolar_momentum.train(bipolarInputData[i], bipolarExpectedOutput[i][0]);
//                errorSum += Math.pow(error, 2) / 2;  // E = 1/2 * SUMu7(C-y)^2
//            }
//            epoch++;
//            System.out.printf("%f and %d\n",errorSum, epoch);
//        } while (error_threshold <= errorSum);

    }
}
