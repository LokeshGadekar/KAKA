package com.kaka.ics.Fragment;

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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kaka.ics.API_retro.API_parameter;
import com.kaka.ics.Adapter.Category_adapter;
import com.kaka.ics.Adapter.Slider_adapter;
import com.kaka.ics.AppUtils.BaseUrl;
import com.kaka.ics.AppUtils.DatabaseHandler;
import com.kaka.ics.AppUtils.HttpHandler;
import com.kaka.ics.AppUtils.Internet_Connectivity;
import com.kaka.ics.Model.Category_model;
import com.kaka.ics.Model.Slider_Responce;
import com.kaka.ics.R;
import com.kaka.ics.View.MainActivity_drawer;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_fragment extends Fragment
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart;

    private SliderView sliderView;

    private Activity activity;
    private String url;

    private RecyclerView recyclerView;
    private Category_adapter adapter;
    private CardView crdEnquiry;

    private List<Category_model> categorylist;
    private DatabaseHandler dbcart;
    Slider_adapter sadapter;
    private API_parameter ApiService;

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
        crdEnquiry = (CardView) view.findViewById(R.id.crd_enquiry);

        dbcart = new DatabaseHandler(getActivity());
        categorylist = new ArrayList<>();
        ApiService = BaseUrl.getAPIService();

        init_Slider();
        if (Internet_Connectivity.isConnected(getActivity()))
        { new GetCat().execute();}
        else
        {   SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Oops...");
            pDialog.setContentText("No Internet Connection !");
            pDialog.show();       }

        txToolbar.setText("KAKA");
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
        ApiService.SLIDER_CALL().enqueue(new Callback<Slider_Responce>() {
            @Override
            public void onResponse(Call<Slider_Responce> call, Response<Slider_Responce> response) {
                Log.e("SLIDER RESPONSE ...", "" + new Gson().toJson(response.body()));
                Log.e("SLIDER RESPONSE ...", "-------------------------------------------------");

                if (response.body().getResponce()) {

                    sadapter = new Slider_adapter(getActivity(),response.body().getData());
                    sadapter.setCount(response.body().getData().size());

                    sliderView.isAutoCycle();
                    sliderView.setAutoCycle(true);
                    sliderView.setScrollTimeInSec(2);
                    sliderView.setSliderAdapter(sadapter);

                    //Toast.makeText(activity, "Order Placed...", Toast.LENGTH_SHORT).show();
                } else if (!response.body().getResponce()) {
                    Toast.makeText(getActivity(), "No Data( false )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Slider_Responce> call, Throwable t) {
                Log.e("error at call", "" + t.getLocalizedMessage());
                Log.e("error at call", "" + t.getMessage());
                Log.e("error at call", "" + t.getCause());
            }
        });

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

        crdEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new Enquiry_Fragment())
                        .addToBackStack(null).commit();
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

    @Override
    public void onDetach() {
        //Toast.makeText(activity, "Detached", Toast.LENGTH_SHORT).show();
//        Home_fragment.this.getView().clearFocus();

        super.onDetach();
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(activity, "Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
//        Home_fragment.this.getView().clearFocus();
        //Toast.makeText(activity, "count is"+getFragmentManager().getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
//      if(getFragmentManager().getBackStackEntryCount()>0)
//      {
//          Home_fragment.this.getFragmentManager().popBackStack();
//      }
        //Toast.makeText(activity, "Destroyed view", Toast.LENGTH_SHORT).show();
        super.onDestroyView();
    }
}

