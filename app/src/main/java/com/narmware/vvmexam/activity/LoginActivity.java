package com.narmware.vvmexam.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.pojo.Login;
import com.narmware.vvmexam.pojo.LoginResponse;
import com.narmware.vvmexam.pojo.StateCoordDetails;
import com.narmware.vvmexam.pojo.StateCoordResponse;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.EndPoints;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_username) EditText mEdtUsername;
    @BindView(R.id.edt_password) EditText mEdtPassword;
    @BindView(R.id.btn_forgot) Button mBtnForgot;
    @BindView(R.id.btn_login) Button mBtnLogin;
    @BindView(R.id.txt_register) TextView mTxtRegister;
    @BindView(R.id.lin_register) LinearLayout mLinearRegister;
    @BindView(R.id.btn_help) ImageButton mImgBtnHelp;
    @BindView(R.id.rootview) NestedScrollView mRootView;

    String username,password;
    int validData=0;

    public Dialog mNoConnectionDialog;
    public RequestQueue mVolleyRequest;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        ButterKnife.bind(this);
        realm=Realm.getInstance(LoginActivity.this);

        mVolleyRequest = Volley.newRequestQueue(LoginActivity.this);
        mNoConnectionDialog = new Dialog(LoginActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        mEdtUsername.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                String s=arg0.toString();
                if(!s.equals(s.toUpperCase()))
                {
                    s=s.toUpperCase();
                    mEdtUsername.setText(s);
                    mEdtUsername.setSelection(s.length());
                }
            }
        });

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
                    LoginUser();
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

        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle(Constants.PLEASE_WAIT);
        dialog.setMessage(Constants.LOGIN_DIALOG_TITLE);
        dialog.setCancelable(false);
        dialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("RESPONSE",response);

                        try {
                            Gson gson = new Gson();
                            LoginResponse dataResponse = gson.fromJson(response, LoginResponse.class);
                            Login data = dataResponse.getResult();
                            if (dataResponse.getStatus_code().equals(Constants.ERROR)) {
                                Toast.makeText(LoginActivity.this, dataResponse.getError_message(), Toast.LENGTH_SHORT).show();
                            }
                            if (dataResponse.getStatus_code().equals(Constants.SUCCESS)) {
                                SharedPreferencesHelper.setIsLogin(true, LoginActivity.this);
                                realm.beginTransaction();
                                realm.copyToRealm(data);
                                realm.commitTransaction();

                                GetStateCoordinators(data.getState_id());
                                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            dialog.dismiss();
                        }catch (Exception e)
                        {e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("RESPONSE ERR","That didn't work!");
                dialog.dismiss();
                showNoConnectionDialog();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.LOGIN);
                params.put(Constants.USERNAME,username);
                params.put(Constants.PASSWORD,password);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    private void GetStateCoordinators(final String state_id) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.e("STATE CO RESPONSE",response);

                        Gson gson=new Gson();
                        StateCoordResponse dataResponse = gson.fromJson(response, StateCoordResponse.class);
                        StateCoordDetails[] codata = dataResponse.getResult();

                        if (dataResponse.getStatus_code().equals(Constants.ERROR)) {
                            Toast.makeText(LoginActivity.this, dataResponse.getError_message(), Toast.LENGTH_SHORT).show();
                        }
                        if (dataResponse.getStatus_code().equals(Constants.SUCCESS)) {

                            for(StateCoordDetails item:codata) {

                                realm.beginTransaction();
                                realm.copyToRealm(item);
                                realm.commitTransaction();
                            }

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
                params.put("param", Constants.GET_COORDINATOR_DETAILS);
                params.put(Constants.STATE_ID,state_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }


    public void showNoConnectionDialog(){
        Snackbar.make(mRootView, getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT)
                /*.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),10);
                    }
                })*/
                //.setActionTextColor(Color.RED)
                .show();
    }
}
