package com.example.parceldelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parceldelivery.R;
import com.example.parceldelivery.rider.AssignedOrder;

import java.util.ArrayList;

public class AssignedOrdersRecycleAdapter extends RecyclerView.Adapter<AssignedOrdersRecycleAdapter.MyViewHolderAssignedRider> {

    private ArrayList<AssignedOrder> orderList;
    private AssignedOrdersRecycleAdapter.AssignedOrdersRecycleViewClickListner listener;

    public AssignedOrdersRecycleAdapter(ArrayList<AssignedOrder> orderList, AssignedOrdersRecycleAdapter.AssignedOrdersRecycleViewClickListner listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    public class MyViewHolderAssignedRider extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        private TextView idText;
        private TextView nameText;
        private TextView addressText;
        private TextView statusText;

        public MyViewHolderAssignedRider(final View view){
            super(view);
            idText = view.findViewById(R.id.list_item_assigned_order_id);
            nameText = view.findViewById(R.id.list_item_assigned_order_desc);
            addressText = view.findViewById(R.id.list_item_assigned_order_address);
            statusText = view.findViewById(R.id.list_item_assigned_order_status);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public AssignedOrdersRecycleAdapter.MyViewHolderAssignedRider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_5, parent, false);
        return new AssignedOrdersRecycleAdapter.MyViewHolderAssignedRider(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignedOrdersRecycleAdapter.MyViewHolderAssignedRider holder, int position) {
        String id = orderList.get(position).getOrderId();
        String desc = orderList.get(position).getOrderDesc();
        String address = orderList.get(position).getOrderAddress();
        String status = orderList.get(position).getOrderStatus();

        holder.idText.setText(id);
        holder.nameText.setText(desc);
        holder.addressText.setText(address);
        holder.statusText.setText(status);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public interface AssignedOrdersRecycleViewClickListner{
        void onClick(View v, int position);
    }
}
