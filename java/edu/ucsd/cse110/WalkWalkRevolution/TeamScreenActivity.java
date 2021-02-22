package edu.ucsd.cse110.WalkWalkRevolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import androidx.appcompat.widget.Toolbar;


public class TeamScreenActivity extends UpdateActivity implements TeamScreenInterface{
    RecyclerView  TeamListView;
    TeamAdapter teamAdapter;
    ArrayList<String> TeamList = new ArrayList<String>();
    private String fitnessServiceKey;
    private NotificationService notificationService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_team_screen);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.Teamlistbar);
        setSupportActionBar(mToolbar);
        fitnessServiceKey = MockManager.getInstance().getKey();

        NotificationServiceFactory.put(fitnessServiceKey, new NotificationServiceFactory.BluePrint() {
            @Override
            public NotificationService create(UpdateActivity activity) {
                return new FirestoreAdapter(activity);
            }
        });
        notificationService = NotificationServiceFactory.create(fitnessServiceKey, this);
        notificationService.setup();

        Button back_home = (Button) findViewById(R.id.Back_walk_btn);
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_home();
            }
        });


        TeamListView = findViewById(R.id.TeamrecyclerView);
        teamAdapter = new TeamAdapter(TeamList,this);
        TeamListView .setAdapter(teamAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        TeamListView.addItemDecoration(dividerItemDecoration);

        TeamList = notificationService.fetchTeammate();
        ArrayList<String> invitee = notificationService.fetchInvitee();
        for( String s : invitee){
            TeamList.add(s);
        }


        Button go_schedule_walk = (Button) findViewById(R.id.Scheduled_walk_btn);
        go_schedule_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_schedule_walk();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invite_friend,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.addTeamate:
                startActivity(new Intent(this,InvitationScreen.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {


    }
    public void back_home(){
        Intent intent =  new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void go_schedule_walk(){
        Intent intent  = new Intent(this, HubPage.class);
        startActivity(intent);
    }
    @Override
    public void updateUI() {
        TeamListView = findViewById(R.id.TeamrecyclerView);
        teamAdapter = new TeamAdapter(TeamList,this);
        TeamListView.setAdapter(teamAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        TeamListView.addItemDecoration(dividerItemDecoration);
    }
}