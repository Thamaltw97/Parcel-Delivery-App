package com.example.parceldelivery.sender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.adapters.MyOrdersRecycleAdapter;
import com.example.parceldelivery.auth.LoginActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {

    private ArrayList<MyOrder> orderList;
    private RecyclerView recyclerView;
    private MyOrdersRecycleAdapter.MyOrdersRecycleViewClickListner listener;
    private ProgressDialog progressDialog;
    private ArrayList<String> arrOrders;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        recyclerView = findViewById(R.id.myOrdersRecycleView);
        orderList = new ArrayList<>();

        progressDialog = new ProgressDialog(MyOrdersActivity.this);

        setOrderInfo();
        setAdapter();
        
    }

    private void setAdapter() {
        setOnClickListener();
        MyOrdersRecycleAdapter adapter = new MyOrdersRecycleAdapter(orderList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = new MyOrdersRecycleAdapter.MyOrdersRecycleViewClickListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                intent.putExtra("orderId", orderList.get(position).getOrderId());
                intent.putExtra("orderDesc", orderList.get(position).getOrderDesc());
                intent.putExtra("orderStatus", orderList.get(position).getOrderStatus());
                startActivity(intent);
            }
        };
    }

    private void setOrderInfo() {
        GetAllOrderByUserId();
    }

    private void GetAllOrderByUserId() {

        showProgressBar("All Orders", "Please wait....");

        SenderApiService retrofitAPI = ApiCaller.getClient().create(SenderApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<List<ResViewOrderModel>>> call = retrofitAPI.getAllOrderByUserId(preferences.getString(Constants.TOKEN_STORE, ""), preferences.getLong(Constants.USERID_STORE, 0));

        call.enqueue(new Callback<ApiResponse<List<ResViewOrderModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ResViewOrderModel>>> call, Response<ApiResponse<List<ResViewOrderModel>>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()){
                        for(ResViewOrderModel order : response.body().getData()) {
                            orderList.add(new MyOrder((order.getOrderId() + ""), order.getDescription(), "Receiver : " + order.getReceiverName(), order.getStatus().equals("P") ? "Pending" : order.getStatus().equals("A") ? "Approved" : order.getStatus().equals("S") ? "Shipped" : "Delivered"));
                        }
                    } else {
                       Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    if(response.code() == 403) {
                        Toast.makeText(getApplicationContext(), "Unauthorized Request.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
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