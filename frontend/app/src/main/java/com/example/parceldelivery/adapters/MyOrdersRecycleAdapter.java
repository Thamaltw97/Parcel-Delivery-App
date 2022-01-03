package com.example.parceldelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parceldelivery.R;
import com.example.parceldelivery.sender.MyOrder;

import java.util.ArrayList;

public class MyOrdersRecycleAdapter extends RecyclerView.Adapter<MyOrdersRecycleAdapter.MyViewHolder> {

    private ArrayList<MyOrder> orderList;
    private MyOrdersRecycleViewClickListner listener;

    public MyOrdersRecycleAdapter(ArrayList<MyOrder> orderList, MyOrdersRecycleViewClickListner listener){
        this.orderList = orderList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView idText;
        private TextView nameText;
        private TextView receiverText;
        private TextView statusText;

        public MyViewHolder(final View view){
            super(view);
            idText = view.findViewById(R.id.list_item_my_order_id);
            nameText = view.findViewById(R.id.list_item_my_order_desc);
            receiverText = view.findViewById(R.id.list_item_my_order_receiver);
            statusText = view.findViewById(R.id.list_item_my_order_status);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyOrdersRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_1, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersRecycleAdapter.MyViewHolder holder, int position) {
        String id = orderList.get(position).getOrderId();
        String desc = orderList.get(position).getOrderDesc();
        String receiver = orderList.get(position).getOrderReceiver();
        String status = orderList.get(position).getOrderStatus();

        holder.idText.setText(id);
        holder.nameText.setText(desc);
        holder.receiverText.setText(receiver);
        holder.statusText.setText(status);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public interface MyOrdersRecycleViewClickListner{
        void onClick(View v, int position);
    }
}
