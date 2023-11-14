import java.io.File;
import java.io.IOException;
import java.util.Random;

public class LookUpTable implements LUTInterface{

    private int myEnergy;
    private int enemyEnergy;
    private int DistanceToEnemy;
    private int DistanceToCenter;
    private int ActionSize;
    private double[][][][][] LUT;
    // keep track of the used actions
    private int[][][][][] visits;


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

    }

    @Override
    public void load(String argFileName) throws IOException {

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
