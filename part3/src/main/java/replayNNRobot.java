package main.java;

import robocode.*;

import java.awt.*;
import java.util.Random;

import static main.java.Utils.oneHotVectorFor;

public class replayNNRobot extends AdvancedRobot {

    //    public enum enumEnergy {zero, low, average, high, highest} // for myEnergy and enemyEnergy
//    public enum enumDistance {closest, close, medium, far, farthest}  // for DistanceToEnemy and DistanceToCenter
    public enum enumAction {attack, forward, backward, left, right}
    public enum enumOperationMode {performScan, performAction}

    static int numInput = 9;
    static int numHidden = 20;
    static double lr = 0.001;
    static double momentum =0.9; //0.0
    static NeuralNet nn = new NeuralNet(numInput, numHidden,1, lr, momentum, false);

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
    static double RewardsPer100 = 0.0;
    static int[] winRate = new int[10000];

    /**
     * Create replay memory to train more than 1 sample at a time step
     */
    static int memSize = 10;
    static ReplayMemory<Experience> rm = new ReplayMemory<>(memSize);

    // CurrentState Initialization
    public State currentState = new State(100.0, 100.0, 500.0, 500.0);
    public enumAction currentAction = enumAction.forward;
    public enumOperationMode operationMode = enumOperationMode.performScan;

    // PreviousState Initialization
    public State prevState = new State(100.0, 100.0, 500.0, 500.0);
    public enumAction prevAction = enumAction.forward;


    // Logging
    static String logFilename = "replay_memSize=10.log";
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
            log = new LogFile(getDataFile(logFilename));
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
                        prevState.myEnergy,
                        prevState.enemyEnergy,
                        prevState.distToEnemy,
                        prevState.distToCenter,
                        prevAction.ordinal()
                };

                rm.add((new Experience(prevState,prevAction,currentReward,currentState)));
                replayExperience(rm);
                operationMode = enumOperationMode.performScan;
                execute();
            }
        }
    }

    public double getQValue(double currentReward, boolean offPolicy){

        // for sarsa on policy
        double currentQValue = nn.outputFor(oneHotVectorFor(new double[]{
                currentState.myEnergy,
                currentState.enemyEnergy,
                currentState.distToEnemy,
                currentState.distToCenter,
                currentAction.ordinal()})
        );

        int GreedyMove = bestAction(
                currentState.myEnergy,
                currentState.enemyEnergy,
                currentState.distToEnemy,
                currentState.distToCenter
        );

        // for q-learning off policy
        double maxQValue = nn.outputFor(oneHotVectorFor(new double[]{
                currentState.myEnergy,
                currentState.enemyEnergy,
                currentState.distToEnemy,
                currentState.distToCenter,
                GreedyMove})
        );

        double prevQValue = nn.outputFor(oneHotVectorFor(new double[]{
                prevState.myEnergy,
                prevState.enemyEnergy,
                prevState.distToEnemy,
                prevState.distToCenter,
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


    public void replayExperience(ReplayMemory rm){
        int memorySize = rm.sizeOf();
        int requestedSampleSize = Math.min(memorySize, memSize);

        Object[] sample = rm.sample(requestedSampleSize);
        for(Object item:sample){
            Experience exp = (Experience) item;

            double[] x = new double[]{
                    exp.prevState.myEnergy,
                    exp.prevState.enemyEnergy,
                    exp.prevState.distToEnemy,
                    exp.prevState.distToCenter,
                    exp.prevAction.ordinal()};

            double[] xScaledOneHotEncoded = oneHotVectorFor(x);
            nn.train(xScaledOneHotEncoded, getQValue(exp.currentReward, offPolicy));
        }
    }

    public double getDistFromCenter(double myX, double myY, double centerX, double centerY){
        double dist = Math.sqrt(Math.pow(myX - centerX, 2) + Math.pow(myY - centerY, 2));
        return dist;
    }



    @Override
    public void onScannedRobot(ScannedRobotEvent e){
        super.onScannedRobot(e);

        myX = getX();
        myY = getY();
        enemyBearing = e.getBearing();
        DistanceToEnemy = e.getDistance();
        enemyEnergy = e.getEnergy();
        myEnergy = getEnergy();

        // Update previous state
        prevState.myEnergy = currentState.myEnergy;
        prevState.enemyEnergy = currentState.enemyEnergy;
        prevState.distToEnemy = currentState.distToEnemy;
        prevState.distToCenter = currentState.distToCenter;
        prevAction = currentAction;
        operationMode = enumOperationMode.performAction;

        //Update current state
        currentState.myEnergy = getEnergy();
        currentState.enemyEnergy = e.getEnergy();
        currentState.distToEnemy = e.getDistance();
        currentState.distToCenter = getDistFromCenter(myX,myY,centerX,centerY);
        operationMode = enumOperationMode.performAction;
    }



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


    @Override
    public void onWin(WinEvent event) {
        currentReward += positiveTerminalRewards;
        RewardsPer100 += currentReward;

        rm.add(new Experience(prevState, prevAction, currentReward, currentState));
        replayExperience(rm);


        if (RoundsPer100 < 100) {
            RoundsPer100++;
            TotalRound++;
            WinsPer100++;
        } else {
            // win rate
            log.stream.printf("%d - %d, %d\n", TotalRound - 100, TotalRound,  100*WinsPer100 / RoundsPer100);
            // rewards
//            log.stream.printf("%d - %d, %f\n", TotalRound - 100, TotalRound,  RewardsPer100 / RoundsPer100);
            log.stream.flush();
            RoundsPer100 = 0;
            WinsPer100 = 0;
            RewardsPer100 = 0;
//            TotalRound++;
        }
    }

    @Override
    public void onDeath(DeathEvent event) {
        currentReward += negativeTerminalRewards;
        RewardsPer100 += currentReward;


        rm.add(new Experience(prevState, prevAction, currentReward, currentState));
        replayExperience(rm);

        if (RoundsPer100 < 100) {
            RoundsPer100++;
            TotalRound++;
        } else {
            // win rate
            log.stream.printf("%d - %d, %d\n", TotalRound - 100, TotalRound,  100*WinsPer100 / RoundsPer100);
            // rewards
//            log.stream.printf("%d - %d, %f\n", TotalRound - 100, TotalRound,  RewardsPer100 / RoundsPer100);
            log.stream.flush();
            RoundsPer100 = 0;
            WinsPer100 = 0;
            RewardsPer100 = 0;
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