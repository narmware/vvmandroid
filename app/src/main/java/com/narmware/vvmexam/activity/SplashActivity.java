package com.narmware.vvmexam.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

public class SplashActivity extends AppCompatActivity {

    static int TIMEOUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

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
}
