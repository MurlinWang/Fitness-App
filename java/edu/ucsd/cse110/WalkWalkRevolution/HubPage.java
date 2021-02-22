package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HubPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huge_page);

        Button route_list = (Button) findViewById(R.id.huge_page_go_to_routelist_btn);
        Button go_to_walk = (Button) findViewById(R.id.huge_page_go_to_walk_btn);
        TextView display_text  = (TextView) findViewById(R.id.huge_showing_msg);


        //TODO add real isScheduled,walkExists,isInviter boolean value
        boolean walkExists = false;
        boolean isScheduled = true;
        boolean isInviter = true;

        if(walkExists &&  isScheduled){
            display_text.setText("You have a scheduled walk!\nClick the GO TO WALK button to change your status or begin");
        }else if(walkExists && !isScheduled && !isInviter) {
            display_text.setText("You have a pending unscheduled walk!\nClick the GO TO WALK button to change your status");

        }else if(walkExists && !isScheduled && isInviter) {
            display_text.setText("You currently have an unscheduled proposed walk!\nClick the GO TO WALK button to schedule it");
        }else{
            display_text.setText("You don't have a proposed or scheduled walk yet...\nif you want to start one, please click the ROUTE LIST button.");
            go_to_walk.setEnabled(false);
        }

        if(isInviter && isScheduled){
            go_to_walk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchinviter_start_schedule();
                }
            });

        }else if(isInviter  && !isScheduled){
            go_to_walk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchinviter_schedule();
                }
            });
        }else if (!isInviter){
            go_to_walk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchinvitee_action();
                }
            });
        }

        route_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRoutelist();
            }
        });

    }
    public void  launchRoutelist(){
        Intent intent = new Intent(this, Route_List_page.class);
        startActivity(intent);
    }
    public void  launchinviter_start_schedule(){
        Intent intent = new Intent(this, inviterScheduled.class);
        startActivity(intent);
    }
    public void launchinviter_schedule(){
        Intent intent = new Intent(this, inviterProposed.class);
        startActivity(intent);
    }

    public void launchinvitee_action(){
        Intent intent = new Intent(this, inviteePlanning.class);
        startActivity(intent);
    }



}
