package edu.ucsd.cse110.WalkWalkRevolution.service;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.WalkWalkRevolution.UpdateActivity;

public class NotificationServiceFactory {
    private static final String TAG = "[NotificationServiceFactory]";

    private static Map<String, NotificationServiceFactory.BluePrint> blueprints = new HashMap<>();

    public static void put(String key, NotificationServiceFactory.BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static NotificationService create(String key, UpdateActivity activity) {
        Log.i(TAG, String.format("creating NotificationService with key %s", key));
        return blueprints.get(key).create(activity);
    }

    public interface BluePrint {
        NotificationService create(UpdateActivity activity);
    }
}
