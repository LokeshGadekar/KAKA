package com.info.manPower.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import com.info.manPower.AppUtils.DatabaseHandler;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.R;
import com.info.manPower.View.MainActivity_drawer;

public class C_Order_fragment extends Fragment
{
    private Button contHiring, checkout;

    Toolbar toolbar;
    TextView txToolbar,cnt;
    ImageView imgToolbar,cart;
    private Activity activity;
    private TextView subtotal,payable;
    private DatabaseHandler db;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        ((MainActivity_drawer)getActivity()).lockDrawer();

        contHiring = (Button) view.findViewById(R.id.conthiring);
        checkout = (Button) view.findViewById(R.id.check_out);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        subtotal = (TextView) view.findViewById(R.id.tx_subtotal);
        payable = (TextView) view.findViewById(R.id.tx_payable);
        cart = (ImageView) view.findViewById(R.id.icon_cart);
        cnt = (TextView) view.findViewById(R.id.cart_count);

        cart.setVisibility(View.GONE);
        cnt.setVisibility(View.GONE);

        txToolbar.setText("Final Order");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        db = new DatabaseHandler(getActivity());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilview.hidekeyboard(getActivity());
                ((MainActivity_drawer)getActivity()).onBackPressed();
            }
        });

        subtotal.setText("₹"+db.getTotalAmount());
        payable.setText("₹"+db.getTotalAmount());
        Click_Listeners();

        return  view;
    }

    private void Click_Listeners()
    {
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order_fragment conf = new Order_fragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,conf)
                        .addToBackStack(null).commit();
            }
        });

        contHiring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_fragment hm = new home_fragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,hm)
                        .addToBackStack(null).commit();
            }
        });

    }

}
