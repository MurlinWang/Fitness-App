package edu.ucsd.cse110.WalkWalkRevolution;

import static edu.ucsd.cse110.WalkWalkRevolution.StepCountActivity.MOCK_SERVICE_KEY;

public class MockManager {
    private static final MockManager ourInstance = new MockManager();
    private static String fitnessServiceKey = "FITNESS_SERVICE_KEY";
    private long total;
    private long time;
    private static boolean TESTING = false; // don't use ui update threads

    public void setKey(String key) {
        this.fitnessServiceKey = key;
        if (fitnessServiceKey.equals(MOCK_SERVICE_KEY)) {
            TESTING = true;
        }
    }

    public String getKey(){
        return fitnessServiceKey;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isTesting(){ return TESTING; }

    public void setTimeInSeconds(long ms){
        this.time = ms*1000;
    }

    public long getTime(){
        return time;
    }

    public static MockManager getInstance() {
        return ourInstance;
    }

    private MockManager() {
        total = 0;
    }
}
