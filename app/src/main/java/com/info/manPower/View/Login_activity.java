package com.info.manPower.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.info.manPower.Model.single_responce;
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
    private TextView cnt, forgotpass;
    private LinearLayout register;
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
        register = (LinearLayout) findViewById(R.id.tx_register);
        forgotpass = (TextView) findViewById(R.id.tx_forgotpass);
        cart = (ImageView) findViewById(R.id.icon_cart);
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
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Login_activity.this,Forget_password_activity.class);
                startActivity(in);
                finish();
            }
        });

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
                        SEND_TOKEN(response.body().getData().getUserId());
                        Intent main_in = new Intent(Login_activity.this,MainActivity_drawer.class);
                        startActivity(main_in);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Login_activity.this, "Invalid Mobile No./Password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Registration_Responce> call, Throwable t) {
                    Log.e("API LOGIN_CALL Error" ,"  "+t.getStackTrace());
                    Log.e("API LOGIN_CALL Error" ,"  "+t.getMessage());
                    Log.e("API LOGIN_CALL Error" ,"  "+t.getCause());
                    Log.e("API LOGIN_CALL Error" ,"  "+t.getLocalizedMessage());
                }
            });
        }

        //____  SEND TOKEN ____

        private void SEND_TOKEN(final String user_id)
        {
            ApiService.Send_TOKEN(user_id,AppPrefrences.getUserToken(Login_activity.this)).enqueue(new Callback<single_responce>() {
                @Override
                public void onResponse(Call<single_responce> call, Response<single_responce> response) {

                    Log.e("SEND_TOKEN RESPONSE.", "" + new Gson().toJson(response.body().getResponce()));
                    Log.e("SEND_TOKEN RESPONSE.", "-------------------------------------------------");

                    if (response.body().getResponce())
                    {
                        Log.e(" TOKEN IS ... "," "+user_id+"_________"+AppPrefrences.getUserToken(Login_activity.this));
                        Toast.makeText(Login_activity.this, "Token Sent Successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else if (!response.body().getResponce())
                    {
                        //Toast.makeText(Login_activity.this, "Token Sent Successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                    }                }

                @Override
                public void onFailure(Call<single_responce> call, Throwable t) {
                    Log.e("API TOKEN Error ..." ,""+t.getStackTrace());
                    Log.e("API TOKEN Error ..." ,""+t.getMessage());
                    Log.e("API TOKEN Error ..." ,""+t.getCause());
                    Log.e("API TOKEN Error ..." ,""+t.getLocalizedMessage());
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
        finish();
        Utilview.hidekeyboard(Login_activity.this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));    }
}
