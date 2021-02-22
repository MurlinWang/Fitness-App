package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_EMAIL;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_FEET;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_INCHES;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_NAME;
import static edu.ucsd.cse110.WalkWalkRevolution.StepCountActivity.MOCK_SERVICE_KEY;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HeightPopAndroidUnitTest {
    private static final String TAG = "[HeightPopAndroidUnitTest]";
    private Intent intent;
    private MockManager manager;

    @Before
    public void setup() {
        Log.i(TAG, "Reset Test Environment");
        manager = MockManager.getInstance();
        manager.setKey(MOCK_SERVICE_KEY);

        intent = new Intent(ApplicationProvider.getApplicationContext(), HeightPop.class);
    }

    @Test
    public void testSavingHeight() {
        try(ActivityScenario<HeightPop> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
               TextView feet = activity.findViewById(R.id.feetField);
               TextView inches = activity.findViewById((R.id.inchesField));
               TextView email = activity.findViewById(R.id.editTextEmail);
               TextView name = activity.findViewById(R.id.editTextUsername);
               feet.setText("6");
               inches.setText("0");
               email.setText("mock@gmail.com");
               name.setText("mock");

               Button nextBtn = activity.findViewById(R.id.next_btn);
               nextBtn.performClick();

               SharedPreferences heightPrefs = activity.getSharedPreferences("user_height", MODE_PRIVATE);
               int ft = heightPrefs.getInt(USER_HEIGHT_FEET,0);
               int in = heightPrefs.getInt(USER_HEIGHT_INCHES, 0);
               String mail = heightPrefs.getString(USER_EMAIL, "");
               String uName = heightPrefs.getString(USER_NAME, "");

               assertEquals(ft, 6);
               assertEquals(in, 0);
               assertEquals(mail, "mock@gmail.com");
               assertEquals(uName, "mock");
            });
        }

    }
}
