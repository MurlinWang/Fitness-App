package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Route_List_page extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_route_list);

        Button my_route =  (Button) findViewById(R.id.my_route_btn);
        Button team_route =  (Button) findViewById(R.id.team_route_button);
        Button back_home = (Button) findViewById(R.id.home_team_btn);
        my_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_myroute();
            }
        });
        team_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_teamroute();
            }
        });
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_home();

            }
        });


    }
    public void go_myroute(){
        Intent intent = new Intent(this,RouteListActivity.class);
        startActivity(intent);
    }
    public void go_teamroute(){
        Intent intent = new Intent(this,TeamRouteListActivity.class);
        startActivity(intent);
    }
    public void back_home(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}