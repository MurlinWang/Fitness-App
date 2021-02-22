package edu.ucsd.cse110.WalkWalkRevolution;

import android.app.Application;

import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;

public class MyApplication extends Application {
    private static NotificationServiceFactory notificationServiceFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationServiceFactory = new NotificationServiceFactory();
    }

    public static NotificationServiceFactory getNotificationServiceFactory() {
        return notificationServiceFactory;
    }

    public static NotificationServiceFactory setChatServiceFactory(NotificationServiceFactory nsf) {
        return notificationServiceFactory = nsf;
    }
}
