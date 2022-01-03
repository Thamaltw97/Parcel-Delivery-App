package com.example.parceldelivery.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.AdminApiService;
import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.adapters.AssignedOrdersRecycleAdapter;
import com.example.parceldelivery.adapters.MyOrdersRecycleAdapter;
import com.example.parceldelivery.adapters.PendingOrdersRecycleAdapter;
import com.example.parceldelivery.rider.AssignedOrder;
import com.example.parceldelivery.sender.MyOrder;
import com.example.parceldelivery.sender.OrderDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOrdersActivity extends AppCompatActivity {

    private ArrayList<PendingOrder> orderList;
    private RecyclerView recyclerView;
    private PendingOrdersRecycleAdapter.PendingOrdersRecycleViewClickListner listener;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        recyclerView = findViewById(R.id.pendingOrdersRecycleView);
        orderList = new ArrayList<>();

        progressDialog = new ProgressDialog(PendingOrdersActivity.this);

        setOrderInfo();
        setAdapter();

    }

    private void setAdapter() {
        setOnClickListener();
        PendingOrdersRecycleAdapter adapter = new PendingOrdersRecycleAdapter(orderList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOrderInfo() {
        getAllPendingOrders();
    }

    private void getAllPendingOrders() {

        showProgressBar("All Orders", "Please wait....");

        AdminApiService retrofitAPI = ApiCaller.getClient().create(AdminApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<List<ResViewOrderModel>>> call = retrofitAPI.getAllOrders(preferences.getString(Constants.TOKEN_STORE, ""));

        call.enqueue(new Callback<ApiResponse<List<ResViewOrderModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ResViewOrderModel>>> call, Response<ApiResponse<List<ResViewOrderModel>>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()){
                        for(ResViewOrderModel order : response.body().getData()) {
                            if (order.getStatus().equals("P")) {
                                orderList.add(new PendingOrder((order.getOrderId() + ""), order.getDescription(), order.getReceiveLocation(), "Pending"));
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }
                }
                setAdapter();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ResViewOrderModel>>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setOnClickListener() {
        listener = new PendingOrdersRecycleAdapter.PendingOrdersRecycleViewClickListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), AssignDriverActivity.class);
                intent.putExtra("orderId", orderList.get(position).getOrderId());
                startActivity(intent);
            }
        };
    }

    private void showProgressBar(String title, String desc) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(desc);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }

}