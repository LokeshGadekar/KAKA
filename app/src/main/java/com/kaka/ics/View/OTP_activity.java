package com.kaka.ics.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class OTP_activity extends AppCompatActivity
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart;
    private EditText ed1, ed2 , ed3, ed4;
    private Button Confirm;
    private String stringotp, user_id;
    String o1,o2,o3,o4;
    API_parameter ApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txToolbar = (TextView) findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);
        cart = findViewById(R.id.icon_cart);
        cart_count = findViewById(R.id.cart_count);
        cart.setVisibility(View.GONE);
        cart_count.setVisibility(View.GONE);

        ed1 = (EditText)findViewById(R.id.ed1);
        ed2 = (EditText)findViewById(R.id.ed2);
        ed3 = (EditText)findViewById(R.id.ed3);
        ed4 = (EditText)findViewById(R.id.ed4);
        Confirm = (Button)findViewById(R.id.button_confirm);

        txToolbar.setText("Forgot Password");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        ApiService  = BaseUrl.getAPIService();

        user_id = getIntent().getStringExtra("userid");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (Validate())
                    {
                        Verify_OTP();
                    }
                    else
                    {
                    }
            }
        });


        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ed1.getText().toString().length()==1)
                {   ed2.requestFocus();         }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ed2.getText().toString().length()==1)
                {   ed3.requestFocus();         }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ed3.getText().toString().length()==1)
                {   ed4.requestFocus();         }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    private void Verify_OTP()
    {
        ApiService.OTP_VERIFY(stringotp,user_id).enqueue(new Callback<Forgot_Responce>() {
            @Override
            public void onResponse(Call<Forgot_Responce> call, Response<Forgot_Responce> response) {
                if (response.body().getResponce()) {
                    Log.e("OTP verify RESPONSE.", "" + new Gson().toJson(response.body()));
                    Intent in1 = new Intent(OTP_activity.this,Reset_password_activity.class);
                    in1.putExtra("userid",response.body().getUserId());
                    Toast.makeText(OTP_activity.this, "OTP Verified (Correct)", Toast.LENGTH_SHORT).show();
                    startActivity(in1);
                    finish();
                }
                else if (!response.body().getResponce())
                {
                    Toast.makeText(OTP_activity.this, "Incorrect OTP...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(OTP_activity.this, "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Forgot_Responce> call, Throwable t) {
                Log.e("OTP_activity FAILURE  ",""+t.getMessage());
                Log.e("OTP_activity FAILURE  ",""+t.getLocalizedMessage());
                Log.e("OTP_activity FAILURE  ",""+t.getStackTrace());
                Log.e("OTP_activity FAILURE  ",""+t.getSuppressed());
                Log.e("OTP_activity FAILURE  ",""+t.getCause());
            }
        });
    }

    private Boolean Validate()
    {
        o1 = ed1.getText().toString().trim();
        o2 = ed2.getText().toString().trim();
        o3 = ed3.getText().toString().trim();
        o4 = ed4.getText().toString().trim();

        if (o1.matches(""))
        {
            Toast.makeText(OTP_activity.this, "Please enter OTP correctly", Toast.LENGTH_SHORT).show();  return false;
        }

        else if (o2.matches(""))
        {
            Toast.makeText(OTP_activity.this, "Please enter OTP correctly", Toast.LENGTH_SHORT).show();  return false;
        }
        else if (o3.matches(""))
        {
            Toast.makeText(OTP_activity.this, "Please enter OTP correctly", Toast.LENGTH_SHORT).show();  return false;
        }
        else if (o4.matches(""))
        {
            Toast.makeText(OTP_activity.this, "Please enter OTP correctly", Toast.LENGTH_SHORT).show();   return false;
        }
        else
        {
            stringotp = o1+o2+o3+o4;
            Log.e("OTP IS ...","______________"+stringotp);
            return true;
        }
    }
}
