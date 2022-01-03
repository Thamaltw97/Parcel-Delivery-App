package com.example.parceldelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parceldelivery.R;
import com.example.parceldelivery.rider.AssignedOrder;
import com.example.parceldelivery.rider.CompletedOrder;

import java.util.ArrayList;

public class CompletedOrdersRecycleAdapter extends RecyclerView.Adapter<CompletedOrdersRecycleAdapter.MyViewHolderCompletedRider> {

    private ArrayList<CompletedOrder> orderList;

    public CompletedOrdersRecycleAdapter(ArrayList<CompletedOrder> orderList) {
        this.orderList = orderList;
    }

    public class MyViewHolderCompletedRider extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView addressText;

        public MyViewHolderCompletedRider(final View view){
            super(view);
            nameText = view.findViewById(R.id.list_item_completed_order_desc);
            addressText = view.findViewById(R.id.list_item_completed_order_address);
        }

    }

    @NonNull
    @Override
    public CompletedOrdersRecycleAdapter.MyViewHolderCompletedRider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_3, parent, false);
        return new CompletedOrdersRecycleAdapter.MyViewHolderCompletedRider(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrdersRecycleAdapter.MyViewHolderCompletedRider holder, int position) {
        String desc = orderList.get(position).getOrderDesc();
        String address = orderList.get(position).getOrderAddress();

        holder.nameText.setText(desc);
        holder.addressText.setText(address);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
