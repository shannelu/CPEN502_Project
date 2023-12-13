package main.java;


/**
 * Experience stored in replay memory
 * - Previous state
 * - Previous action
 * - Current reward
 * - Current state
 */
public class Experience {
    /**
     * Experience stored in replay memory
     * - Previous state
     * - Previous action
     * - Current reward
     * - Current state
     */
        public State prevState;
        public replayNNRobot.enumAction prevAction;
        public double currentReward;
        public State currentState;

        // Constructor
        public Experience(State prevState, replayNNRobot.enumAction prevAction, double currentReward, State currentState) {
            this.prevState = prevState;
            this.prevAction = prevAction;
            this.currentReward = currentReward;
            this.currentState = currentState;
        }

        // Convert to string
        @Override
        public String toString() {
            return "[Prev State:" + prevState + "][" +
                    "Prev Action:" + prevAction + "][" +
                    "Curr Reward:" + currentReward + "][" +
                    "Curr State:" + currentState + "]";
        }

}
