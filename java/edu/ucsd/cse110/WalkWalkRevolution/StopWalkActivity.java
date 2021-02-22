package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;


/**
 * StopWalkActivity store the data after the user stop the walk/run.
 */
public class StopWalkActivity extends UpdateActivity {

    private static final String TAG = StopWalkActivity.class.getName();

    /**
     * Method name:onCreate
     * Usage: to set the page of our project as activity_stop_walk, and store the WalkDate by the Gson.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_walk);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText targetEditText = (EditText)findViewById(R.id.route_name_et);

        notificationServiceSetup();

        SharedPreferences prefs = getSharedPreferences("saved_walk_data", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString("tmp_walk_data", "");
        WalkData walkData = gson.fromJson(json, WalkData.class);

        Time time = walkData.getTime();

        Log.d(TAG, String.format("%2d:%2d:%2d", time.getHour(), time.getMinute(), time.getSecond()));

        // DISPLAY WHAT'S IN WALKDATA OBJECT
        TextView timeDisplay = (TextView) findViewById(R.id.time_display);
        TextView msDisplay = (TextView) findViewById(R.id.ms_display);

        TextView distanceDisplay = (TextView) findViewById(R.id.distance_display);
        TextView stepsDisplay = (TextView) findViewById(R.id.steps_display);


//once we don't have the information about the time,distance and steps, the initial format of time distance and step will display.
// if else, it will display the real data
        if(timeDisplay == null || distanceDisplay == null || stepsDisplay == null){
            // Might have to change default-behavior to be centered
            timeDisplay.setText("--:--:--");
            msDisplay.setText("---");

            distanceDisplay.setText("--.-- Mi");
            stepsDisplay.setText("------");

        }
        else{
            timeDisplay.setText(String.format("%2d:%2d:%2d", time.getHour(), time.getMinute(), time.getSecond()));
            msDisplay.setText(String.format("%3d",time.getMs()));

            distanceDisplay.setText(String.format("%5.2f", walkData.getDistance()));
            stepsDisplay.setText(String.format("%7d", walkData.getSteps()));
        }




        Button doneButton = (Button) findViewById(R.id.done_btn);

        doneButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method name: onclick
             * Usage: set the toast and message information and condition once we tap done the StopWalkActivity
             */
            @Override
            public void onClick(View view) {

                // Make a route, and populate it's features with UI elements
                Route route = new Route();
                route.walkdata = walkData;
                populateFeatures(route);



                Log.d("TAG", "This is the Route found: " + route);

                Log.d("TAG", "This is the name found: " + route.name);

                // Also do a null check to require name
                if(route.name == null || route.name.equals("")){
                    Toast needName = Toast.makeText(getApplicationContext(),"Must enter name to save route!",
                                            Toast.LENGTH_LONG);
                    needName.show();
                }else {

                    saveWalkData(walkData);
                    addToRoutesList(route);

                    // Go route list
                    returnRouteList();
                    finish();
                }
            }
        });


        Button cancelButton = (Button) findViewById(R.id.cancel_btn);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This is use for the cancel button. once we tap the cancel, then will go back to the homescrren.
             */
            @Override
            public void onClick(View view) {
                returnHomeScreen();
                finish();
            }
        });

    }

    /**
     * this is feature for the type of route such as the walk type, the difficult of the route etc..
     */
    public void populateFeatures(Route newRoute){
        String name = ((EditText) findViewById(R.id.route_name_et)).getText().toString();
        String startLocation = ((EditText) findViewById(R.id.start_location_et)).getText().toString();
        Boolean isFavorite = ((Switch) findViewById(R.id.favorite_switch)).isEnabled();

        int isFlat = 0;
        if( ((RadioButton) findViewById(R.id.flat)).isChecked()){
            isFlat = 1;
        }else if( ((RadioButton) findViewById(R.id.hilly)).isChecked()){
            isFlat = 2;
        }

        int isDifficult = 0;
        if( ((RadioButton) findViewById(R.id.difficult)).isChecked()){
            isDifficult = 1;
        }else if( ((RadioButton) findViewById(R.id.easy)).isChecked()){
            isDifficult = 2;
        }

        int isEven = 0;
        if( ((RadioButton) findViewById(R.id.even)).isChecked()){
            isEven = 1;
        }else if( ((RadioButton) findViewById(R.id.uneven)).isChecked()){
            isEven = 2;
        }

        int isTrail = 0;
        if( ((RadioButton) findViewById(R.id.trail)).isChecked()){
            isTrail = 1;
        }else if( ((RadioButton) findViewById(R.id.street)).isChecked()){
            isTrail = 2;
        }

        int isLoop = 0;
        if( ((RadioButton) findViewById(R.id.loop)).isChecked()){
            isLoop = 1;
        }else if( ((RadioButton) findViewById(R.id.outandback)).isChecked()){
            isLoop = 2;
        }

        String notes = ((EditText) findViewById(R.id.note_field_ev)).getText().toString();

        newRoute.setUIFeatures(name, startLocation, isFavorite, isFlat, isDifficult, isEven, isTrail,
                         isLoop, notes);
    }

    /**
     *
     * add the route to the route list once we tap the done button in the StopWalkActivity.
     */
    public void addToRoutesList(Route route){

        // SAVE IT TO SHARED PREFERENCES
        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Get routesList from JSON string
        Gson gson = new Gson();
        String routesJson = prefs.getString("routeList", "");

        RouteList routeList =new RouteList(getSharedPreferences
                ("user_height", Context.MODE_PRIVATE).getString("userid", ""));

        // Initialize routes list for first use
        if(routesJson.equals("")){
            routeList.addRoute(route);
            notificationService.addRoute(route, getSharedPreferences
                    ("user_height", Context.MODE_PRIVATE).getString("userid", ""));
        }else {
            routeList = gson.fromJson(routesJson, routeList.getClass());
            routeList.setUserEmail(getSharedPreferences
                    ("user_height", Context.MODE_PRIVATE).getString("userid", ""));
            routeList.addRoute(route);
            notificationService.addRoute(route, getSharedPreferences
                    ("user_height", Context.MODE_PRIVATE).getString("userid", ""));
        }

        // Sort routeList now that we added an element
        routeList.sort();

        // Save new RoutesList back into the same SharedPreferences key
        String newRouteListJson = gson.toJson(routeList);
        editor.putString("routeList", newRouteListJson);
        editor.apply();
    }

    // saves current WalkData to key "previousWalkData" in SharedPreferences
    public void saveWalkData(WalkData previousWalkData){

        SharedPreferences sharedPref = getSharedPreferences("saved_walk_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Save previousWalkData object to gson
        Gson gson = new Gson();
        String json = gson.toJson(previousWalkData);
        editor.putString("previous_walk_data", json);
        editor.apply();
    }

    // Goes back to the home screen
    public void returnHomeScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(StopWalkActivity.FITNESS_SERVICE_KEY, serviceKey);
        startActivity(intent);
    }
    //goes to the RouteList
    public void returnRouteList(){
        Intent intent = new Intent(this, RouteListActivity.class);
        //intent.putExtra(StopWalkActivity.FITNESS_SERVICE_KEY, serviceKey);
        startActivity(intent);
    }
    //go back home screen.
    @Override
    public void onBackPressed() {
        returnHomeScreen();
        finish();
    }
}