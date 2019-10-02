package com.info.manPower.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.Internet_Connectivity;
import com.info.manPower.AppUtils.Session_management;
import com.info.manPower.Model.Support_Responce;
import com.info.manPower.R;
import com.info.manPower.View.MainActivity_drawer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Support_fragment extends Fragment
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart;

    private WebView webView;

    private API_parameter ApiService;

    private String print;

    Session_management sessionManagement;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_description,container,false);

        webView = (WebView) view.findViewById(R.id.webview);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView)view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        cart = view.findViewById(R.id.icon_cart);
        cart_count = view.findViewById(R.id.cart_count);

        print = getArguments().getString("Desc");

        if (print.equals("Support")){    txToolbar.setText("Support"); }
        else if (print.equals("About"))
        {  txToolbar.setText("About Us");         }
        else
        {        txToolbar.setText("Terms & Conditions");     }
        cart.setVisibility(View.GONE);
        cart_count.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        sessionManagement = new Session_management(getActivity());
        ApiService = BaseUrl.getAPIService();

        if (Internet_Connectivity.isConnected(getActivity()))
        {
           if(print.equals("Support"))
            { APIcall_support(); }
           else if (print.equals("About"))
           {
                    APIcall_About();
//               Toast.makeText(getActivity(), "About US", Toast.LENGTH_SHORT).show();
           }
           else if (print.equals("Terms"))
           {
                    APIcall_Terms();
//               Toast.makeText(getActivity(), "Terms", Toast.LENGTH_SHORT).show();
           }
        }
        else
        {   SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Oops...");
            pDialog.setContentText("No Internet Connection !");
            pDialog.show();       }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity_drawer)getActivity()).onBackPressed();
            }
        });

        return view;
    }

    private void APIcall_support()
    {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.SUPPORT_CALL().enqueue(new Callback<Support_Responce>() {
            @Override
            public void onResponse(Call<Support_Responce> call, Response<Support_Responce> response) {
                Log.e("SUPPORT RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("SUPPORT RESPONSE.", "-------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce())
                {
                    String html = response.body().getData().getDescription();
                    webView.loadDataWithBaseURL("",html,mimeType,encoding,"");
                   // paragraph.setText(response.body().getData().getDescription());
                }
                else
                {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Support_Responce> call, Throwable t) {
                dialog.dismiss();
                Log.e("error at call" , ""+t.getLocalizedMessage());
                Log.e("error at call" , ""+t.getMessage());
                Log.e("error at call" , ""+t.getCause());

            }
        });
    }

    private void APIcall_About()
    {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.ABOUT_CALL().enqueue(new Callback<Support_Responce>() {
            @Override
            public void onResponse(Call<Support_Responce> call, Response<Support_Responce> response) {
                Log.e("ABOUT RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("ABOUT RESPONSE.", "-------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce())
                {
                    String html = response.body().getData().getDescription();
                    webView.loadDataWithBaseURL("",html,mimeType,encoding,"");
                    //paragraph.setText(response.body().getData().getDescription());
                }
                else
                {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Support_Responce> call, Throwable t) {
                                dialog.dismiss();
            }
        });
    }


    private void APIcall_Terms()
    {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.TERMS_CALL().enqueue(new Callback<Support_Responce>() {
            @Override
            public void onResponse(Call<Support_Responce> call, Response<Support_Responce> response) {
                Log.e("TERMS RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("TERMS RESPONSE.", "-------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce())
                {
                    String html = response.body().getData().getDescription();
                    webView.loadDataWithBaseURL("",html,mimeType,encoding,"");
                    //paragraph.setText(response.body().getData().getDescription());
                }
                else
                {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Support_Responce> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}
