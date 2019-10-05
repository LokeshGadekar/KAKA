package com.info.manPower.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.API_retro.Click_Listener;
import com.info.manPower.Adapter.Category_adapter;
import com.info.manPower.AppUtils.BaseUrl;
import com.info.manPower.AppUtils.HttpHandler;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.Model.Category_model;
import com.info.manPower.Model.Enquiry_Responce;
import com.info.manPower.R;
import com.info.manPower.View.MainActivity_drawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Enquiry_Fragment extends Fragment
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar, cart;

    ArrayList<String> catName;
    HashMap<String,String>  ccatId;

    private Spinner spinnercat;
    private EditText edDate, edName, edMobile, edAddr1, edAddr2, edNum, edDesc;
    private ImageView calendar;
    private DatePickerDialog picker;
    private Button submit;
    private String name, date, address1, address2, wdesc, mobile, wnum, ctname, catid;
    private API_parameter ApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enquiry, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView) view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        cart = view.findViewById(R.id.icon_cart);
        cart_count = view.findViewById(R.id.cart_count);

        spinnercat = (Spinner) view.findViewById(R.id.spinner_category);
        edDate = (EditText) view.findViewById(R.id.ed_dfrom);
        calendar = (ImageView) view.findViewById(R.id.ic_cal);
        edName = (EditText) view.findViewById(R.id.ed_uname);
        edMobile = (EditText) view.findViewById(R.id.ed_umobile) ;
        edAddr1 = (EditText) view.findViewById(R.id.ed_addr1);
        edAddr2 = (EditText) view.findViewById(R.id.ed_addr2);
        edNum = (EditText) view.findViewById(R.id.ed_numworkers);
        edDesc = (EditText) view.findViewById(R.id.ed_wrkdesc);
        submit = (Button) view.findViewById(R.id.button_submit);

        catName = new ArrayList<>();
        ccatId = new HashMap<>();
        new GetCat().execute();
        ApiService = BaseUrl.getAPIService();
        cart.setVisibility(View.GONE);
        cart_count.setVisibility(View.GONE);

        txToolbar.setText("Enquiry");
        toolbar.setNavigationIcon(R.drawable.menu1);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ((MainActivity_drawer)getActivity()).openDrawer();       }   });

        Click_Listeners();

        return view;
    }


    private void Click_Listeners()
    {
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                Log.d("mill sec is",""+System.currentTimeMillis());
                picker.getDatePicker().setMinDate( (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
                // picker.getDatePicker().setMinDate( (System.currentTimeMillis() -10000000) );
//                    picker.getDatePicker().getCalendarView().setMinDate();
                picker.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (validate())
                    {
                        CALLAPI();
                    }
                    else
                    {              }
            }
        });

        spinnercat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ctname = (String)spinnercat.getItemAtPosition(position);
                Log.e("SPINNER ","____________________"+ctname+"________"+catid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void CALLAPI()
    {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();

        ApiService.ENQUIRY_CALL(name,mobile,address1,address2,wnum,wdesc,catid,ctname,date).enqueue(new Callback<Enquiry_Responce>() {
            @Override
            public void onResponse(Call<Enquiry_Responce> call, Response<Enquiry_Responce> response) {
                Log.e("ENQUIRY RESPONSE ...", "" + new Gson().toJson(response.body()));
                Log.e("ENQUIRY RESPONSE ...", "-------------------------------------------------");
                dialog.dismiss();
                if (response.body().getResponce()) {
                    Toast.makeText(getActivity(), "Enquiry Placed...", Toast.LENGTH_SHORT).show();
                    Clear_ed();
                } else if (!response.body().getResponce()) {
                    Toast.makeText(getActivity(), "No Data( false )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Enquiry_Responce> call, Throwable t) {
                dialog.dismiss();
                Log.e("error at call 1 ", "" + t.getLocalizedMessage());
                Log.e("error at call 2 ", "" + t.getMessage());
                Log.e("error at call 3 ", "" + t.getCause());
                Log.e("error at call 4 ", "" + t.getStackTrace());
            }
        });
    }

    private boolean validate()
    {
        name = edName.getText().toString().trim();
        mobile = edMobile.getText().toString().trim();
        address1 = edAddr1.getText().toString().trim();
        address2 = edAddr2.getText().toString().trim();
        wdesc = edDesc.getText().toString().trim();
        wnum = edNum.getText().toString().trim();
        date = edDate.getText().toString().trim();
        catid = ccatId.get(ctname);

        if ( ctname.matches("") && name.matches("") && mobile.matches("") && address2.matches("") && address1.matches("") && wdesc.matches(""))
        {
            Toast.makeText(getActivity(), "Please fill data correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (ctname.matches("") || ctname.equals("Select Category"))
        {
            Toast.makeText(getActivity(), "Please select work type", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (name.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!validCellPhone(mobile))
        {
            Toast.makeText(getActivity(), "Please enter mobile no.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (address1.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter address one", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (address2.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter address two", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (wdesc.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter work description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (wdesc.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter work description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (date.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter date " , Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (wnum.matches(""))
        {
            Toast.makeText(getActivity(), "Please enter number of workers " , Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;

        }
    }


        private void Clear_ed()
        {
            edDesc.getText().clear();
            edNum.getText().clear();
            edAddr1.getText().clear();
            edName.getText().clear();
            edMobile.getText().clear();
            edAddr2.getText().clear();
            edName.getText().clear();
            edDate.getText().clear();
        }

    public boolean validCellPhone(String number) {
        return !TextUtils.isEmpty(number) && (number.length() == 10) && android.util.Patterns.PHONE.matcher(number).matches();
    }

    class GetCat extends AsyncTask<String, String, String>
    {
        String output = "", url;
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
            Log.e("ENQUIRY>>>>>>>>>", "__________________________________");
            output = HttpHandler.makeServiceCall(url);
            //   Log.e("getcomment_url", output);
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

                        catName.add(cname);
                        ccatId.put(cname,id);

                        Log.e("ENQUIRY  RESPONSE "," --- "+id+" --- "+cname+image);
                    }
                    ArrayAdapter<String> adsupplier = Utilview.setupAdapter(getActivity(), spinnercat, (String[]) catName.toArray(new String[catName.size()]), "Select Category");
                    spinnercat.setAdapter(adsupplier);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            super.onPostExecute(s);
        }
    }



}
