package com.info.manPower.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.Adapter.Category_adapter;
import com.info.manPower.Adapter.SubCategory_adapter;
import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.DatabaseHandler;
import com.info.manPower.AppUtils.HttpHandler;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.Model.Category_model;
import com.info.manPower.Model.Order_Responce;
import com.info.manPower.Model.Subcategory_data;
import com.info.manPower.R;
import com.info.manPower.View.MainActivity_drawer;
import com.info.manPower.View.Registration_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_fragment extends Fragment
{
    private Button placeOrder;

    Toolbar toolbar;
    TextView txToolbar, cnt;
    ImageView imgToolbar,cart, ic_map;
    private Activity activity;
    private TextView subtotal,payable, advpay;
    private DatabaseHandler db;
    private EditText name, mobile, mobileopt, landmark, address;
    private RadioGroup radio_payment;
    private String pay_mode,nam,phone,optphone,landm,addr;
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
        activity = (Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ((MainActivity_drawer)getActivity()).lockDrawer();

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
        landmark = (EditText) view.findViewById(R.id.ed_landmark);
        address = (EditText) view.findViewById(R.id.ed_aaddress);
        radio_payment = (RadioGroup) view.findViewById(R.id.radiopayment);

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
                ((MainActivity_drawer)getActivity()).onBackPressed();
            }
        });

        subtotal.setText("₹"+db.getTotalAmount());
        payable.setText("₹"+db.getTotalAmount());
        name.setText(""+ AppPrefrences.getName(getActivity())+"  "+AppPrefrences.getMobile(getActivity()));

        Click_Listeners();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localBroadcastRec, new IntentFilter("StringAddr"));


        return view;
    }

    private void Click_Listeners()
    {
        ic_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               MapDialog_fragment mapf = new MapDialog_fragment();
               mapf.show(getFragmentManager(),null);

            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                {
                    passArray = new JSONArray();
                    make_JSON();
                    Toast.makeText(activity, "Order Placed...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Please fill data correctly...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Google Map Address Receiver
    private final BroadcastReceiver localBroadcastRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null)
            {
                String Faddress = intent.getStringExtra("Addr");
                address.setText(""+Faddress);

                //Log.e("FINALY Address in Delivery Fragment :","------------------"+intent.getStringExtra("Addr"));
            }
        }
    };


    private  boolean validate()
    {
        nam = name.getText().toString().trim();
        phone = mobile.getText().toString().trim();
        optphone = mobileopt.getText().toString().trim();
        landm = landmark.getText().toString();
        addr = address.getText().toString().trim();

        int sel = radio_payment.getCheckedRadioButtonId();
        RadioButton pay = getView().findViewById(sel);
        if (pay!=null)
        { pay_mode = pay.getText().toString().trim();}


        if (nam.matches("") && phone.matches("") && addr.matches("") && landm.matches("") && pay_mode==null)
        {
            Toast.makeText(getActivity(), "Please fill data correctly", Toast.LENGTH_SHORT).show(); return false;
        }
        else if (nam.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter Name", Toast.LENGTH_SHORT).show();  return false;
        }
        else if (!validCellPhone(phone))
        {
            Toast.makeText(getActivity(), "Please enter valid mobile no.", Toast.LENGTH_SHORT).show();  return false;
        }
        else if (landm.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter landmark", Toast.LENGTH_SHORT).show(); return false;
        }
        else if (addr.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter address", Toast.LENGTH_SHORT).show(); return false;
        }
        else if (pay_mode==null)
        {
            Toast.makeText(getActivity(), "Please select payment mode", Toast.LENGTH_SHORT).show(); return false;
        }
        else
        {
            return true;
        }
    }

    private void make_JSON()
    {
        int cnt = 0;

        ArrayList<HashMap<String, String>> dlist = db.getCartAll();

        Log.e("Order Fragment ","------------------------------------"+dlist.get(0));


        for (int i=0 ; i<dlist.size() ; i++) {

            cnt++;

            Log.e("Order Fragment ","------------------------------------");

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
                jObj.put("payment_mode",pay_mode);
                jObj.put("address",addr);
                jObj.put("sub_cat_no", qty);                        // give count of number of helpers
                jObj.put("sub_cat_id", subcatid);
                jObj.put("p_cat_id",catid);
                jObj.put("date_from", dfrom);
                jObj.put("date_to", dto);
                jObj.put("description", workdetail);
                passArray.put(jObj);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        if (cnt == dlist.size()) {
            Call_Order(passArray);
            Log.e("JSON ARRAY IS >>> ", "" + passArray);
            Log.e(" SIZE ......... ", " COUNT ........ " + cnt + "...... List Size is ..... " + dlist.size() + " |||||  DATABASE COUNT " + dbcart.getCartCount());
        }
    }




    private void Call_Order(JSONArray jsonarray)
    {
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


    class Advance_pay extends AsyncTask<String, String, String>
    {
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
                        String adv = c.getString("advance");
                        advance = Double.parseDouble(adv);
                        Log.e("ORDER ADVANCE..."," --- "+id+" --- "+adv);
                    }
                    Log.e("ORDER ADVANCE... ", "---------------------"+advance+"----------------------------");
                    advance = (advance * Double.parseDouble(db.getTotalAmount()))/100 ;
                    Log.e("ADVANCE VALUES ___","___________advance % "+advance+" _________Total Amount_____ "+Double.parseDouble(db.getTotalAmount()));
                    advpay.setText("₹"+advance);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   ////////////// Google Map /////////////////////////////


    private void Check_Permission(SupportMapFragment mMap)
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            locationC = locationManager.getLastKnownLocation(provider);
            getMAP(mMap);
        }

        else {
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },8);

              /*  ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    8);*/
                //Toast.makeText(getActivity(),"Allow Required Permission", Toast.LENGTH_LONG).show();

            } }
    }



    private void getMAP(SupportMapFragment mMap) {
        mMap.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(final GoogleMap mMap) {

                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);

                //        Geoaddress(googleMap.getMyLocation().getLatitude(),googleMap.getMyLocation().getLongitude());

                if (locationC!=null) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(locationC.getLatitude(), locationC.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(19);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                }
             /*   LatLng latLng = new LatLng(locationCt.getLatitude(),
                        locationCt.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,10);
                    mMap.moveCamera(cameraUpdate);
                    googleMap.moveCamera(cameraUpdate);
                    googleMap.animateCamera(cameraUpdate);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                   Geoaddress(locationCt.getLatitude(), locationCt.getLongitude());*/

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.clear();

                        Geoaddress(latLng.latitude, latLng.longitude);

                        //Log.e("Latitude : "+latitude,"Longitude : "+longitude);

                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Custom Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        // Toast.makeText(getActivity(), "lat long is" + latLng, Toast.LENGTH_SHORT).show();

                    }
                });
                if (googleMap != null) {
                    Toast.makeText(getActivity(), "not null", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 8: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    mMap.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getMyLocation();
                        }
                    });
                    LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    String provider = locationManager.getBestProvider(criteria, true);
                    try {   locationC = locationManager.getLastKnownLocation(provider); }
                    catch (SecurityException ex)
                    {  ex.printStackTrace();     }
                    //getMAP();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void Geoaddress(double latitude, double longitude) {
        try {
            Geocoder geo = new Geocoder(getActivity(), Locale.ENGLISH);
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                //yourtextfieldname.setText("Waiting for Location");
            } else {
                if (addresses.size() > 0) {

                    String saddress = addresses.get(0).getAddressLine(0);
                    Toast.makeText(getActivity(), ""+saddress, Toast.LENGTH_SHORT).show();
                    // Intent in = new Intent("StringAddr");
                    // in.putExtra("Addr", saddress);
                    // LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
                    getFragmentManager().popBackStack();

                    Log.e("address is", "" + addresses.get(0).getAddressLine(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
    }


  /////////////  end of Goole Map //////////////////////////////

    public boolean validCellPhone(String number)
    {
        return  !TextUtils.isEmpty(number) && (number.length()==10) && android.util.Patterns.PHONE.matcher(number).matches();
    }
}
