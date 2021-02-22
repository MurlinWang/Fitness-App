package edu.ucsd.cse110.WalkWalkRevolution;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 *  Route_List_adaptor adapts the UI RouteList element for our backend ArrayList in a
 *  convenient holder to allow each object to be displayed
 */
public class Route_List_Adapter extends RecyclerView.Adapter<Route_List_Adapter.ViewHolder> {

    // Keep route_list and route_list_features parallel to each other, storing them by index
    ArrayList<String>  Route_List;
    ArrayList<String>  Route_List_features;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    // Set the onCluck interface and data representations to be displayed in the RycyclerViewer
    public Route_List_Adapter(ArrayList<String> Route_List,ArrayList<String> Route_List_features,
                              RecyclerViewClickInterface recyclerViewClickInterface){
        this.Route_List = Route_List;
        this.Route_List_features = Route_List_features;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    // Contains ViewHolder per each route
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.route_list_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Countains ViewTHolder for the entire routesList
    public void onBindViewHolder(ViewHolder holder,int position){
        //TODO add the real Routename and NoteField here
        holder.NoteField.setText(Route_List_features.get(position));
        holder.RouteName.setText(Route_List.get(position));
    }

    public int getItemCount(){
        return Route_List.size();
    }

    // ViewHolder maps onto each route, displaying chosen information and cute (rn default) icon
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView  RouteName, NoteField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            RouteName = itemView.findViewById(R.id.RouteName);
            NoteField = itemView.findViewById(R.id.NoteField);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });


        }
    }
}
