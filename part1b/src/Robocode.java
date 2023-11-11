import robocode.AdvancedRobot;

public class Robocode extends AdvancedRobot {

    public enum enumEnergy {low, medium, high}
    public enum enumDistance {close, medium, far}
    public enum enumAction {fire, forward, backward, left, right}
    public enum enumOptionalMode {onScan, OnAction}

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
//    public double


    private double gamma = 0.9;
    private double alpha = 0.1;


}
