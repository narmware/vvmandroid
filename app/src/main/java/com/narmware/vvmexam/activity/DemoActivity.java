package com.narmware.vvmexam.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.pojo.StatesResponse;
import com.narmware.vvmexam.pojo.States;
import com.narmware.vvmexam.support.EndPoints;

import java.util.HashMap;
import java.util.Map;

public class DemoActivity extends AppCompatActivity {
    RequestQueue mVolleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mVolleyRequest = Volley.newRequestQueue(DemoActivity.this);
        getStates();
    }

    private void getStates() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        Gson gson=new Gson();
                        StatesResponse dataResponse=gson.fromJson(response,StatesResponse.class);
                        States[] array=dataResponse.getResult();

                        for(States item:array)
                        {
                            Log.e("State name",item.getState_name());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESPONSE ERR","That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", "state");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

}
