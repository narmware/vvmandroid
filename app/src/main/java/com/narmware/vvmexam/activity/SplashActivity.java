package com.narmware.vvmexam.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.db.RealmController;
import com.narmware.vvmexam.pojo.AppVersionResponse;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.EndPoints;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends AppCompatActivity {

    static int TIMEOUT=2000;
    public RequestQueue mVolleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mVolleyRequest = Volley.newRequestQueue(SplashActivity.this);
        CheckVersion();

    }

    private void CheckVersion() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("VERSION RESPONSE",response);

                        Gson gson=new Gson();
                        AppVersionResponse dataResponse=gson.fromJson(response,AppVersionResponse.class);
                        if(dataResponse.getStatus_code().equals(Constants.SUCCESS))
                        {
                            if(Constants.APP_VERSION.equals(dataResponse.getVersion_name()))
                            {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(SharedPreferencesHelper.getIsLogin(SplashActivity.this)==false)
                                        {
                                            Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
                                            startActivity(intent);
                                        }
                                        finish();
                                    }
                                },TIMEOUT);

                            }
                            else{

                                SweetAlertDialog sweetAlertDialog= new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.WARNING_TYPE);
                                sweetAlertDialog.setCancelable(false);
                                sweetAlertDialog.setTitleText("APP UPDATE AVAILABLE")
                                        .setContentText(getResources().getString(R.string.update_app))
                                        .setConfirmText("Update Now")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                            }
                                        })
                                        .showCancelButton(true)
                                        .setCancelText("Exit")
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        })

                                        .show();


                              //  Toast.makeText(SplashActivity.this, "Update app", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(SharedPreferencesHelper.getIsLogin(SplashActivity.this)==false)
                        {
                            Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                },TIMEOUT);

            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.VERSION_NAME);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

}
