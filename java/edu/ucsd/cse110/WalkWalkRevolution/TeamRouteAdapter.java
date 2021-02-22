package edu.ucsd.cse110.WalkWalkRevolution;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.lang.Object;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 *  Route_List_adaptor adapts the UI RouteList element for our backend ArrayList in a
 *  convenient holder to allow each object to be displayed
 */
public class TeamRouteAdapter extends RecyclerView.Adapter<TeamRouteAdapter.ViewHolder> {

    // Keep route_list and route_list_features parallel to each other, storing them by index
    RouteList  Temmate_Route_List;
    ArrayList<String>  Temmate_Route_List_features;
    ArrayList<String>  Temmate_Route_Member_List;
    private TeamRouteinterface teamRouteinterface;
    ColorGenerator generator = ColorGenerator.MATERIAL;

    // Set the onCluck interface and data representations to be displayed in the RycyclerViewer
    public TeamRouteAdapter(RouteList  Temmate_Route_List,ArrayList<String>  Temmate_Route_List_features,
                            TeamRouteinterface teamRouteinterface, ArrayList<String> Temmate_Route_Member_List){
        this.Temmate_Route_List = Temmate_Route_List;
        this.Temmate_Route_List_features = Temmate_Route_List_features;
        this.teamRouteinterface = teamRouteinterface;
        this.Temmate_Route_Member_List = Temmate_Route_Member_List;
    }

    // Contains ViewHolder per each route
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.teammate_route_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Countains ViewTHolder for the entire routesList
    public void onBindViewHolder(ViewHolder holder,int position){

        //TODO add the real Routename and NoteField here
        holder.Teammate_RouteName.setText(Temmate_Route_List.get(position).getName());
        holder.Teammate_NoteField.setText(Temmate_Route_List_features.get(position));
        int i = Temmate_Route_Member_List.get(position).toString().hashCode();
        //TODO now is  display the route inital, need teammate  name  to  display initall of temmate
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(Temmate_Route_Member_List.get(position).charAt(0))
                        , i*i*i*2*11*17*23*29*(-1));
        holder.Teammate_imageView.setImageDrawable(drawable);

    }

    public int getItemCount(){
        return Temmate_Route_List.size();
    }

    // ViewHolder maps onto each route, displaying chosen information and cute (rn default) icon
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Teammate_imageView;
        TextView  Teammate_RouteName, Teammate_NoteField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Teammate_imageView = itemView.findViewById(R.id.teammate_imageView);
            Teammate_RouteName = itemView.findViewById(R.id.teammate_RouteName);
            Teammate_NoteField = itemView.findViewById(R.id.teammate_NoteField);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    teamRouteinterface.onItemClick(getAdapterPosition());
                }
            });


        }
    }

}
