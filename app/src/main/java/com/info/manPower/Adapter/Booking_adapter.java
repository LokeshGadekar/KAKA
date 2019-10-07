package com.info.manPower.Adapter;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.Fragment.Order_details_fragment;
import com.info.manPower.Fragment.SubCategory_fragment;
import com.info.manPower.Model.Booking_data;
import com.info.manPower.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class Booking_adapter extends RecyclerView.Adapter<Booking_adapter.ViewHolder>
{
    List<Booking_data> dataList;
    Activity mactivity;
    private static int numpay=0;

    private int cpos;

    public Booking_adapter(Activity mactivity, List<Booking_data> dataList)
    {
        this.mactivity=mactivity;
        this.dataList=dataList;
    }


    @Override
    public Booking_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_booking,viewGroup,false);
        return new Booking_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Booking_adapter.ViewHolder viewHolder, final int i) {
        if (viewHolder!=null) {

            final Booking_data ob = dataList.get(i);

            viewHolder.txnm.setText(ob.getCatName());
            viewHolder.orderid.setText(ob.getOrderId());
            viewHolder.pay.setText(ob.getTotalAmt());
            viewHolder.dfrom.setText(ob.getDate());
            int adv =(Integer.parseInt(ob.getTotalAmt())*Integer.parseInt(ob.getAdvance()))/100;
            viewHolder.advance.setText(""+adv);
            viewHolder.amtpending.setText(""+(Integer.parseInt(ob.getTotalAmt())-adv));

            if (Integer.parseInt(ob.getStatus()) == 0)
            {
                viewHolder.status.setText("Pending");
                viewHolder.status.setBackground(mactivity.getResources().getDrawable(R.drawable.chip_pending));
                viewHolder.Buttnpay.setVisibility(View.GONE);
            }

            else if (Integer.parseInt(ob.getStatus()) == 1)
            {
                viewHolder.status.setText("Confirm");
                viewHolder.status.setBackground(mactivity.getResources().getDrawable(R.drawable.chip_fill));
                viewHolder.Buttnpay.setVisibility(View.GONE);
            }

            else if (Integer.parseInt(ob.getStatus()) == 2)
            {
                viewHolder.status.setText("Complete");
                viewHolder.status.setBackground(mactivity.getResources().getDrawable(R.drawable.chip_complete));
                viewHolder.Buttnpay.setVisibility(View.VISIBLE);
            }
            else if (Integer.parseInt(ob.getStatus()) == 3)
            {
                viewHolder.status.setText("Cancel");
                viewHolder.status.setBackground(mactivity.getResources().getDrawable(R.drawable.chip_cancel));
                viewHolder.Buttnpay.setVisibility(View.VISIBLE);
            }
            if (viewHolder.Buttnpay.getVisibility() == View.VISIBLE)
            {
                viewHolder.Buttnpay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callInstamojoPay(AppPrefrences.getMail(mactivity), AppPrefrences.getMobile(mactivity),""+10 ,"online payment", AppPrefrences.getName(mactivity),viewHolder);
                        numpay = i;
                    }
                });

            }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Fragment fragment;
                        Bundle args = new Bundle();
                        args.putString("orid",""+ob.getOrderId());
                        fragment = new Order_details_fragment();
                        fragment.setArguments(args);
                        FragmentManager fragmentmanager = activity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_layout,fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txnm, orderid , amtpending , dfrom , advance, pay, status;
        Button Buttnpay;

        public ViewHolder(View itemview) {
            super(itemview);
            txnm = (TextView)itemview.findViewById(R.id.tx_snm);
            status = (TextView)itemview.findViewById(R.id.tx_status);
            orderid = (TextView) itemview.findViewById(R.id.tx_orderid);
            dfrom = (TextView) itemview.findViewById(R.id.tx_dfr);
            amtpending = (TextView)itemview.findViewById(R.id.tx_Pending);
            advance = (TextView) itemview.findViewById(R.id.tx_adv);
            pay = (TextView) itemview.findViewById(R.id.tx_pmode);
            Buttnpay = (Button) itemview.findViewById(R.id.button_pay);
        }
    }


    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername, ViewHolder viewh) {
        final Activity activity = mactivity;
//      String  Payment_method = radio_online_pay.getText().toString();

        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        activity.registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener(viewh);
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;

    private void initListener(final ViewHolder itemview) {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                //   pay_status = "Success";
                notifyDataSetChanged();
                itemview.status.setVisibility(View.GONE);
                itemview.Buttnpay.setText("PAID");
                Toast.makeText(mactivity, "Payment Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("Error "," Pay "+s+"________________"+i);
            }


            /// eof INSTA_MOJO PAYMENT /////////////
        };
    }



}
