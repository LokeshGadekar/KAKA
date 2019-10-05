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
import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.Internet_Connectivity;
import com.info.manPower.AppUtils.Session_management;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.Model.Registration_Responce;
import com.info.manPower.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login_activity extends AppCompatActivity
{
    private EditText edMobile, edPass;
    private Button login;
    private String mobile, pass;
    private TextView register, cnt;
    Toolbar toolbar;
    TextView txToolbar;
    ImageView imgToolbar, cart;
    Session_management sessionManagement;

    private API_parameter ApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        edMobile = (EditText) findViewById(R.id.ed_name);
        edPass = (EditText) findViewById(R.id.ed_password);
        login = (Button) findViewById(R.id.buttn_login);
        register = (TextView) findViewById(R.id.tx_register);
        cart = (ImageView) findViewById(R.id.cart);
        cnt = (TextView) findViewById(R.id.cart_count);
        cart.setVisibility(View.GONE);
        cnt.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txToolbar = (TextView) findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);

        txToolbar.setText("Login");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Click_Listeners();
        ApiService = BaseUrl.getAPIService();
        sessionManagement = new Session_management(Login_activity.this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


    private void Click_Listeners()
    {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Login_activity.this,Registration_activity.class);
                startActivity(in);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                {
                    if (Internet_Connectivity.isConnected(Login_activity.this))
                {   APIlogin(); }
                else
                    {   SweetAlertDialog pDialog = new SweetAlertDialog(Login_activity.this, SweetAlertDialog.ERROR_TYPE);
                        pDialog.setTitleText("Oops...");
                        pDialog.setContentText("No Internet Connection !");
                        pDialog.show();                                         }
                }
                else
                {

                }
            }
        });
    }

    private Boolean validate()
    {
        mobile = edMobile.getText().toString().trim();
        pass = edPass.getText().toString().trim();

        if (mobile.isEmpty())
        {
            Toast.makeText(Login_activity.this, "Please enter mobile no. correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (pass.isEmpty())
        {
            Toast.makeText(Login_activity.this, "Please enter password correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }


        private void APIlogin()
        {
            ApiService.LOGIN_CALL(mobile,pass).enqueue(new Callback<Registration_Responce>() {
                @Override
                public void onResponse(Call<Registration_Responce> call, Response<Registration_Responce> response) {
                    Log.e("REGISTRATION RESPONSE.", "" + new Gson().toJson(response.body()));
                    Log.e("REGISTRATION RESPONSE.", "-------------------------------------------------");

                    if (response.body().getResponce())
                    {
                        Toast.makeText(Login_activity.this, "Logged In...", Toast.LENGTH_SHORT).show();
                        AppPrefrences.setName(Login_activity.this, response.body().getData().getfName()+" "+response.body().getData().getlName());
                        AppPrefrences.setMobile(Login_activity.this, response.body().getData().getMobileNo());
                        AppPrefrences.setUserid(Login_activity.this, response.body().getData().getUserId());
                        AppPrefrences.setMail(Login_activity.this, response.body().getData().getUserEmail());
                        AppPrefrences.setAddress(Login_activity.this, response.body().getData().getAddress());
                        sessionManagement.createLoginSession(response.body().getData().getUserId(),response.body().getData().getfName()
                                ,response.body().getData().getUserEmail(),response.body().getData().getPassword());
                        clear_edText();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Login_activity.this, "Invalid Mobile No./Password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Registration_Responce> call, Throwable t) {
                    Log.e("API Login Error ..." ,""+t);
                }
            });
        }

        private void clear_edText()
        {
            edMobile.getText().clear();
            edPass.getText().clear();
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilview.hidekeyboard(Login_activity.this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));    }
}
