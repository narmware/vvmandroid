package com.narmware.vvmexam.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.fragment.PersonalInfoFragment;
import com.narmware.vvmexam.support.EndPoints;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_username) EditText mEdtUsername;
    @BindView(R.id.edt_password) EditText mEdtPassword;
    @BindView(R.id.btn_forgot) Button mBtnForgot;
    @BindView(R.id.btn_login) Button mBtnLogin;
    @BindView(R.id.txt_register) TextView mTxtRegister;

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
        }
    }

    public void GetCategories() {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Getting Categories...");
        dialog.setCancelable(false);
        dialog.show();

        String url= EndPoints.USER_LOGIN;

        Log.e("Cat url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            Log.e("Cat Json_string",response.toString());
                            Gson gson = new Gson();

                        } catch (Exception e) {

                            e.printStackTrace();
                            dialog.dismiss();
                        }
                        if(mNoConnectionDialog.isShowing()==true)
                        {
                            mNoConnectionDialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                        dialog.dismiss();
                        showNoConnectionDialog();
                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

    public void showNoConnectionDialog() {
        mNoConnectionDialog.setContentView(R.layout.dialog_no_internet);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button tryAgain = mNoConnectionDialog.findViewById(R.id.txt_retry);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
