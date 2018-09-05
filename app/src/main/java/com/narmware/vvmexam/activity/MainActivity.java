package com.narmware.vvmexam.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.narmware.vvmexam.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_username) EditText mEdtUsername;
    @BindView(R.id.edt_password) EditText mEdtPassword;
    @BindView(R.id.btn_forgot) Button mBtnForgot;
    @BindView(R.id.btn_login) Button mBtnLogin;
    @BindView(R.id.txt_register) TextView mTxtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        ButterKnife.bind(this);

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
                break;

            case R.id.txt_register:
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
