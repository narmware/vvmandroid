package com.narmware.vvmexam.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvigilatorActivity extends AppCompatActivity {
@BindView(R.id.btn_submit) Button mBtnSubmit;
    @BindView(R.id.edt_key) EditText mEdtKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invigilator);

        init();
    }

    private void init() {

        ButterKnife.bind(this);

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key=mEdtKey.getText().toString();
                if(key.equals(""))
                {
                    mEdtKey.setError("Please enter key");
                }
                else {
                    SharedPreferencesHelper.setInvKey(key,InvigilatorActivity.this);
                    Intent intent = new Intent(InvigilatorActivity.this, DemoActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
