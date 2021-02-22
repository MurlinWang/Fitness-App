package edu.ucsd.cse110.WalkWalkRevolution;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder>{

    ArrayList<String> Teammate_status_list;
    private Invitation_interface invitation_interface;


    public InvitationAdapter(ArrayList<String> Teammate_status_list,Invitation_interface invitation_interface){
        this.Teammate_status_list = Teammate_status_list;
        this.invitation_interface = invitation_interface;
    }
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater  layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.teammate_status_list,  parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.invitation_teammate_name.setText(Teammate_status_list.get(position));
    }


    @Override
    public int getItemCount() {
        return Teammate_status_list.size();
    }

    class ViewHolder  extends RecyclerView.ViewHolder{
        TextView invitation_teammate_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            invitation_teammate_name = itemView.findViewById(R.id.invitation_teammate_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invitation_interface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
