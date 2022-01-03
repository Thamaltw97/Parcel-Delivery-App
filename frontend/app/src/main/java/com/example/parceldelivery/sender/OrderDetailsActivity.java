package com.example.parceldelivery.sender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ReqCreateFeedbackModel;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.rider.CurrentOrderActivity;
import com.example.parceldelivery.rider.RiderHomeActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView tv_name_my_order_details,tv_id_my_order_details,
    tv_address_my_order_details, tv_pick_my_order_details, tv_pick_phone_my_order_details,
            tv_desc_my_order_details, tv_status_my_order_details;
    RelativeLayout btn_drop_a_feedback, btn_delete_pending_order;
    private ProgressDialog progressDialog;

    long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tv_name_my_order_details = findViewById(R.id.tv_name_my_order_details);
        tv_id_my_order_details = findViewById(R.id.tv_id_my_order_details);
        tv_address_my_order_details = findViewById(R.id.tv_address_my_order_details);
        tv_pick_my_order_details = findViewById(R.id.tv_pick_my_order_details);
        tv_pick_phone_my_order_details = findViewById(R.id.tv_pick_phone_my_order_details);
        tv_desc_my_order_details = findViewById(R.id.tv_desc_my_order_details);
        tv_status_my_order_details = findViewById(R.id.tv_status_my_order_details);

        btn_drop_a_feedback = findViewById(R.id.btn_drop_a_feedback);
        btn_delete_pending_order = findViewById(R.id.btn_delete_pending_order);

        progressDialog = new ProgressDialog(OrderDetailsActivity.this);

        orderId = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            orderId = Long.parseLong(extras.getString("orderId"));
        }

        setOrderInfo(orderId);

        btn_drop_a_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_status_my_order_details.getText().equals("Delivered")) {
                    openFeedbackDialog(tv_desc_my_order_details.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Order is not completed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_delete_pending_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_status_my_order_details.getText().equals("Pending")) {
                    openDeleteDialog(tv_desc_my_order_details.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Can't delete. Order is already approved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setOrderInfo(long orderId) {
        getOrderInfoByOrderId(orderId);
    }

    private void getOrderInfoByOrderId(long orderId) {

        showProgressBar("Getting order details", "Please wait....");

        SenderApiService retrofitAPI = ApiCaller.getClient().create(SenderApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<ResViewOrderModel>> call = retrofitAPI.getOrderInfoByOrderId(preferences.getString(Constants.TOKEN_STORE, ""), orderId);

        call.enqueue(new Callback<ApiResponse<ResViewOrderModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResViewOrderModel>> call, Response<ApiResponse<ResViewOrderModel>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        //ApiResponse resApi = response.body();
                        tv_name_my_order_details.setText(response.body().getData().getReceiverName());
                        tv_id_my_order_details.setText(response.body().getData().getOrderId() + "");
                        tv_address_my_order_details.setText(response.body().getData().getReceiveLocation());
                        tv_pick_my_order_details.setText(response.body().getData().getPickupLocation());
                        tv_pick_phone_my_order_details.setText(response.body().getData().getReceiverMobile());
                        tv_desc_my_order_details.setText(response.body().getData().getDescription());
                        tv_status_my_order_details.setText(response.body().getData().getStatus().equals("P") ? "Pending" : response.body().getData().getStatus().equals("A") ? "Approved" : response.body().getData().getStatus().equals("S") ? "Shipped" : "Delivered");
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown error.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResViewOrderModel>> call, Throwable t) {
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

    public void openFeedbackDialog(String description){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText strFeedback = new EditText(OrderDetailsActivity.this);
        strFeedback.setInputType(InputType.TYPE_CLASS_TEXT);
        //strFeedback.setPadding(10, 5, 10, 5);
        builder.setView(strFeedback);

        builder.setMessage("Enter your feedback.")
                .setCancelable(false)
                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //Toast.makeText(getApplicationContext(), strFeedback.getText(), Toast.LENGTH_SHORT).show();

                        showProgressBar("Sending feedback", "Please wait to send the feedback");


                        SenderApiService retrofitAPI = ApiCaller.getClient().create(SenderApiService.class);
                        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
                        ReqCreateFeedbackModel feedback = new ReqCreateFeedbackModel(strFeedback.getText().toString(), orderId, preferences.getLong(Constants.USERID_STORE, 0));
                        Call<ApiResponse<String>> call = retrofitAPI.createFeedback(preferences.getString(Constants.TOKEN_STORE, ""), feedback);

                        call.enqueue(new Callback<ApiResponse<String>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                                hideProgressBar();
                                if(response.isSuccessful()) {
                                    String res = response.body().getData();
                                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                                hideProgressBar();
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(description);
        alert.show();

    }

    public void openDeleteDialog(String description){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to delete order : " + description)
                .setCancelable(false)
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        showProgressBar("Cancelling thr order", "Please wait to cancel the order");
                        SenderApiService retrofitAPI = ApiCaller.getClient().create(SenderApiService.class);
                        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
                        Call<ApiResponse<String>> call = retrofitAPI.deleteOrder(preferences.getString(Constants.TOKEN_STORE, ""), orderId);

                        call.enqueue(new Callback<ApiResponse<String>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                                hideProgressBar();
                                if(response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), response.body().getData(), Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(OrderDetailsActivity.this, MyOrdersActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                                hideProgressBar();
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("CONFIRMATION");
        alert.show();

    }

}