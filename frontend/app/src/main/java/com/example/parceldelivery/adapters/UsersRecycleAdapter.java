package com.example.parceldelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parceldelivery.R;
import com.example.parceldelivery.admin.User;
import com.example.parceldelivery.rider.CompletedOrder;

import java.util.ArrayList;

public class UsersRecycleAdapter extends RecyclerView.Adapter<UsersRecycleAdapter.MyViewHolderUsers> {

    private ArrayList<User> userList;

    public UsersRecycleAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    public class MyViewHolderUsers extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView typeText;

        public MyViewHolderUsers(final View view){
            super(view);
            nameText = view.findViewById(R.id.list_item_driver_name);
            typeText = view.findViewById(R.id.list_item_driver_status);
        }

    }

    @NonNull
    @Override
    public UsersRecycleAdapter.MyViewHolderUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_4, parent, false);
        return new UsersRecycleAdapter.MyViewHolderUsers(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRecycleAdapter.MyViewHolderUsers holder, int position) {
        String name = userList.get(position).getUserName();
        String type = userList.get(position).getUserType();

        holder.nameText.setText(name);
        holder.typeText.setText(type);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
