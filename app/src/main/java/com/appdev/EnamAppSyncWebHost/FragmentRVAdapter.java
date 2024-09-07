package com.appdev.EnamAppSyncWebHost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FragmentRVAdapter extends RecyclerView.Adapter<FragmentRVAdapter.ViewHolder> {

   private Context context;
    private List<ParticipantModal> participantList;

    // Constructor to initialize the list
    public FragmentRVAdapter(Context context,List<ParticipantModal> participantList) {
        this.context = context;
        this.participantList = participantList;
    }

    @Override
    public FragmentRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewfg = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_item, parent, false);
        return new ViewHolder(viewfg);
    }

    @Override
    public void onBindViewHolder(FragmentRVAdapter.ViewHolder holder, int position) {
        ParticipantModal participant = participantList.get(position);
        holder.tvParticipantName.setText(participant.getName());
        holder.tvParticipantGender.setText(participant.getGender());
        // Set other details as needed
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvParticipantName, tvParticipantGender;
        // Other views

        public ViewHolder(View itemView) {
            super(itemView);
            tvParticipantName = itemView.findViewById(R.id.tvParticipantName);
            tvParticipantGender = itemView.findViewById(R.id.tvParticipantGender);
            // Initialize other views
        }
    }

    // ... other methods if needed ...
}
