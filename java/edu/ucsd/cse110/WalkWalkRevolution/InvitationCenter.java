package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import androidx.appcompat.widget.Toolbar;


public class InvitationCenter extends UpdateActivity implements Invitation_interface {

    String TAG = InvitationCenter.class.getSimpleName();

    RecyclerView   Teammate_status_recyclerView;
    InvitationAdapter invitationAdapter;
    ArrayList<String> Teammate_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_center);

        notificationServiceSetup();

        Teammate_name = notificationService.fetchData();

        Button back_home = (Button) findViewById(R.id.Back_home_invitation_btn);
        Button teammate_list = (Button) findViewById(R.id.go_to_teammate_list);

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(MainActivity.class);
            }
        });
        teammate_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(TeamScreenActivity.class);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, Invitation_click.class);
        intent.putExtra("accept_decline", Teammate_name.get(position));
        startActivity(intent);
    }

    public void launchActivity(Class<?> cls ) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void updateUI() {
        Teammate_status_recyclerView = findViewById(R.id.Teammate_status_recyclerView);
        invitationAdapter = new InvitationAdapter(Teammate_name,this);
        Teammate_status_recyclerView.setAdapter(invitationAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        Teammate_status_recyclerView.addItemDecoration(dividerItemDecoration);
    }
}