package edu.ucsd.cse110.WalkWalkRevolution;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * class name: RouteListActivity
 *
 */
public class RouteListActivity extends UpdateActivity implements RecyclerViewClickInterface {




    RecyclerView  RouteListView;
    Route_List_Adapter route_list_adapter;
    ArrayList<String> RouteList = new ArrayList<String>();
    ArrayList<String> RouteList_features = new ArrayList<String>();
    RouteList routeList;
    NotificationService service;
    String fitnessServiceKey;


    @Override
    /**
     * method name: it create the screen of rout list page and display routes
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_my_route);

        fitnessServiceKey = MockManager.getInstance().getKey();

        NotificationServiceFactory.put(fitnessServiceKey, new NotificationServiceFactory.BluePrint() {
            @Override
            public NotificationService create(UpdateActivity activity) {
                return new FirestoreAdapter(activity);
            }
        });

        service = NotificationServiceFactory.create(fitnessServiceKey, this);
        service.setup();


        Toolbar mToolbar = (Toolbar) findViewById(R.id.Routelistbar);
        setSupportActionBar(mToolbar);

        SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
        routeList = new RouteList(getSharedPreferences
                ("user_height", Context.MODE_PRIVATE).getString("email", ""));
        // Get routesList from JSON string
        Gson gson = new Gson();
        String routesJson = prefs.getString("routeList", "");

        // Initialize routes list for first use
        if(routesJson.equals("")){
            routeList = new RouteList(getSharedPreferences
                    ("user_height", Context.MODE_PRIVATE).getString("email", ""));
        } else {
            routeList = gson.fromJson(routesJson, routeList.getClass());
            routeList.setUserEmail(getSharedPreferences
                    ("user_height", Context.MODE_PRIVATE).getString("email", ""));
        }
        // add route names to list
        Iterator<Route> itr= routeList.routes.iterator();
        Route tempRoute;
        //put data into the arraylist
        while(itr.hasNext()) {
            tempRoute = itr.next();
            RouteList.add(tempRoute.name);
            DecimalFormat format = new DecimalFormat("#.##");
            RouteList_features.add("start location: "+tempRoute.startLocation+"\n"+
                                    "distance: "+format.format(tempRoute.walkdata.getDistance())
                                    +"\n"+ "steps: "+tempRoute.walkdata.getSteps()+"\n"+
                                    "duration: "+tempRoute.walkdata.getTime().getHour()+" hr "+
                                    tempRoute.walkdata.getTime().getMinute()+" min "+
                                    tempRoute.walkdata.getTime().getSecond()+" sec "+
                                    tempRoute.walkdata.getTime().getMs()+" millis");

        }
        if(RouteList != null){

            RouteListView =  findViewById(R.id.recyclerView);
            route_list_adapter = new Route_List_Adapter(RouteList,RouteList_features, this);
            RouteListView.setAdapter(route_list_adapter);


            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            RouteListView.addItemDecoration(dividerItemDecoration);


            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(RouteListView);
        }


    }

    String deleteRoute = null;

    Route delete_route_object = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int route_position = viewHolder.getAdapterPosition();


            switch (direction){
                case ItemTouchHelper.LEFT:
                    deleteRoute = RouteList.get(route_position);
                    delete_route_object = routeList.get(route_position);

                    RouteList.remove(route_position);
                    service.delete(route_position, getSharedPreferences
                                    ("user_height", Context.MODE_PRIVATE).getString("userid", "")
                            ,routeList);
                    routeList.delete(route_position);
                    route_list_adapter.notifyItemRemoved(route_position);
                    SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    Gson gson = new Gson();
                    String routesJson = prefs.getString("routeList", "");

                    String newRouteListJson = gson.toJson(routeList);
                    editor.putString("routeList", newRouteListJson);
                    editor.apply();

                    Snackbar.make(RouteListView,deleteRoute,Snackbar.LENGTH_LONG).setAction("Recovery Route", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            routeList.addRoute(delete_route_object);
                            service.addRoute(delete_route_object,
                                    getSharedPreferences
                                            ("user_height",
                                                    Context.MODE_PRIVATE).getString("userid",""));
                            routeList.sort();
                            RouteList.add(route_position,deleteRoute);
                            route_list_adapter.notifyItemInserted(route_position);
                            SharedPreferences prefs = getSharedPreferences("user_name", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            Gson gson = new Gson();
                            String routesJson = prefs.getString("routeList", "");

                            String newRouteListJson = gson.toJson(routeList);
                            editor.putString("routeList", newRouteListJson);
                            editor.apply();
                        }
                    }).show();
                    break;
            }
        }

        /**
         * This method is called when a route list activity is swiped.
         * @param c
         * @param routeListView
         * @param viewHolder
         * @param dX
         * @param dY
         * @param actionState
         * @param isCurrentlyActive
         */
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView routeListView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(RouteListActivity.this, c, routeListView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(RouteListActivity.this, R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .setActionIconTint(ContextCompat.getColor(routeListView.getContext(), android.R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, routeListView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    /**
     * This method is called when an item on routes list is clicked.
     * Creates an activity of the routes information.
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, Route_List_Click.class);
        intent.putExtra("route_index", position);
        startActivity(intent);
        finish();
    }

    /**
     * This method is called when the menu button is clicked.
     * Expands the menu on the UI.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_route_list,menu);
        return true;
    }

    /**
     * This method is called when the "+" button is clicked. Switches to
     * an activity where the user can add a route list.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addRoute:
                startActivity(new Intent(this, AddNewRouteListActivity.class));
                return true;

            case R.id.back_route_screen_my_route:
                startActivity(new Intent(this, Route_List_page.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Overrides the native back button.
     */
    @Override
    public void onBackPressed(){
        moveToHome();
        finish();
    }

    /**
     * This method creates and switches to a new main activity.
     */
    public void moveToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(StopWalkActivity.FITNESS_SERVICE_KEY, serviceKey);
        startActivity(intent);
    }

}
