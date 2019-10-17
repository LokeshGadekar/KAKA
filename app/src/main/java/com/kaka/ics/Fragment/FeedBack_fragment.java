package com.kaka.ics.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.kaka.ics.API_retro.API_parameter;
import com.kaka.ics.AppUtils.BaseUrl;
import com.kaka.ics.Model.single_responce;
import com.kaka.ics.R;
import com.kaka.ics.View.MainActivity_drawer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBack_fragment extends Fragment
{
    Toolbar toolbar;
    TextView txToolbar, cart_count;
    ImageView imgToolbar,cart;
    EditText edname, edmobile, edaddr, edmail;
    Button submit;
    String name , mobile , mail , description;
    API_parameter ApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     View view = inflater.inflate(R.layout.fragment_feedback,container,false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txToolbar = (TextView)view.findViewById(R.id.txToolbar);
        imgToolbar = (ImageView) view.findViewById(R.id.imgToolbar);
        cart = view.findViewById(R.id.icon_cart);
        cart_count = view.findViewById(R.id.cart_count);

        edname = (EditText) view.findViewById(R.id.ed_nname);
        edaddr = (EditText) view.findViewById(R.id.ed_addr);
        edmobile = (EditText) view.findViewById(R.id.ed_phonenu);
        edmail = (EditText) view.findViewById(R.id.ed_emaill);
        submit = (Button) view.findViewById(R.id.b_submit);

        cart.setVisibility(View.GONE);
        cart_count.setVisibility(View.GONE);

        ApiService = BaseUrl.getAPIService();

        txToolbar.setText("Feedback");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity_drawer)getActivity()).onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                {
                    FEEDBACK();
                }
                else
                {

                }
            }
        });

     return view;
    }

    private boolean validate()
    {
     name = edname.getText().toString().trim();
     mobile = edmobile.getText().toString().trim();
     mail = edmail.getText().toString().trim();
     description = edaddr.getText().toString().trim();

     if (name.matches("") && mobile.matches("") && mail.matches("") && description.matches(""))
     {
         Toast.makeText(getActivity(), "Please fill data correctly...", Toast.LENGTH_SHORT).show();
         return false;
     }
     else if(name.matches(""))
     {
         Toast.makeText(getActivity(), "Please fill name field", Toast.LENGTH_SHORT).show();  return false;
     }
     else if(mobile.matches(""))
     {
         Toast.makeText(getActivity(), "Please fill mobile field", Toast.LENGTH_SHORT).show();  return false;
     }
     else if(mail.matches(""))
     {
         Toast.makeText(getActivity(), "Please fill e-mail field", Toast.LENGTH_SHORT).show();  return false;
     }
     else if(description.matches(""))
     {
         Toast.makeText(getActivity(), "Please fill address field", Toast.LENGTH_SHORT).show();  return false;
     }
    else
     {
         return  true;
     }
    }


    private void FEEDBACK()
    {
        ApiService.FEEDBACK_CALL(name,mobile,mail,description).enqueue(new Callback<single_responce>() {
            @Override
            public void onResponse(Call<single_responce> call, Response<single_responce> response) {
                Log.e("FEEDBACK_CALL RESPONSE.", "" + new Gson().toJson(response.body()));
                Log.e("FEEDBACK_CALL RESPONSE.", "-------------------------------------------------");
                if (response.body().getResponce()) {
                    Toast.makeText(getActivity(), "Feedback Submitted...", Toast.LENGTH_SHORT).show();
                    clear_ed();
                }
                else if (!response.body().getResponce())
                {
                    Toast.makeText(getActivity(), "No Data( false )", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Error while Connecting...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<single_responce> call, Throwable t) {
                Log.e("error at call", "" + t.getLocalizedMessage());
                Log.e("error at call", "" + t.getMessage());
                Log.e("error at call", "" + t.getCause());
            }
        });
    }

    private void clear_ed()
    {
        edname.getText().clear();
        edmail.getText().clear();
        edmobile.getText().clear();
        edaddr.getText().clear();
    }

}
