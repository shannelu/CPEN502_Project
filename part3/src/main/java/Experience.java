package main.java;


public class Experience {
        public State prevState;
        public State currentState;
        public replayNNRobot.enumAction prevAction;
        public double currentReward;


        // Constructor
        public Experience(State prevState, replayNNRobot.enumAction prevAction, double currentReward, State currentState) {
            this.prevState = prevState;
            this.prevAction = prevAction;
            this.currentReward = currentReward;
            this.currentState = currentState;
        }


}
