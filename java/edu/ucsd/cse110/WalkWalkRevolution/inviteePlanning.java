package edu.ucsd.cse110.WalkWalkRevolution;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class inviteePlanning extends AppCompatActivity implements invitee_page_interface{

    RecyclerView ivitee_recyclerview;
    invitee_page_adapter invitee_page_adapter;
    ArrayList<String> Teammate_name = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_invitee_page);


        ivitee_recyclerview = findViewById(R.id.Invitee_recyclerView);
        invitee_page_adapter = new invitee_page_adapter(Teammate_name, this);
        ivitee_recyclerview.setAdapter(invitee_page_adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        ivitee_recyclerview.addItemDecoration(dividerItemDecoration);

        Teammate_name.add("junkai wang");
        Teammate_name.add("blake");
        Teammate_name.add("joe");
        Teammate_name.add("wei");
        Teammate_name.add("wang");
        Teammate_name.add("Adi");

        //TODO add real isScheduled boolean value
        boolean isScheduled = false;


        if (!isScheduled) {
            Button start = (Button) findViewById(R.id.invitee_start);
            start.setEnabled(false);
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}