package main.java;

import robocode.*;

import java.awt.*;
import java.util.Random;

import static main.java.Utils.oneHotVectorFor;

public class NNRobot extends AdvancedRobot {

//    public enum enumEnergy {zero, low, average, high, highest} // for myEnergy and enemyEnergy
//    public enum enumDistance {closest, close, medium, far, farthest}  // for DistanceToEnemy and DistanceToCenter
    public enum enumAction {attack, forward, backward, left, right}
    public enum enumOperationMode {performScan, performAction}

    static int numInput = 9;
    static int numHidden = 20;
    static double lr = 0.001;
    static double momentum =0.9; //0.0
    static private NeuralNet nn = new NeuralNet(numInput, numHidden,1, lr, momentum, false);

    // my state
    public double myX = 0.0;
    public double myY = 0.0;
    public double myEnergy = 0.0;

    // Enemy state
    public double enemyBearing = 0.0;
    public double enemyEnergy = 0.0;
    public double DistanceToEnemy = 0.0; //enemyDistance


    public double centerX = 0.0;
    public double centerY = 0.0;

    // CurrentState Initialization
    private double myCurrentEnergy = 100;
    private double enemyCurrentEnergy = 100;
    private double currentDisToEnemy = 500;
    private double currentDisToCenter = 500;
    private enumAction currentAction = enumAction.forward;
    private enumOperationMode operationMode = enumOperationMode.performScan;


    // PreviousState Initialization
    private double myPrevEnergy = 100;
    private double enemyPrevEnergy = 100;
    private double prevDisToEnemy = 500;
    private double prevDisToCenter = 500;
    private enumAction prevAction = enumAction.forward;

    // RL learning parameters
    private double gamma = 0.9;  // 0.2, 0.5, 0.7, 0.9
    private double alpha = 0.1;  // learning rate: 0.1
    private double epsilon = 0.1; // exploration rate: 0.0, 0.1, 0.2, 0.5, 0.8
    private boolean offPolicy = true; // true for Q-leaning, false for Sarsa

    // reward
    private double currentReward = 0.0;
    private double negativeReward = -0.1;  // set to 0 when only consider terminal, -0.1
    private double positiveReward = 0.5;   // set to 0 when only consider terminal, 0.5
    private double negativeTerminalRewards = -0.2; // -0.2
    private double positiveTerminalRewards = 1; //1
    static boolean NNinitialized = false;

    // number of round
    static int TotalRound = 0;
    static int WinsPer100 = 0;
    static int RoundsPer100 =0;
    // Win rate counter: winRate[0] = # of wins in rounds 1-100, winRate[1] = # of wins in rounds 101-200, etc
    static int[] winRate = new int[10000];


    // Logging
//    static String logFilename = "nnrobot-winrate.log";
//    LogFile log = new LogFile(getDataFile(logFilename));
//    static String LUTDataFilename = "LUT-first.txt";

    // Logging
    static String logFilename = "winrate_momentum0.9_gamma0.5.log";
    static LogFile log = null;



    @Override
    public void run() {
        super.run();

        setGunColor(Color.blue);
        setBodyColor(Color.cyan);
        setBulletColor(Color.black);
        setRadarColor(Color.gray);
        setScanColor(Color.green);

        centerX = getBattleFieldWidth()/2;
        centerY = getBattleFieldHeight()/2;

        // Create log file
        if (log == null) {
            System.out.print("!!!*********************!!!");
            System.out.print(logFilename);
            log = new LogFile(getDataFile(logFilename));
            log.stream.printf("Start writing log\n");
            log.stream.printf("gamma,   %2.2f\n", gamma);
            log.stream.printf("alpha,   %2.2f\n", alpha);
            log.stream.printf("epsilon, %2.2f\n", epsilon);
        }

        if(!NNinitialized){
            NNinitialized = true;
            nn.initializeWeights();
        }

        while(true){
            if(TotalRound > 8000) epsilon=0;

            switch (operationMode){
                case performScan:{
                    currentReward = 0.0;
                    turnRadarLeft(90);
                    break;
                }
                case performAction:{
                    if(Math.random() <= epsilon){
                        currentAction = enumAction.values()[selectRandomAction()];
                    } else {
                        double DistanceToCenter = getDistFromCenter(myX,myY,centerX,centerY);

                        currentAction = enumAction.values()[bestAction(
                                myEnergy,
                                enemyEnergy,
                                DistanceToEnemy,
                                DistanceToCenter
                        )];
                    }
                    switch (currentAction){
                        case attack:
                            setRadarColor(Color.red);
                            double amountToTurn = getHeading() - getGunHeading() + enemyBearing;
                            if(amountToTurn == 360.0 || amountToTurn == -360.0){
                                amountToTurn = 0.0;
                            }
                            turnGunRight(amountToTurn);
                            fire(5);
                            break;

                        case forward:
                            setAhead(100);
                            execute();
                            break;

                        case backward:
                            setBack(100);
                            execute();
                            break;

                        case left:
                            setTurnLeft(30);
                            setAhead(100);
                            execute();
                            break;

                        case right:
                            setTurnRight(30);
                            setAhead(100);
                            execute();
                            break;
                    }
                }
                // Update previous Q
                double[] X = new double[]{
                        myPrevEnergy,
                        enemyPrevEnergy,
                        prevDisToEnemy,
                        prevDisToCenter,
                        prevAction.ordinal()
                };

                double QValue = getQValue(currentReward,offPolicy);
                double[] xScaledOneHotEncoded = oneHotVectorFor(X);
                nn.train(xScaledOneHotEncoded, QValue);
                operationMode = enumOperationMode.performScan;
                execute();
            }
        }
    }

    public double getQValue(double currentReward, boolean offPolicy){

        // for sarsa on policy
        double currentQValue = nn.outputFor(oneHotVectorFor(new double[]{
                myCurrentEnergy,
                enemyCurrentEnergy,
                currentDisToEnemy,
                currentDisToCenter,
                currentAction.ordinal()})
        );

        int GreedyMove = bestAction(
                myCurrentEnergy,
                enemyCurrentEnergy,
                currentDisToEnemy,
                currentDisToCenter
        );

        // for q-learning off policy
        double maxQValue = nn.outputFor(oneHotVectorFor(new double[]{
                myCurrentEnergy,
                enemyCurrentEnergy,
                currentDisToEnemy,
                currentDisToCenter,
                GreedyMove})
        );

        double prevQValue = nn.outputFor(oneHotVectorFor(new double[]{
                myPrevEnergy,
                enemyPrevEnergy,
                prevDisToEnemy,
                prevDisToCenter,
                prevAction.ordinal()})
        );

        double newQValue;
        // Q-learning (off-policy)
        if(offPolicy){
            newQValue = prevQValue + alpha * (currentReward + gamma * maxQValue - prevQValue);
        }else {
            // Sarsa (on-policy)
            newQValue = prevQValue + alpha * (currentReward + gamma * currentQValue - prevQValue);
        }

        return newQValue;
    }


    public double getDistFromCenter(double myX, double myY, double centerX, double centerY){
        double dist = Math.sqrt(Math.pow(myX - centerX, 2) + Math.pow(myY - centerY, 2));
        return dist;
    }


    /**
     * Fire when we see a robot
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
        super.onScannedRobot(e);

        myX = getX();
        myY = getY();
//        myHeading = getHeading();
        enemyBearing = e.getBearing();
        DistanceToEnemy = e.getDistance();
        enemyEnergy = e.getEnergy();
        myEnergy = getEnergy();

        // Update previous state
        myPrevEnergy = myCurrentEnergy;
        enemyPrevEnergy = enemyCurrentEnergy;
        prevDisToEnemy = currentDisToEnemy;
        prevDisToCenter = currentDisToCenter;
        prevAction = currentAction;
        operationMode = enumOperationMode.performAction;

         //Update current state
        myCurrentEnergy = getEnergy();
        enemyCurrentEnergy = e.getEnergy();
        currentDisToEnemy = e.getDistance();
        currentDisToCenter = getDistFromCenter(myX,myY,centerX,centerY);
        operationMode = enumOperationMode.performAction;
    }


    /**
     * We were hit!  Turn perpendicular to the bullet,
     * so our seesaw might avoid a future shot.
     */
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        currentReward += negativeReward;
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        currentReward += positiveReward;
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        currentReward += negativeReward;
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        currentReward += negativeReward;
        setBack(200);
        fire(3);
        setTurnRight(60);
        execute();
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        currentReward += negativeReward;
        setBack(200);
        setTurnRight(60);
        execute();
    }


//    public void saveToLog() {
//        if ((TotalRound % 100 == 0) && (TotalRound != 0)) {
//            double winPercentage = (double) TotalWins / 100;
//            System.out.println(String.format("%d, %.3f", ++round, winPercentage));
//            File folderDst1 = getDataFile(logFilename);
//            log.writeToFile(folderDst1, winPercentage, round);
//            TotalWins = 0;
//        }
//    }

//    public void saveStats(int[] winArr) {
//        try {
//            File winRatesFile = getDataFile("WinRate.txt");
//            PrintStream out = new PrintStream(new RobocodeFileOutputStream(winRatesFile));
//            out.format("Win rate, %d/%d = %d\n", TotalWins, TotalRound, TotalWins*100/TotalRound);
//            out.format("Every 100 Rounds, Wins,\n");
//            for (int i = 0; i < (getRoundNum() + 1) / 100; i++) {
//                out.format("%d, %d,\n", i + 1, winArr[i]);
//            }
//            out.close();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//    }


    @Override
    public void onWin(WinEvent event) {
        currentReward += positiveTerminalRewards;

        // update previous Q
        double[] X = new double[]{
                myPrevEnergy,
                enemyPrevEnergy,
                prevDisToEnemy,
                prevDisToCenter,
                prevAction.ordinal()
        };

        double QValue = getQValue(currentReward,offPolicy);
        double[] xScaledOneHotEncoded = oneHotVectorFor(X);
        nn.train(xScaledOneHotEncoded, QValue);

        // stats
        if (RoundsPer100 < 100) {
            RoundsPer100++;
            TotalRound++;
            WinsPer100++;
        } else {
//            log.stream.println(WinsPer100 / RoundsPer100);
//            System.out.printf("%d - %d  win rate, %2.1f\n", TotalRound - 100, TotalRound, 100*WinsPer100 / RoundsPer100);
            log.stream.printf("%d - %d  win rate, %d\n", TotalRound - 100, TotalRound, 100*WinsPer100 / RoundsPer100);
            log.stream.flush();
            RoundsPer100 = 0;
            WinsPer100 = 0;
//            TotalRound++;
        }
    }

    @Override
    public void onDeath(DeathEvent event) {
        currentReward += negativeTerminalRewards;

        // update previous Q
        double[] X = new double[]{
                myPrevEnergy,
                enemyPrevEnergy,
                prevDisToEnemy,
                prevDisToCenter,
                prevAction.ordinal()
        };

        double QValue = getQValue(currentReward, offPolicy);
        double[] xScaledOneHotEncoded = oneHotVectorFor(X);
        nn.train(xScaledOneHotEncoded, QValue);

        if (RoundsPer100 < 100) {
            RoundsPer100++;
            TotalRound++;
        } else {
//            log.stream.println(WinsPer100 / RoundsPer100);
            log.stream.printf("%d - %d  win rate, %d\n", TotalRound - 100, TotalRound,  100*WinsPer100 / RoundsPer100);
            log.stream.flush();
            RoundsPer100 = 0;
            WinsPer100 = 0;
//            TotalRound++;
        }
    }

    public int selectRandomAction(){
        Random rand = new Random();
        int r = rand.nextInt(enumAction.values().length);
        return r;
    }

    public int bestAction(double myEnergy, double enemyEnergy, double distanceToEnemy, double distanceToCenter){
        double bestQ = -Double.MAX_VALUE;
        int bestAction = -Integer.MAX_VALUE;

        for(int i=0; i<enumAction.values().length; i++){
            double[] x = new double[]{myEnergy, enemyEnergy, distanceToEnemy, distanceToCenter, i};
            double[] xScaledOneHotEncoded = oneHotVectorFor(x);
            double predictedQ = nn.outputFor(xScaledOneHotEncoded);
            if(predictedQ > bestQ){
                bestQ = predictedQ;
                bestAction = i;
            }
        }
        return bestAction;
    }


}