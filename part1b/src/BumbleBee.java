import robocode.*;

import java.awt.*;
import java.io.File;

public class BumbleBee extends AdvancedRobot {

    public enum enumEnergy {zero, low, average, high, highest} // for myEnergy and enemyEnergy
    public enum enumDistance {closest, close, medium, far, farthest}  // for DistanceToEnemy and DistanceToCenter
    public enum enumAction {attack, forward, backward, left, right}
    public enum enumOperationMode {performScan, performAction}

    static private LookUpTable LUT= new LookUpTable(
            enumEnergy.values().length,
            enumEnergy.values().length,
            enumDistance.values().length,
            enumDistance.values().length,
            enumAction.values().length
    );


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
    private enumEnergy myCurrentEnergy = enumEnergy.highest;
    private enumEnergy enemyCurrentEnergy = enumEnergy.highest;
    private enumDistance currentDisToEnemy = enumDistance.farthest;
    private enumDistance currentDisToCenter = enumDistance.farthest;
    private enumAction currentAction = enumAction.forward;
    private enumOperationMode operationMode = enumOperationMode.performScan;


    // PreviousState Initialization
    private enumEnergy myPrevEnergy = enumEnergy.highest;
    private enumEnergy enemyPrevEnergy = enumEnergy.highest;
    private enumDistance prevDisToEnemy = enumDistance.farthest;
    private enumDistance prevDisToCenter = enumDistance.farthest;
    private enumAction prevAction = enumAction.forward;

    // RL learning parameters
    private double gamma = 0.9;
    private double alpha = 0.0;  // learning rate: 0.1
    private double epsilon = 0.0; // exploration rate: 0.0, 0.1, 0.2, 0.5, 0.8
    private boolean offPolicy = true; // true for Q-leaning, false for Sarsa

    // reward
    private double currentReward = 0.0;
    private double negativeReward = -0.1;  // set to 0 when only consider terminal
    private double positiveReward = 0.5;   // set to 0 when only consider terminal
    private double negativeTerminalRewards = -0.2;
    private double positiveTerminalRewards = 1.0;

    // number of round
    static int TotalRound = 0;
    static int TotalWins = 0;
    static int round = 0;

    // Logging
    static String logFilename = "benchmark.log";
    static LogFile log = new LogFile();
    static String LUTDataFilename = "LUT.txt";



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



        while(true){
            switch (operationMode){
                case performScan:{
                    currentReward = 0.0;
                    turnRadarLeft(90);
                    break;
                }
                case performAction:{
                    if(Math.random() <= epsilon){
                        currentAction = enumAction.values()[LUT.getExploratoryMove()];
                    } else {
                        double DistanceToCenter = getDistFromCenter(myX,myY,centerX,centerY);

                        currentAction = enumAction.values()[LUT.getGreedyMove(
                                getEnumEnergyOf(myEnergy).ordinal(),
                                getEnumEnergyOf(enemyEnergy).ordinal(),
                                getEnumDistOf(DistanceToEnemy).ordinal(),
                                getEnumDistOf(DistanceToCenter).ordinal()
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
                        myPrevEnergy.ordinal(),
                        enemyPrevEnergy.ordinal(),
                        prevDisToEnemy.ordinal(),
                        prevDisToCenter.ordinal(),
                        prevAction.ordinal()
                };

                double QValue = getQValue(currentReward,offPolicy);
                LUT.train(X, QValue);
                operationMode = enumOperationMode.performScan;
                execute();
            }
        }
    }

    public double getQValue(double currentReward, boolean offPolicy){

        // for sarsa on policy
        double currentQValue = LUT.getValueFromLUT(
                myCurrentEnergy.ordinal(),
                enemyCurrentEnergy.ordinal(),
                currentDisToEnemy.ordinal(),
                currentDisToCenter.ordinal(),
                currentAction.ordinal()
        );

        int GreedyMove = LUT.getGreedyMove(
                myCurrentEnergy.ordinal(),
                enemyCurrentEnergy.ordinal(),
                currentDisToEnemy.ordinal(),
                currentDisToCenter.ordinal()
        );

        // for q-learning off policy
        double maxQValue = LUT.getValueFromLUT(
                myCurrentEnergy.ordinal(),
                enemyCurrentEnergy.ordinal(),
                currentDisToEnemy.ordinal(),
                currentDisToCenter.ordinal(),
                GreedyMove
        );

        double prevQValue = LUT.getValueFromLUT(
                myPrevEnergy.ordinal(),
                enemyPrevEnergy.ordinal(),
                prevDisToEnemy.ordinal(),
                prevDisToCenter.ordinal(),
                prevAction.ordinal()
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


    public enumEnergy getEnumEnergyOf(double energy){
        enumEnergy res;
        if(energy < 0) {
            return null;
        } else if(energy == 0){
            res = enumEnergy.zero;
        } else if(energy < 20) {
            res = enumEnergy.low;
        } else if(energy < 40){
            res = enumEnergy.average;
        } else if (energy < 60){
            res = enumEnergy.high;
        } else {
            res = enumEnergy.highest;
        }
        return res;
    }

    public enumDistance getEnumDistOf(double dist){
        enumDistance res ;
        if(dist < 0) {
            return null;
        } else if(dist < 100){
            res = enumDistance.closest;
        } else if(dist < 300){
            res = enumDistance.close;
        } else if(dist < 500){
            res = enumDistance.medium;
        } else if(dist < 700){
            res = enumDistance.far;
        } else {
            res = enumDistance.farthest;
        }
        return res;
    }

    public double getDistFromCenter(double myX, double myY, double centerX, double centerY){
        double dist;
        dist = Math.sqrt(Math.pow(myX - centerX, 2) + Math.pow(myY - centerY, 2));
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

        // Update current state
        myCurrentEnergy = getEnumEnergyOf(getEnergy());
        enemyCurrentEnergy = getEnumEnergyOf(e.getEnergy());
        currentDisToEnemy = getEnumDistOf(e.getDistance());
        currentDisToCenter = getEnumDistOf(getDistFromCenter(myX,myY,centerX,centerY));
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


    public void saveToLog() {
        if ((TotalRound % 100 == 0) && (TotalRound != 0)) {
            double winPercentage = (double) TotalWins / 100;
            System.out.println(String.format("%d, %.3f", ++round, winPercentage));
            File folderDst1 = getDataFile(logFilename);
            log.writeToFile(folderDst1, winPercentage, round);
            TotalWins = 0;
        }
    }


    @Override
    public void onWin(WinEvent event) {
        currentReward = positiveTerminalRewards;

        // update previous Q
        double[] X = new double[]{
                myPrevEnergy.ordinal(),
                enemyPrevEnergy.ordinal(),
                prevDisToEnemy.ordinal(),
                prevDisToCenter.ordinal(),
                prevAction.ordinal()
        };

        double QValue = getQValue(currentReward,offPolicy);
        LUT.train(X, QValue);

        TotalWins++;
        TotalRound++;
        saveToLog();
        LUT.save(getDataFile(LUTDataFilename));
    }

    @Override
    public void onDeath(DeathEvent event) {
        currentReward = negativeTerminalRewards;

        // update previous Q
        double[] X = new double[]{
                myPrevEnergy.ordinal(),
                enemyPrevEnergy.ordinal(),
                prevDisToEnemy.ordinal(),
                prevDisToCenter.ordinal(),
                prevAction.ordinal()
        };

        double QValue = getQValue(currentReward, offPolicy);
        LUT.train(X, QValue);

        TotalRound++;
        saveToLog();
        LUT.save(getDataFile(LUTDataFilename));
    }


}
