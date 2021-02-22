package edu.ucsd.cse110.WalkWalkRevolution.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import edu.ucsd.cse110.WalkWalkRevolution.HeightPop;
import edu.ucsd.cse110.WalkWalkRevolution.MainActivity;
import edu.ucsd.cse110.WalkWalkRevolution.Route;
import edu.ucsd.cse110.WalkWalkRevolution.RouteList;
import edu.ucsd.cse110.WalkWalkRevolution.Invitation_click;
import edu.ucsd.cse110.WalkWalkRevolution.UpdateActivity;
import edu.ucsd.cse110.WalkWalkRevolution.WalkData;

import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_EMAIL;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_FEET;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_HEIGHT_INCHES;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_ID;
import static edu.ucsd.cse110.WalkWalkRevolution.HeightPop.USER_NAME;

public class FirestoreAdapter implements NotificationService {

    private static final String TAG = FirestoreAdapter.class.getSimpleName();

    private UpdateActivity activity;
    private CollectionReference invitations;
    private CollectionReference acceptance;
    private String collectionKey;
    private String groupID;
    private String invitationsKey;
    private String acceptanceKey;
    private String timeStampKey;

    private String senderKey;
    private String contentKey;


    FirebaseFirestore db;

    private String leader_id;


    public FirestoreAdapter(UpdateActivity activity) {
        this.activity = activity;
    }

    public void setup() {

        this.collectionKey = "notifications";
        this.groupID = "group1";
        this.invitationsKey = "invitations";
        this.acceptanceKey = "acceptance"; // was acceptence, losers -Joe <3
        this.timeStampKey = "timestamp";

        this.senderKey = "sender";
        this.contentKey = "content";

        db = FirebaseFirestore.getInstance();

        this.acceptance = db
                .collection(collectionKey)
                .document(groupID)
                .collection(acceptanceKey);

        this.invitations = db
                .collection(collectionKey)
                .document(groupID)
                .collection(invitationsKey);
    }

    public void createNewUser(int user_feet, int user_inches, String user_email, String user_name, String user_id) {

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put(USER_HEIGHT_FEET, user_feet);
        user.put(USER_HEIGHT_INCHES, user_inches);
        user.put(USER_EMAIL, user_email);
        user.put(USER_NAME, user_name);
        user.put("leader_id", user_id);
        user.put("onteam", false);


        // Add a new document with a generated ID
        db.collection("users").document(user_id)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + user_id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public Task<?> addNotification(Map<String, String> notification) {


        if (notification.get("notificationTypeKey") == invitationsKey) {

            return invitations.add(notification);
        } else {
            return acceptance.add(notification);
        }
    }

    @Override
    public void subscribeToInvitations(Consumer<List<Notification>> listener) {
        invitations.orderBy(timeStampKey, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG + " subscribeToInvitations", error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();

                        List<Notification> newMessages = documentChanges.stream()
                                .map(DocumentChange::getDocument)
                                .map(doc -> new Notification(doc.getString(senderKey), doc.getString(contentKey)))
                                .collect(Collectors.toList());

                        listener.accept(newMessages);
                    }
                });

    }

    @Override
    public void subscribeToAcceptances(Consumer<List<Notification>> listener) {
        acceptance.orderBy(timeStampKey, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG + " subscribeToAcceptence", error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();

                        List<Notification> newMessages = documentChanges.stream()
                                .map(DocumentChange::getDocument)
                                .map(doc -> new Notification(doc.getString(senderKey), doc.getString(contentKey)))
                                .collect(Collectors.toList());

                        listener.accept(newMessages);
                    }
                });
    }

    @Override
    public void saveToFirestore(String email) {
        int index = email.indexOf('@');
        String user_id = email.substring(0, index);
        SharedPreferences prefs = activity.getSharedPreferences(HeightPop.USER_HEIGHT_SP, Context.MODE_PRIVATE);
        CollectionReference ref_inviter = db.collection("users")
                .document(user_id)
                .collection("invitations");
        Map<String, String> data = new HashMap<>();
        data.put("name", prefs.getString(USER_NAME,""));
        data.put("email", prefs.getString(USER_EMAIL,""));

        // retrieve my leader_id
        SharedPreferences curPref= activity.getSharedPreferences(HeightPop.USER_HEIGHT_SP, Context.MODE_PRIVATE);
        String user_email = curPref.getString(USER_EMAIL, "");
        int  index_inviter = user_email.indexOf('@');
        String userid = user_email.substring(0, index_inviter);
        DocumentReference ref_invitee = db.collection("users")
                .document(userid);
        ref_invitee.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    data.put("leader_id", document.getString("leader_id"));
                    ref_inviter.add(data);
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public String createNewGroup(String email) {
        String topic;
        SharedPreferences prefs = activity.getSharedPreferences(HeightPop.USER_HEIGHT_SP, Context.MODE_PRIVATE);
        String user_email = prefs.getString(USER_EMAIL, "");
        int index = user_email.indexOf('@');
        String leader_id = user_email.substring(0, index);
        topic = leader_id;
        CollectionReference groupRef = db.collection("groups").document(leader_id).collection("users");
        Map<String, String> data = new HashMap<>();
        data.put("name", prefs.getString(USER_NAME,""));
        groupRef.add(data);

        CollectionReference inviteeRef = db.collection("groups")
                .document(leader_id)
                .collection("invitees");
        Map<String, String> invitee = new HashMap<>();
        int index_invitee = email.indexOf('@');
        String user_id = email.substring(0, index_invitee);
        invitee.put("name", user_id);
        inviteeRef.add(invitee);
        return topic;
    }

    @Override
    public void subscribeToNotificationsTopic(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public ArrayList<String> fetchData() {
        SharedPreferences prefs = activity.getSharedPreferences(HeightPop.USER_HEIGHT_SP, Context.MODE_PRIVATE);
        ArrayList<String> Teammate_name = new ArrayList<String>();
        CollectionReference ref = db.collection("users").document(prefs.getString(USER_ID, ""))
                .collection("invitations");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : document) {
                        Log.d(TAG, "DocumentSnapshot data: " + queryDocumentSnapshot.getData());
                        Teammate_name.add((String) queryDocumentSnapshot.getData().get("name"));
                        activity.updateUI();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return Teammate_name;
    }

    public Map<String, Object> fetchRoute(String user_id){
        Map<String, Object> teamRoutes= new HashMap<>();
        ArrayList<String> teamMember = new ArrayList<String>();
      //  ArrayList<String> leader_id = new ArrayList<String>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            DocumentSnapshot document = task.getResult();
                            String leader_id = document.get("leader_id").toString();

                            getGroupMems(leader_id, user_id,teamMember, teamRoutes);


                            Log.d(TAG, "Cached document data: " + document.getData());
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }

                    }
                });


        return teamRoutes;

    }

    public void getGroupMems(String leader_id, String user_id, ArrayList<String> teamMember,
                             Map<String, Object> teamRoutes){

        FirebaseFirestore.getInstance()
                .collection("groups")
                .document(leader_id)
                .collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String member_id = document.get("name").toString();
                                if(!member_id.equals(user_id)) {
                                    teamMember.add(member_id);

                                    addRoutes(member_id, teamRoutes, teamMember);

                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public void addRoutes(String member_id, Map<String, Object> teamRoutes, ArrayList<String> teamMember){
        RouteList teamRouteList = new RouteList();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(member_id)
                .collection("routes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                teamRouteList.addRoute(document.toObject(Route.class));
                                teamRoutes.put(member_id, teamRouteList);
                            }
                            teamRoutes.put("members", teamMember);
                            activity.updateUI();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        teamRoutes.put(member_id, teamRouteList);
                    }
                });
    }

    @Override
    public void addRoute(Route toAdd, String user_email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(user_email)
                .collection("routes")
                .document(toAdd.getName())
                .set(toAdd);

    }

    @Override
    public void updateWalkData(WalkData data, int index, String user_email, RouteList list){
        Route newRoute = list.get(index).setData(data);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(user_email)
                .collection("routes")
                .document(newRoute.getName())
                .set(newRoute);
    }

    @Override
    public void delete(int position, String user_email, RouteList list){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(user_email)
                .collection("routes")
                .document(list.get(position).getName())
                .delete();
    }

    //method to accept invitation
    @Override
    public void acceptInvitation(String s) {

        // TODO retrieve the inviter's current leader_id and compare it with what is in the invitation list
        SharedPreferences prefs = activity.getSharedPreferences(HeightPop.USER_HEIGHT_SP, Context.MODE_PRIVATE);
        String user_id = prefs.getString(HeightPop.USER_ID, "");

        CollectionReference id_ref = db.collection("users").document(user_id).collection("invitations");
        DocumentReference myref = db.collection("users").document(user_id);

        //retrieve the leader_id
        id_ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    for (QueryDocumentSnapshot snapshot : document) {
                        if (snapshot.getData().get("name").equals(s)) {
                            Map<String, Object> invitee_onteam = new HashMap<>();
                            invitee_onteam.put("onteam", true);
                            myref.update(invitee_onteam);
                            String leader_id = snapshot.getData().get("leader_id").toString();
                            Map<String, Object> invitee_leader_id = new HashMap<>();
                            invitee_leader_id.put("leader_id", leader_id);
                            myref.update(invitee_leader_id);
                            subscribeToNotificationsTopic(leader_id);
                            DocumentReference memberRef = db.collection("groups")
                                    .document(leader_id)
                                    .collection("users")
                                    .document(user_id);
                            Map<String, String> invitee = new HashMap<>();
                            invitee.put("name", user_id);
                            invitee.put("status", "pending");
                            memberRef.set(invitee);
                            db.collection("groups")
                                    .document(leader_id)
                                    .collection("invitees")
                                    .document(user_id)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }


    public ArrayList<String> fetchTeammate() {
        //find your own leader_id
        SharedPreferences prefs = activity.getSharedPreferences(HeightPop.USER_HEIGHT_SP, Context.MODE_PRIVATE);
        String user_id = prefs.getString(USER_ID, "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference refs = db.collection("users").document(user_id);
        ArrayList<String> teammate = new ArrayList<>();
        refs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "leader id is :" + document.getData().get("leader_id"));
                    String leader_id = document.getData().get("leader_id").toString();
                    CollectionReference ref_users = db.collection("groups").document(leader_id).collection("users");
                    ref_users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot document = task.getResult();
                                for (QueryDocumentSnapshot queryDocumentSnapshot : document) {
                                    Log.d(TAG, "DocumentSnapshot data: " + queryDocumentSnapshot.getData());
                                    teammate.add((String) queryDocumentSnapshot.getData().get("name") + "(member)");
                                    activity.updateUI();
                                }
                            }
                        }
                    });
                }
            }
        });

        return teammate;
    }

    public ArrayList<String> fetchInvitee() {
        //find your own leader_id
        SharedPreferences prefs = activity.getSharedPreferences(HeightPop.USER_HEIGHT_SP, Context.MODE_PRIVATE);
        String user_id = prefs.getString(USER_ID, "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference refs = db.collection("users").document(user_id);
        ArrayList<String> teammate = new ArrayList<>();
        refs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "leader id is :" + document.getData().get("leader_id"));
                    String leader_id = document.getData().get("leader_id").toString();
                    CollectionReference ref_users = db.collection("groups").document(leader_id).collection("invitees");
                    ref_users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot document = task.getResult();
                                for (QueryDocumentSnapshot queryDocumentSnapshot : document) {
                                    Log.d(TAG, "DocumentSnapshot data: " + queryDocumentSnapshot.getData());
                                    teammate.add((String) queryDocumentSnapshot.getData().get("name") + "(pending)");
                                    activity.updateUI();
                                }
                            }
                        }
                    });

                }
            }
        });


        return teammate;

    }

    @Override
    public void  createProposedWalk() {
        //find your own leader_id
        SharedPreferences prefs = activity.getSharedPreferences(HeightPop.USER_HEIGHT_SP, Context.MODE_PRIVATE);
        String user_id = prefs.getString(USER_ID, "");
        String user_name = prefs.getString(USER_NAME, "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference refs = db.collection("users").document(user_id);
        ArrayList<String> teammate = new ArrayList<>();
        refs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "leader id is :" + document.getData().get("leader_id"));
                    String leader_id = document.getData().get("leader_id").toString();
                    DocumentReference ref_users = db.collection("groups")
                            .document(leader_id)
                            .collection("ProposedWalks")
                            .document("PropsedWalk");
                    Map<String, Object> fields = new HashMap<>();
                    fields.put("isSchedules", false);
                    fields.put("proposername", user_name);
                    fields.put("proposer_id", user_id);
                    ref_users.set(fields);
                }
            }
        });
    }
}

