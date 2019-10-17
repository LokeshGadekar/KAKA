package com.kaka.ics.Fragment;

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
import com.kaka.ics.API_retro.API_parameter;
import com.kaka.ics.Adapter.Order_details_adapter;
import com.kaka.ics.AppUtils.AppPrefrences;
import com.kaka.ics.AppUtils.BaseUrl;
import com.kaka.ics.AppUtils.Utilview;
import com.kaka.ics.Model.Suborder_Responce;
import com.kaka.ics.R;
import com.kaka.ics.View.MainActivity_drawer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_details_fragment extends Fragment
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart, ic_map;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    API_parameter ApiService;
    String orderid;
    Order_details_adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_sub, container,false);

        recyclerView = view.findViewById(R.id.orecyclerview);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        cart = view.findViewById(R.id.icon_cart);
        cart_count = view.findViewById(R.id.cart_count);
        cart.setVisibility(View.GONE);
        cart_count.setVisibility(View.GONE);

        txToolbar.setText("Order Details");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilview.hidekeyboard(getActivity());
                ((MainActivity_drawer) getActivity()).onBackPressed();
            }
        });

        ApiService = BaseUrl.getAPIService();
        orderid = getArguments().getString("orid");
        GetOrder();

        return view;
    }


    private void GetOrder()
    {
        String userid = AppPrefrences.getUserid(getActivity());

        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.SUBORDER_CALL(userid,orderid).enqueue(new Callback<Suborder_Responce>() {
            @Override
            public void onResponse(Call<Suborder_Responce> call, Response<Suborder_Responce> response) {
                Log.e("SUBORDER_CALL RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("SUBORDER_CALL RESPONSE.", "-------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce()) {
                    adapter = new Order_details_adapter(getActivity(),response.body().getData());

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
            public void onFailure(Call<Suborder_Responce> call, Throwable t) {
                dialog.dismiss();
                Log.e("error at call", "" + t.getLocalizedMessage());
                Log.e("error at call", "" + t.getMessage());
                Log.e("error at call", "" + t.getCause());
            }
        });
    }

}
