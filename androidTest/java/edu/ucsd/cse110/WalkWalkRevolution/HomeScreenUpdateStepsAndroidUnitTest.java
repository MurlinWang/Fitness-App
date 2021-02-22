package edu.ucsd.cse110.WalkWalkRevolution;


import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_EMAIL;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_FEET;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_INCHES;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_NAME;
import static edu.ucsd.cse110.WalkWalkRevolution.StepCountActivity.MOCK_SERVICE_KEY;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeScreenUpdateStepsAndroidUnitTest {
    private static final String TAG = "[HeightPopAndroidUnitTest]";
    private Intent intent;
    private MockManager manager;

    @Before
    public void setup() {
        Log.i(TAG, "Reset Test Environment");
        manager = MockManager.getInstance();
        manager.setKey(MOCK_SERVICE_KEY);

        // Mock shared preferences
        SharedPreferences heightPrefs = ApplicationProvider.getApplicationContext().getSharedPreferences("user_height", MODE_PRIVATE);
        SharedPreferences.Editor editor = heightPrefs.edit();
        editor.putInt(USER_HEIGHT_FEET,6);
        editor.putInt(USER_HEIGHT_INCHES,0);
        editor.apply();

        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
    }

    @Test
    public void updateStepsTest() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // get current distance and steps
                SharedPreferences heightPrefs = activity.getSharedPreferences("user_height", MODE_PRIVATE);
                // activity.setStepCount(500);//MockManager.getInstance().getTotal());
                TextView distanceView = activity.findViewById(R.id.dailyDistanceField);
                TextView stepsView = activity.findViewById(R.id.dailyStepsField);

                String distance = distanceView.getText().toString();
                String steps = stepsView.getText().toString();
                String height = Integer.toString(heightPrefs.getInt(USER_HEIGHT_FEET,0));
                System.out.println("\nThe distance is: " + distance);
                System.out.println("Steps are: " + steps);
                System.out.println("Mode is: " + MockManager.getInstance().getKey());
                System.out.println("User Height is: " + height);

                assertEquals(steps,"0");
                assertEquals(distance, "0 miles");
            });


        }

    }
}
