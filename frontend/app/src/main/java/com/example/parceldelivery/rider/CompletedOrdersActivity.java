package com.example.parceldelivery.rider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.RiderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.adapters.AssignedOrdersRecycleAdapter;
import com.example.parceldelivery.adapters.CompletedOrdersRecycleAdapter;
import com.example.parceldelivery.adapters.PendingOrdersRecycleAdapter;
import com.example.parceldelivery.admin.PendingOrder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedOrdersActivity extends AppCompatActivity {

    private ArrayList<CompletedOrder> orderList;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders);

        recyclerView = findViewById(R.id.completedOrdersRecycleView);
        orderList = new ArrayList<>();

        progressDialog = new ProgressDialog(CompletedOrdersActivity.this);

        setOrderInfo();
        setAdapter();

    }

    private void setAdapter() {
        CompletedOrdersRecycleAdapter adapter = new CompletedOrdersRecycleAdapter(orderList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOrderInfo() {
//        orderList.add(new CompletedOrder("Macbook Pro 2019", "Address : Ambalangoda"));
//        orderList.add(new CompletedOrder("Iphone 13 pro max", "Address : Malabe"));
//        orderList.add(new CompletedOrder("IWatch series 7", "Address : Kurunegala"));
        GetAllOrderByUserId();
    }

    private void GetAllOrderByUserId() {

        showProgressBar("Completed Orders", "Please wait....");

        RiderApiService retrofitAPI = ApiCaller.getClient().create(RiderApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<List<ResViewOrderModel>>> call = retrofitAPI.getOrderById(preferences.getString(Constants.TOKEN_STORE, ""), preferences.getLong(Constants.USERID_STORE, 0));

        call.enqueue(new Callback<ApiResponse<List<ResViewOrderModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ResViewOrderModel>>> call, Response<ApiResponse<List<ResViewOrderModel>>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()){
                        for(ResViewOrderModel order : response.body().getData()) {
                            if (order.getStatus().equals("D")) {
                                orderList.add(new CompletedOrder(order.getDescription(), order.getReceiveLocation()));
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
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