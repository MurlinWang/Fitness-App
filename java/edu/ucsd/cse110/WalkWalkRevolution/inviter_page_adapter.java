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

public class inviter_page_adapter extends RecyclerView.Adapter<inviter_page_adapter.ViewHolder>{

    ArrayList<String> inviter_name_list;
    private inviter_page_interface inviter_page_interface;
    ColorGenerator generator = ColorGenerator.MATERIAL;


    public inviter_page_adapter(ArrayList<String> inviter_name_list,inviter_page_interface inviter_page_interfacee){
        this.inviter_name_list = inviter_name_list;
        this.inviter_page_interface = inviter_page_interfacee;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater  layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.inviter_row,  parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.inviter_name.setText(inviter_name_list.get(position));
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(inviter_name_list.get(position).charAt(0)), generator.getRandomColor());
        holder.inviter_imageView.setImageDrawable(drawable);
    }


    @Override
    public int getItemCount() {
        return inviter_name_list.size();
    }

    class ViewHolder  extends RecyclerView.ViewHolder{
        TextView inviter_name;
        ImageView  inviter_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            inviter_name = itemView.findViewById(R.id.inviter_name);
            inviter_imageView = itemView.findViewById(R.id.inviter_imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inviter_page_interface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
