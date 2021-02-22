package edu.ucsd.cse110.WalkWalkRevolution.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import edu.ucsd.cse110.WalkWalkRevolution.MockManager;
import edu.ucsd.cse110.WalkWalkRevolution.Route;
import edu.ucsd.cse110.WalkWalkRevolution.RouteList;
import edu.ucsd.cse110.WalkWalkRevolution.UpdateActivity;
import edu.ucsd.cse110.WalkWalkRevolution.WalkData;

public class MockFirestoreAdapter implements NotificationService {
    UpdateActivity activity;

    public MockFirestoreAdapter (UpdateActivity activity) {
        this.activity = activity;
    }

    @Override
    public void createNewUser(int user_feet, int user_inches, String user_email, String user_name, String user_id) {

    }

    @Override
    public Task<?> addNotification(Map<String, String> notification){

        return null;
    }

    @Override
    public void subscribeToInvitations(Consumer<List<Notification>> listener){

    }

    @Override
    public void subscribeToAcceptances(Consumer<List<Notification>> listener){

    }

    @Override
    public void setup(){

    }

    @Override
    public void saveToFirestore(String email){

    }

    @Override
    public ArrayList<String> fetchData(){
        ArrayList<String> a = new ArrayList<String>();
        a.add("MOCKING");
        return a;
    }

    @Override
    public void addRoute(Route toAdd, String user_email){
    }

    @Override
    public void updateWalkData(WalkData data, int index, String user_email, RouteList list){

    }
    @Override
    public void delete(int position, String user_email, RouteList list){
    }

    public String createNewGroup(String email){return "";}

    @Override
    public void subscribeToNotificationsTopic(String topic) {}

    @Override
    public Map<String, Object> fetchRoute(String user_id){return null;}

    



    //method to accept invitation
    @Override
    public void acceptInvitation(String inviter_name) {}

    public ArrayList<String> fetchTeammate() { return null;}
    @Override
    public  ArrayList<String> fetchInvitee() {return null;}
    @Override
    public void  createProposedWalk() {}


}
