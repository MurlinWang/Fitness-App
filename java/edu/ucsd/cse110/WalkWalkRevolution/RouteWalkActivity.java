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

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;

import static java.lang.String.valueOf;


/**
 * class name: RouteWalkActivity
 * parameter:
 * usage: this activity class is used to display distance and steps for
 * walk that start on the route
 */
// TODO: refactor to have RouteWalkActivity and NewWalkActivity inherit/extend their shared methods
public class RouteWalkActivity extends StepCountActivity{

    public static final String START_STEPS = "START_STEPS";
    NotificationService service;
    String fitnessServiceKey;

    // For logging
    private static final String TAG = "RouteWalkActivity";

    // UI Elements
    private TextView textSteps;
    private TextView textDistance;
    private TextView textHour;
    private TextView textMin;
    private TextView textSecond;
    private TextView textMillis;
    private TextView routeDisplay;

    // variables for steps and distance calculations
    private long startSteps;

    public WalkData walkData = new WalkData(new Time(), 0, 0.0);

    @Override
    /**
     * method name : onCreate(Bundle savedIntanceState)
     * parameter : Bundle savedInstanceState
     * usage: this method is used to set up the screen and activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_walk);

        fitnessServiceKey = MockManager.getInstance().getKey();

        NotificationServiceFactory.put(fitnessServiceKey, new NotificationServiceFactory.BluePrint() {
            @Override
            public NotificationService create(UpdateActivity activity) {
                return new FirestoreAdapter(activity);
            }
        });

        service = NotificationServiceFactory.create(fitnessServiceKey, this);
        service.setup();

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

        // Sets default value for startSteps (to be overwritten in setSteps() method in first call
        startSteps = -1;

        Button btnStopWalk = findViewById(R.id.buttonStopWalk);
        btnStopWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endWalk();
            }
        });

        setupFitnessServiceAndThread(R.id.mock_btn_route);

        // Use the routeList and the current index to get the current Route object
        Route currentRoute = this.getRouteList().get(this.getRouteIndex());
        routeDisplay = (TextView) findViewById(R.id.walk_title);
        routeDisplay.setText(currentRoute.getName());
    }

    @Override
    /**
     * method name: onActivityResult(int requestCode, int resultCode, Intent data)
     * parameter :int requestCode, int resultCode, Intent data
     * usage: the method is used to connect to google fit and update
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
     * method name: setStepCount
     * @param stepCount
     * usage: this method is used to set the step for walk screen
     */
    @Override
    public void setStepCount(long stepCount) {

        if(startSteps == -1){
            startSteps = stepCount;
        }

        textSteps.setText(String.valueOf(stepCount - startSteps));
        System.out.println(stepCount-startSteps);

        double distance = getDistance(stepCount - startSteps);
        DecimalFormat format = new DecimalFormat("#.##");
        String twoDecis = format.format(distance);
        textDistance.setText(twoDecis);

        walkData.setDistance(distance);
        walkData.setSteps((int)(stepCount - startSteps));
    }

    /**
     * method name: setTimeCount
     * parameter:
     * usage: set the time for walk screen
     */
    @Override
    public void setTimeCount(){
        textHour.setText(String.valueOf(time.getHour()));
        textMin.setText(String.valueOf(time.getMinute()));
        textSecond.setText(String.valueOf(time.getSecond()));
        textMillis.setText(String.valueOf(time.getMs()));
        System.out.println("minute: " + time.getMinute() + " second: "
                +time.getSecond() + " ms: " + time.getMs());
        walkData.setTime(time);
    }

    /**
     * method name: endWalk
     * usage: the method is used to save the walk data and pass to route
     */
    public void endWalk() {

        cancelTask();

        int routeIndex = getRouteIndex();

        Route route;
        if(routeIndex != -1) {
            // Save our walkData into our chosen route object
            RouteList routeList = this.getRouteList();
           // route = routeList.routes.get(routeIndex);
            routeList.updateWalkData(walkData, routeIndex);
            service.updateWalkData(walkData, routeIndex, getSharedPreferences
                    ("user_height", Context.MODE_PRIVATE).getString("userid", ""), routeList);
            this.saveWalkData(walkData);
            this.saveRouteList(routeList);

        }
        launchActivity(RouteListActivity.class);
    }

    /**
     * name: getRouteIndex
     * @return the index of the route in the list
     *
     */
    public int getRouteIndex(){
        SharedPreferences prefs = getSharedPreferences("saved_route_index", MODE_PRIVATE);
        return prefs.getInt("tmp_route_index", -1);
    }

    /**
     * name: getRouteList
     * @return
     * usage: it is used to return the routelist from sharedpreference
     */
    public RouteList getRouteList(){
        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Get routesList from JSON string
        Gson gson = new Gson();
        String routesJson = prefs.getString("routeList", "");

        RouteList routeList = new RouteList(getSharedPreferences
                ("user_height", Context.MODE_PRIVATE).getString("userid", ""));

        // Initialize routes list for first use
        if(!routesJson.equals("")){
            routeList = gson.fromJson(routesJson, routeList.getClass());
        }

        return routeList;
    }

    /**
     * name: saveRouteList
     * @param routes
     * usage: save the routelist to shared perreference
     */
    public void saveRouteList(RouteList routes){
        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save routes into RouteList
        Gson gson = new Gson();
        String newRouteListJson = gson.toJson(routes);
        editor.putString("routeList", newRouteListJson);
        editor.apply();
    }

    /**
     * method name: saveWalkData
     * @param previousWalkData
     * usage: save the current walk data
     */
    public void saveWalkData(WalkData previousWalkData){

        SharedPreferences sharedPref = getSharedPreferences("saved_walk_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Save previousWalkData object to gson
        Gson gson = new Gson();
        String json = gson.toJson(previousWalkData);
        editor.putString("previous_walk_data", json);
        editor.apply();
    }

    /**
     * Overrides the native back button to do nothing.
     */
    @Override
    public void onBackPressed() {
    }
}