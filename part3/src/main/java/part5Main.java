package main.java;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonConfig;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static main.java.Utils.oneHotVectorFor;

public class part5Main {


//    static ArrayList<Double> WinrateList = new ArrayList<Double>();
//    static ArrayList<Integer> epochList = new ArrayList<Integer>();
//    static LookUpTable lut = new LookUpTable(5, 5, 5, 5, 5);
//    static Plot plt2 = Plot.create();

// We use target error rate to determine the convergence of the neural network
    private static final double errorTarget = 0.05;
    private static final int num_input_nodes = 9;
    private static final int num_hidden_nodes =30;
    private static final double learningRate = 0.05;
    private static final double momentumTerm = 0.01;

    static Plot plt = Plot.create();
    static List<Integer> epochs = new ArrayList<>();
    static List<Double> errorLog_List = new ArrayList<>();


            static Map<Integer, Double> normalizedMap = new HashMap<Integer, Double>(){{
                put(0, 0.2);
                put(1, 0.4);
                put(2, 0.6);
                put(3, 0.8);
                put(4, 1.0);
            }};

//            private static List<String> errorLog_List = new LinkedList<>();
            public static void main(String[] args) throws IOException, PythonExecutionException {
                plt = Plot.create(PythonConfig.pythonBinPathConfig("/usr/bin/python3"));
                plt.xlabel("Epoch");
                plt.ylabel("RMSE");
                plt.title("RMSE vs. Number of Epochs with Different Learning Rate"); //Number of Hidden Neurons

//                NeuralNet nn = new NeuralNet(
//                        num_input_nodes,
//                        num_hidden_nodes,
//                        learningRate,
//                        momentumTerm,
//                        argA,
//                        argB
//                );
                NeuralNet nn = new NeuralNet(num_input_nodes,num_hidden_nodes,1,learningRate, momentumTerm, false);
//                nn.setBinary(isBinary);
                LookUpTable lut = new LookUpTable(5,5,5,5,5);
//                lut.load("out/production/part3/main/java/MyOwnRobot.data/LUT-fire.txt");
                String fileName_input = "out/production/part3/main/java/MyOwnRobot.data/LUT-fire.txt";
                lut.load(fileName_input);
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(fileName_input))));

                int trainDataNum = Integer.parseInt(inputReader.readLine().trim());

                System.out.println("trainDataNum: " + trainDataNum);
//                lut.normalize();
                StringBuilder fileName = new StringBuilder();
                fileName.append("src/reportPart3/nntrain_h");
                fileName.append(num_hidden_nodes);
                fileName.append("_lr");
                fileName.append(learningRate);
                fileName.append("_m");
                fileName.append(momentumTerm);
                System.out.println("Bipolar");

                // create the file
                File logfile = new File(fileName.toString());
                logfile.createNewFile();
                FileWriter writer = new FileWriter(logfile);

                int epochSum = 0;
                nn.initializeWeights();
                errorLog_List.clear();
                int epoch_num_to_reach_target_error = 0;
                double total_error;
                double rms;
                do {
                    total_error = 0;
                    // read the input file and calculate the error
                    inputReader.readLine();
                    for (int j = 0; j < trainDataNum; j++) {
                        String[] input = inputReader.readLine().trim().split(","); //\s+
//                        System.out.println(Integer.parseInt(input[0]));
                        int a = Integer.parseInt(input[0]);
                        int b = Integer.parseInt(input[1]);
                        int c = Integer.parseInt(input[2]);
                        int d = Integer.parseInt(input[3]);
                        int e = Integer.parseInt(input[4]);
                        double qValue = Double.parseDouble(input[5]);
                        int f = Integer.parseInt(input[6]);
                        double[] x = new double[]{
                                normalizedMap.get(a),
                                normalizedMap.get(b),
                                normalizedMap.get(c),
                                normalizedMap.get(d),
                                e
                        };
                        double[] scaledX = oneHotVectorFor(x);
                        double[] inputX = new double[] {
                                x[0],
                                x[1],
                                x[2],
                                x[3],
                                scaledX[4],
                                scaledX[5],
                                scaledX[6],
                                scaledX[7],
                                scaledX[8],
                        };
                        double single_error = nn.train(inputX, qValue);
                        total_error += Math.pow(single_error, 2);
                    }
                    epoch_num_to_reach_target_error++;
                    rms = Math.sqrt(total_error/trainDataNum);
                    errorLog_List.add(rms);
                    epochs.add(epoch_num_to_reach_target_error);
                    plt.plot().add(epochs, errorLog_List);
//                    losses.add(rmsError);
                    // write the error to the file
//                    writer.write(epoch_num_to_reach_target_error + " " + rms + "\n");
                    System.out.println("RMSE at epoch " + epoch_num_to_reach_target_error + " = " + rms);
                    // reset the reader
                    inputReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(fileName_input))));
                    inputReader.readLine();
                } while (epoch_num_to_reach_target_error < 1000 && rms > errorTarget);


                plt.legend();
                plt.savefig("./figures/RMSE-lr.png");
                plt.show();
                // save weights
//                nn.save_not_using_RobocodeFileOutputStream(new File("src/reportPart3/weights_corner.txt"));
//                writer.close();

    }
}