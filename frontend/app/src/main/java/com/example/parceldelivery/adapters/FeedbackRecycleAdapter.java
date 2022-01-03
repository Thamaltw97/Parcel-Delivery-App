package com.example.parceldelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parceldelivery.R;
import com.example.parceldelivery.admin.Feedback;
import com.example.parceldelivery.admin.User;

import java.util.ArrayList;

public class FeedbackRecycleAdapter extends RecyclerView.Adapter<FeedbackRecycleAdapter.MyViewHolderFeedback> {

    private ArrayList<Feedback> feedbackList;

    public FeedbackRecycleAdapter(ArrayList<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public class MyViewHolderFeedback extends RecyclerView.ViewHolder {
        private TextView senderNameText;
        private TextView feedbackDescText;

        public MyViewHolderFeedback(final View view){
            super(view);
            senderNameText = view.findViewById(R.id.list_item_completed_order_desc);
            feedbackDescText = view.findViewById(R.id.list_item_completed_order_address);
        }

    }

    @NonNull
    @Override
    public FeedbackRecycleAdapter.MyViewHolderFeedback onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_3, parent, false);
        return new FeedbackRecycleAdapter.MyViewHolderFeedback(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackRecycleAdapter.MyViewHolderFeedback holder, int position) {
        String name = feedbackList.get(position).getSenderName();
        String type = feedbackList.get(position).getFeedbackDesc();

        holder.senderNameText.setText(name);
        holder.feedbackDescText.setText(type);
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }
}
