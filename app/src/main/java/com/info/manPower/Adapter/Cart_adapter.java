package com.info.manPower.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.DatabaseHandler;
import com.info.manPower.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.ViewHolder>
{
    //List<Cart_data> dataList;
    ArrayList<HashMap<String, String>> dataList;
    List<CardView>cardViewList = new ArrayList<>();
    private DatabaseHandler db;

    Activity mactivity;

    private int cpos;

    public Cart_adapter(Activity mactivity, ArrayList<HashMap<String,String>> dataList)
    {
        this.mactivity=mactivity;
        this.dataList=dataList;
        db = new DatabaseHandler(mactivity);
    }


    @Override
    public Cart_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_cart,viewGroup,false);
        return new Cart_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Cart_adapter.ViewHolder viewHolder, final int i) {
        if (viewHolder!=null)
        {
            //final Cart_data cob = dataList.get(i);
            final HashMap<String, String> cob = dataList.get(i);

            //viewHolder.txWorkernm.setText(cob.getSubcatName());
            //viewHolder.txTime.setText(cob.getTimeFrom()+"  to  "+cob.getTimeTo());
            //viewHolder.txFrom.setText(""+cob.getDateFrom());
            //viewHolder.txhnum.setText(""+cob.getSubcatnum());
            //viewHolder.txRate.setText(""+cob.getRate());

            int rate = Integer.parseInt(cob.get("unit_value")) * Integer.parseInt(cob.get("num_helpers"));
            int num = Integer.parseInt(cob.get("num_helpers"));
            final int onrate = rate/num;

            viewHolder.txWorkernm.setText(cob.get("subcat_name"));
            viewHolder.txTime.setText(cob.get("time_from")+"  to  "+cob.get("time_to"));
            viewHolder.txFrom.setText(""+cob.get("date_from"));
            viewHolder.txTo.setText(""+cob.get("date_to"));
            viewHolder.txhnum.setText(""+cob.get("num_helpers"));
            viewHolder.txRate.setText(""+rate);

            Glide.with(mactivity).load(BaseUrl.baseimg+""+cob.get("image")).into(viewHolder.img);

            viewHolder.icAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = Integer.parseInt(viewHolder.txRate.getText().toString()) + onrate; // rate
                    int nm = Integer.parseInt(viewHolder.txhnum.getText().toString()) + 1 ;  // number of helpers
                    if (nm>=0){
                    viewHolder.txRate.setText(""+n);
                    viewHolder.txhnum.setText(""+nm);
                    db.setCart(cob, nm);
                    updateintent();
                    }
                }
            });

            viewHolder.icRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = Integer.parseInt(viewHolder.txRate.getText().toString()) - onrate;
                    int nm = Integer.parseInt(viewHolder.txhnum.getText().toString()) - 1 ;
                    if (nm>0){
                    viewHolder.txRate.setText(""+n);
                    viewHolder.txhnum.setText(""+nm);
                    db.setCart(cob, nm);
                    Log.e("Number of Helpers : "," "+nm+"----------CULUMN_UID  "+cob.get("uid")+ " CART ADAPTER");
                    updateintent();
                    }
                    else
                    {
                        new AlertDialog.Builder(mactivity)
                                .setTitle("Delete "+cob.get("subcat_name"))
                                .setMessage("Are you sure to delete ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.removeItemFromCart(cob.get("subcat_id"));
                                        updateintent();
                                        dataList.remove(i);
                                        notifyDataSetChanged();
                                        Toast.makeText(mactivity, "Deleted from cart", Toast.LENGTH_SHORT).show();
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
                }
            });

            viewHolder.icDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.removeItemFromCart(cob.get("subcat_id"));
                    updateintent();
                    dataList.remove(i);
                    notifyDataSetChanged();
                    Toast.makeText(mactivity, "Deleted from cart", Toast.LENGTH_SHORT).show();
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
        TextView tx, txWorkernm, txTime, txRate, txFrom, txTo, txhnum;
        ImageView img, icAdd, icRemove, icDelete;

        public ViewHolder(View itemview) {
            super(itemview);
            txWorkernm = (TextView) itemview.findViewById(R.id.tx_mwrker);
            txTime = (TextView) itemview.findViewById(R.id.tx_time);
            txRate = (TextView) itemview.findViewById(R.id.tx_ratee);
            txFrom = (TextView) itemview.findViewById(R.id.tx_hirefrom);
            txTo = (TextView) itemview.findViewById(R.id.tx_hireto);
            txhnum = (TextView) itemview.findViewById(R.id.tx_add);
            icAdd = (ImageView) itemview.findViewById(R.id.ic_add);
            icRemove = (ImageView) itemview.findViewById(R.id.ic_remove);
            img = (ImageView) itemview.findViewById(R.id.wimg);
            icDelete = (ImageView) itemview.findViewById(R.id.ic_delete);
        }
    }

    private void updateintent(){
        Intent updates = new Intent("MCART");
        updates.putExtra("type", "update");
        mactivity.sendBroadcast(updates);
    }
}
