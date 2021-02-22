package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;

public class Invitation_click extends UpdateActivity {

    private String TAG = Invitation_click.class.getSimpleName();

    private String topic;

    String inviter_name;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.context_invitation_click);

        Button accept = findViewById(R.id.invitation_accept);
        Button decline = findViewById(R.id.invitation_decline);

        notificationServiceSetup();

        TextView name = (TextView)  findViewById(R.id.Pending_ev);
        String Teammate_name =  getIntent().getStringExtra("accept_decline");
        name.setText(Teammate_name);
        this.inviter_name = Teammate_name;
        Log.d(TAG, "teammate name : " + Teammate_name);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete current invitation, disable notification center? cancel all other invitations?
                // notifications sent to all new team members
                notificationService.acceptInvitation(inviter_name);
                Toast.makeText(Invitation_click.this, "Joined a team!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Invitation_click.this, InvitationCenter.class));
                finish();

            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete current invitation
                // no notification sent to anyone
                Toast.makeText(Invitation_click.this, "Invitation Declined", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Invitation_click.this, InvitationCenter.class));
                finish();
            }
        });
    }
    

}