package com.kaka.ics.AppUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.kaka.ics.R;

public class Utilview
{
    public static void hidekeyboard(FragmentActivity activity)
    {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView !=null)
        {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void clear_Fragments(FragmentActivity activity)
    {
        FragmentManager fm = activity.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public static ArrayAdapter<String> setupAdapter(Activity activity, Spinner spinner, String[] stringArray, String spinner_title) {

        spinner.setPadding(0,0,0,0);
        String[] adapter_array = new String[0];
        if (stringArray.length > 0) {
            adapter_array = new String[stringArray.length + 1];
            adapter_array[0] = spinner_title;
            for (int i = 0; i < stringArray.length; i++) {
                adapter_array[i + 1] = stringArray[i];
            }
        }
        if (stringArray.length <= 0) {
            adapter_array = new String[1];
            adapter_array[0] = spinner_title;
        }
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity, R.layout.spinner_text_item, adapter_array) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(R.id.textView1);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_text_item);
        spinner.setAdapter(spinnerArrayAdapter);

        return spinnerArrayAdapter;
    }


}
