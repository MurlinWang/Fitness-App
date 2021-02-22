package edu.ucsd.cse110.WalkWalkRevolution;

import android.media.Image;
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

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder>{

    ArrayList<String> Team_list;
    private TeamScreenInterface teamScreenInterface;
    ColorGenerator generator = ColorGenerator.MATERIAL;


    public TeamAdapter(ArrayList<String> Team_list,TeamScreenInterface teamScreenInterface){
        this.Team_list = Team_list;
        this.teamScreenInterface = teamScreenInterface;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater  layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.route_list_team_item,  parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.teammate_name.setText(Team_list.get(position));
        //TODO need real teammate name to display   the initial
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(Team_list.get(position).charAt(0)), generator.getRandomColor());
        holder.imageView_teammate.setImageDrawable(drawable);
    }


    @Override
    public int getItemCount() {
        return Team_list.size();
    }
    class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView imageView_teammate;
        TextView teammate_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView_teammate = itemView.findViewById(R.id.teammate_list_imageView);
            teammate_name = itemView.findViewById(R.id.teammate_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    teamScreenInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
