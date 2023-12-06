package main.java;

import robocode.RobocodeFileOutputStream;

import java.io.*;
import java.util.Random;

public class LookUpTable implements LUTInterface{

    private int myEnergy;
    private int enemyEnergy;
    private int DistanceToEnemy;
    private int DistanceToCenter;
    private int ActionSize;
    public double[][][][][] LUT;
    // keep track of the used actions
    public int[][][][][] visits;


    public LookUpTable(int myEnergy, int enemyEnergy, int DistanceToEnemy, int DistanceToCenter, int Action){
        this.myEnergy = myEnergy;
        this.enemyEnergy = enemyEnergy;
        this.DistanceToEnemy = DistanceToEnemy;
        this.DistanceToCenter = DistanceToCenter;
        this.ActionSize = Action;
        this.LUT = new double[myEnergy][enemyEnergy][DistanceToEnemy][DistanceToCenter][Action];
        this.visits = new int[myEnergy][enemyEnergy][DistanceToEnemy][DistanceToCenter][Action];
        initialiseLUT();
    }


    public int visits(double[] X) throws ArrayIndexOutOfBoundsException {
        if (X.length != 5) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            int i = (int) X[0];
            int j = (int) X[1];
            int k = (int) X[2];
            int m = (int) X[3];
            int n = (int) X[4];
            return visits[i][j][k][m][n];
        }
    }

    public int getExploratoryMove() {
        Random ran = new Random();
        int res = ran.nextInt(ActionSize);
        return res;
    }


    public int getGreedyMove(int myEnergy, int enemyEnergy, int DistanceToEnemy, int DistanceToCenter){
        double bestQ = -Double.MAX_VALUE;
        int GreedyAction = -1;
        for(int i=0; i<ActionSize; i++){
            if(LUT[myEnergy][enemyEnergy][DistanceToEnemy][DistanceToCenter][i] > bestQ){
                bestQ = LUT[myEnergy][enemyEnergy][DistanceToEnemy][DistanceToCenter][i];
                GreedyAction = i;
            }
        }
        return GreedyAction;
    }

    @Override
    public double outputFor(double[] X) {
        return 0;
    }

    @Override
    public double train(double[] X, double argValue) throws ArrayIndexOutOfBoundsException {
        if (X.length != 5) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            int i = (int) X[0];
            int j = (int) X[1];
            int k = (int) X[2];
            int m = (int) X[3];
            int n = (int) X[4];
            LUT[i][j][k][m][n] = argValue;
            visits[i][j][k][m][n]++;
        }
        return 1;
    }

    public double getValueFromLUT(int myEnergy, int enemyEnergy, int DistanceToEnemy, int DistanceToCenter, int ActionSize){
        return LUT[myEnergy][enemyEnergy][DistanceToEnemy][DistanceToCenter][ActionSize];
    }

    @Override
    public void save(File argFile) {
        //TODO: change this code
        System.out.println("start saving");

        PrintStream saveFile = null;

        try {
            saveFile = new PrintStream(new RobocodeFileOutputStream(argFile));
        } catch (IOException e) {
            System.out.println("*** Could not create output stream for NN save file.");
        }

        // First line is the number of rows of data
        assert saveFile != null;
        saveFile.println(myEnergy * enemyEnergy * DistanceToEnemy * DistanceToCenter * ActionSize);

        // Second line is the number of dimensions per row
        saveFile.println(5);

        System.out.println("start writing");

        for (int a = 0; a < myEnergy; a++) {
            for (int b = 0; b < enemyEnergy; b++) {
                for (int c = 0; c < DistanceToEnemy; c++) {
                    for (int d = 0; d < DistanceToCenter; d++) {
                        for (int e = 0; e < ActionSize; e++) {
                            // e, d, e2, d2, a, q, visits
                            String row = String.format("%d,%d,%d,%d,%d,%2.5f,%d",
                                    a, b, c, d, e,
                                    LUT[a][b][c][d][e],
                                    visits[a][b][c][d][e]
                            );
                            saveFile.println(row);
                        }
                    }
                }
            }
        }
        saveFile.close();
        System.out.println("finish saving");
    }

    @Override
    public void load(String argFileName) throws IOException {

        FileInputStream inputFile = new FileInputStream(argFileName);
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputFile));
        int numExpectedRows = myEnergy * enemyEnergy * DistanceToEnemy * DistanceToCenter * ActionSize;

        // Check the number of rows is compatible
        int numRows = Integer.valueOf(inputReader.readLine());
        // Check the number of dimensions is compatible
        int numDimensions = Integer.valueOf(inputReader.readLine());

        if (numRows != numExpectedRows || numDimensions != 5) {
            System.out.printf(
                    "*** rows/dimensions expected is %s/%s but %s/%s encountered\n",
                    numExpectedRows, 5, numRows, numDimensions
            );
            inputReader.close();
            throw new IOException();
        }

        for (int a = 0; a < myEnergy; a++) {
            for (int b = 0; b < enemyEnergy; b++) {
                for (int c = 0; c < DistanceToEnemy; c++) {
                    for (int d = 0; d < DistanceToCenter; d++) {
                        for (int e = 0; e < ActionSize; e++) {

                            // Read line formatted like this: <e,d,e2,d2,a,q,visits\n>
                            String line = inputReader.readLine();
                            String tokens[] = line.split(",");
                            int dim1 = Integer.parseInt(tokens[0]);
                            int dim2 = Integer.parseInt(tokens[1]);
                            int dim3 = Integer.parseInt(tokens[2]);
                            int dim4 = Integer.parseInt(tokens[3]);
                            int dim5 = Integer.parseInt(tokens[4]); // actions
                            double q = Double.parseDouble(tokens[5]);
                            int v = Integer.parseInt(tokens[6]);
                            LUT[a][b][c][d][e] = q;
                            visits[a][b][c][d][e] = v;
                        }
                    }
                }
            }
        }
        inputReader.close();
    }

    @Override
    public void initialiseLUT() {
        for(int i=0; i<myEnergy; i++){
            for(int j = 0; j < enemyEnergy; j++) {
                for(int k = 0; k < DistanceToEnemy; k++) {
                    for(int m = 0; m < DistanceToCenter; m++) {
                        for(int n = 0; n < ActionSize; n++) {
                            LUT[i][j][k][m][n] = Math.random();
                            visits[i][j][k][m][n] = 0;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int indexFor(double[] X) {
        return 0;
    }

    public int getMyEnergy(){
        return myEnergy;
    }

    public void setMyEnergy(int myEnergy){
        this.myEnergy = myEnergy;
    }

    public int getEnemyEnergy(){
        return enemyEnergy;
    }

    public void setEnemyEnergy(int enemyEnergy){
        this.enemyEnergy = enemyEnergy;
    }

    public int getDistanceToEnemy(){
        return DistanceToEnemy;
    }

    public void setDistanceToEnemy(int DistanceToEnemy){
        this.DistanceToEnemy = DistanceToEnemy;
    }

    public int getDistanceToCenter(){
        return DistanceToCenter;
    }

    public void setDistanceToCenter(int DistanceToCenter){
        this.DistanceToCenter = DistanceToCenter;
    }

    public int getActionSize(){
        return ActionSize;
    }

    public void setActionSize(int Action){
        this.ActionSize = Action;
    }
}
