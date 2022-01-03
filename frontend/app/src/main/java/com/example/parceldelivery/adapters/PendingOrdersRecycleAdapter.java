package com.example.parceldelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parceldelivery.R;
import com.example.parceldelivery.admin.PendingOrder;
import com.example.parceldelivery.rider.AssignedOrder;

import java.util.ArrayList;

public class PendingOrdersRecycleAdapter extends RecyclerView.Adapter<PendingOrdersRecycleAdapter.MyViewHolderPendingAdmin> {

    private ArrayList<PendingOrder> orderList;
    private PendingOrdersRecycleViewClickListner listener;

    public PendingOrdersRecycleAdapter(ArrayList<PendingOrder> orderList
            , PendingOrdersRecycleAdapter.PendingOrdersRecycleViewClickListner listener
    ) {
        this.orderList = orderList;
        this.listener = listener;
    }

    public class MyViewHolderPendingAdmin extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        private TextView idText;
        private TextView nameText;
        private TextView addressText;
        private TextView statusText;

        public MyViewHolderPendingAdmin(final View view){
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
    public PendingOrdersRecycleAdapter.MyViewHolderPendingAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_2, parent, false);
        return new PendingOrdersRecycleAdapter.MyViewHolderPendingAdmin(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrdersRecycleAdapter.MyViewHolderPendingAdmin holder, int position) {
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

    public interface PendingOrdersRecycleViewClickListner{
        void onClick(View v, int position);
    }
}
