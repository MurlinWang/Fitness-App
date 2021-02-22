package edu.ucsd.cse110.WalkWalkRevolution;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import edu.ucsd.cse110.WalkWalkRevolution.fitness.FitnessServiceFactory;

/** MockActivity lets you input custom steps and distance and time to set the system time accordingly
 *
 * TODO: Do dependency injection on GoogleFitAdapter for more genuine mocking
 */
public class MockActivity extends StepCountActivity {
    private EditText textTime;

    // Get fields from UI elements into the textSteps, step, textTime
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MockManager manager = MockManager.getInstance();
        serviceKey = manager.getKey();
        if(!serviceKey.equals(MOCK_SERVICE_KEY)) {
            finish();
        }
        fitnessService = FitnessServiceFactory.create(serviceKey, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textSteps = (TextView)findViewById(R.id.dailySteps);
        Button step = (Button)findViewById(R.id.daily_steps_text);
        Button submit = findViewById(R.id.submit);
        textTime = (EditText) findViewById(R.id.time_field);
        fitnessService.updateStepCount();
        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fitnessService.addSteps(500);
                fitnessService.updateStepCount();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long seconds = Long.parseLong(textTime.getText().toString());
                manager.setTimeInSeconds(seconds);
                Toast.makeText(MockActivity.this, "Time set to: " + seconds + " seconds", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount));
    }


}
