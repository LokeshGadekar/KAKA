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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Order_details_adapter extends RecyclerView.Adapter<Order_details_adapter.ViewHolder>
{
    List<Suborder_data> dataList;
    Activity mactivity;
    long days=0;

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

            String dat1 = ob.getDateFrom();
            String dat2 = ob.getDateTo();
            Date date1,date2;
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date1 = myFormat.parse(dat1);
                date2 = myFormat.parse(dat2);
                long diff = date2.getTime() - date1.getTime();
                days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
                System.out.println (diff+"         "+"Days: _____________________________" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" -------------------- "+days);
            }catch (ParseException pex)
            {
                pex.printStackTrace();
            }
            viewHolder.tx_days.setText(""+days+" days");

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txnm, txto , amtpending , txfrom , advance, amount, description, tx_days;

        public ViewHolder(View itemview) {
            super(itemview);
            txnm = (TextView)itemview.findViewById(R.id.tx_subnm);
            description = (TextView)itemview.findViewById(R.id.tx_description);
            txto = (TextView) itemview.findViewById(R.id.tx_dto);
            txfrom = (TextView) itemview.findViewById(R.id.tx_dtfr);
            amtpending = (TextView)itemview.findViewById(R.id.tx_Pending);
            advance = (TextView) itemview.findViewById(R.id.tx_adv);
            amount = (TextView) itemview.findViewById(R.id.tx_tamt);
            tx_days = (TextView) itemview.findViewById(R.id.tx_ddays);
        }
    }

}
