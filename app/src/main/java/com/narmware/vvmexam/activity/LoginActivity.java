package com.narmware.vvmexam.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.fragment.PersonalInfoFragment;
import com.narmware.vvmexam.pojo.States;
import com.narmware.vvmexam.pojo.StatesResponse;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.EndPoints;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_username) EditText mEdtUsername;
    @BindView(R.id.edt_password) EditText mEdtPassword;
    @BindView(R.id.btn_forgot) Button mBtnForgot;
    @BindView(R.id.btn_login) Button mBtnLogin;
    @BindView(R.id.txt_register) TextView mTxtRegister;
    @BindView(R.id.lin_register) LinearLayout mLinearRegister;
    @BindView(R.id.btn_help) ImageButton mImgBtnHelp;

    String username,password;
    int validData=0;

    public Dialog mNoConnectionDialog;
    public RequestQueue mVolleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        mVolleyRequest = Volley.newRequestQueue(LoginActivity.this);
        mNoConnectionDialog = new Dialog(LoginActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        mBtnForgot.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTxtRegister.setOnClickListener(this);
        mLinearRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_forgot:
                break;

            case R.id.btn_login:
                validData=0;

                username=mEdtUsername.getText().toString().trim();
                password=mEdtPassword.getText().toString().trim();

                if(username==null || username.isEmpty())
                {
                    validData=1;
                    mEdtUsername.setError("Enter Username");
                }
                if(password==null || password.isEmpty())
                {
                    validData=1;
                    mEdtPassword.setError("Enter Password");
                }

                if(validData==0)
                {
                    Toast.makeText(this, "Valid data", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.txt_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.lin_register:
                Intent intentReg=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intentReg);
                break;
        }
    }

    private void LoginUser() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        Gson gson=new Gson();

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
                params.put("param", Constants.LOGIN);
                params.put(Constants.MOBILE_NUMBER,username);
                params.put(Constants.PASSWORD,password);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    private void ForgetPassword() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        Gson gson=new Gson();

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
                params.put("param", Constants.LOGIN);
                params.put(Constants.MOBILE_NUMBER,username);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }


    public void showNoConnectionDialog() {
        mNoConnectionDialog.setContentView(R.layout.dialog_no_internet);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button tryAgain = mNoConnectionDialog.findViewById(R.id.txt_retry);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
    }
}
