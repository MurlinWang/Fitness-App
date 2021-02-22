package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class inviterProposed extends AppCompatActivity implements propose_interface {
    RecyclerView proposeView;
    Propose_adapter propose_adapter;
    ArrayList<String> propose_name = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propose_invite);

        TextView date = findViewById(R.id.display_date);
        TextView time = findViewById(R.id.display_time);
        ArrayList<String> proposeTime = (ArrayList<String>) getIntent().getSerializableExtra("time");
        date.setText(proposeTime.get(0) + "." + proposeTime.get(1) + "." + proposeTime.get(2));
        time.setText(proposeTime.get(3) + " : " + proposeTime.get(4));
        proposeView =  findViewById(R.id.propose_recylerview);
       propose_adapter = new Propose_adapter(propose_name,this);
        proposeView.setAdapter(propose_adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        proposeView.addItemDecoration(dividerItemDecoration);

        propose_name.add("a");
        propose_name.add("b");
        propose_name.add("c");
        propose_name.add("d");
        propose_name.add("e");
        propose_name.add("f");
        propose_name.add("g");
        propose_name.add("h");
        propose_name.add("l");
        propose_name.add("m");
        propose_name.add("pproppose_name");


        Button schedule_walk = (Button) findViewById(R.id.propose_schedule);
        Button withdraw_walk =(Button) findViewById(R.id.propose_withdraw);



        schedule_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule();
            }
        });
        withdraw_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdraw();
            }
        });

    }


    @Override
    public void onItemClick(int position) {

    }
    //TODO need to implement schedule walk
    public void schedule(){
        Intent intent = new Intent(this, HubPage.class);
        startActivity(intent);
    }
    //TODO need to implement withdraw walkk
    public void withdraw(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
