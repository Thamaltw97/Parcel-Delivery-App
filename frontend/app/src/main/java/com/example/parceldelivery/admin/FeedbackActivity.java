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
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ResViewFeedbackModel;
import com.example.parceldelivery.Models.ResViewUsersModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.adapters.FeedbackRecycleAdapter;
import com.example.parceldelivery.adapters.UsersRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private ArrayList<Feedback> feedbackList;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        recyclerView = findViewById(R.id.feedbackRecycleView);
        feedbackList = new ArrayList<>();

        progressDialog = new ProgressDialog(FeedbackActivity.this);

        setFeedbackInfo();
        setAdapter();

    }

    private void setAdapter() {
        FeedbackRecycleAdapter adapter = new FeedbackRecycleAdapter(feedbackList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setFeedbackInfo() {
//        feedbackList.add(new Feedback("Thamal Wijetunge", "Good service. Good service. Good service."));
//        feedbackList.add(new Feedback("Kusal Perera", "Nice support. Nice support. Nice support."));
//        feedbackList.add(new Feedback("Dimuthu Abeysinghe", "Impressive."));
        getAllFeedbacks();
    }

    private void getAllFeedbacks() {

        showProgressBar("All Users", "Please wait....");

        AdminApiService retrofitAPI = ApiCaller.getClient().create(AdminApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<List<ResViewFeedbackModel>>> call = retrofitAPI.getAllFeedbacks(preferences.getString(Constants.TOKEN_STORE, ""));

        call.enqueue(new Callback<ApiResponse<List<ResViewFeedbackModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ResViewFeedbackModel>>> call, Response<ApiResponse<List<ResViewFeedbackModel>>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        for(ResViewFeedbackModel feedback : response.body().getData()) {
                            feedbackList.add(new Feedback((feedback.getUserName()), feedback.getFeedback()));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }
                }
                setAdapter();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ResViewFeedbackModel>>> call, Throwable t) {
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