package com.info.manPower.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.info.manPower.AppUtils.AppPrefrences;
import com.info.manPower.R;

public class Splash_activity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        Thread backgound = new Thread(){
            public void run()
            { try{
                sleep(2 * 1000);
                go_next();

            }      catch(Exception e)
            {      e.printStackTrace();            } }
        };

        backgound.start();
    }
    private void go_next()
    {
        if (AppPrefrences.getLocation(Splash_activity.this).matches(""))
        {
            Intent in = new Intent(Splash_activity.this, Location_activity.class);
            startActivity(in);
            finish();
        }
        else {
            Intent in = new Intent(Splash_activity.this, MainActivity_drawer.class);
            startActivity(in);
            finish();
        }
    }

}
