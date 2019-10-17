package com.kaka.ics.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kaka.ics.AppUtils.BaseUrl;
import com.kaka.ics.Model.Subcategory_data;
import com.kaka.ics.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import at.blogc.android.views.ExpandableTextView;

public class SubCategory_adapter extends RecyclerView.Adapter<SubCategory_adapter.ViewHolder>
{
    List<Subcategory_data> dataList;
    List<CardView>cardViewList = new ArrayList<>();
    private DatePickerDialog picker;
    Activity mactivity;

    int todate,tomonth,toyear;

    public SubCategory_adapter(Activity mactivity, List<Subcategory_data> dataList)
    {
        this.mactivity=mactivity;
        this.dataList=dataList;
    }


    @Override
    public SubCategory_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_subcategory,viewGroup,false);

        return new SubCategory_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SubCategory_adapter.ViewHolder viewHolder, int i) {
        if (viewHolder!=null) {

            final Subcategory_data sob = dataList.get(i);
            String from = sob.getTimeFrom().substring(0,5);
            String to = sob.getTimeTo().substring(0,5);
            viewHolder.TxMain.setText(""+sob.getSubcatName());
            viewHolder.Time.setText(from+"   to   "+to);
//            viewHolder.Rate.setText(sob.getSubcatRate());
            viewHolder.Rate.setText("0");
            viewHolder.txAdd.setText("0");
            viewHolder.txUnitRate.setText("( ₹"+sob.getSubcatRate() +" for 1 )");
            Glide.with(mactivity).load(BaseUrl.baseimg+""+sob.getImage()).into(viewHolder.imgmain);
            viewHolder.exp_txvu.setAnimationDuration(10L);
            viewHolder.exp_txvu.setInterpolator(new OvershootInterpolator());
            viewHolder.exp_txvu.setExpandInterpolator(new OvershootInterpolator());
            viewHolder.exp_txvu.setCollapseInterpolator(new OvershootInterpolator());
            viewHolder.exp_txvu.setText(""+sob.getDescription());

            sob.setTimeFrom(sob.getTimeFrom());
            sob.setTimeTo(sob.getTimeTo());
            sob.setNum_helpers(viewHolder.txAdd.getText().toString());

            viewHolder.icshow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //viewHolder.exp_txvu.toggle();
                    viewHolder.exp_txvu.setVisibility(View.VISIBLE);
                    viewHolder.exp_txvu.expand();
                    viewHolder.icshow.setVisibility(View.GONE);
                    viewHolder.ichide.setVisibility(View.VISIBLE);
                }
            });
            viewHolder.ichide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.exp_txvu.collapse();
                    viewHolder.exp_txvu.setVisibility(View.GONE);
                    viewHolder.icshow.setVisibility(View.VISIBLE);
                    viewHolder.ichide.setVisibility(View.GONE);
                }
            });

            viewHolder.hireFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(mactivity,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    if (dayOfMonth<10)
                                    {
                                        viewHolder.hireFrom.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        sob.setHire_from("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year); }
                                    else
                                    {
                                        viewHolder.hireFrom.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        sob.setHire_from(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                    todate = dayOfMonth-1;
                                    tomonth = monthOfYear;
                                    toyear = year;
                                }
                            }, year, month, day);
                    Log.d("mill sec is",""+System.currentTimeMillis());
                    picker.getDatePicker().setMinDate( (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
                    // picker.getDatePicker().setMinDate( (System.currentTimeMillis() -10000000) );
//                    picker.getDatePicker().getCalendarView().setMinDate();
                    picker.show();
                }
            });


            viewHolder.hireTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(mactivity,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    if (dayOfMonth<10)
                                    {
                                    viewHolder.hireTo.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    sob.setHire_to("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year); }
                                    else
                                    {
                                        viewHolder.hireTo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        sob.setHire_to(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }                                }
                            }, year, month, day);

                    Calendar c = Calendar.getInstance();
                    c.set(toyear,tomonth,todate);
                    picker.getDatePicker().setMinDate(c.getTimeInMillis() + TimeUnit.DAYS.toMillis(1));
                    //picker.getDatePicker().setMinDate( (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));

                    picker.show();
                }
            });

            viewHolder.icAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n =Integer.parseInt(viewHolder.Rate.getText().toString()) + Integer.parseInt(sob.getSubcatRate());
                    viewHolder.Rate.setText(""+n);
                    sob.setUnit_rate(""+n);

                    int rrte = Integer.parseInt(viewHolder.Rate.getText().toString());
                    int rr = Integer.parseInt(sob.getSubcatRate());
                    viewHolder.txAdd.setText(""+rrte/rr);
                    sob.setNum_helpers(""+rrte/rr);
                }
            });

            viewHolder.icRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int n =Integer.parseInt(viewHolder.Rate.getText().toString()) - Integer.parseInt(sob.getSubcatRate());
                    int nm = Integer.parseInt(viewHolder.txAdd.getText().toString()) - 1 ;

                    if ( nm>=0 ) {
                    viewHolder.Rate.setText(""+n);
                    sob.setUnit_rate(""+n);
                        viewHolder.txAdd.setText(""+nm);
                        sob.setNum_helpers(""+nm); }
                }
            });

            viewHolder.workdetails.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String s1 = viewHolder.workdetails.getText().toString().trim();
                            sob.setDescription(s1);
                }

                @Override
                public void afterTextChanged(Editable s) {

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
        ImageView imgmain, icAdd, icRemove, icshow, ichide;
        TextView Time, Rate, Member, txAdd, TxMain, txUnitRate;
        EditText hireFrom, hireTo, workdetails;
        ExpandableTextView exp_txvu;

        public ViewHolder(View itemview) {
            super(itemview);
            TxMain = (TextView) itemview.findViewById(R.id.tx_mainworker);
            imgmain = (ImageView) itemview.findViewById(R.id.img_main);
            Time = (TextView) itemview.findViewById(R.id.tx_time);
            Rate = (TextView) itemview.findViewById(R.id.tx_rate);
            txUnitRate = (TextView) itemview.findViewById(R.id.tx_unitRate);
            Member = (TextView) itemview.findViewById(R.id.tx_mem);
            txAdd = (TextView) itemview.findViewById(R.id.tx_addd);
            hireFrom = (EditText) itemview.findViewById(R.id.ed_hirefrom);
            hireTo = (EditText) itemview.findViewById(R.id.ed_hireto);
            icAdd = (ImageView) itemview.findViewById(R.id.ic_add);
            icRemove = (ImageView) itemview.findViewById(R.id.ic_remove);
            exp_txvu = (ExpandableTextView) itemview.findViewById(R.id.expandableTextView);
            icshow = (ImageView) itemview.findViewById(R.id.ic_show);
            ichide = (ImageView) itemview.findViewById(R.id.ic_close);
            workdetails = (EditText) itemview.findViewById(R.id.ed_workdetails);
        }
    }
}
