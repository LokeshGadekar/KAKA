package com.info.manPower.View;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class Registration_activity extends AppCompatActivity
{
    private EditText edName, edLastnm, edMail, edMobile, edAddress, edpass, edconfirmpass;
    private RadioGroup edGender;
    private Button Register;

    Toolbar toolbar;
    TextView txToolbar,cnt;
    ImageView imgToolbar,cart;
    private FragmentManager fragmentmanager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;

    private String name,lname,mail,mobile,gender,address,password,cpassword;

    private API_parameter ApiService;
    Session_management sessionManagement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edName = (EditText) findViewById(R.id.ed_name);
        edLastnm = (EditText) findViewById(R.id.ed_lastname);
        edMail = (EditText) findViewById(R.id.ed_email);
        edMobile = (EditText) findViewById(R.id.ed_phonenum);
        edAddress = (EditText) findViewById(R.id.ed_address);
        edpass = (EditText) findViewById(R.id.ed_password);
        edconfirmpass = (EditText) findViewById(R.id.ed_confirm_password);
        edGender = (RadioGroup) findViewById(R.id.radioGrp);
        Register = (Button) findViewById(R.id.register);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txToolbar = (TextView) findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);
        cart = (ImageView) findViewById(R.id.cart);
        cnt = (TextView) findViewById(R.id.cart_count);
        cart.setVisibility(View.GONE);
        cnt.setVisibility(View.GONE);

        txToolbar.setText("Register");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        ApiService = BaseUrl.getAPIService();
        sessionManagement = new Session_management(Registration_activity.this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Internet_Connectivity.isConnected(Registration_activity.this))
        { }
        else
        {
            SweetAlertDialog pDialog = new SweetAlertDialog(Registration_activity.this, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Oops...");
            pDialog.setContentText("No Internet Connection !");
            pDialog.show();
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Click_Listeners();
    }


    private void Click_Listeners()
    {
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate())
                {
                    if (Internet_Connectivity.isConnected(Registration_activity.this))
                    {      register(); }
                    else
                    {
                        SweetAlertDialog pDialog = new SweetAlertDialog(Registration_activity.this, SweetAlertDialog.ERROR_TYPE);
                        pDialog.setTitleText("Oops...");
                        pDialog.setContentText("No Internet Connection !");
                        pDialog.show();
                    }
                    //Toast.makeText(Registration_activity.this, "All Values Correct", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(Registration_activity.this, "Please Fill Data Correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Boolean validate()
    {
        name = edName.getText().toString().trim();
        lname = edLastnm.getText().toString().trim();
        mobile = edMobile.getText().toString().trim();
        address = edAddress.getText().toString().trim();
        int sel =  edGender.getCheckedRadioButtonId();
        RadioButton gen = findViewById(sel);
        if (gen!=null)
        {gender = gen.getText().toString().trim();}
        mail = edMail.getText().toString().trim();
        password = edpass.getText().toString().trim();
        cpassword = edconfirmpass.getText().toString().trim();

        if (name.matches("") && lname.matches("") && mobile.matches("") && address.matches("") && gender==null && mail.matches("") && password.matches("") && cpassword.matches(""))
        {
            Toast.makeText(Registration_activity.this, "Please fill data correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (name.matches(""))
        {
            Toast.makeText(Registration_activity.this, "Please enter Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (lname.matches(""))
        {
            Toast.makeText(Registration_activity.this, "Please enter Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isEmail(mail))
        {
            Toast.makeText(Registration_activity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mobile.matches(""))
        {
            Toast.makeText(Registration_activity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!validCellPhone(mobile))
        {      Toast.makeText(Registration_activity.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
            return false;                     }

        else if (address.matches(""))
        {
            Toast.makeText(Registration_activity.this, "Please enter address ", Toast.LENGTH_SHORT).show();
            return  false;       }


        else if (gender==null)
        {
            Toast.makeText(Registration_activity.this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password.matches(""))
        {
            Toast.makeText(Registration_activity.this, "Please enter password ", Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if (cpassword.matches(""))
        {
            Toast.makeText(Registration_activity.this, "Please enter confirm password ", Toast.LENGTH_SHORT).show();
            return  false;
        }

        else if (!password.equals(cpassword))
        {
            Toast.makeText(Registration_activity.this, "Please insert correct confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }

        else
        {
            return  true;
        }
    }


    private void register() {

        ApiService.REGISTRATION_CALL(name, lname, mail, mobile, password, address, gender).enqueue(new Callback<Registration_Responce>() {
            @Override
            public void onResponse(Call<Registration_Responce> call, Response<Registration_Responce> response) {
                Log.e("REGISTRATION RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("REGISTRATION RESPONSE.", "-------------------------------------------------");

                if (response.body().getResponce())
                {
                    AppPrefrences.setName(Registration_activity.this, response.body().getData().getfName()+" "+response.body().getData().getlName());
                    AppPrefrences.setMobile(Registration_activity.this, response.body().getData().getMobileNo());
                    AppPrefrences.setUserid(Registration_activity.this, response.body().getData().getUserId());
                    AppPrefrences.setMail(Registration_activity.this, response.body().getData().getUserEmail());
                    AppPrefrences.setAddress(Registration_activity.this, response.body().getData().getAddress());
                    sessionManagement.createLoginSession(response.body().getData().getUserId(),
                            response.body().getData().getfName(),response.body().getData().getUserEmail(),
                            response.body().getData().getPassword());
                    Toast.makeText(Registration_activity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    clear_edText();
                    finish();
                }
                else
                {
                    Toast.makeText(Registration_activity.this, "Mobile or Email already registered...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Registration_Responce> call, Throwable t) {
                Log.e("API REGISTER FAILURE.","error  "+t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilview.hidekeyboard(Registration_activity.this);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));    }


        private void clear_edText()
        {
            edName.getText().clear();
            edLastnm.getText().clear();
            edGender.clearCheck();
            edconfirmpass.getText().clear();
            edpass.getText().clear();
            edAddress.getText().clear();
            edMail.getText().clear();
            edMobile.getText().clear();
            edconfirmpass.getText().clear();
        }

    public static boolean isEmail(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public boolean validCellPhone(String number)
    {
        return  !TextUtils.isEmpty(number) && (number.length()==10) && android.util.Patterns.PHONE.matcher(number).matches();
    }

}
