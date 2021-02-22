package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RouteList {

    // Should we initialize it with an empty ArrayList<Route>?
    ArrayList<Route> routes;
    String user_email;

    public RouteList(){
        routes = new ArrayList<Route>();
        user_email =  null;
    }

    public RouteList(String user_email) {
        this.routes = new ArrayList<Route>();
        this.user_email = user_email;
    }

    public RouteList(ArrayList<Route> routes){
        this.routes = routes;
    }

    public void setUserEmail(String user_email){this.user_email = user_email;}

    public void addRoute(Route toAdd){
        this.routes.add(toAdd);
    }

    public void updateWalkData(WalkData data, int index){
        Route newRoute = this.get(index).setData(data);
    }

    public void sort(){
        Collections.sort(routes);
    }

    public void delete(int position){
        this.routes.remove(position);

    }

    public Route get(int position){
        if(position > size() - 1 )return null;
        return this.routes.get(position);
    }

    public int size(){
        return this.routes.size();
    }

    public void remove (int index){
        this.routes.remove(index);
    }

    public int addRoutes(RouteList toAddList){
        int i = 0;
        while(toAddList!=null && toAddList.size()!=0) {
            addRoute(toAddList.get(0));
            toAddList.remove(0);
            i++;
        }
        return i;
    }

    public void update(int index, Route route){
        this.routes.set(index, route);
    }
}
