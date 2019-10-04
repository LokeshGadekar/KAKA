package com.info.manPower.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.Adapter.Cart_adapter;
import com.info.manPower.AppUtils.DatabaseHandler;
import com.info.manPower.AppUtils.Session_management;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.R;
import com.info.manPower.View.Login_activity;
import com.info.manPower.View.MainActivity_drawer;
import com.instamojo.android.models.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart_fragment extends Fragment {
    private Button contHiring, next;
    Toolbar toolbar;
    TextView txToolbar, tx_Total, tx_items, cart_count;
    ImageView imgToolbar;
    Activity activity;

    private Cart_adapter adapter;
    private RecyclerView recyclerView;

    private API_parameter ApiService;
    private DatabaseHandler db;
    private Session_management session_management;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ((MainActivity_drawer) getActivity()).lockDrawer();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        cart_count = (TextView) view.findViewById(R.id.cart_count);

        contHiring = (Button) view.findViewById(R.id.conthiring);
        next = (Button) view.findViewById(R.id.button_next);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tx_Total = (TextView) view.findViewById(R.id.total_amt);
        tx_items = (TextView) view.findViewById(R.id.tx_items);

        txToolbar.setText("Cart");
        session_management = new Session_management(getActivity());

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        db = new DatabaseHandler(getActivity());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilview.hidekeyboard(getActivity());
                home();
            }
        });


        Click_Listeners();
        updateData();
        ArrayList<HashMap<String, String>> map = db.getCartAll();
        adapter = new Cart_adapter(activity, map);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        /*if (Internet_Connectivity.isConnected(getActivity()))
        {     get_Cart(); }
        else
        {   SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Oops...");
            pDialog.setContentText("No Internet Connection !");
            pDialog.show();}*/

        return view;
    }

    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mCart,new IntentFilter("MCART"));
    }

    private void updateData() {

        String tt = db.getTotalAmount();
        cart_count.setText(""+db.getCartCount());
        tx_Total.setText(""+tt);
        tx_items.setText(""+db.getCartCount());
        Log.e("TTTOOTTAALL >>> ","  >>>>>>>>>>> "+tt);

    }

    private void home() {
        home_fragment hm = new home_fragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, hm)
                .addToBackStack(null).commit();
    }

    private void Click_Listeners() {
        contHiring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session_management.isLoggedIn())
                {
                Order_fragment orfrag = new Order_fragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, orfrag)
                        .addToBackStack(null).commit(); }
                else
                {
                    Intent in1 = new Intent(getActivity(), Login_activity.class);
                    startActivity(in1);
                }
            }
        });
    }
}


// ------------------------------
   /* private void get_Cart()
    {
        String userid = AppPrefrences.getUserid(getActivity());
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.CART_CALL(userid).enqueue(new Callback<Cart_Responce>() {
            @Override
            public void onResponse(Call<Cart_Responce> call, Response<Cart_Responce> response) {
                dialog.dismiss();
                Log.e("CART RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("CART RESPONSE.", "-------------------------------------------------");
                if (response.body().getResponce())
                {
                    adapter = new Cart_adapter(activity, response.body().getData());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
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
            public void onFailure(Call<Cart_Responce> call, Throwable t) {
                dialog.dismiss();
                Log.e("CART CALL ","ERROR > "+t.getMessage());
                Log.e("CART CALL ","ERROR > "+t.getCause());
                Log.e("CART CALL ","ERROR > "+t.getLocalizedMessage());
                Log.e("CART CALL ","ERROR > "+t.getSuppressed());
            }
        });
    }*/
