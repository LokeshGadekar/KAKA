package com.info.manPower.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.Fragment.Order_details_fragment;
import com.info.manPower.Fragment.SubCategory_fragment;
import com.info.manPower.Model.Booking_data;
import com.info.manPower.Model.Cancel_order_responce;
import com.info.manPower.Model.Pay_responce;
import com.info.manPower.Model.single_responce;
import com.info.manPower.R;
import com.instamojo.android.models.Order;

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
            viewHolder.dfrom.setText(ob.getDate().substring(0,10));
            int adv =(Integer.parseInt(ob.getTotalAmt())*Integer.parseInt(ob.getAdvance()))/100;
            viewHolder.advance.setText("Rs. "+adv);
            viewHolder.amtpending.setText("Rs. "+(Integer.parseInt(ob.getTotalAmt())-adv));


            ///////////////////
            if (Integer.parseInt(ob.getStatus()) == 0)
            {
                viewHolder.status.setText("Pending");
                viewHolder.ButtnWprogress.setVisibility(View.GONE);
                viewHolder.status.setBackground(mactivity.getResources().getDrawable(R.drawable.chip_pending));
            }
            else if (Integer.parseInt(ob.getStatus()) == 1)
            {
                viewHolder.status.setText("Confirm");
                viewHolder.ButtnWprogress.setVisibility(View.GONE);
                viewHolder.status.setBackground(mactivity.getResources().getDrawable(R.drawable.chip_fill));
            }
            else if (Integer.parseInt(ob.getStatus()) == 2)
            {
                viewHolder.ButtnWprogress.setVisibility(View.GONE);
                viewHolder.ButtnComplete.setVisibility(View.GONE);
                viewHolder.ButtnpayCash.setVisibility(View.VISIBLE);
                viewHolder.ButtnpayOnline.setVisibility(View.VISIBLE);
            }
            else if (Integer.parseInt(ob.getStatus()) == 3)
            {
                viewHolder.status.setText("Cancelled");
                viewHolder.ButtnWprogress.setVisibility(View.GONE);
                viewHolder.ButtnCancel.setVisibility(View.GONE);
                viewHolder.status.setBackground(mactivity.getResources().getDrawable(R.drawable.chip_pending));
            }
            else if (Integer.parseInt(ob.getStatus()) == 4)
            {
                viewHolder.ButtnWprogress.setVisibility(View.VISIBLE);
                viewHolder.ButtnComplete.setVisibility(View.VISIBLE);
            }

            if (Integer.parseInt(ob.getCancel_time())==0)
            {   viewHolder.ButtnCancel.setVisibility(View.VISIBLE);   }
            else if (Integer.parseInt(ob.getCancel_time())==1)
            {   viewHolder.ButtnCancel.setVisibility(View.GONE);   }
          ////////////


            if (Integer.parseInt(ob.getPayment_complete()) == 0)
            {
                //viewHolder.Buttnpay.setVisibility(View.VISIBLE);
                viewHolder.ButtnPaid.setVisibility(View.GONE);
            }

            else if (Integer.parseInt(ob.getPayment_complete()) == 1)
            {
                viewHolder.ButtnpayOnline.setVisibility(View.GONE);
                viewHolder.ButtnpayCash.setVisibility(View.GONE);
                viewHolder.ButtnWprogress.setVisibility(View.GONE);
                viewHolder.ButtnComplete.setVisibility(View.GONE);
                viewHolder.ButtnPaid.setVisibility(View.VISIBLE);
            }

            if (viewHolder.ButtnpayOnline.getVisibility() == View.VISIBLE)
            {
                viewHolder.ButtnpayOnline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callInstamojoPay(AppPrefrences.getMail(mactivity), AppPrefrences.getMobile(mactivity),""+10 ,"online payment", AppPrefrences.getName(mactivity),viewHolder,i);
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
            viewHolder.ButtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mactivity)
                            .setTitle("Cancel this Order ?")
                            .setMessage("Are you sure to cancel ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Confirm_Cancel(ob.getOrderId(),i);
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
            viewHolder.ButtnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mactivity)
                            .setTitle("Complete this Order")
                            .setMessage("Are you sure for work Completion ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Complete_Order(ob.getOrderId(),i);
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
            viewHolder.ButtnpayCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mactivity)
                            .setTitle("Pay for this Order?")
                            .setMessage("Are you sure to pay in Cash ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    //Complete_Order(ob.getOrderId(),i);
                                    Confirm_PayCash(ob.getOrderId(),i);
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
        TextView txnm, orderid , amtpending , dfrom , advance, pay, status;
        Button ButtnpayCash, ButtnpayOnline, ButtnPaid, ButtnWprogress, ButtnCancel, ButtnComplete;

        public ViewHolder(View itemview) {
            super(itemview);
            txnm = (TextView)itemview.findViewById(R.id.tx_snm);
            status = (TextView)itemview.findViewById(R.id.tx_status);
            orderid = (TextView) itemview.findViewById(R.id.tx_orderid);
            dfrom = (TextView) itemview.findViewById(R.id.tx_dfr);
            amtpending = (TextView)itemview.findViewById(R.id.tx_Pending);
            advance = (TextView) itemview.findViewById(R.id.tx_adv);
            pay = (TextView) itemview.findViewById(R.id.tx_pmode);
            ButtnCancel = (Button) itemview.findViewById(R.id.tx_cancel);
            ButtnWprogress = (Button) itemview.findViewById(R.id.button_workprogress);
            ButtnpayCash = (Button) itemview.findViewById(R.id.button_paycash);
            ButtnpayOnline = (Button) itemview.findViewById(R.id.button_payonline);
            ButtnComplete = (Button) itemview.findViewById(R.id.button_complete);
            ButtnPaid= (Button) itemview.findViewById(R.id.button_paid);
        }
    }


    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername, ViewHolder viewh, int i) {
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
        initListener(viewh,i);
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;

    private void initListener(final ViewHolder itemview, final int a) {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                //   pay_status = "Success";
                notifyDataSetChanged();
                itemview.status.setVisibility(View.GONE);
                dataList.get(a).setPayment_complete("1");
                notifyItemChanged(a);
                itemview.ButtnPaid.setVisibility(View.VISIBLE);

                Call_PAY_API( a );
                Toast.makeText(mactivity, "Payment Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("Error "," Pay "+s+"________________"+i);
            }
            /// eof INSTA_MOJO PAYMENT /////////////
        };
    }

    // ----------------

    private void Call_PAY_API(int adv)
    {
        String order_id = dataList.get(adv).getOrderId();
        int advance;
        int payment = 1;
        if ( Integer.parseInt(dataList.get(adv).getAdvance())>0)
        {
             advance = 1;
        }
        else
        {
            advance = 0;
        }
        ApiService.PAY_TRACK(AppPrefrences.getUserid(mactivity), order_id,advance,payment,"online").enqueue(new Callback<Pay_responce>() {
        @Override
        public void onResponse(Call<Pay_responce> call, Response<Pay_responce> response) {
            Log.e("PAY_TRACK RESPONSE.", "" + new Gson().toJson(response.body()));
            Log.e("PAY_TRACK RESPONSE.", "-------------------------------------------------");
            if (response.body().getResponce()) {
                Toast.makeText(mactivity, "Pay History Submitted...", Toast.LENGTH_SHORT).show();
            }
            else if (!response.body().getResponce())
            {
                Toast.makeText(mactivity, "No Data( false )", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(mactivity, "Error while Connecting...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Pay_responce> call, Throwable t) {
            Log.e("error at paycall", "" + t.getLocalizedMessage());
            Log.e("error at paycall", "" + t.getMessage());
            Log.e("error at paycall", "" + t.getCause());
        }
    });
    }


    // ----------------

    private void Complete_Order(String Order_id,int a)
    {
        final int listpos = a;
        ApiService.WORK_COMPLETE(Order_id,AppPrefrences.getUserid(mactivity)).enqueue(new Callback<single_responce>() {
            @Override
            public void onResponse(Call<single_responce> call, Response<single_responce> response) {
                Log.e("WORK COMPLETE RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("WORK COMPLETE RESPONSE.", "-------------------------------------------------");
                if (response.body().getResponce()) {
                    Toast.makeText(mactivity, "Order Complete Request Sent...", Toast.LENGTH_SHORT).show();
                    dataList.get(listpos).setStatus("2");
                    notifyItemChanged(listpos);
                } else if (!response.body().getResponce()) {
                    Toast.makeText(mactivity, "No Data( false )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mactivity, "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<single_responce> call, Throwable t) {
                Log.e("error at call", "" + t.getLocalizedMessage());
                Log.e("error at call", "" + t.getMessage());
                Log.e("error at call", "" + t.getCause());
                Log.e("error at call", "" + t.getStackTrace());
            }
        });
    }


    private void Confirm_Cancel(final String ordrid,final int pos)
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
                    CALL_CANCEL(desc,ordrid,pos);
                    dialogBuilder.dismiss();
                }            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void CALL_CANCEL(String description, String ordrid,final int c)
    {
        String user_id = AppPrefrences.getUserid(mactivity);
        ApiService.CANCEL_ORDER(ordrid,user_id,description).enqueue(new Callback<Cancel_order_responce>() {
            @Override
            public void onResponse(Call<Cancel_order_responce> call, Response<Cancel_order_responce> response) {
                Log.e("CALL_CANCEL RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("CALL_CANCEL RESPONSE.", "-------------------------------------------------");
                if (response.body().getResponce()) {
                    Toast.makeText(mactivity, "Order Cancel Request Sent...", Toast.LENGTH_SHORT).show();
                    dataList.get(c).setCancel_time("1");
                    dataList.get(c).setOrder_Cancel("1");
                    notifyItemChanged(c);
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


    private void Confirm_PayCash(final String ordr_id,final int u)
    {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(mactivity).create();
        LayoutInflater inflater = mactivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_cash, null);

        final EditText ednname = (EditText) dialogView.findViewById(R.id.ed_nname);
        final EditText edmmobile = (EditText) dialogView.findViewById(R.id.ed_mmobile);
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
                String nname = ednname.getText().toString().trim();
                String mmobile = edmmobile.getText().toString().trim();
                if (nname.matches(""))
                {
                    Toast.makeText(mactivity, "Please fill name correctly...", Toast.LENGTH_SHORT).show();
                }
                else if (!validCellPhone(mmobile))
                {
                    Toast.makeText(mactivity, "Please fill valid mobile number...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   // Toast.makeText(mactivity, "___ CASH ON DELIVERY ___", Toast.LENGTH_SHORT).show();
                    CALL_PAYCash(ordr_id,nname,mmobile,u);
                    dialogBuilder.dismiss();
                }            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void CALL_PAYCash(String order_id, String name, String mobile, final int y)
    {
        ApiService.PAY_IN_CASH_CALL(AppPrefrences.getUserid(mactivity), order_id, name, mobile).enqueue(new Callback<single_responce>() {
            @Override
            public void onResponse(Call<single_responce> call, Response<single_responce> response) {
                Log.e("PAY_IN_CASH_CALL RES", "" + new Gson().toJson(response.body()));
                Log.e("PAY_IN_CASH_CALL RES", "-------------------------------------------------");
                if (response.body().getResponce()) {
                    Toast.makeText(mactivity, "Order PAY in CASH Request Sent...", Toast.LENGTH_SHORT).show();
                    dataList.get(y).setPayment_complete("1");
                    notifyItemChanged(y);
                } else if (!response.body().getResponce()) {
                    Toast.makeText(mactivity, "No Data( false )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mactivity, "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<single_responce> call, Throwable t) {
                Log.e("error at call", "" + t.getLocalizedMessage());
                Log.e("error at call", "" + t.getMessage());
                Log.e("error at call", "" + t.getCause());
                Log.e("error at call", "" + t.getStackTrace());
            }
        });
    }

    public boolean validCellPhone(String number)
    {
        return  !TextUtils.isEmpty(number) && (number.length()==10) && android.util.Patterns.PHONE.matcher(number).matches();
    }

}
