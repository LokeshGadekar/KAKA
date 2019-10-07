package com.info.manPower.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.Internet_Connectivity;
import com.info.manPower.Model.Forgot_Responce;
import com.info.manPower.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Forget_password_activity extends AppCompatActivity
{

    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart;

    private EditText edmail;
    private Button otpbutton;
    private String mail;
    private API_parameter ApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txToolbar = (TextView) findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);
        cart = findViewById(R.id.icon_cart);
        cart_count = findViewById(R.id.cart_count);
        cart.setVisibility(View.GONE);
        cart_count.setVisibility(View.GONE);

        edmail = findViewById(R.id.ed_mmail);
        otpbutton = findViewById(R.id.buttn_otp);
        txToolbar.setText("Forgot Password");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        ApiService  = BaseUrl.getAPIService();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        otpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Internet_Connectivity.isConnected(Forget_password_activity.this))
                {
                    mail = edmail.getText().toString().trim();
                    if (mail.matches(""))
                    {
                        Toast.makeText(Forget_password_activity.this, "Please enter Email address...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {        GETOTP();            }
                }
        else
        {   SweetAlertDialog pDialog = new SweetAlertDialog(Forget_password_activity.this, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Oops...");
            pDialog.setContentText("No Internet Connection !");
            pDialog.show();        }
            }
        });

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));    }

    private void GETOTP()
    {
        ApiService.FORGOT_CALL(mail).enqueue(new Callback<Forgot_Responce>() {
            @Override
            public void onResponse(Call<Forgot_Responce> call, Response<Forgot_Responce> response) {
                if (response.body().getResponce()) {
                    Log.e("Forget pass RESPONSE.", "" + new Gson().toJson(response.body()));
                    Intent in1 = new Intent(Forget_password_activity.this,OTP_activity.class);
                    Toast.makeText(Forget_password_activity.this, "OTP sent to mail address...", Toast.LENGTH_SHORT).show();
                    in1.putExtra("userid",response.body().getUserId());
                    startActivity(in1);
                    finish();
                }
                else if (!response.body().getResponce())
                {
                    Toast.makeText(Forget_password_activity.this, "Password not Updated", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Forget_password_activity.this, "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Forgot_Responce> call, Throwable t) {
                Log.e("Forget FAILURE  ",""+t.getMessage());
                Log.e("Forget FAILURE  ",""+t.getLocalizedMessage());
                Log.e("Forget FAILURE  ",""+t.getStackTrace());
                Log.e("Forget FAILURE  ",""+t.getSuppressed());
                Log.e("Forget FAILURE  ",""+t.getCause());
            }
        });
    }

}
