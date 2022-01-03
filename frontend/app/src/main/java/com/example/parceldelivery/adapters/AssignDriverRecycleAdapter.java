package com.example.parceldelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parceldelivery.R;
import com.example.parceldelivery.admin.AssignDriver;
import com.example.parceldelivery.admin.PendingOrder;

import java.util.ArrayList;

public class AssignDriverRecycleAdapter extends RecyclerView.Adapter<AssignDriverRecycleAdapter.MyViewHolderAssignDriver> {

    private ArrayList<AssignDriver> driverList;
    private AssignDriverRecycleViewClickListner listener;

    public AssignDriverRecycleAdapter(ArrayList<AssignDriver> driverList
            , AssignDriverRecycleAdapter.AssignDriverRecycleViewClickListner listener
    ) {
        this.driverList = driverList;
        this.listener = listener;
    }

    public class MyViewHolderAssignDriver extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        private TextView idText;
        private TextView nameText;
        private TextView statusText;

        public MyViewHolderAssignDriver(final View view){
            super(view);
            idText = view.findViewById(R.id.list_item_driver_id);
            nameText = view.findViewById(R.id.list_item_driver_name);
            statusText = view.findViewById(R.id.list_item_driver_status);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public AssignDriverRecycleAdapter.MyViewHolderAssignDriver onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_4, parent, false);
        return new AssignDriverRecycleAdapter.MyViewHolderAssignDriver(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignDriverRecycleAdapter.MyViewHolderAssignDriver holder, int position) {
        long id = driverList.get(position).getDriverId();
        String desc = driverList.get(position).getDriverName();
        String status = driverList.get(position).getDriverStatus();

        holder.idText.setText(id + "");
        holder.nameText.setText(desc);
        holder.statusText.setText(status);
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public interface AssignDriverRecycleViewClickListner{
        void onClick(View v, int position);
    }
}
