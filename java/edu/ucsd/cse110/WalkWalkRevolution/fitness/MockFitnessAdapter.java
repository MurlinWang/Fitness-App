package edu.ucsd.cse110.WalkWalkRevolution.fitness;

import android.util.Log;

import edu.ucsd.cse110.WalkWalkRevolution.MockManager;
import edu.ucsd.cse110.WalkWalkRevolution.StepCountActivity;


public class MockFitnessAdapter implements FitnessService {
    private static final String TAG = "MockFitnessAdapter";
    private final int MOCK_ADAPTER_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private StepCountActivity activity;
    private static long total;
    private MockManager manager;

    public MockFitnessAdapter(StepCountActivity activity) {
        this.activity = activity;
        manager = MockManager.getInstance();
        total = manager.getTotal();
    }

    @Override
    public int getRequestCode() {
        return MOCK_ADAPTER_PERMISSIONS_REQUEST_CODE;
    }

    @Override
    public void setup() {
        System.out.println(TAG + "setup");
    }

    @Override
    public void updateStepCount() {
        // System.out.println(TAG + "updateStepCount");
        activity.setStepCount(manager.getTotal());

        Log.d(TAG, "Total steps: " + total);
    }

    public void addSteps(long steps) {
        total += steps;
        manager.setTotal(total);
    }
}
