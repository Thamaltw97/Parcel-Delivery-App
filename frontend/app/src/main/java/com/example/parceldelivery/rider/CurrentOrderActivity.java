package com.example.parceldelivery.rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.RiderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ReqUpdateOrderStatusModel;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.admin.PendingOrder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentOrderActivity extends AppCompatActivity {

    private ArrayList<PendingOrder> orderList;
    RelativeLayout btn_at_door_steps, btn_delivered;
    private ProgressDialog progressDialog;
    TextView tv_name_assigned_order, tv_id_assigned_order, tv_send_address_assigned_order, tv_receive_address_assigned_order,
            tv_pick_phone_assigned_order, tv_desc_assigned_order;

    long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_name_assigned_order = findViewById(R.id.tv_name_assigned_order);
        tv_id_assigned_order = findViewById(R.id.tv_id_assigned_order);
        tv_send_address_assigned_order = findViewById(R.id.tv_send_address_assigned_order);
        tv_receive_address_assigned_order = findViewById(R.id.tv_receive_address_assigned_order);
        tv_pick_phone_assigned_order = findViewById(R.id.tv_pick_phone_assigned_order);
        tv_desc_assigned_order = findViewById(R.id.tv_desc_assigned_order);

        btn_at_door_steps = findViewById(R.id.btn_at_door_steps);
        btn_delivered = findViewById(R.id.btn_delivered);
        orderList = new ArrayList<>();

        progressDialog = new ProgressDialog(CurrentOrderActivity.this);
        orderId = 0;

        setOrderInfo();

        btn_at_door_steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(RiderHomeActivity.this, CurrentOrderActivity.class));

                if (ContextCompat.checkSelfPermission(CurrentOrderActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    //When permission is granted
                    //Create method
                    sendMessage();
                } else {
                    //When permission is not granted
                    //Request permission
                    ActivityCompat.requestPermissions(CurrentOrderActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }

            }
        });

        btn_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressBar("Deliver Order", "Please wait to deliver the order");

                RiderApiService retrofitAPI = ApiCaller.getClient().create(RiderApiService.class);
                SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
                ReqUpdateOrderStatusModel order = new ReqUpdateOrderStatusModel(orderId, "D");
                Call<ApiResponse<String>> call = retrofitAPI.updateOrderStatus(preferences.getString(Constants.TOKEN_STORE, ""), order);

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

    }

    private void setOrderInfo() {
        GetAllOrderByUserId();
    }

    private void GetAllOrderByUserId() {

        showProgressBar("Assigned Orders", "Please wait....");

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
                            if (order.getStatus().equals("S")) {
                                orderList.add(new PendingOrder((order.getOrderId() + ""), order.getDescription(), "Receiver : " + order.getReceiverName(), order.getStatus().equals("P") ? "Pending" : order.getStatus().equals("A") ? "Approved" : order.getStatus().equals("S") ? "Shipped" : "Delivered"));
                                tv_name_assigned_order.setText(("Name : " + order.getReceiverName()));
                                tv_id_assigned_order.setText(("Order ID : " + order.getOrderId()));
                                tv_send_address_assigned_order.setText(("Send addr. : " + order.getPickupLocation()));
                                tv_receive_address_assigned_order.setText(("Rec. addr. : " + order.getReceiveLocation()));
                                tv_pick_phone_assigned_order.setText(("Mobile : " + order.getReceiverMobile()));
                                tv_desc_assigned_order.setText(("Description : " + order.getDescription()));
                                orderId = order.getOrderId();
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                }
                //setAdapter();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ResViewOrderModel>>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void sendMessage() {
        //Get values from
        String tempMobile = (tv_pick_phone_assigned_order.getText().toString()).substring(1);
        String sPhone = "+94" + tempMobile;
        String sMessage = "Hi, your order at your door steps. Please collect.";
        //Check condition
        if (!sPhone.equals("") && !sMessage.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            try {
                smsManager.sendTextMessage(sPhone, null, sMessage, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent successfully!", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "SMS send failed!", Toast.LENGTH_LONG).show();
            }

            Toast.makeText(getApplicationContext(), "SMS sent successfully!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Details not sufficient!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if (requestCode == 100 && grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage();
        } else {
            //When permission is denied
            Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
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