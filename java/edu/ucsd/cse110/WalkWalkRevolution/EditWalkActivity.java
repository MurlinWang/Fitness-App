package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

// TODO MS2I1: Implement next iteration, low priority and easy done after refactoring DataEntry code
public class EditWalkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_route_list);

        Intent i = getIntent();
        int def = 0;
        int index = i.getIntExtra("route_index", def);

        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("user_height", Context.MODE_PRIVATE);
        RouteList routeList = new RouteList(sharedPreferences.getString("email", ""));
        // Get routesList from JSON string
        Gson gson = new Gson();
        String routesJson = prefs.getString("routeList", "");

        // Initialize routes list for first use
        if(routesJson.equals("")){
            routeList = new RouteList(sharedPreferences.getString("email", ""));
        }
        else {
            routeList = gson.fromJson(routesJson, routeList.getClass());
            routeList.setUserEmail(getSharedPreferences
                    ("user_height", Context.MODE_PRIVATE).getString("email", ""));
        }
        Route route = routeList.routes.get(index);
        populateUI(route);
    }

    public void populateUI(Route r){
        TextView textName = findViewById(R.id.route_name_et);
        textName.setText(r.name, TextView.BufferType.EDITABLE);

        TextView textStartp = (TextView)findViewById(R.id.start_location_et);
        textStartp.setText(r.startLocation);

        TextView timeDisplay = (TextView) findViewById(R.id.time_display);
        TextView msDisplay = (TextView) findViewById(R.id.ms_display);

        TextView distanceDisplay = (TextView) findViewById(R.id.distance_display);
        TextView stepsDisplay = (TextView) findViewById(R.id.steps_display);


        WalkData walkData = r.walkdata;
        Time time = walkData.getTime();

        if (time == null || time.getHour() == 0 && time.getMinute() == 0 && time.getMs() == 0 && time.getSecond() == 0) {
            // Might have to change default-behavior to be centered
            timeDisplay.setText("--:--:--");
            msDisplay.setText("---");

        } else {
            timeDisplay.setText(String.format("%2d:%2d:%2d", time.getHour(), time.getMinute(), time.getSecond()));
            msDisplay.setText(String.format("%3d", time.getMs()));

        }
        if(walkData.getDistance() != 0 && walkData.getSteps() != 0){
            distanceDisplay.setText(String.format("%5.2f", walkData.getDistance()));
            stepsDisplay.setText(String.format("%7d", walkData.getSteps()));

        }else {
            distanceDisplay.setText("--.-- Mi");
            stepsDisplay.setText("------");
        }

        Switch fav = (Switch)findViewById(R.id.favorite_switch);
        RadioButton terr1 = (RadioButton)findViewById(R.id.flat);
        RadioButton terr2 = (RadioButton)findViewById(R.id.hilly);
        RadioButton dif1 = (RadioButton)findViewById(R.id.easy);
        RadioButton dif2 = (RadioButton)findViewById(R.id.difficult);
        RadioButton even1 = (RadioButton)findViewById(R.id.even);
        RadioButton even2 = (RadioButton)findViewById(R.id.uneven);
        RadioButton trail1 = (RadioButton)findViewById(R.id.street);
        RadioButton trail2 = (RadioButton)findViewById(R.id.trail);
        RadioButton walk1 = (RadioButton)findViewById(R.id.loop);
        RadioButton walk2 = (RadioButton)findViewById(R.id.outandback);


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
        if(r.isDifficult == 1){
            dif1.setChecked(true);
        }else if(r.isDifficult == 2){
            dif2.setChecked(true);
        }
        if(r.isEven == 1) {
            even1.setChecked(true);
        }
        else if(r.isEven == 2) {
            even2.setChecked(true);
        }
        if(r.isTrail == 1){
            trail1.setChecked(true);
        }
        else if(r.isTrail == 2){
            trail2.setChecked(true);
        }
        if(r.isLoop == 1){
            walk1.setChecked(true);
        }
        else if(r.isLoop == 2){
            walk2.setChecked(true);
        }
        TextView note = findViewById(R.id.note_field_ev);
        note.setText(r.notes, TextView.BufferType.EDITABLE);

    }
}