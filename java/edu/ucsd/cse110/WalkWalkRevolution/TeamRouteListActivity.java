package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;

public class TeamRouteListActivity extends UpdateActivity implements TeamRouteinterface{


    RecyclerView Teammate_RouteListView;
    TeamRouteAdapter teamRouteAdapter;
    ArrayList<String> Teammate_RouteList_features = new ArrayList<String>();
    ArrayList<String> Teammate_RouteList_Memeber = new ArrayList<String>();
    NotificationService service;
    String fitnessServiceKey;
    Map<String, Object> teamRoutes;
    RouteList routeList = new RouteList();

    Map<String, Object> changedRoutes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_team_route);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.TeamRoutelistbar);
        setSupportActionBar(mToolbar);

        fitnessServiceKey = MockManager.getInstance().getKey();

        NotificationServiceFactory.put(fitnessServiceKey, new NotificationServiceFactory.BluePrint() {
            @Override
            public NotificationService create(UpdateActivity activity) {
                return new FirestoreAdapter(activity);
            }
        });

        service = NotificationServiceFactory.create(fitnessServiceKey, this);
        service.setup();

        teamRoutes = service.fetchRoute(getSharedPreferences
                ("user_height", Context.MODE_PRIVATE).getString("userid", ""));

        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        // Get routesList from JSON string
        Gson gson = new Gson();
        String routesJson = prefs.getString("changedRouteList", "");
        if(routesJson.equals("")){
            changedRoutes = null;
        }else {
            Map<String, Object>test = new HashMap<String,Object>();
            changedRoutes = gson.fromJson(routesJson, test.getClass());
        }


    }



    /**
     * This method is called when the menu button is clicked.
     * Expands the menu on the UI.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back_route,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called when the "+" button is clicked. Switches to
     * an activity where the user can add a route list.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case  R.id.back_route_screen:
                startActivity(new Intent(this,Route_List_page.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, Teammateroute_click.class);
        intent.putExtra("route_index", position);
        startActivity(intent);
    }

    public void updateUI(){

        Teammate_RouteListView =  findViewById(R.id.Teammate_recyclerView);
        ArrayList<String> members = (ArrayList<String>) teamRoutes.get("members");


        Iterator<String> itr = members.iterator();
        while(itr.hasNext()){
            String nextMem = itr.next();
            int size = routeList.addRoutes((RouteList)teamRoutes.get(nextMem));
            for(int i = 0; i < size; i++){
                Teammate_RouteList_Memeber.add(nextMem);
                Route tempRoute = routeList.get(routeList.size() - size + i);

                DecimalFormat format = new DecimalFormat("#.##");
                Teammate_RouteList_features.add("start location: "+tempRoute.startLocation+"\n"+
                        "distance: "+format.format(tempRoute.walkdata.getDistance())
                        +"\n"+ "steps: "+tempRoute.walkdata.getSteps()+"\n"+
                        "duration: "+tempRoute.walkdata.getTime().getHour()+" hr "+
                        tempRoute.walkdata.getTime().getMinute()+" min "+
                        tempRoute.walkdata.getTime().getSecond()+" sec "+
                        tempRoute.walkdata.getTime().getMs()+" millis");
            }
        }

//        if(changedRoutes!= null) {
//            RouteList changedRouteList = new RouteList();
//            ArrayList<String> eachChangedMember = new ArrayList<String>();
//            ArrayList<String> changedMember = (ArrayList<String>) changedRoutes.get("members");
//            Iterator<String> itrChanged = changedMember.iterator();
//            while (itrChanged.hasNext()) {
//                String nextMem = itrChanged.next();
//                int size = changedRouteList.addRoutes((RouteList) changedRoutes.get(nextMem));
//
//                for (int i = 0; i < size; i++) {
//                    eachChangedMember.add(nextMem);
//                }
//            }
//
//            Iterator<String> itrUpdate = eachChangedMember.iterator();
//            while (itrUpdate.hasNext()) {
//                String curMem = itrUpdate.next();
//                Route tempRoute = changedRouteList.get(0);
//
//                int index = findIndexOf(curMem, tempRoute.getName(), routeList, Teammate_RouteList_Memeber);
//                routeList.update(index, tempRoute);
//                DecimalFormat format = new DecimalFormat("#.##");
//                Teammate_RouteList_features.set(index,
//                        "start location: " + tempRoute.startLocation + "\n" +
//                                "distance: " + format.format(tempRoute.walkdata.getDistance())
//                                + "\n" + "steps: " + tempRoute.walkdata.getSteps() + "\n" +
//                                "duration: " + tempRoute.walkdata.getTime().getHour() + " hr " +
//                                tempRoute.walkdata.getTime().getMinute() + " min " +
//                                tempRoute.walkdata.getTime().getSecond() + " sec " +
//                                tempRoute.walkdata.getTime().getMs() + " millis");
//
//                changedRouteList.remove(0);
//            }
//        }


        teamRouteAdapter = new TeamRouteAdapter(routeList,
                Teammate_RouteList_features,this, Teammate_RouteList_Memeber);
        Teammate_RouteListView.setAdapter(teamRouteAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        Teammate_RouteListView.addItemDecoration(dividerItemDecoration);

        // SAVE IT TO SHARED PREFERENCES
        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Get routesList from JSON string
        Gson gson = new Gson();


        String newRouteListJson = gson.toJson(routeList);
        editor.putString("teamRouteList", newRouteListJson);
        editor.apply();
    }

    public int findIndexOf(String wantedMem, String routeName, RouteList routeList, ArrayList<String> teamList){

        for(int i = 0; i < teamList.size(); i ++){

            if(teamList.get(i).equals(wantedMem) && routeList.get(i).getName().equals(routeName))return i;

        }
        return -1;
    }

}
