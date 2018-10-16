package com.narmware.vvmexam.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.narmware.vvmexam.fragment.ConfirmFragment;
import com.narmware.vvmexam.fragment.ExamInfoFragment;
import com.narmware.vvmexam.fragment.MobileVarifyFragment;
import com.narmware.vvmexam.fragment.SelectLocationFragment;
import com.narmware.vvmexam.pojo.OtpResponse;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.EndPoints;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity implements SelectLocationFragment.OnFragmentInteractionListener,
ExamInfoFragment.OnFragmentInteractionListener,MobileVarifyFragment.OnFragmentInteractionListener,ConfirmFragment.OnFragmentInteractionListener{

    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.progress) ProgressBar mProgressBar;
    @BindView(R.id.btn_previous) Button mBtnPrevious;
    @BindView(R.id.btn_next) Button mBtnNext;

    private PagerAdapter pagerAdapter;
    int pagerCount=0;
    int validData=0;

    String mName,mMobile,mPassword,mOtp;

    public Dialog mNoConnectionDialog;
    public RequestQueue mVolleyRequest;
    String otp;
    /*pos 0 : select location
    pos 1 : select personal info
    pos 2 : mobile varfication
    pos 3 : confirm all details
    */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pagerCount<=mViewPager.getAdapter().getCount()) {
                    validData=0;
                    int currentProgress = mProgressBar.getProgress();
                    int progress = currentProgress + 25;
                    final ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, progress);
                    progressAnimator.setDuration(500);
                    progressAnimator.setInterpolator(new LinearInterpolator());

                    if(pagerCount==0)
                    {
                        if(SelectLocationFragment.mState==null || SelectLocationFragment.mState.isEmpty())
                        {
                            validData=1;
                            Toast.makeText(RegisterActivity.this, "Please select state", Toast.LENGTH_SHORT).show();
                        }
                        if(SelectLocationFragment.mCity==null || SelectLocationFragment.mCity.isEmpty())
                        {
                            validData=1;
                            Toast.makeText(RegisterActivity.this, "Please select city", Toast.LENGTH_SHORT).show();
                        }
                        if(SelectLocationFragment.mDistrict==null || SelectLocationFragment.mDistrict.isEmpty())
                        {
                            validData=1;
                            Toast.makeText(RegisterActivity.this, "Please select district", Toast.LENGTH_SHORT).show();
                        }
                        if(SharedPreferencesHelper.getIsTcAccpted(RegisterActivity.this)==false)
                        {
                            validData=1;
                            Toast.makeText(RegisterActivity.this, "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(pagerCount==1)
                    {

                        if(ExamInfoFragment.mState==null || ExamInfoFragment.mState.isEmpty())
                        {
                            validData=1;
                            Toast.makeText(RegisterActivity.this, "Please select state", Toast.LENGTH_SHORT).show();
                        }
                        if(ExamInfoFragment.mCity==null || ExamInfoFragment.mCity.isEmpty())
                        {
                            validData=1;
                            Toast.makeText(RegisterActivity.this, "Please select city", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(pagerCount==2)
                    {
                        mMobile=MobileVarifyFragment.mEdtMobile.getText().toString().trim();
                        mPassword=MobileVarifyFragment.mEdtPassword.getText().toString().trim();

                        SharedPreferencesHelper.setUserMobile(mMobile,RegisterActivity.this);
                        SharedPreferencesHelper.setUserPassword(mPassword,RegisterActivity.this);

                        if(mMobile.length()<10)
                        {
                            validData=1;
                            MobileVarifyFragment.mEdtMobile.setError("Enter valid mobile number");
                        }
                        if(mPassword==null || mPassword.isEmpty()){
                            validData=1;
                            MobileVarifyFragment.mEdtPassword.setError("Enter password");
                        }
                    }

                    if(pagerCount==3)
                    {
                        mOtp=ConfirmFragment.mEdtOtp.getText().toString().trim();

                        if(mOtp==null || mOtp.isEmpty()){
                            validData=1;
                            ConfirmFragment.mEdtOtp.setError("Enter valid OTP");
                        }

                    }

                    if(validData==0) {

                        if(pagerCount==2)
                        {
                            RequestOTP();
                        }
                        if(pagerCount==3)
                        {

                            if(mOtp.equals(otp))
                            {
                                new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Registeration Successfull !")
                                        //.setContentText("Your want to Logout")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                            else{
                                ConfirmFragment.mEdtOtp.setError("Enter valid OTP");
                            }
                           /* if(ConfirmFragment.mEdtOtp.getText().toString().trim().equals())
                            {
                                new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Registeration Successfull !")
                                        //.setContentText("Your want to Logout")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .show();
                            }*/

                            }
                        else{
                            progressAnimator.start();
                            mViewPager.setCurrentItem(pagerCount + 1);
                            pagerCount++;
                        }

                    }
                }
            }
        });

        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pagerCount==0)
                {
                    finish();
                }

                if(pagerCount>0) {

                    int currentProgress = mProgressBar.getProgress();
                    int progress = currentProgress - 25;
                    final ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, progress);
                    progressAnimator.setDuration(500);
                    progressAnimator.setInterpolator(new LinearInterpolator());

                    mViewPager.setCurrentItem(pagerCount - 1);
                    pagerCount--;
                    progressAnimator.start();
                }


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void init() {
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(RegisterActivity.this);
        mNoConnectionDialog = new Dialog(RegisterActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        pagerAdapter.addFragment(new SelectLocationFragment(),"Select Location");
        pagerAdapter.addFragment(new ExamInfoFragment(),"Personal Info");
        pagerAdapter.addFragment(new MobileVarifyFragment(),"Mobile Varification");
        pagerAdapter.addFragment(new ConfirmFragment(),"Confirm");

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0)
                {
                    mBtnPrevious.setText("Cancel Registration");
                    mBtnNext.setText("Next Step");
                }
                if (position == 1)
                {
                    mBtnPrevious.setText("Previous Step");
                    mBtnNext.setText("Next Step");
                }

                if (position == 2)
                {
                    mBtnPrevious.setText("Previous Step");
                    mBtnNext.setText("Request OTP");
                }

                if (position == 3)
                {
                    mBtnPrevious.setText("Previous Step");
                    mBtnNext.setText("Confirm");
                }
                //Toast.makeText(RegisterActivity.this, "Pos "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(pagerAdapter);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        List<Fragment> fragments=new ArrayList<>();
        List<String> mFragmentTitleList = new ArrayList<>();

        @Override
        public Fragment getItem(int index) {

            return fragments.get(index);
        }

        public void addFragment(Fragment fragment,String title) {
            fragments.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private void RegisterStudent() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        //Log.e("REGISTER RESPONSE",response);

                        Gson gson=new Gson();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("RESPONSE ERR","That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.REGISTER);
                params.put(Constants.STD_NAME, mName);
                params.put(Constants.STD_MOBILE, mMobile);
                params.put(Constants.PASSWORD, mPassword);
                params.put(Constants.DIST_ID, SelectLocationFragment.mDistrict_id);
                params.put(Constants.CITY_ID, SelectLocationFragment.mCity_id);
                params.put(Constants.STATE_ID, SelectLocationFragment.mState_id);
                params.put(Constants.SCHOOL_STATE_ID,"1");
                params.put(Constants.SCHOOL_CITY_ID, "1");
                params.put(Constants.SCHOOL_DIST_ID,"1");

                return params;
            }
        };
        // Add the request to the RequestQueue.
        mVolleyRequest.add(stringRequest);
    }

    private void RequestOTP() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        //Log.e("REGISTER RESPONSE",response);

                        Gson gson=new Gson();
                        OtpResponse dataResponse=gson.fromJson(response,OtpResponse.class);
                        String res=dataResponse.getResult();
                        String[] separated = res.split(" - ");
                        otp = separated[1];
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("RESPONSE ERR","That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key","VVM");
                params.put("param", Constants.OTP);
                params.put(Constants.MOBILE_NUMBER, mMobile);

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
            }
        });
    }
}
