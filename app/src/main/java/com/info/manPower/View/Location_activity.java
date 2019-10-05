package com.info.manPower.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.HttpHandler;
import com.info.manPower.AppUtils.Utilview;
import com.info.manPower.Model.Location_data;
import com.info.manPower.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Location_activity extends AppCompatActivity
{
    Spinner spinner_city;
    List<Location_data> datalist;
    ArrayList<String> citylist;
    TextView txNext;
    String selcity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        txNext = (TextView) findViewById(R.id.ic_next);
        datalist = new ArrayList<>();
        citylist = new ArrayList<>();
        new Get_Location().execute();

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selcity = (String)spinner_city.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if (selcity.matches("") || selcity.equals("Select City"))
                  {
                      Toast.makeText(Location_activity.this,"Please select City",Toast.LENGTH_SHORT).show();
                  }
                  else
                  {
                      AppPrefrences.setLocation(Location_activity.this,selcity);
                      Intent in = new Intent(Location_activity.this, MainActivity_drawer.class);
                      startActivity(in);
                      finish();
                  }
            }
        });


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));    }

    class Get_Location extends AsyncTask<String, String, String> {
        String output = "";
        String url;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Location_activity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                url = "https://www.lotusenterprises.net/manpower/Api/get_city";
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("GET LOCATION  .....", url);
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
                        String city = c.getString("city");
                        String date = c.getString("date");
                        datalist.add(new Location_data(id,city,date));
                        citylist.add(city);
                        Log.e("ORDER ADVANCE...", " --- " + id + " --- " + city+ " --- " + date);
                    }
                    ArrayAdapter<String> adcity = Utilview.setupAdapter(Location_activity.this, spinner_city, (String[]) citylist.toArray(new String[citylist.size()]), "Select City");
                    spinner_city.setAdapter(adcity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
