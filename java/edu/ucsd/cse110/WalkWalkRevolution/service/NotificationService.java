package edu.ucsd.cse110.WalkWalkRevolution.service;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import edu.ucsd.cse110.WalkWalkRevolution.Route;
import edu.ucsd.cse110.WalkWalkRevolution.RouteList;
import edu.ucsd.cse110.WalkWalkRevolution.WalkData;

public interface NotificationService {
    Task<?> addNotification(Map<String, String> notification);
    void createNewUser(int user_feet, int user_inches, String user_email, String user_name, String user_id);
    void subscribeToInvitations(Consumer<List<Notification>> listener);
    void setup();
    void subscribeToAcceptances(Consumer<List<Notification>> listener);
    void saveToFirestore(String email);
    ArrayList<String> fetchData();
    String createNewGroup(String email);
    void subscribeToNotificationsTopic(String topic);

    void acceptInvitation(String inviter_name);

    void addRoute(Route toAdd, String user_email);
    void updateWalkData(WalkData data, int index, String user_email, RouteList list);
    void delete(int position, String user_email, RouteList list);
    Map<String, Object> fetchRoute(String user_id);

    ArrayList<String> fetchTeammate();
    ArrayList<String> fetchInvitee();

    void  createProposedWalk();


}
