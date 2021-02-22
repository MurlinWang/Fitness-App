package edu.ucsd.cse110.WalkWalkRevolution.fitness;

public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateStepCount();
    void addSteps(long steps);
}
