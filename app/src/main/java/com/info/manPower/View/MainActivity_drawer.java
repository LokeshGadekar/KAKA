package com.info.manPower.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.AppUtils.DatabaseHandler;
import com.info.manPower.AppUtils.HttpHandler;
import com.info.manPower.AppUtils.Session_management;
import com.info.manPower.BuildConfig;
import com.info.manPower.Fragment.Booking_fragment;
import com.info.manPower.Fragment.Enquiry_Fragment;
import com.info.manPower.Fragment.FeedBack_fragment;
import com.info.manPower.Fragment.Home_fragment;
import com.info.manPower.Fragment.Profile_fragment;
import com.info.manPower.Fragment.Support_fragment;
import com.info.manPower.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity_drawer extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    Activity activity;
    private FragmentManager fragmentmanager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private NavigationView nav_view;
    private Session_management sessionManagement;
    private TextView name, mail;
    private DatabaseHandler dbcart;
    private Handler handler;
    Runnable r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        activity = this;

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        View header = nav_view.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.head_name);
        mail = (TextView) header.findViewById(R.id.head_mail);
        handler = new Handler();

//  r = new Runnable() {
//            public void run() {
////                tv.append("Hello World");
//                if(sessionManagement.isLoggedIn()) {
//                    new CheckStatus().execute();
//                }else {
//                    Log.e("status" , "1");
////                    handler.removeCallbacks(r);
//                    handler.removeCallbacks(null);
//                }
//                             handler.postDelayed(this, 2000);
//            }
//        };
//
//        handler.postDelayed(r, 3000);
        toggle = new ActionBarDrawerToggle(activity, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new GetOrder_Phone().execute();
        dbcart = new DatabaseHandler(MainActivity_drawer.this);
        sessionManagement = new Session_management(MainActivity_drawer.this);

        fragment = new Home_fragment();
        fragmentmanager = getSupportFragmentManager();
        fragmentTransaction =fragmentmanager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout,fragment);
        fragmentTransaction.commit();
       // fragmentTransaction.addToBackStack(Home_fragment.class.getSimpleName());
        setUpNavigationView();

        check_Login_Status();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_drawer, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            if(dbcart.getCartCount() >0) {
            Cart_fragment crt = new Cart_fragment();
            fragmentTransaction.replace(R.id.fragment_layout,crt);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();}
            else
            {
                Toast.makeText(activity, "No items in Cart...", Toast.LENGTH_SHORT).show();
            }
        }

        else
        {

        }
        return super.onOptionsItemSelected(item);
    }*/

    public void openDrawer() {
        unlockDrawer();
        drawer.openDrawer(GravityCompat.START);    }

    public void closeDrawer() {
        drawer.closeDrawers();
    }

    public void lockDrawer(){  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);  }

    public void unlockDrawer(){ drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); }


    @Override
    public void onBackPressed() {
        Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() <1)
        {
            //if (fragment == getSupportFragmentManager().findFragmentByTag("Home_fragment"))
            if (fragmentInFrame instanceof Home_fragment)
            {
                finish();
            }
            else {
                //Toast.makeText(activity, "Maiondytf", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity_drawer.this, MainActivity_drawer.class);
                startActivity(intent);
                finish();
            }
        }else {
            //Toast.makeText(activity, "fh", Toast.LENGTH_SHORT).show();
        }
        closeDrawer();
    }

    public void check_Login_Status()
    {
        if (sessionManagement.isLoggedIn())
        {
            name.setText(""+ AppPrefrences.getName(activity));
            mail.setText(""+ AppPrefrences.getMail(activity));
           nav_view.getMenu().findItem(R.id.nav_logout).setVisible(true);
           nav_view.getMenu().findItem(R.id.nav_login).setVisible(false);     }
        else
        {
            nav_view.getMenu().findItem(R.id.nav_logout).setVisible(false);
            nav_view.getMenu().findItem(R.id.nav_login).setVisible(true);
        }
    }


    private void setUpNavigationView()
    {
        final Bundle bn = new Bundle();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           switch (item.getItemId()) {

               case R.id.nav_home:
                   if (item.isChecked())
                   {closeDrawer();               }
                   else{
                   fragment = new Home_fragment();
                   fragmentmanager = getSupportFragmentManager();
//                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                   fragmentTransaction.addToBackStack(null);
                   fragmentTransaction.commit();
                   //fragmentTransaction.commitNow();
                   closeDrawer(); }
                   break;

              case R.id.nav_profile:
                  if (item.isChecked())
                  {closeDrawer();
                  }
                  else
                  {
                  if(!sessionManagement.isLoggedIn())
                  {     Intent in = new Intent(MainActivity_drawer.this, Login_activity.class);
                        startActivity(in); }

                  else {
                  fragment = new Profile_fragment();
                  fragmentmanager = getSupportFragmentManager();
                //  fragmentmanager.popBackStack();
                  fragmentTransaction = fragmentmanager.beginTransaction();
                  fragmentTransaction.replace(R.id.fragment_layout,fragment);
                  fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                 // fragmentTransaction.commit();
                  fragmentTransaction.commit();
//                      fragment.getActivity().finish();
                  lockDrawer();
                  closeDrawer(); } }
                  break;

               case R.id.nav_booking:
                   if (item.isChecked())
                   {closeDrawer();
                       //Toast.makeText(activity, "IS CHECKEDDDDD.....", Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                   if (!sessionManagement.isLoggedIn())
                   { Intent in = new Intent(MainActivity_drawer.this, Login_activity.class);
                       startActivity(in);   }
                   else
                   {
                       fragment = new Booking_fragment();
                       fragmentmanager = getSupportFragmentManager();
                       fragmentTransaction = fragmentmanager.beginTransaction();
                       fragmentTransaction.replace(R.id.fragment_layout,fragment);
                       fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                       fragmentTransaction.commit();
                       lockDrawer();
                   }     }              break;

               case R.id.nav_enquiry:
                   if (item.isChecked())
                   {closeDrawer();
                       //Toast.makeText(activity, "IS CHECKEDDDDD.....", Toast.LENGTH_SHORT).show();
                   }
                   else{
                   fragment = new Enquiry_Fragment();
                   fragmentmanager = getSupportFragmentManager();
//                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                //   fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                //   fragmentTransaction.commit();
                   fragmentTransaction.commitNow();
//                   fragment.getActivity().finish();
                   closeDrawer(); }
                   break;

               case R.id.nav_support:
                   if (item.isChecked())
                   {closeDrawer();
                   }
                   else {
                   fragment = new Support_fragment();
                   bn.putString("Desc","Support");
                   fragment.setArguments(bn);
                   fragmentmanager = getSupportFragmentManager();
//                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                //   fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                //   fragmentTransaction.commit();
                   fragmentTransaction.commitNow();
//                   fragment.getActivity().finish();
                   lockDrawer();
                   closeDrawer(); }
                   break;

               case R.id.nav_aboutus:
                   if (item.isChecked())
                   {closeDrawer();
                   }
                   else
                   {
                   fragment = new Support_fragment();
                   bn.putString("Desc","About");
                   fragment.setArguments(bn);
                   fragmentmanager = getSupportFragmentManager();
//                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                //   fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
               //    fragmentTransaction.commit();
                   fragmentTransaction.commitNow();
//                   fragment.getActivity().finish();
                   lockDrawer();
                   closeDrawer(); }
                   break;

               case R.id.nav_tnc:
                   if (item.isChecked())
                   {closeDrawer();
                   }
                   else
                   {
                   fragment = new Support_fragment();
                   bn.putString("Desc","Terms");
                   fragment.setArguments(bn);
                   fragmentmanager = getSupportFragmentManager();
//                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
              //     fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                ///   fragmentTransaction.commit();
                   fragmentTransaction.commitNow();
//                   fragment.getActivity().finish();
                   lockDrawer();
                   closeDrawer();  }
                   break;

               case R.id.nav_feedback:
                   if (item.isChecked())
                   {closeDrawer();
                   }
                   else
                   {
                   fragment = new FeedBack_fragment();
                   fragmentmanager = getSupportFragmentManager();
//                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                   //     fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                   ///   fragmentTransaction.commit();
                   fragmentTransaction.commitNow();
//                   fragment.getActivity().finish();
                   lockDrawer();
                   closeDrawer(); }
                   break;

               case R.id.nav_login:
                   if (item.isChecked())
                   {closeDrawer();
                   }
                   else
                   {
                   Intent in = new Intent(MainActivity_drawer.this, Login_activity.class);
                   startActivity(in); }
                   break;

               case R.id.nav_logout:
                   if (item.isChecked())
                   {closeDrawer();
                   }
                   else{
                   sessionManagement.logoutSession();
                   Toast.makeText(MainActivity_drawer.this, "Logged Out...", Toast.LENGTH_SHORT).show();
                   check_Login_Status(); }
                   break;

               case R.id.nav_payhistory:
                   if (item.isChecked())
                   {closeDrawer();
                   }else
                   {
                   Toast.makeText(activity, "Coming Soon.....", Toast.LENGTH_SHORT).show(); }
                   break;

               case R.id.nav_orderphone:
                   if (item.isChecked())
                   {closeDrawer();
                   }
                   else {
                   try {
                   Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppPrefrences.getOrderPhone(activity)));
                   startActivity(intent);}
                   catch (Exception ex)
                   {   ex.printStackTrace();
                       Toast.makeText(activity, "Error No Sim Card Found......", Toast.LENGTH_SHORT).show();
                           } }                  break;

               case R.id.nav_share:
                   if (item.isChecked())
                   {closeDrawer();             }
                   else
                   {    shareApp();  }
                    break;
                 }
                 return true;
                } }
        );
        }

    public void shareApp() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }



    class GetOrder_Phone extends AsyncTask<String, String, String>
    {
        String output = "", url;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                url = "https://www.lotusenterprises.net/manpower/Api/get_contact";
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", url);
            output = HttpHandler.makeServiceCall(url);
            //   Log.e("getcomment_url", output);
            System.out.println("ORDER ONPHONE RESPONSE " + output);
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
                    JSONObject job = obj.getJSONObject("data");

                    String id = job.getString("id");
                    String number = job.getString("contact");
                    String date = job.getString("date");

                    AppPrefrences.setOrderPhone(activity,number);
                    Log.e("ORDER ONPHONE RESPONSE ", "------------"+number+"-------------------------------------");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);
        }
    }


    private class CheckStatus extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
//            dialog = new ProgressDialog(MainActivity_drawer.this);
//            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://www.lotusenterprises.net/manpower/Api/check_block_user");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", AppPrefrences.getUserid(MainActivity_drawer.this));

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
//                dialog.dismiss();

                // JSONObject jsonObject = null;
                Log.e("SendJsonDataToServer>>>", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Boolean responce = jsonObject.getBoolean("responce");
                    Log.e("status" , "   "+responce);
                    if(!responce){
                        sessionManagement.logoutSession();
                   //     handler.removeCallbacks(r);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }
}

