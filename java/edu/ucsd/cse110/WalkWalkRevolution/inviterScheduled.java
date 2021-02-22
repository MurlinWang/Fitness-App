package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class inviterScheduled extends AppCompatActivity implements inviter_page_interface{

    RecyclerView iviter_recyclerview;
    inviter_page_adapter inviter_page_adapter;
    ArrayList<String> Teammate_name = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_inviter_page);


        iviter_recyclerview =  findViewById(R.id.Inviter_recyclerView);
        inviter_page_adapter = new inviter_page_adapter(Teammate_name,this);
        iviter_recyclerview.setAdapter(inviter_page_adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        iviter_recyclerview.addItemDecoration(dividerItemDecoration);

        Teammate_name.add("junkai wang");
        Teammate_name.add("blake");
        Teammate_name.add("joe");
        Teammate_name.add("wei");
        Teammate_name.add("wang");
        Teammate_name.add("Adi");

        Button withdraw_btn = (Button)findViewById(R.id.inviter_withdraw);
        withdraw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdraw();
            }
        });
    }
     public void withdraw(){
         Intent intent = new Intent(this, HubPage.class);
         startActivity(intent);
     }

    @Override
    public void onItemClick(int position) {

    }
}