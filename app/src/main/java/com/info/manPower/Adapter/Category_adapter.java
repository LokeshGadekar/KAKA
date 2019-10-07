package com.info.manPower.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.Fragment.SubCategory_fragment;
import com.info.manPower.Model.Category_model;
import com.info.manPower.R;

import java.util.ArrayList;
import java.util.List;

public class Category_adapter extends RecyclerView.Adapter<Category_adapter.ViewHolder>
{
    List<Category_model> dataList;
    List<CardView>cardViewList = new ArrayList<>();

    Activity mactivity;

    private int cpos;

    public Category_adapter(Activity mactivity, List<Category_model> dataList)
    {
        this.mactivity=mactivity;
        this.dataList=dataList;
    }


    @Override
    public Category_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_category,viewGroup,false);
        return new Category_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Category_adapter.ViewHolder viewHolder, int i) {
        if (viewHolder!=null)
        {
          final Category_model cob = dataList.get(i);

         viewHolder.tx.setText(cob.getCat_name());

            Glide.with(mactivity).load(BaseUrl.baseimg+""+cob.getImage()).into(viewHolder.img);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment fragment;
                    Bundle args = new Bundle();
                    args.putString("CatName",""+cob.getCat_name());
                    args.putString("CatId",""+cob.getId());
                    Log.e("NAME & ID :-",""+cob.getCat_name()+"---"+cob.getId());
                    fragment= new SubCategory_fragment();
                    fragment.setArguments(args);
                    FragmentManager fragmentmanager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_layout,fragment);
                    fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
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
        TextView tx;
        ImageView img;

        public ViewHolder(View itemview) {
            super(itemview);
            tx = (TextView)itemview.findViewById(R.id.Wtx);
            img = (ImageView)itemview.findViewById(R.id.Wimg);
        }
    }


}
