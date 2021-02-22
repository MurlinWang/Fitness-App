package edu.ucsd.cse110.WalkWalkRevolution;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

/**
 * HeightPop is the Activity that asks for the height popup and initializes the height data
 *   into SharedPreferences for persistent data storage
 */
public class HeightPop extends UpdateActivity {

    private static final String TAG = HeightPop.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 100;

    public static String USER_HEIGHT_SP = "user_height";
    public static String USER_HEIGHT_FEET = "feet";
    public static String USER_HEIGHT_INCHES = "inches";
    public static String USER_NAME = "name";
    public static String USER_EMAIL = "email";
    public static String USER_ID = "userid";


    private String TOPIC = "";

    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText feet;
    private EditText inches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_pop);

        // check permissions
        showPhoneStatePermission();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        feet = findViewById(R.id.feetField);
        inches = findViewById(R.id.inchesField);

        notificationServiceSetup();

        // Set next button to save height and move to mainActivity after terminating the popup
        Button nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().isEmpty() || editTextUsername.getText().toString().isEmpty() ||
                    feet.getText().toString().isEmpty() || inches.getText().toString().isEmpty()) {
                    Toast.makeText(HeightPop.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData();
                notificationService.subscribeToNotificationsTopic(HeightPop.this.TOPIC);
                startActivity(new Intent(HeightPop.this, MainActivity.class));
                finish();
            }
        });
    }



    // Gets feet and inches straight from EditText fields and saves into SharedPreference height
    // Also saves the data to Firestore
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_HEIGHT_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Integer user_feet = Integer.parseInt(feet.getText().toString());
        Integer user_inches = Integer.parseInt(inches.getText().toString());
        String user_email = editTextEmail.getText().toString();
        String user_name = editTextUsername.getText().toString();

        int index = user_email.indexOf('@');
        this.TOPIC = user_email.substring(0,index);
        String user_id  = this.TOPIC;

        editor.putInt(USER_HEIGHT_FEET, user_feet);
        editor.putInt(USER_HEIGHT_INCHES, user_inches);
        editor.putString(USER_EMAIL, user_email);
        editor.putString(USER_NAME, user_name);
        editor.putString(USER_ID, user_id);
        editor.apply();

        notificationService.createNewUser(user_feet, user_inches, user_email, user_name, user_id);
    }

    @Override
    public void onRequestPermissionsResult(@NonNull int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Toast.makeText(HeightPop.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    checkUserStatus();

                } else {
                    // permission denied
                    Toast.makeText(HeightPop.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    checkUserStatus();
                }
            }
        }
    }

    private void showPhoneStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION);
        if ( permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACTIVITY_RECOGNITION)) {
                showExplanation(Manifest.permission.ACTIVITY_RECOGNITION, MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
            } else {
                requestPermission(Manifest.permission.ACTIVITY_RECOGNITION, MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
            }
        } else {
            Toast.makeText(HeightPop.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
            checkUserStatus();
        }
    }

    private void showExplanation(String permission,
                                 int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Needed")
                .setMessage("We need permission to access your device's current state in order to measure how many steps you have taken.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    private void checkUserStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_HEIGHT_SP, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(USER_NAME) && sharedPreferences.contains(USER_HEIGHT_FEET) &&
                sharedPreferences.contains(USER_HEIGHT_INCHES) && sharedPreferences.contains(USER_EMAIL)) {
            startActivity(new Intent(HeightPop.this, MainActivity.class));
            finish();
        }
    }
}
