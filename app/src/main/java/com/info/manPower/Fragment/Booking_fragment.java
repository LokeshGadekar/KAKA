package com.info.manPower.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.Adapter.Booking_adapter;
import com.info.manPower.Adapter.SubCategory_adapter;
import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.DatabaseHandler;
import com.info.manPower.AppUtils.Internet_Connectivity;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.Model.Booking_Responce;
import com.info.manPower.Model.Subcategory_data;
import com.info.manPower.R;
import com.info.manPower.View.MainActivity_drawer;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Booking_fragment extends Fragment
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart, ic_map;

    private RecyclerView recyclerView;
    private DatabaseHandler dbcart;
    private API_parameter ApiService;
    LinearLayoutManager linearLayoutManager;
    Booking_adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booking,container,false);
        recyclerView = view.findViewById(R.id.brecyclerview);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        cart = view.findViewById(R.id.icon_cart);
        cart_count = view.findViewById(R.id.cart_count);
        dbcart = new DatabaseHandler(getActivity());
        ApiService = BaseUrl.getAPIService();
        txToolbar.setText("Orders");
        toolbar.setNavigationIcon(R.drawable.menu1);
        cart.setVisibility(View.VISIBLE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilview.hidekeyboard(getActivity());
                ((MainActivity_drawer) getActivity()).openDrawer();
            }
        });

        cart_count.setText(""+dbcart.getCartCount());

        if (Internet_Connectivity.isConnected(getActivity()))
        {   GetOrders();      }
        else
        {   SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Oops...");
            pDialog.setContentText("No Internet Connection !");
            pDialog.show();       }


        return view;
    }


    private void GetOrders()
    {
        String userid = AppPrefrences.getUserid(getActivity());

        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.BOOKING_CALL(userid).enqueue(new Callback<Booking_Responce>() {
            @Override
            public void onResponse(Call<Booking_Responce> call, Response<Booking_Responce> response) {
                Log.e("BOOKING RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("BOOKING RESPONSE.", "-------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce()) {
                    adapter = new Booking_adapter(getActivity(),response.body().getData());

                    linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                } else if (!response.body().getResponce()) {
                    Toast.makeText(getActivity(), "No Data( false )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Booking_Responce> call, Throwable t) {
                dialog.dismiss();
                Log.e("error at call", "" + t.getLocalizedMessage());
                Log.e("error at call", "" + t.getMessage());
                Log.e("error at call", "" + t.getCause());
            }
        });
    }

}
