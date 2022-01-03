package com.example.parceldelivery.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.AdminApiService;
import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.Models.ResViewUsersModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.adapters.CompletedOrdersRecycleAdapter;
import com.example.parceldelivery.adapters.UsersRecycleAdapter;
import com.example.parceldelivery.rider.CompletedOrder;
import com.example.parceldelivery.sender.MyOrder;
import com.example.parceldelivery.sender.MyOrdersActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends AppCompatActivity {

    private ArrayList<User> userList;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.usersRecycleView);
        userList = new ArrayList<>();

        progressDialog = new ProgressDialog(UsersActivity.this);

        setUserInfo();
        //setAdapter();

    }

    private void setAdapter() {
        UsersRecycleAdapter adapter = new UsersRecycleAdapter(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setUserInfo() {
        getAllUsers();
    }

    private void getAllUsers() {

        showProgressBar("All Users", "Please wait....");

        AdminApiService retrofitAPI = ApiCaller.getClient().create(AdminApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<List<ResViewUsersModel>>> call = retrofitAPI.getAllUsers(preferences.getString(Constants.TOKEN_STORE, ""));

        call.enqueue(new Callback<ApiResponse<List<ResViewUsersModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ResViewUsersModel>>> call, Response<ApiResponse<List<ResViewUsersModel>>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        for(ResViewUsersModel user : response.body().getData()) {
                            userList.add(new User((user.getUserName()), user.getUserType().equals("A") ? "Admin" : user.getUserType().equals("S") ? "Sender" : "Rider"));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }
                }
                setAdapter();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ResViewUsersModel>>> call, Throwable t) {
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