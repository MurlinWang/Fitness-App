package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.net.Uri;

public class GoogleMapAdapter {
    public static Intent goToMap(String destination){

        Uri gmmIntentUri = Uri.parse("google.navigation:q="+ destination );
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        return mapIntent.setPackage("com.google.android.apps.maps");
    }
}
