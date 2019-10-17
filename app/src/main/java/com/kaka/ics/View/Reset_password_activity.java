package com.kaka.ics.View;

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
import com.kaka.ics.API_retro.API_parameter;
import com.kaka.ics.AppUtils.BaseUrl;
import com.kaka.ics.Model.Forgot_Responce;
import com.kaka.ics.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Reset_password_activity extends AppCompatActivity
{

    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart;
    private EditText ed1, ed2;
    private Button Confirm;
    private String password,pass2,userid;
    API_parameter ApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txToolbar = (TextView) findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);
        cart = findViewById(R.id.icon_cart);
        cart_count = findViewById(R.id.cart_count);
        cart.setVisibility(View.GONE);
        cart_count.setVisibility(View.GONE);

        ed1 = (EditText) findViewById(R.id.ed_passwrd);
        ed2 = (EditText) findViewById(R.id.ed_password1);
        Confirm = (Button) findViewById(R.id.buttn_reset);
        txToolbar.setText("Reset Password");
        userid = getIntent().getStringExtra("userid");
        Log.e("RESET PASS ... "," "+userid );
        ApiService = BaseUrl.getAPIService();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                {
                    RESET();
                }
                else
                {

                }
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

    private void RESET()
    {
        ApiService.PASSWORD_RESET(password,userid).enqueue(new Callback<Forgot_Responce>() {
            @Override
            public void onResponse(Call<Forgot_Responce> call, Response<Forgot_Responce> response) {
                if (response.body().getResponce()) {
                    Log.e("RESET RESPONSE.", "" + new Gson().toJson(response.body()));
                    Intent in1 = new Intent(Reset_password_activity.this,Login_activity.class);
                    Toast.makeText(Reset_password_activity.this, "Password Updated...", Toast.LENGTH_SHORT).show();
                    startActivity(in1);
                    finish();
                }
                else if (!response.body().getResponce())
                {
                    Toast.makeText(Reset_password_activity.this, "No such Email exists", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Reset_password_activity.this, "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Forgot_Responce> call, Throwable t) {
                Log.e("R_paswod_actviy FAILURE",""+t.getMessage());
                Log.e("R_paswod_actviy FAILURE",""+t.getLocalizedMessage());
                Log.e("R_paswod_actviy FAILURE",""+t.getStackTrace());
                Log.e("R_paswod_actviy FAILURE",""+t.getSuppressed());
                Log.e("R_paswod_actviy FAILURE",""+t.getCause());
            }
        });
    }

    private Boolean validate()
    {
        password = ed1.getText().toString().trim();
        pass2 = ed2.getText().toString().trim();

        if (password.matches("") && pass2.matches(""))
        {
            Toast.makeText(Reset_password_activity.this, "Please fill password correctly...", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password.matches(""))
        {
            Toast.makeText(Reset_password_activity.this, "Please fill new password...", Toast.LENGTH_SHORT).show(); return false;
        }
        else if (pass2.matches(""))
        {
            Toast.makeText(Reset_password_activity.this, "Confirm password not match...", Toast.LENGTH_SHORT).show(); return false;
        }
        else if (!pass2.equals(password))
        {
            Toast.makeText(Reset_password_activity.this, "Confirm password not match...", Toast.LENGTH_SHORT).show(); return false;
        }
        else
        {
            Log.e("RESET PASS"," "+password+" --- "+ userid);
            return true;
        }

    }

}
