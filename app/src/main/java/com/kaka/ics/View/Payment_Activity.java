package com.kaka.ics.View;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kaka.ics.R;

import org.json.JSONException;
import org.json.JSONObject;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class Payment_Activity extends AppCompatActivity
{
    Activity activity;
    int payed=0;
    String amail , aphone , aamt , apurpose , abuyernm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        activity = Payment_Activity.this;

        amail = getIntent().getStringExtra("amail");
        aphone = getIntent().getStringExtra("aphone");
        aamt = getIntent().getStringExtra("aamt");
        apurpose = getIntent().getStringExtra("apur");
        abuyernm = getIntent().getStringExtra("abnm");

        callInstamojoPay(amail , aphone , aamt , apurpose , abuyernm);
    }

    ///   INSTA_MOJO PAYMENT /////////////

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {

//      String  Payment_method = radio_online_pay.getText().toString();

        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        activity.registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;

    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                //pay_status = "Success";
                Toast.makeText(activity, "Payment Success", Toast.LENGTH_SHORT).show();
                payed = 1;
                updatedata();
                //Call_Order(passArray);
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("Error "," Pay "+s+"________________"+i);
                payed = 0;
                updatedata();
            }
        };
    }

    /// eof INSTA_MOJO PAYMENT /////////////

    private void updatedata(){
        Intent updates = new Intent("POST_ORDER");
        updates.putExtra("type", ""+payed);
        activity.sendBroadcast(updates);
    }

}


