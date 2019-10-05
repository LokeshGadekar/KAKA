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
import com.info.manPower.Fragment.Home_fragment;
import com.info.manPower.Fragment.Profile_fragment;
import com.info.manPower.Fragment.Support_fragment;
import com.info.manPower.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
//        fragmentTransaction.addToBackStack(null);
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
        super.onBackPressed();
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
                   fragment = new Home_fragment();
                   fragmentmanager = getSupportFragmentManager();
                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                   fragmentTransaction.commit();

                   closeDrawer();
                   break;

              case R.id.nav_profile:

                  if(!sessionManagement.isLoggedIn())
                  {     Intent in = new Intent(MainActivity_drawer.this, Login_activity.class);
                        startActivity(in); }

                  else {
                  fragment = new Profile_fragment();
                  fragmentmanager = getSupportFragmentManager();
                      fragmentmanager.popBackStack();
                  fragmentTransaction = fragmentmanager.beginTransaction();
                  fragmentTransaction.replace(R.id.fragment_layout,fragment);
                  fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                  fragmentTransaction.commit();
//                      fragment.getActivity().finish();
                  lockDrawer();
                  closeDrawer(); }
                  break;

               case R.id.nav_booking:
                   if (!sessionManagement.isLoggedIn())
                   { Intent in = new Intent(MainActivity_drawer.this, Login_activity.class);
                       startActivity(in);   }
                   else
                   { fragment = new Booking_fragment();
                       fragmentmanager = getSupportFragmentManager();
                       fragmentmanager.popBackStack();
                       fragmentTransaction = fragmentmanager.beginTransaction();
                       fragmentTransaction.replace(R.id.fragment_layout,fragment);
//                       fragmentTransaction.addToBackStack(null);
                       fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                       fragmentTransaction.commit();
//                       fragment.getActivity().finish();
                       lockDrawer();
                   }                   break;

               case R.id.nav_enquiry:
                   fragment = new Enquiry_Fragment();
                   fragmentmanager = getSupportFragmentManager();
                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                   fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                   fragmentTransaction.commit();
//                   fragment.getActivity().finish();
                   closeDrawer();
                   break;

               case R.id.nav_support:
                   fragment = new Support_fragment();
                   bn.putString("Desc","Support");
                   fragment.setArguments(bn);
                   fragmentmanager = getSupportFragmentManager();
                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                   fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                   fragmentTransaction.commit();
//                   fragment.getActivity().finish();
                   lockDrawer();
                   closeDrawer();
                   break;

               case R.id.nav_aboutus:
                   fragment = new Support_fragment();
                   bn.putString("Desc","About");
                   fragment.setArguments(bn);
                   fragmentmanager = getSupportFragmentManager();
                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                   fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                   fragmentTransaction.commit();
//                   fragment.getActivity().finish();
                   lockDrawer();
                   closeDrawer();
                   break;

               case R.id.nav_tnc:
                   fragment = new Support_fragment();
                   bn.putString("Desc","Terms");
                   fragment.setArguments(bn);
                   fragmentmanager = getSupportFragmentManager();
                   fragmentmanager.popBackStack();
                   fragmentTransaction = fragmentmanager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_layout,fragment);
                   fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                   fragmentTransaction.commit();
//                   fragment.getActivity().finish();
                   lockDrawer();
                   closeDrawer();
                   break;

               case R.id.nav_login:
                   Intent in = new Intent(MainActivity_drawer.this, Login_activity.class);
                   startActivity(in);
                   break;

               case R.id.nav_logout:
                   sessionManagement.logoutSession();
                   Toast.makeText(MainActivity_drawer.this, "Logged Out...", Toast.LENGTH_SHORT).show();
                   finish();
                   break;

               case R.id.nav_payhistory:
                   Toast.makeText(activity, "Coming Soon.....", Toast.LENGTH_SHORT).show();
                   break;

               case R.id.nav_orderphone:
                   try {
                   Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppPrefrences.getOrderPhone(activity)));
                   startActivity(intent);}
                   catch (Exception ex)
                   {   ex.printStackTrace();
                       Toast.makeText(activity, "Error No Sim Card Found......", Toast.LENGTH_SHORT).show();
                                          }                   break;

               case R.id.nav_share:
                   shareApp();
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
}

