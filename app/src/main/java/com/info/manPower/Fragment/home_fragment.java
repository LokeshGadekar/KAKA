package com.info.manPower.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.info.manPower.Adapter.Category_adapter;
import com.info.manPower.Adapter.Slider_adapter;
import com.info.manPower.AppUtils.DatabaseHandler;
import com.info.manPower.AppUtils.HttpHandler;
import com.info.manPower.AppUtils.Internet_Connectivity;
import com.info.manPower.Model.Category_model;
import com.info.manPower.R;
import com.info.manPower.View.MainActivity_drawer;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class home_fragment extends Fragment
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart, location, notifier;

    private SliderView sliderView;

    private Activity activity;
    private String url;

    private RecyclerView recyclerView;
    private Category_adapter adapter;

    private List<Category_model> categorylist;
    private DatabaseHandler dbcart;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);

        ((MainActivity_drawer)getActivity()).unlockDrawer();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        cart = view.findViewById(R.id.icon_cart);
        cart_count = view.findViewById(R.id.cart_count);
        sliderView = (SliderView) view.findViewById(R.id.imageSlider);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        dbcart = new DatabaseHandler(getActivity());
        categorylist = new ArrayList<>();



        init_Slider();
        if (Internet_Connectivity.isConnected(getActivity()))
        { new GetCat().execute();}
        else
        {   SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Oops...");
            pDialog.setContentText("No Internet Connection !");
            pDialog.show();       }

        txToolbar.setText("Man Power");
        toolbar.setNavigationIcon(R.drawable.menu1);
        cart.setVisibility(View.VISIBLE);

        ((MainActivity_drawer)getActivity()).check_Login_Status();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity_drawer)getActivity()).openDrawer();
            }
        });

        Click_Listeners();

        return view;
    }

    private void init_Slider()
    {
        final Slider_adapter adapter = new Slider_adapter(getActivity());
        adapter.setCount(6);

        sliderView.isAutoCycle();
        sliderView.setAutoCycle(true);
        sliderView.setScrollTimeInSec(2);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
        cart_count.setText(""+dbcart.getCartCount());
    }

    private void Click_Listeners()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dbcart.getCartCount() >0) {
                    Cart_fragment crt = new Cart_fragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, crt)
                            .addToBackStack(null).commit();
                }
                else 
                {
                    Toast.makeText(getActivity(), "No item in cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

     class GetCat extends AsyncTask<String, String, String>
     {
         String output = "";
         ProgressDialog dialog;

         @Override
         protected void onPreExecute() {
             dialog = new ProgressDialog(getActivity());
             dialog.setMessage("Processing");
             dialog.setCancelable(true);
             dialog.show();
             super.onPreExecute();
         }

         @Override
         protected String doInBackground(String... strings) {
             try {
                 url = "https://www.lotusenterprises.net/manpower/Api/get_category";
             } catch (Exception e) {
                 e.printStackTrace();
             }
             Log.e("sever_url>>>>>>>>>", url);
             output = HttpHandler.makeServiceCall(url);
             //   Log.e("getcomment_url", output);
             System.out.println("getcomment_url" + output);
             return output;
         }

         @Override
         protected void onPostExecute(String s) {
             if (output == null) {
                 dialog.dismiss();
             } else {
                 try {
                     dialog.dismiss();
                     JSONObject obj = new JSONObject(output);
                     String responce = obj.getString("responce");
                     JSONArray Data_array = obj.getJSONArray("data");
                     for (int i = 0; i < Data_array.length(); i++) {

                         JSONObject c = Data_array.getJSONObject(i);
                         String id = c.getString("id");
                         String cname = c.getString("cat_name");
                        // String crate = c.getString("cat_rate");
                         String image = c.getString("image");

                         categorylist.add(new Category_model(id,cname,image));
                         Log.e("CATEGORY RESPONSE "," --- "+id+" --- "+cname+image);
                     }
                     Log.e("CATEGORY RESPONSE ", "-------------------------------------------------");
                     adapter = new Category_adapter(activity,categorylist);
                     recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
                     recyclerView.setItemAnimator(new DefaultItemAnimator());
                     recyclerView.setAdapter(adapter);

                 }
                 catch (JSONException e) {
                     e.printStackTrace();
                 }
                 }

             super.onPostExecute(s);
         }
     }
}
