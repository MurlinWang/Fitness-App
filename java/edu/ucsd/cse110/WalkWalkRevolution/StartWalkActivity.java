package edu.ucsd.cse110.WalkWalkRevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;

import static java.lang.String.valueOf;


/**
 * StartWalkActivity is use for the intentional walk.
 */
public class StartWalkActivity extends StepCountActivity {

    public static final String START_STEPS = "START_STEPS";

    private static final String TAG = "StartWalkActivity";

    private int startSteps;
    private TextView textHour;
    private TextView textMin;
    private TextView textSecond;
    private TextView textMillis;

    public WalkData walkData = new WalkData(new Time(), 0, 0.0);

    @Override
    /**
     * method name: onCreate
     * usage:set the initial screen to the activity_start_walk
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_walk);

        SharedPreferences sharedPreferences = getSharedPreferences("user_height", Context.MODE_PRIVATE);
        int feet = sharedPreferences.getInt("feet", 0);
        int inches = sharedPreferences.getInt("inches", 0);
        heightInInch = feet * 12 + inches;

        textSteps = findViewById(R.id.textCurrentSteps);
        textDistance = findViewById(R.id.textCurrentDistance);
        textHour = findViewById(R.id.textHour);
        textMin = findViewById(R.id.textMin);
        textSecond = findViewById(R.id.textSecond);
        textMillis = findViewById(R.id.textMillis);

        time = new Time();
        time.startRecord(manager.getKey().equals(MOCK_SERVICE_KEY));

        startSteps = getIntent().getIntExtra(START_STEPS, 0);

        Button btnStopWalk = findViewById(R.id.buttonStopWalk);
        btnStopWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endWalk();
            }
        });
        setupFitnessServiceAndThread(R.id.mock_btn_start);
    }

    @Override
    /**
     * Method name:onActivityResult
     * usage: connect with API
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    /**
     * Method Name: setStepCount
     * usage: this is use for the calculate the steps and distance.
     * @param stepCount is use for coute the steps.
     */
    @Override
    public void setStepCount(long stepCount) {

        textSteps.setText(String.valueOf(stepCount - startSteps));

        double distance = getDistance(stepCount - startSteps);
        DecimalFormat format = new DecimalFormat("#.##");
        String twoDecis = format.format(distance);
        textDistance.setText(String.valueOf(twoDecis));

        walkData.setDistance(distance);
        walkData.setSteps(stepCount - startSteps);
    }

    /**
     * Method name: setTimeCount
     * usage: this is use for the set the hours, mins, seconds and millisecond in order to display it.
     */
    @Override
    public void setTimeCount(){
        textHour.setText(String.valueOf(time.getHour()));
        textMin.setText(String.valueOf(time.getMinute()));
        textSecond.setText(String.valueOf(time.getSecond()));
        textMillis.setText(String.valueOf(time.getMs()));
        System.out.println("minute: "+ valueOf(time.getMinute())+" second: "
                +valueOf(time.getSecond())+" ms: "+time.getMs());
        walkData.setTime(time);
    }

    /**
     * Method name: endWalk
     * Usage: After the user stop walk/run, this method will pass all the data into the sharedpreferences.
     */
    public void endWalk() {
        Intent intent = new Intent(this, StopWalkActivity.class);
        startActivity(intent);
        cancelTask();

        SharedPreferences prefs = getSharedPreferences("saved_walk_data", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(walkData);
        prefsEditor.putString("tmp_walk_data", json);
        prefsEditor.apply();

        finish();
    }

    @Override
    public void onBackPressed() {
    }

}