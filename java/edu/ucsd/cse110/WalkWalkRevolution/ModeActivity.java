package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import edu.ucsd.cse110.WalkWalkRevolution.R;

import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_EMAIL;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_FEET;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_INCHES;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_SP;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_ID;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_NAME;
import static edu.ucsd.cse110.WalkWalkRevolution.StepCountActivity.FITNESS_SERVICE_KEY;
import static edu.ucsd.cse110.WalkWalkRevolution.StepCountActivity.MOCK_SERVICE_KEY;

public class ModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MockManager manager = MockManager.getInstance();

        Button mockBtn = findViewById(R.id.mock_mode_btn);
        Button normalBtn = findViewById(R.id.normal_mode_btn);
        mockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(USER_HEIGHT_SP, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(!sharedPreferences.contains(USER_EMAIL)) {
                    editor.putInt(USER_HEIGHT_FEET, 6);
                    editor.putInt(USER_HEIGHT_INCHES, 0);
                    editor.putString(USER_EMAIL, "mock@gmail.com");
                    editor.putString(USER_NAME, "Mocker");
                    editor.putString(USER_ID, "mock");
                    editor.apply();
                }

                manager.setKey(MOCK_SERVICE_KEY);
                Intent intent = new Intent(ModeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setKey(FITNESS_SERVICE_KEY);
                Intent intent = new Intent(ModeActivity.this, HeightPop.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
