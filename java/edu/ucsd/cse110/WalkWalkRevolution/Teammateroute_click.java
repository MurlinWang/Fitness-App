package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Teammateroute_click extends AppCompatActivity {
    private int routeIndex;
    private RouteList teamRouteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_teammate_route_list);

        Intent i = getIntent();
        int def = 0;
        int index = i.getIntExtra("route_index", def);
        routeIndex = index;

        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String routesJson = prefs.getString("teamRouteList","");


        teamRouteList = gson.fromJson(routesJson, RouteList.class);

        Route route = teamRouteList.routes.get(index);
        populateUI(route);

        Button teammate_btn_startPos = (Button)findViewById(R.id.teammate_start_route_btn);
        Button  teammate_propose = (Button)findViewById(R.id.teammate_propose_btn);
        teammate_propose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_propose();
            }
        });

        teammate_btn_startPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = GoogleMapAdapter.goToMap(teammate_btn_startPos.getText().toString());

                startActivity(mapIntent);
            }
        });


//        Switch fav = (Switch)findViewById(R.id.teammate_favorite_switch);
//    RadioButton terr1 = (RadioButton)findViewById(R.id.teammate_flat);
//    RadioButton terr2 = (RadioButton)findViewById(R.id.teammate_hilly);
//    RadioButton dif1 = (RadioButton)findViewById(R.id.teammate_easy);
//    RadioButton dif2 = (RadioButton)findViewById(R.id.teammate_difficult);
//    RadioButton even1 = (RadioButton)findViewById(R.id.teammate_even);
//    RadioButton even2 = (RadioButton)findViewById(R.id.teammate_uneven);
//    RadioButton trail1 = (RadioButton)findViewById(R.id.teammate_street);
//    RadioButton trail2 = (RadioButton)findViewById(R.id.teammate_trail);
//    RadioButton walk1 = (RadioButton)findViewById(R.id.teammate_loop);
//    RadioButton walk2 = (RadioButton)findViewById(R.id.teammate_outandback);
//
//        terr1.setEnabled(false);
//        terr2.setEnabled(false);
//        fav.setEnabled(false);
//        dif1.setEnabled(false);
//        dif2.setEnabled(false);
//        even1.setEnabled(false);
//        even2.setEnabled(false);
//        trail1.setEnabled(false);
//        trail2.setEnabled(false);
//        walk1.setEnabled(false);
//        walk2.setEnabled(false);
//
//
//    TextView note = findViewById(R.id.teammate_note_field_ev);
//        note.setEnabled(false);

}

    public void set_propose(){
        Intent intent = new Intent(this, setDateTime.class);
        startActivity(intent);
    }


    /**
     *   This method iterates through all the fields of Route r, setting UI elements to reflect
     *   the stored fields (null checks happen here)
     * @param r The Route r that is iterated through to populate the UI elements
     */
    public void populateUI(Route r){

        // Update name and time fields accordingly
        TextView textName = findViewById(R.id.teammate_route_name_et);
        textName.setText(r.name);

        Button textStartp = (Button)findViewById(R.id.teammate_start_location_et);
        textStartp.setText(r.startLocation);

        TextView timeDisplay = (TextView) findViewById(R.id.teammate_time_display);
        TextView msDisplay = (TextView) findViewById(R.id.teammate_ms_display);

        TextView distanceDisplay = (TextView) findViewById(R.id.teammate_distance_display);
        TextView stepsDisplay = (TextView) findViewById(R.id.teammate_steps_display);


        WalkData walkData = r.walkdata;
        Time time = walkData.getTime();

        // If time is 0 or null, print default values
        if (time == null || time.getHour() == 0 && time.getMinute() == 0 && time.getMs() == 0 && time.getSecond() == 0) {
            timeDisplay.setText("--:--:--");
            msDisplay.setText("---");

        } else {
            timeDisplay.setText(String.format("%2d:%2d:%2d", time.getHour(), time.getMinute(), time.getSecond()));
            msDisplay.setText(String.format("%3d", time.getMs()));

        }

        // If we have no steps and distance, print default values
        if(walkData.getDistance() != 0 && walkData.getSteps() != 0){
            distanceDisplay.setText(String.format("%5.2f", walkData.getDistance()));
            stepsDisplay.setText(String.format("%7d", walkData.getSteps()));

        }else {
            distanceDisplay.setText("--.-- Mi");
            stepsDisplay.setText("------");
        }

        // Get all switches to change
        Switch fav = (Switch)findViewById(R.id.teammate_favorite_switch);
        RadioButton terr1 = (RadioButton)findViewById(R.id.teammate_flat);
        RadioButton terr2 = (RadioButton)findViewById(R.id.teammate_hilly);
        RadioButton dif1 = (RadioButton)findViewById(R.id.teammate_easy);
        RadioButton dif2 = (RadioButton)findViewById(R.id.teammate_difficult);
        RadioButton even1 = (RadioButton)findViewById(R.id.teammate_even);
        RadioButton even2 = (RadioButton)findViewById(R.id.teammate_uneven);
        RadioButton trail1 = (RadioButton)findViewById(R.id.teammate_street);
        RadioButton trail2 = (RadioButton)findViewById(R.id.teammate_trail);
        RadioButton walk1 = (RadioButton)findViewById(R.id.teammate_loop);
        RadioButton walk2 = (RadioButton)findViewById(R.id.teammate_outandback);

        terr1.setEnabled(false);
        terr2.setEnabled(false);
        fav.setEnabled(false);
        dif1.setEnabled(false);
        dif2.setEnabled(false);
        even1.setEnabled(false);
        even2.setEnabled(false);
        trail1.setEnabled(false);
        trail2.setEnabled(false);
        walk1.setEnabled(false);
        walk2.setEnabled(false);

        // Set all the switches by the routes data
        if(r.isFavorite == Boolean.TRUE ){
            fav.setChecked(Boolean.TRUE);
        }
        else{
            fav.setChecked(Boolean.FALSE);
        }
        if(r.isFlat == 1){
            terr1.setChecked(true);
        }
        else if (r.isFlat == 2){
            terr2.setChecked(true);
        }
        if(r.isDifficult == 2){
            dif1.setChecked(true);
        }else if(r.isDifficult == 1){
            dif2.setChecked(true);
        }
        if(r.isEven == 1) {
            even1.setChecked(true);
        }
        else if(r.isEven == 2) {
            even2.setChecked(true);
        }
        if(r.isTrail == 2){
            trail1.setChecked(true);
        }
        else if(r.isTrail == 1){
            trail2.setChecked(true);
        }
        if(r.isLoop == 1){
            walk1.setChecked(true);
        }
        else if(r.isLoop == 2){
            walk2.setChecked(true);
        }
        TextView note = findViewById(R.id.teammate_note_field_ev);
        note.setText(r.notes);
        note.setEnabled(false);


        // startWalkButton functionality
        Button startWalkButton = (Button) findViewById(R.id.teammate_start_route_btn);
        startWalkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startWalk(routeIndex);
                finish();
            }
        });
    }
    // Starts a new Walk
    public void startWalk(int routeIndex){

    
        SharedPreferences sharedPref = getSharedPreferences("saved_route_index", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("tmp_route_index", routeIndex);
        editor.apply();

        Log.d("tmp", "The route index is " + routeIndex + "!\n");

        Intent intent = new Intent(this, RouteWalkActivity.class);

        startActivity(intent);
        finish();
    }

    // Override backpressed behavior to els us go back to the old activity, finishing the old one
    @Override
    public void onBackPressed(){

        startActivity(new Intent(this, Route_List_page.class));
        finish();
    }

}