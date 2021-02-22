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

public class Propose_adapter extends RecyclerView.Adapter<Propose_adapter.ViewHolder>{

    ArrayList<String> propose_name_list;
    private propose_interface propose_interface;
    ColorGenerator generator = ColorGenerator.MATERIAL;


    public Propose_adapter(ArrayList<String> propose_name,propose_interface propose_interface){
        this.propose_name_list = propose_name;
        this.propose_interface = propose_interface;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater  layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.proppose_status_row,  parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.propose_name.setText(propose_name_list.get(position));
        //TODO now is  display the route inital, need teammate  name  to  display initall of temmate
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(propose_name_list.get(position).charAt(0)), generator.getRandomColor());
        holder.propose_imageView.setImageDrawable(drawable);
    }


    @Override
    public int getItemCount() {
        return propose_name_list.size();
    }

    class ViewHolder  extends RecyclerView.ViewHolder{
        TextView propose_name;
        ImageView propose_imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propose_name = itemView.findViewById(R.id.propose_name);
            propose_imageView = itemView.findViewById(R.id.propose_imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    propose_interface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
