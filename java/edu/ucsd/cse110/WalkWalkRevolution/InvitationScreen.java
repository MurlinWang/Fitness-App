package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;


public class InvitationScreen extends UpdateActivity {

    private String token;
    private EditText name;
    private EditText email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_screen);

        notificationServiceSetup();

        name = findViewById(R.id.invitation_name_ev);
        email = findViewById(R.id.invitation_email_ev);
        Button send_invitation = (Button) findViewById(R.id.send_invitation_btn);
        send_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationService.saveToFirestore(InvitationScreen.this.email.getText().toString());
                String topic = notificationService.createNewGroup(InvitationScreen.this.email.getText().toString());
                notificationService.subscribeToNotificationsTopic(topic);
                launchActivity(TeamScreenActivity.class);
            }
        });
    }

    public void launchActivity(Class<?> cls ) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

}
