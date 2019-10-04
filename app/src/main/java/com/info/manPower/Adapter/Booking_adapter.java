package com.info.manPower.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.info.manPower.Model.Booking_data;
import com.info.manPower.R;

import java.util.List;

public class Booking_adapter extends RecyclerView.Adapter<Booking_adapter.ViewHolder>
{
    List<Booking_data> dataList;
    Activity mactivity;

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

            Booking_data ob = dataList.get(i);

            viewHolder.txnm.setText(ob.getSubcatName());
            viewHolder.orderid.setText(ob.getOrderId());
            viewHolder.paymode.setText(ob.getPaymentMode());
            viewHolder.dfrom.setText(ob.getDateFrom());
            viewHolder.dto.setText(ob.getDateTo());
            viewHolder.work.setText(ob.getDescription());

            if (Integer.parseInt(ob.getStatus()) == 0)
            {
                viewHolder.status.setText("Pending");
            }

            else if (Integer.parseInt(ob.getStatus()) == 1)
            {
                viewHolder.status.setText("Confirm");
            }

            else if (Integer.parseInt(ob.getStatus()) == 2)
            {
                viewHolder.status.setText("Complete");
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txnm, orderid , dto , dfrom , work, paymode, status;

        public ViewHolder(View itemview) {
            super(itemview);
            txnm = (TextView)itemview.findViewById(R.id.tx_snm);
            status = (TextView)itemview.findViewById(R.id.tx_status);
            orderid = (TextView) itemview.findViewById(R.id.tx_orderid);
            dfrom = (TextView) itemview.findViewById(R.id.tx_dfr);
            dto = (TextView)itemview.findViewById(R.id.tx_dto);
            work = (TextView) itemview.findViewById(R.id.tx_wdtl);
            paymode = (TextView) itemview.findViewById(R.id.tx_pmode);
        }
    }
}
