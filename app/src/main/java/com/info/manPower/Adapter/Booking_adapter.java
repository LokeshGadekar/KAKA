package com.info.manPower.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.Fragment.Order_details_fragment;
import com.info.manPower.Fragment.SubCategory_fragment;
import com.info.manPower.Model.Booking_data;
import com.info.manPower.Model.Cancel_order_responce;
import com.info.manPower.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Booking_adapter extends RecyclerView.Adapter<Booking_adapter.ViewHolder>
{
    List<Booking_data> dataList;
    Activity mactivity;
    private static int numpay=0;
    API_parameter ApiService;

    private int cpos;

    public Booking_adapter(Activity mactivity, List<Booking_data> dataList)
    {
        this.mactivity=mactivity;
        this.dataList=dataList;
        ApiService = BaseUrl.getAPIService();
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
            viewHolder.pay.setText("Rs. "+ob.getTotalAmt());
            viewHolder.dfrom.setText(ob.getDate());
            int adv =(Integer.parseInt(ob.getTotalAmt())*Integer.parseInt(ob.getAdvance()))/100;
            viewHolder.advance.setText("Rs. "+adv);
            viewHolder.amtpending.setText("Rs. "+(Integer.parseInt(ob.getTotalAmt())-adv));

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

            if (Integer.parseInt(ob.getOrder_Cancel())==1)
            {
                viewHolder.status.setText("Cancel Request Sent");
                viewHolder.status.setBackground(mactivity.getResources().getDrawable(R.drawable.chip_pending));
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


            viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mactivity)
                            .setTitle("Cancel this Order")
                            .setMessage("Are you sure to cancel ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Confirm_Cancel(ob.getOrderId());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
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
        TextView txnm, orderid , amtpending , dfrom , advance, pay, status, cancel;
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
            cancel = (TextView) itemview.findViewById(R.id.tx_cancel);
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

    private void Confirm_Cancel(final String ordrid)
    {
       final AlertDialog dialogBuilder = new AlertDialog.Builder(mactivity).create();
        LayoutInflater inflater = mactivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_box, null);

        final EditText eddesc = (EditText) dialogView.findViewById(R.id.ed_descrption);
        Button buttn_cancl = (Button) dialogView.findViewById(R.id.cancl);
        Button buttn_submit = (Button) dialogView.findViewById(R.id.submit);

        buttn_cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        buttn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                String desc = eddesc.getText().toString().trim();
                if (desc.matches(""))
                {
                    Toast.makeText(mactivity, "Please type some description to cancel...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CALL_CANCEL(desc,ordrid);
                    dialogBuilder.dismiss();
                }            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }


    private void CALL_CANCEL(String description, String ordrid)
    {
        String user_id = AppPrefrences.getUserid(mactivity);
        ApiService.CANCEL_ORDER(ordrid,user_id,description).enqueue(new Callback<Cancel_order_responce>() {
            @Override
            public void onResponse(Call<Cancel_order_responce> call, Response<Cancel_order_responce> response) {
                Log.e("CALL_CANCEL RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("CALL_CANCEL RESPONSE.", "-------------------------------------------------");
                if (response.body().getResponce()) {
                    Toast.makeText(mactivity, "Order Cancel Request Sent...", Toast.LENGTH_SHORT).show();
                } else if (!response.body().getResponce()) {
                    Toast.makeText(mactivity, "No Data( false )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mactivity, "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cancel_order_responce> call, Throwable t) {
                Log.e("error at call", "" + t.getLocalizedMessage());
                Log.e("error at call", "" + t.getMessage());
                Log.e("error at call", "" + t.getCause());
                Log.e("error at call", "" + t.getStackTrace());
            }
        });

    }
}
