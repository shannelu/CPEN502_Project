package main.java;

public class State {
    public double myEnergy;
    public double enemyEnergy;
    public double distToEnemy;
    public double distToCenter;

    // Constructor
    public State (double myEnergy, double enemyEnergy, double distToEnemy, double distToCenter) {
        this.myEnergy = myEnergy;
        this.enemyEnergy = enemyEnergy;
        this.distToEnemy = distToEnemy;
        this.distToCenter = distToCenter;
    }
}