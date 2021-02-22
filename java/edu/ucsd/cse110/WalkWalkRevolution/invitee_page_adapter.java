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

public class invitee_page_adapter extends RecyclerView.Adapter<invitee_page_adapter.ViewHolder>{

    ArrayList<String> invitee_name_list;
    private invitee_page_interface invitee_page_interface;
    ColorGenerator generator = ColorGenerator.MATERIAL;


    public invitee_page_adapter(ArrayList<String> invitee_name_list,invitee_page_interface invitee_page_interfacee){
        this.invitee_name_list = invitee_name_list;
        this.invitee_page_interface = invitee_page_interfacee;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater  layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.invitee_row,  parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.invitee_name.setText(invitee_name_list.get(position));
        //TODO need real teammate name to display   the initial
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(invitee_name_list.get(position).charAt(0)), generator.getRandomColor());
        holder.invitee_imageView.setImageDrawable(drawable);
    }


    @Override
    public int getItemCount() {
        return invitee_name_list.size();
    }

    class ViewHolder  extends RecyclerView.ViewHolder{
        TextView invitee_name;
        ImageView  invitee_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            invitee_name = itemView.findViewById(R.id.invitee_name);
            invitee_imageView = itemView.findViewById(R.id.invitee_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invitee_page_interface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
