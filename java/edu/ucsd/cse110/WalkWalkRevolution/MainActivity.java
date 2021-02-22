package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.text.DecimalFormat;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;

/**
 * The Walk Walk Revolution program main activity is where the app is launched from.
 */
public class MainActivity extends StepCountActivity {
    String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 100;

    /**
     * This method is used to set up the UI for the main activity and connect its features.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationServiceFactory.put(serviceKey, new NotificationServiceFactory.BluePrint() {
            @Override
            public NotificationService create(UpdateActivity activity) {
                return new FirestoreAdapter(activity);
            }
        });
        notificationService = NotificationServiceFactory.create(serviceKey, this);
        notificationService.setup();
        initNotificationUpdateListener();


        // call height
        SharedPreferences heightPref = getSharedPreferences("user_height", MODE_PRIVATE);

        // run main activity if height is set
        setContentView(R.layout.activity_main);
        textSteps = findViewById(R.id.dailyStepsField);
        textDistance = findViewById(R.id.dailyDistanceField);
        int feet = heightPref.getInt("feet", 0);
        int inches = heightPref.getInt("inches", 0);
        heightInInch = feet * 12 + inches;

        setPreviousWalkData();

        // Populate previous Walk Data
        Button btnGoToSteps = findViewById(R.id.start_btn);
        btnGoToSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStartWalkActivity();
            }
        });

        Button Route_list = (Button) findViewById(R.id.route_list_btn);
        Route_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(Route_List_page.class);
            }
        });
        Button team_list = (Button) findViewById(R.id.team_list_btn);
        team_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(TeamScreenActivity.class);
            }
        });

        setupFitnessServiceAndThread(R.id.mock_btn_main);
        fitnessService.updateStepCount();


    }


    /**
     * This method is for launching the walk activity when the button is pressed.
     * It will also stop the async thread and end the main activity.
     */
    public void launchStartWalkActivity() {
        Intent intent = new Intent(this, StartWalkActivity.class);
        intent.putExtra(StartWalkActivity.FITNESS_SERVICE_KEY, serviceKey);
        intent.putExtra(StartWalkActivity.START_STEPS, Integer.parseInt(textSteps.getText().toString()));
        cancelTask();
        startActivity(intent);
        finish();
    }

    /**
     * This method is called in onCreate(). It gets the previous walk's data from
     * the programs saved files and displays the data on the activity UI.
     */
    public void setPreviousWalkData(){

        TextView prevStepsField = findViewById(R.id.prev_steps_field);
        TextView prevDistanceField = findViewById(R.id.prev_distance_field);

        // Display previous Walk stats using WalkData object in Shared Preferences
        SharedPreferences prefs = getSharedPreferences("saved_walk_data", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString("previous_walk_data", "");

        Log.d("TAG", "This is the json string: " + json);

        WalkData walkData = new WalkData();
        if(!json.equals("")) {
            walkData = gson.fromJson(json, walkData.getClass());
            prevStepsField.setText(String.valueOf(walkData.getSteps()));

            // My version of formatted distance
            DecimalFormat format = new DecimalFormat("#.##");
            String twoDecis = format.format(walkData.getDistance());
            Log.d("TAG", "This is the WalkData.distance: " + walkData.getDistance() + "\n\n\n");
            Log.d("TAG", "This is the WalkData.distance: " + twoDecis + "\n\n\n");
            prevDistanceField.setText(twoDecis + " miles");

            Log.d("TAG", "This is the WalkData.distance: " + walkData.getDistance() + "\n\n\n");

            // Display WalkData time
            setTimeCount(walkData.getTime());
        }
        else{ // No previous WalkData initialized
            prevStepsField.setText("");
            prevDistanceField.setText("");
            clearTime();
        }
    }

    /**
     * This method is called everything the asynchronous thread updates the UI.
     * This method gets the newest state of the time and updates the UI.
     * @param time
     */
    public void setTimeCount(Time time){

        Log.d("TAG", "This is the Time: " + time);

        TextView textHour = findViewById(R.id.textHour);
        TextView textMin = findViewById(R.id.textMin);
        TextView textSecond = findViewById(R.id.textSecond);
        TextView textMillis = findViewById(R.id.textMillis);

        textHour.setText(String.valueOf(time.getHour()));
        textMin.setText(String.valueOf(time.getMinute()));
        textSecond.setText(String.valueOf(time.getSecond()));
        textMillis.setText(String.valueOf(time.getMs()));
    }

    /**
     * This method will set the time UI to a default empty state if there is
     * no data to load onto the UI.
     */
    public void clearTime(){

        TextView textHour = findViewById(R.id.textHour);
        TextView textMin = findViewById(R.id.textMin);
        TextView textSecond = findViewById(R.id.textSecond);
        TextView textMillis = findViewById(R.id.textMillis);

        textHour.setText("--");
        textMin.setText("--");
        textSecond.setText("--");
        textMillis.setText("");
    }

    /**
     * This method is called from the asynchronous thread with the fitness
     * service step value. This method will update the UI to display the most
     * recent number of steps and to calculate and display the new distance walked.
     * @param stepCount
     */
    @Override
    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount));
        double distance = getDistance(stepCount);
        DecimalFormat format = new DecimalFormat("#.##");
        String twoDecis = format.format(distance);
        textDistance.setText(twoDecis + " miles");
    }

    /**
     * Overrides the native back button.
     * Cancels the asynchronous thread and cancels the main activity.
     */
    @Override
    public void onBackPressed() {
        cancelTask();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invitation_center,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.teammate_invite_status:
                startActivity(new Intent(this,InvitationCenter.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }




    private void initNotificationUpdateListener() {
        notificationService.subscribeToInvitations(notifications -> notifications.forEach(notification -> {

          //TODO
        }));
        notificationService.subscribeToAcceptances(notifications -> notifications.forEach(notification -> {
            //TODO
        }));
    }
}
