package edu.ucsd.cse110.WalkWalkRevolution;

/**
 * class WalkData is save steps, distance and time for our project.
*/
public class WalkData {

    private Time time;
    private long steps;
    private double distance;

    /**
    default walk data constructor an initialize
    */
    public WalkData(){
        this.time = new Time(0, 0, 0, 0);
        this.steps = 0;
        this.distance = 0;
    }

    /**
     * Constructor of walk data with given data
     *
     * @param time  the duration the walk taken
     * @param steps the steps the walk have
     * @param distance the distance the walk has
     */
    public WalkData(Time time, long steps, double distance){
        this.time = time;
        this.steps = steps;
        this.distance = distance;
    }

    /**
     * setTime function
     * @param time this time is use for the StartWalkActivity.
     */
    public void setTime(Time time){
        this.time = time;
    }

    /**
     * This method will take the distance and update the WalkData object.
     * @param distance is use for the StartWalkActivity.
     */
    public void setDistance(double distance){
        this.distance = distance;
    }

    /**
     * This method will take the steps and update the WalkData object.
     * @param steps is use for the StartWalkActivity.
     */
    public void setSteps(long steps){
        this.steps = steps;
    }
    /*
    get the time steps and distance.
     */
    public Time getTime() { return time; }
    public long getSteps() { return steps; }
    public double getDistance() { return distance; }

}
