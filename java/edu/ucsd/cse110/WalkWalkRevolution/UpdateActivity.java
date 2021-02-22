package edu.ucsd.cse110.WalkWalkRevolution;

import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.MockFirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;

import static edu.ucsd.cse110.WalkWalkRevolution.StepCountActivity.MOCK_SERVICE_KEY;

public abstract class UpdateActivity extends AppCompatActivity {
    NotificationService notificationService;
    String serviceKey;

    public void updateUI(){}

    public void notificationServiceSetup() {

        serviceKey = MockManager.getInstance().getKey();
        if(serviceKey.equals(MOCK_SERVICE_KEY)) {
            NotificationServiceFactory.put(serviceKey, new NotificationServiceFactory.BluePrint() {
                @Override
                public NotificationService create(UpdateActivity activity) {
                    return new MockFirestoreAdapter(activity);
                }
            });
        } else {
            NotificationServiceFactory.put(serviceKey, new NotificationServiceFactory.BluePrint() {
                @Override
                public NotificationService create(UpdateActivity activity) {
                    return new FirestoreAdapter(activity);
                }
            });
        }
        notificationService = NotificationServiceFactory.create(serviceKey, this);
        notificationService.setup();


    }
}
