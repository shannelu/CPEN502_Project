package main.java;


/**
 * Experience stored in replay memory
 * - Previous state
 * - Previous action
 * - Current reward
 * - Current state
 */
public class Experience {
    public double myPrevEnergy;
    public double enemyPrevEnergy;
    public double prevDisToEnemy;
    public double prevDisToCenter;
    public NNRobot.enumAction prevAction;
    public double currentReward;

    public double myCurrentEnergy;
    public double enemyCurrentEnergy;
    public double currentDisToEnemy;
    public double currentDisToCenter;
    public NNRobot.enumAction currentAction;



    // Constructor
    public Experience(double currentReward, double myPrevEnergy, double enemyPrevEnergy, double prevDisToEnemy, double prevDisToCenter,
                      NNRobot.enumAction prevAction, double myCurrentEnergy, double enemyCurrentEnergy, double currentDisToEnemy,
                      double currentDisToCenter, NNRobot.enumAction currentAction) {
//        this.prevState = prevState;
//        this.prevAction = prevAction;
//        this.currReward = currReward;
//        this.currState = currState;

        this.currentReward = currentReward;

        this.myPrevEnergy = myPrevEnergy;
        this.enemyPrevEnergy = enemyPrevEnergy;
        this.prevDisToEnemy = prevDisToEnemy;
        this.prevDisToCenter = prevDisToCenter;
        this.prevAction = prevAction;

        this.myCurrentEnergy = myCurrentEnergy;
        this.enemyCurrentEnergy = enemyCurrentEnergy;
        this.currentDisToEnemy = currentDisToEnemy;
        this.currentDisToCenter = currentDisToCenter;
        this.currentAction = currentAction;
    }



}
