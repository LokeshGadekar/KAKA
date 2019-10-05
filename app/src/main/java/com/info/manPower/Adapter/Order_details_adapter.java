package com.info.manPower.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.info.manPower.Model.Booking_data;
import com.info.manPower.Model.Subcategory_data;
import com.info.manPower.Model.Suborder_data;
import com.info.manPower.R;

import java.util.List;

public class Order_details_adapter extends RecyclerView.Adapter<Order_details_adapter.ViewHolder>
{
    List<Suborder_data> dataList;
    Activity mactivity;

    private int cpos;

    public Order_details_adapter(Activity mactivity, List<Suborder_data> dataList)
    {
        this.mactivity=mactivity;
        this.dataList=dataList;
    }


    @Override
    public Order_details_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_order_details,viewGroup,false);
        return new Order_details_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Order_details_adapter.ViewHolder viewHolder, final int i) {
        if (viewHolder!=null) {

            Suborder_data ob = dataList.get(i);

            viewHolder.txnm.setText(ob.getSubcatName());
            viewHolder.amount.setText(ob.getAmount());
            viewHolder.txfrom.setText(ob.getDateFrom());
            viewHolder.txto.setText(ob.getDateTo());
            viewHolder.description.setText(ob.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txnm, txto , amtpending , txfrom , advance, amount, description;

        public ViewHolder(View itemview) {
            super(itemview);
            txnm = (TextView)itemview.findViewById(R.id.tx_subnm);
            description = (TextView)itemview.findViewById(R.id.tx_description);
            txto = (TextView) itemview.findViewById(R.id.tx_dto);
            txfrom = (TextView) itemview.findViewById(R.id.tx_dtfr);
            amtpending = (TextView)itemview.findViewById(R.id.tx_Pending);
            advance = (TextView) itemview.findViewById(R.id.tx_adv);
            amount = (TextView) itemview.findViewById(R.id.tx_tamt);
        }
    }

}
