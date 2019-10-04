package com.info.manPower.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.DatabaseHandler;
import com.info.manPower.AppUtils.HttpHandler;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.Model.Order_Responce;
import com.info.manPower.R;
import com.info.manPower.View.MainActivity_drawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Order_fragment extends Fragment {

    private Button placeOrder;

    Toolbar toolbar;
    TextView txToolbar, cnt;
    ImageView imgToolbar, cart, ic_map;
    private Activity activity;
    private TextView subtotal, payable, advpay, headadv;
    private DatabaseHandler db;
    private EditText name, mobile, mobileopt, landmark, address;
    private RadioGroup radio_payment;
    private String pay_mode="online", nam, phone, optphone, landm, addr;
    private API_parameter ApiService;
    JSONArray passArray;
    private DatabaseHandler dbcart;
    static double advance = 0;
    SupportMapFragment mMap;

    private GoogleMap googleMap;
    private Location locationC;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ((MainActivity_drawer) getActivity()).lockDrawer();

        placeOrder = (Button) view.findViewById(R.id.place_order);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        ic_map = (ImageView) view.findViewById(R.id.ic_map);

        subtotal = (TextView) view.findViewById(R.id.tx_subtotal);
        payable = (TextView) view.findViewById(R.id.tx_payable);
        advpay = (TextView) view.findViewById(R.id.tx_advpay);
        name = (EditText) view.findViewById(R.id.ed_name);
        mobile = (EditText) view.findViewById(R.id.ed_mobile);
        mobileopt = (EditText) view.findViewById(R.id.ed_mobileopt);
        headadv = (TextView) view.findViewById(R.id.head_adv);
        landmark = (EditText) view.findViewById(R.id.ed_landmark);
        address = (EditText) view.findViewById(R.id.ed_aaddress);

        cart = (ImageView) view.findViewById(R.id.icon_cart);
        cnt = (TextView) view.findViewById(R.id.cart_count);

        new Advance_pay().execute();
        cart.setVisibility(View.GONE);
        cnt.setVisibility(View.GONE);
        dbcart = new DatabaseHandler(getActivity());
        ApiService = BaseUrl.getAPIService();

        db = new DatabaseHandler(getActivity());
        txToolbar.setText("Final Order");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilview.hidekeyboard(getActivity());
                ((MainActivity_drawer) getActivity()).onBackPressed();
            }
        });

        subtotal.setText("₹" + db.getTotalAmount());

        name.setText("" + AppPrefrences.getName(getActivity()));
        mobile.setText(""+AppPrefrences.getMobile(getActivity()));

        Click_Listeners();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localBroadcastRec, new IntentFilter("StringAddr"));


        return view;
    }

    private void Click_Listeners() {
        ic_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MapDialog_fragment mapf = new MapDialog_fragment();
                mapf.show(getFragmentManager(), null);

            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    passArray = new JSONArray();
                    make_JSON();
//                    Toast.makeText(activity, "Order Placed...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill data correctly...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Google Map Address Receiver
    private final BroadcastReceiver localBroadcastRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String Faddress = intent.getStringExtra("Addr");
                address.setText("" + Faddress);

                //Log.e("FINALY Address in Delivery Fragment :","------------------"+intent.getStringExtra("Addr"));
            }
        }
    };


    private boolean validate() {
        nam = name.getText().toString().trim();
        phone = mobile.getText().toString().trim();
        optphone = mobileopt.getText().toString().trim();
        landm = landmark.getText().toString();
        addr = address.getText().toString().trim();


        if (nam.matches("") && phone.matches("") && addr.matches("") && landm.matches("")) {
            Toast.makeText(getActivity(), "Please fill data correctly", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nam.matches("")) {
            Toast.makeText(getActivity(), "Please enter Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!validCellPhone(phone)) {
            Toast.makeText(getActivity(), "Please enter valid mobile no.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (landm.matches("")) {
            Toast.makeText(getActivity(), "Please enter landmark", Toast.LENGTH_SHORT).show();
            return false;
        } else if (addr.matches("")) {
            Toast.makeText(getActivity(), "Please enter address", Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }

    private void make_JSON() {
        int cnt = 0;

        ArrayList<HashMap<String, String>> dlist = db.getCartAll();

        Log.e("Order Fragment ", "------------------------------------" + dlist.get(0));


        for (int i = 0; i < dlist.size(); i++) {

            cnt++;

            Log.e("Order Fragment ", "------------------------------------");

            HashMap<String, String> cob = dlist.get(i);

            String userid = AppPrefrences.getUserid(getActivity());
            String address = AppPrefrences.getAddress(getActivity());

            String catid = cob.get("category_id");
            String subcatid = cob.get("subcat_id");
            String qty = cob.get("num_helpers");
            String dfrom = cob.get("date_from");
            String dto = cob.get("date_to");
            String workdetail = cob.get("work_details");


            JSONObject jObj = new JSONObject();

            try {
                jObj.put("user_id", userid);
                jObj.put("payment_mode", pay_mode);
                jObj.put("address", addr);
                jObj.put("sub_cat_no", qty);                        // give count of number of helpers
                jObj.put("sub_cat_id", subcatid);
                jObj.put("p_cat_id", catid);
                jObj.put("date_from", dfrom);
                jObj.put("date_to", dto);
                jObj.put("description", workdetail);
                passArray.put(jObj);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        if (cnt == dlist.size()) {
            Log.e("INSTAMOJO CALL___",""+AppPrefrences.getMail(getActivity())+" --- "+phone+" --- "+advance+" --- "+pay_mode+" --- "+nam);
            //callInstamojoPay(AppPrefrences.getMail(getActivity()),phone,""+10,pay_mode,nam);
            Call_Order(passArray);
            Log.e("JSON ARRAY IS >>> ", "" + passArray);
            Log.e(" SIZE ......... ", " COUNT ........ " + cnt + "...... List Size is ..... " + dlist.size() + " |||||  DATABASE COUNT " + dbcart.getCartCount());
        }
    }


    private void Call_Order(JSONArray jsonarray) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.Order_Cart(jsonarray).enqueue(new Callback<Order_Responce>() {
            @Override
            public void onResponse(Call<Order_Responce> call, Response<Order_Responce> response) {
                Log.e("ORDER RESPONSE ...", "" + new Gson().toJson(response.body()));
                Log.e("ORDER RESPONSE ...", "-------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce()) {
                    Clear_ed();
                    Toast.makeText(activity, "Order Placed...", Toast.LENGTH_SHORT).show();
                } else if (!response.body().getResponce()) {
                    Toast.makeText(getActivity(), "No Data( false )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order_Responce> call, Throwable t) {
                dialog.dismiss();
                Log.e("error at call 1 ", "" + t.getLocalizedMessage());
                Log.e("error at call 2 ", "" + t.getMessage());
                Log.e("error at call 3 ", "" + t.getCause());
                Log.e("error at call 4 ", "" + t.getStackTrace());
            }
        });
    }


    class Advance_pay extends AsyncTask<String, String, String> {
        String output = "";
        String url;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                url = "https://www.lotusenterprises.net/manpower/Api/get_advance";
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("GET ADVANCE PAY .....", url);
            output = HttpHandler.makeServiceCall(url);
            //   Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);

            return output;
        }


        @Override
        protected void onPostExecute(String s) {
            String adv="";
            super.onPostExecute(s);
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
                        adv = c.getString("advance");
                        advance = Double.parseDouble(adv);
                        Log.e("ORDER ADVANCE...", " --- " + id + " --- " + adv);
                    }
                    Log.e("ORDER ADVANCE... ", "---------------------" + advance + "----------------------------");
                    advance = (advance * Double.parseDouble(db.getTotalAmount())) / 100;
                    Log.e("ADVANCE VALUES ___", "___________advance % " + advance + " _________Total Amount_____ " + Double.parseDouble(db.getTotalAmount()));
                    headadv.setText("Advance "+ "("+adv+"%)");
                    advpay.setText("₹" + advance);
                    payable.setText("₹" + (Integer.parseInt(dbcart.getTotalAmount())-advance));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ///   INSTA_MOJO PAYMENT /////////////

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = getActivity();
//      String  Payment_method = radio_online_pay.getText().toString();

        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        getActivity().registerReceiver(instamojoPay, filter);
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
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;

    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                //   pay_status = "Success";
                Toast.makeText(activity, "Payment Success", Toast.LENGTH_SHORT).show();
                Call_Order(passArray);
            }

            @Override
            public void onFailure(int i, String s) {
                        Log.e("Error "," Pay "+s+"________________"+i);
            }


            /// eof INSTA_MOJO PAYMENT /////////////
        };
    }

    private void Clear_ed()
    {
        name.getText().clear();
        mobile.getText().clear();
        mobileopt.getText().clear();
        landmark.getText().clear();
        address.getText().clear();
    }


        public boolean validCellPhone(String number) {
            return !TextUtils.isEmpty(number) && (number.length() == 10) && android.util.Patterns.PHONE.matcher(number).matches();
        }
}
