
package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
 * Add Route activity adds route with unll WalkData straight from the Routes List screen
 */
public class AddNewRouteListActivity extends UpdateActivity {

    NotificationService service;
    String fitnessServiceKey;
    /**
     *  On Creation, sets up the correct layout and makes sure the time, ms, distance, and steps
     *    to some default string values
     *
     *  Also set's up doneButton activity to moveBackToRouteList() saving the current data, while
     *    the cancel button goes back to the routes list without changing anything
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_route_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fitnessServiceKey = MockManager.getInstance().getKey();

        NotificationServiceFactory.put(fitnessServiceKey, new NotificationServiceFactory.BluePrint() {
            @Override
            public NotificationService create(UpdateActivity activity) {
                return new FirestoreAdapter(activity);
            }
        });

        service = NotificationServiceFactory.create(fitnessServiceKey, this);
        service.setup();

        // DISPLAY WHAT'S IN WALKDATA OBJECT
        TextView timeDisplay = (TextView) findViewById(R.id.time_display);
        TextView msDisplay = (TextView) findViewById(R.id.ms_display);

        TextView distanceDisplay = (TextView) findViewById(R.id.distance_display);
        TextView stepsDisplay = (TextView) findViewById(R.id.steps_display);

        timeDisplay.setText("--:--:--");
        msDisplay.setText("---");

        distanceDisplay.setText("--.-- Mi");
        stepsDisplay.setText("------");

        // Go back to the Routes List saving Routes
        Button doneButton = (Button) findViewById(R.id.done_btn);
        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                moveBackToRouteList();
            }
        });

        // Go back to the Routes List without saving any of the data
        Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                returnRouteList();
                finish();
            }
        });
    }

    // Override back pressed to return to the routes list without saving (~ cancel button function)
    @Override
    public void onBackPressed(){
        returnRouteList();
        finish();
    }

    /**
     *  moveBackToRouteList() moves back to the routes list ensuring that the name field is
     * complete, and creating the routes from it
     */
    public void moveBackToRouteList(){
        Route route = new Route();
        route.walkdata = new WalkData();
        populateFeatures(route);
        if(route.name == null || route.name.equals("")){
            Toast needName = Toast.makeText(getApplicationContext(),"Must enter name to save route!",
                    Toast.LENGTH_LONG);
            needName.show();
        }else {
            addToRoutesList(route);
            // Go back Home
            returnRouteList();
            finish();
        }

    }

    /**
     *  populateFeatures() takes in the data from the UI screen text, switch, and radio fields
     *    and populates newRoute features
     * @param newRoute the empty (potentially initialized with walkData) routes with fields
     *                 from the UI fields on screen
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
     *  Takes in a route, and unravels the JSON object to add the current route the RouteList object,
     *    and sorting the RouteList object to keep the objects in a reasonable order
     * @param route The route parameter is the completely populated route that is ready to be
     *              added to the (potentially null on initialization) routeList object
     */
    public void addToRoutesList(Route route){

        // Initialize SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("user_height", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get routesList from JSON string
        Gson gson = new Gson();
        String routesJson = prefs.getString("routeList", "");

        RouteList routeList = new RouteList(sharedPreferences.getString("userid", ""));
        // Initialize routes list for first use
        if(routesJson.equals("")){
            routeList.addRoute(route);
            service.addRoute(route, sharedPreferences.getString("userid",""));

        }else {
            routeList = gson.fromJson(routesJson, routeList.getClass());
            routeList.setUserEmail(sharedPreferences.getString("userid", ""));
            routeList.addRoute(route);
            service.addRoute(route, sharedPreferences.getString("userid",""));
        }

        // Sort routeList now that we added an element
        routeList.sort();

        // Save new RoutesList back into the same SharedPreferences key
        String newRouteListJson = gson.toJson(routeList);
        editor.putString("routeList", newRouteListJson);
        editor.apply();
    }

    /**
     *  returnRouteList() goes back to routesList without saving any data, terminating this activity
     *      and initializing an intent to move on
     */
    public void returnRouteList(){
        Intent intent = new Intent(this, RouteListActivity.class);
        //intent.putExtra(StopWalkActivity.FITNESS_SERVICE_KEY, serviceKey);
        startActivity(intent);
    }
}

