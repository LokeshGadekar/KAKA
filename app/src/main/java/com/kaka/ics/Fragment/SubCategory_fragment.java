package com.kaka.ics.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.kaka.ics.API_retro.API_parameter;
import com.kaka.ics.Adapter.SubCategory_adapter;
import com.kaka.ics.AppUtils.AppPrefrences;
import com.kaka.ics.AppUtils.BaseUrl;
import com.kaka.ics.AppUtils.DatabaseHandler;
import com.kaka.ics.AppUtils.Utilview;
import com.kaka.ics.Model.Subcategory_Responce;
import com.kaka.ics.Model.Subcategory_data;
import com.kaka.ics.R;
import com.kaka.ics.View.MainActivity_drawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategory_fragment extends Fragment {
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar, cart;
    private SubCategory_adapter adapter;
    private RecyclerView recyclerView;
    private Activity activity;
    private API_parameter ApiService;
    private String id, wname;
    private Button contHiring, AddToCart;
    private String Rate, Hirefrom, Hireto;
    private int data_size, cnt;
    private DatabaseHandler dbcart;
    LinearLayoutManager linearLayoutManager;
    private FloatingActionMenu fabMenu;
    private FloatingActionButton fabphone, fabcart;

    ArrayList<HashMap<String,String>> ListMap;

    static List<Subcategory_data> SubData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public SubCategory_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        contHiring = (Button) view.findViewById(R.id.conthiring);
        AddToCart = (Button) view.findViewById(R.id.addtocart);
        cart = (ImageView) view.findViewById(R.id.icon_cart);
        cart_count = (TextView) view.findViewById(R.id.cart_count);
        fabMenu = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        fabphone = (FloatingActionButton) fabMenu.findViewById(R.id.floating_orderph);
        fabcart = (FloatingActionButton) fabMenu.findViewById(R.id.floating_chkout);

        id = getArguments().getString("CatId");
        wname = getArguments().getString("CatName");

        ((MainActivity_drawer) getActivity()).lockDrawer();

        txToolbar.setText("" + wname);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilview.hidekeyboard(getActivity());
                ((MainActivity_drawer) getActivity()).onBackPressed();
            }
        });
        cart.setVisibility(View.VISIBLE);

        ApiService = BaseUrl.getAPIService();
        dbcart = new DatabaseHandler(getActivity());

        CallAPI();
        Click_Listeners();
        cart_count.setText(""+dbcart.getCartCount());

        return view;
    }

    private void CallAPI() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.SubCategory_CALL(id).enqueue(new Callback<Subcategory_Responce>() {
            @Override
            public void onResponse(Call<Subcategory_Responce> call, Response<Subcategory_Responce> response) {
                Log.e("SUBCATEGORY RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("SUBCATEGORY RESPONSE.", "-------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce()) {
                    adapter = new SubCategory_adapter(activity, response.body().getData());
                    SubData = new ArrayList<Subcategory_data>(response.body().getData());
                    data_size = response.body().getData().size();
                    Log.e("Subcategory "," SIZE is >>> "+data_size);
                    linearLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                } else if (!response.body().getResponce()) {
                    Toast.makeText(getActivity(), "No Data( false )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Subcategory_Responce> call, Throwable t) {
                dialog.dismiss();
                Log.e("error at call", "" + t.getLocalizedMessage());
                Log.e("error at call", "" + t.getMessage());
                Log.e("error at call", "" + t.getCause());
            }
        });
    }

    private void Click_Listeners() {
        contHiring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity_drawer) getActivity()).onBackPressed();
            }
        });

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

        AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListMap = new ArrayList<HashMap<String, String>>();

                Log.e(" ADAPTER COUNT : "," "+adapter.getItemCount()+"____ recyclerView COUNT :______"+recyclerView.getChildCount()+
                        "_________________Sub Data List COUNT :___"+SubData.size());

                for (int i = 0; i <adapter.getItemCount(); i++) {
                    Log.e(" DATA "+i,SubData.get(i).getSubcatName()+"______"+ SubData.get(i).getDescription()+"________"+SubData.get(i).getSubcatRate()
                    +"____________"+SubData.get(i).getUnit_rate()+"_________"+SubData.get(i).getHire_from()+"___________"
                            +SubData.get(i).getHire_to()+"_______");

                    int titems=0;

                    for (int j=0 ; j<adapter.getItemCount(); j++)
                    {
                        int qty = Integer.parseInt(SubData.get(j).getNum_helpers());
                        if (qty>0)
                        {   titems = titems + 1;   }
                    }  // for count of all itemns quantitiy

                    int qty = Integer.parseInt(SubData.get(i).getNum_helpers()); // count of single quantity

                    if (qty>=1)
                    {
                        Validate_addCart(i , ""+qty, titems);
                    }
                    else if (titems == 0)
                    {
                        Toast.makeText(activity, "Minimum 1 item is required to add to cart", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        fabphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppPrefrences.getOrderPhone(getActivity())));
                startActivity(intent);}
                catch (Exception ex)
                {              ex.printStackTrace();
                    Toast.makeText(activity, "Error No Sim Card Found......", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        fabcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call_Cart();
            }
        });

    }

    private  void Call_Cart()
    {
        Cart_fragment crt = new Cart_fragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, crt)
                .addToBackStack(null).commit();
    }

    private void Validate_addCart(int i,String qty, int tt) {
        HashMap<String, String> map = new HashMap<>();

        try {
            String subcatid = SubData.get(i).getId();
            String rate = SubData.get(i).getUnit_rate(); // unit rate
            String unit_rate = SubData.get(i).getSubcatRate();  // All helpers added rate
            String sb_name = SubData.get(i).getSubcatName();
            String img = SubData.get(i).getImage();
            String tfrom = SubData.get(i).getTimeFrom();
            String tto = SubData.get(i).getTimeTo();
            String dfrom = SubData.get(i).getHire_from();
            String dto = SubData.get(i).getHire_to();
            String workdetail = SubData.get(i).getDescription();
            String catid = id;

            if (dfrom==null && dto==null && workdetail==null) {
                Toast.makeText(activity, "Please fill Data Correctly " + SubData.get(i).getSubcatName(), Toast.LENGTH_SHORT).show();
            } else if (dfrom==null) {
                Toast.makeText(activity, "Please fill Hire From date in " + SubData.get(i).getSubcatName(), Toast.LENGTH_SHORT).show();
            } else if (dto==null) {
                Toast.makeText(activity, "Please fill Hire To date in " + SubData.get(i).getSubcatName(), Toast.LENGTH_SHORT).show();
            } else if (workdetail.matches("")) {
                Toast.makeText(activity, "Please fill Work Details in " + SubData.get(i).getSubcatName(), Toast.LENGTH_SHORT).show();
            } else {
                map.put("category_id", catid);
                map.put("subcat_id", subcatid);
                map.put("subcat_name", sb_name);
                map.put("num_helpers", qty);
                map.put("image", img);
                map.put("unit_value", unit_rate);
                map.put("date_from", dfrom);
                map.put("date_to", dto);
                map.put("time_from", tfrom);
                map.put("time_to", tto);
                map.put("work_details", workdetail);
                //dbcart.setCart(map, Integer.parseInt(qty),rate);
                ListMap.add(map);
                Log.e("SubCategory Fragment : ","__ ListMap Size ___"+ListMap.size()+"___Total items_____"+tt);
                if (ListMap.size() == tt) {
                    addToCart(ListMap, Integer.parseInt(qty));
                    Toast.makeText(activity, tt + " Items Added to Cart", Toast.LENGTH_SHORT).show();
                }

            }
        }
        catch (Exception ex)
        {
            Log.e("ERROR : CART_","________________________________________________________________");
            ex.printStackTrace();
        }
    }

    private void addToCart( ArrayList<HashMap<String, String>> listMap, int qtty)
    {
        HashMap<String,String> mmap = new HashMap<>();
        for (int i=0 ; i<listMap.size() ; i++)
        {
           mmap = listMap.get(i);
           dbcart.setCart(mmap,qtty);
        }
        Log.e("SUCCESSS : ","==============  ADDED ====================== ");
        Call_Cart();
    }

}



///  CALLED API for adding items in cart

   /* private void make_JSON(String dfrom,String dto, String rate, String subcatid , String workdetail, String add)
    {
        String catid = id;
        String userid = AppPrefrences.getUserid(getActivity());
        String address = AppPrefrences.getAddress(getActivity());
        cnt++;

        try
        {
            jObj.put("user_id",userid);
            jObj.put("address",address);
            jObj.put("sub_cat_no",add);                        // give count of number of helpers
            jObj.put("rate", rate);
            jObj.put("sub_cat_id",subcatid);
            jObj.put("p_cat_id",catid);
            jObj.put("date_from",dfrom);
            jObj.put("date_to",dto);
            jObj.put("description",workdetail);
            passArray.put(jObj);
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        Log.e(" JSON ARRAY : ",""+passArray);
        if (cnt == data_size)
        {
          CALL_API(passArray);
        }
        else
        {}
    }

    private void CALL_API(JSONArray jsonArray)
    {
        ApiService.SubCategory_Post(jsonArray).enqueue(new Callback<Order_Post>() {
            @Override
            public void onResponse(Call<Order_Post> call, Response<Order_Post> response) {
                Log.e("SUBCATEGORY POST.", "" + new Gson().toJson(response.body()));
                Log.e("SUBCATEGORY POST.", "-------------------------------------------------");
                if (response.body().getResponce())
                {
                    Toast.makeText(activity, "Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order_Post> call, Throwable t) {
                Log.e("error at call" , ""+t.getLocalizedMessage());
                Log.e("error at call" , ""+t.getMessage());
                Log.e("error at call" , ""+t.getCause());
            }
        });
    }  */


   ///----------------------------------------------------------------------------------------------------------------------------
   ///============================================================================================================================

       /*AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(" ADAPTER COUNT : "," "+adapter.getItemCount()+"____ recyclerView COUNT :______"+recyclerView.getChildCount());
                for (int i = 0; i <adapter.getItemCount(); i++) {
                    View view = recyclerView.getChildAt(i);


                    //                    View view = linearLayoutManager.findViewByPosition(i);
                    //recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tx_rate);
//                    RecyclerView.ViewHolder view = recyclerView.findViewHolderForAdapterPosition(i);
                    //View view = recyclerView.findViewHolderForItemId(adapter.getItemId(i)).itemView;
                    Log.e(" LOOOP : ","_____________ "+i+"_________"+SubData.get(i).getSubcatName());
                    TextView txRate = (TextView) view.findViewById(R.id.tx_rate);
                    TextView txqty = (TextView) view.findViewById(R.id.tx_addd);
                    EditText hfrom = (EditText) view.findViewById(R.id.ed_hirefrom);
                    EditText hto = (EditText) view.findViewById(R.id.ed_hireto);
                    EditText desc = (EditText) view.findViewById(R.id.ed_workdetails);

                   /* TextView txRate = (TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tx_rate);
                    TextView txqty = (TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tx_addd);
                    EditText hfrom = (EditText) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.ed_hirefrom);
                    EditText hto = (EditText) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.ed_hireto);
                    EditText desc = (EditText) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.ed_workdetails);

                    Log.e(data_size + "___DATA  = ", " >" + " - - - " + " - - - "
                            + " - - - " + txqty.getText().toString() + " - - - " + txRate.getText().toString() + " - - - " +
                            SubData.get(i).getId() + " - - - " + id + " - - - " + hfrom.getText().toString() + " - - - "
                            + " - - - " + hto.getText().toString() + " - - - " + desc.getText().toString());


//                    String rate = txRate.getText().toString().trim();
//                    String sb_name = sub_name.getText().toString().trim();

                    String qty = txqty.getText().toString().trim();
                    String dfrom = hfrom.getText().toString().trim();
                    String dto = hto.getText().toString().trim();
                    String workdetail = desc.getText().toString().trim();

                    if (Integer.parseInt(qty)>=1) {

                        if (dfrom.isEmpty() && dto.isEmpty() && workdetail.isEmpty()) {
                            Toast.makeText(getActivity(), "Please fill data properly", Toast.LENGTH_SHORT).show();
                        } else if (dfrom.isEmpty()) {
                            Toast.makeText(getActivity(), "Please add hirefrom date", Toast.LENGTH_SHORT).show();
                        } else if (dto.isEmpty()) {
                            Toast.makeText(getActivity(), "Please add hireto date", Toast.LENGTH_SHORT).show();
                        } else if (workdetail.isEmpty()) {
                            Toast.makeText(getActivity(), "Please add work details", Toast.LENGTH_SHORT).show();
                        } else {
                            //make_JSON(dfrom,dto,rate,subcatid,workdetail,add);
                            addCart(i , dfrom, dto, workdetail, qty);
                        }
                    }

                    else
                    {         }
                }

            }
        });*/
