import java.io.File;
import java.io.IOException;
import java.util.Random;

public class LookUpTable implements LUTInterface{

    private int myEnergy;
    private int enemyEnergy;
    private int DistanceToEnemy;
    private int DistanceToCenter;
    private int Action;
    private double[][][][][] LUT;
    // keep track of the used actions
    private int[][][][][] visits;


    public LookUpTable(int myEnergy, int enemyEnergy, int DistanceToEnemy, int DistanceToCenter, int Action){
        this.myEnergy = myEnergy;
        this.enemyEnergy = enemyEnergy;
        this.DistanceToEnemy = DistanceToEnemy;
        this.DistanceToCenter = DistanceToCenter;
        this.Action = Action;
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
        return ran.nextInt(Action);
    }

    public int getGreedyMove(int myEnergy, int enemyEnergy, int DistanceToEnemy, int DistanceToCenter){
        double max = -1;
        int GreedyAction = -1;
        for(int i=0; i<Action; i++){
            if(LUT[myEnergy][enemyEnergy][DistanceToEnemy][DistanceToCenter][i] > max){
                max = LUT[myEnergy][enemyEnergy][DistanceToEnemy][DistanceToCenter][i];
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
                        for(int n = 0; n < Action; n++) {
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

    public int getAction(){
        return Action;
    }

    public void setAction(int Aciton){
        this.Action = Aciton;
    }
}
