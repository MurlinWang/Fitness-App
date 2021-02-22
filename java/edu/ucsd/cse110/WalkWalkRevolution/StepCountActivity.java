package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ucsd.cse110.WalkWalkRevolution.fitness.FitnessService;
import edu.ucsd.cse110.WalkWalkRevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.WalkWalkRevolution.fitness.GoogleFitAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.fitness.MockFitnessAdapter;

import static android.view.View.GONE;

public abstract class StepCountActivity extends UpdateActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static final String MOCK_SERVICE_KEY = "MOCK_SERVICE_KEY";
    MockManager manager = MockManager.getInstance();
    protected boolean TESTING = manager.isTesting();
    protected Time time;

    protected TextView textSteps;
    protected TextView textDistance;
    protected FitnessService fitnessService;
    protected double heightInInch;

    protected UpdateStepsAsyncTask updateStepsAsyncTask;

    public void setStepCount(long stepCount){}

    public void setTimeCount(){}

    protected void setupFitnessServiceAndThread(int id) {
        // decide to use mocking
        Button mockBtn = findViewById(id);
        serviceKey = MockManager.getInstance().getKey();

        if(TESTING) {
            mockBtn.setVisibility(View.VISIBLE);
            FitnessServiceFactory.put(serviceKey, new FitnessServiceFactory.BluePrint() {
                @Override
                public FitnessService create(StepCountActivity activity) {
                    return new MockFitnessAdapter(activity);
                }
            });
        } else {
            mockBtn.setVisibility(GONE);
            FitnessServiceFactory.put(serviceKey, new FitnessServiceFactory.BluePrint() {
                @Override
                public FitnessService create(StepCountActivity activity) {
                    return new GoogleFitAdapter(activity);
                }
            });
        }
        fitnessService = FitnessServiceFactory.create(serviceKey, this);
        startTask();
        fitnessService.setup();
        mockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(MockActivity.class);
            }
        });
    }

    protected void onRestart() {
        startTask();
        super.onRestart();
    }

    public void launchActivity(Class<?> cls ) {
        Intent intent = new Intent(this, cls);
        cancelTask();
        startActivity(intent);
    }

    /**
     * Method name: getDistance
     * usage: this method is use for calculate the distance according to the height of user.
     * @param stepCount is the step counts.
     * @return the distance of the walk/run.
     */
    public double getDistance(long stepCount){
        double distanceInFeet;
        distanceInFeet = (double) (stepCount * (heightInInch*0.413/12));
        return distanceInFeet/5280;
    }

    /**
     * This method creates and starts an asyncronous task.
     */
    public void startTask() {
        if(!TESTING) {
            updateStepsAsyncTask = new UpdateStepsAsyncTask();
            updateStepsAsyncTask.execute();
        } else {
            if(time != null) {
                time.getUpdate();
            }
            fitnessService.updateStepCount();
            setTimeCount();
        }
    }

    /**
     * This method cancels the asynchronous task if it is running.
     */
    public void cancelTask() {
        if (updateStepsAsyncTask != null) {
            updateStepsAsyncTask.cancel(true);
        }
    }

    // AsyncTask<TypeOfVarArgParams, ProgressValue, ResultValue>
    /**
     * Asynchronous thread which connects the fitness service to the main
     * activity UI.
     */
    protected class UpdateStepsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            while (!isCancelled()) {
                publishProgress();
                try {
                    if(time != null) {
                        time.getUpdate();
                    }
                    Thread.sleep(5);
                    // System.out.println("UI Thread");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            fitnessService.updateStepCount();
        }

        @Override
        protected void onPreExecute() {
            // leave empty; we have no pre execute
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            fitnessService.updateStepCount();
            setTimeCount();
        }
    }
}
