package com.info.manPower.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.Internet_Connectivity;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.Model.UpdateProfile_Responce;
import com.info.manPower.R;
import com.info.manPower.View.MainActivity_drawer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_Profile_fragment extends Fragment
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar, cart;

    private Button Update;
    private EditText edName,edLname,edAddress;
    private String name,lname,address;
    private API_parameter ApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_profile,container,false);

        Update = (Button) view.findViewById(R.id.button_updt);
        edName = (EditText) view.findViewById(R.id.ed_name);
        edLname = (EditText) view.findViewById(R.id.ed_lname);
        edAddress = (EditText) view.findViewById(R.id.ed_addrrss);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        cart = (ImageView) view.findViewById(R.id.icon_cart);
        cart_count = (TextView) view.findViewById(R.id.cart_count);

        cart_count.setVisibility(View.GONE);
        cart.setVisibility(View.GONE);


        txToolbar.setText("Update Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilview.hidekeyboard(getActivity());
                ((MainActivity_drawer)getActivity()).onBackPressed();
            }
        });

        ApiService = BaseUrl.getAPIService();

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        return view;
    }

    private void validate()
    {
        name = edName.getText().toString().trim();
        lname = edLname.getText().toString().trim();
        address = edAddress.getText().toString().trim();

        if (name.isEmpty() && lname.isEmpty() && address.isEmpty())
        {
            edName.setError("Required Field");
            edLname.setError("Required Field");
            edAddress.setError("Required Field");
        }
        else if (name.isEmpty())
        {     edName.setError("Required Field");                                      }
        else if (lname.isEmpty())
        {       edLname.setError("Required Field");                                     }
        else if (address.isEmpty())
        {    edAddress.setError("Required Field");                                      }

        else
        {
            if (Internet_Connectivity.isConnected(getActivity()))
            {  Update_Profile(name,lname,address); }
            else
            {   SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Oops...");
                pDialog.setContentText("No Internet Connection !");
                pDialog.show();                                  }
        }
    }

    private void Update_Profile(String name, String lname, String address)
    {
        String userid = AppPrefrences.getUserid(getActivity());
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();
        ApiService.UPDATE_PROFILE_CALL(userid,name,lname,address).enqueue(new Callback<UpdateProfile_Responce>() {
            @Override
            public void onResponse(Call<UpdateProfile_Responce> call, Response<UpdateProfile_Responce> response) {
                Log.e("UPDATE PROFILE ", "RESPONSE" + new Gson().toJson(response.body()));
                Log.e("UPDATE PROFILE ", "RESPONSE     -------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce()) {
                    //Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Updated");
                    pDialog.setContentText("Profile Updated Successfully");
                    pDialog.show();
                    getActivity().onBackPressed();
                }
                else if (!response.body().getResponce())
                {
                    Toast.makeText(getActivity(), "No Data( false )", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfile_Responce> call, Throwable t) {
                dialog.dismiss();
                Log.e("UPDATE PROFILE error" , ""+t.getLocalizedMessage());
                Log.e("UPDATE PROFILE error" , ""+t.getMessage());
                Log.e("UPDATE PROFILE error" , ""+t.getCause());
            }
        });

    }
}
