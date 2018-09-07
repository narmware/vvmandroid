package com.narmware.vvmexam.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.dialogs.TnCDialogFragment;
import com.narmware.vvmexam.fragment.ConfirmFragment;
import com.narmware.vvmexam.fragment.MobileVarifyFragment;
import com.narmware.vvmexam.fragment.PersonalInfoFragment;
import com.narmware.vvmexam.fragment.SelectLocationFragment;
import com.narmware.vvmexam.pojo.Register;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.EndPoints;
import com.narmware.vvmexam.support.SharedPreferencesHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity implements SelectLocationFragment.OnFragmentInteractionListener,
PersonalInfoFragment.OnFragmentInteractionListener,MobileVarifyFragment.OnFragmentInteractionListener,ConfirmFragment.OnFragmentInteractionListener{

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
                        mName=PersonalInfoFragment.mEdtName.getText().toString().trim();
                        SharedPreferencesHelper.setUserName(mName,RegisterActivity.this);

                        if(mName==null || mName.isEmpty())
                        {
                            validData=1;
                            PersonalInfoFragment.mEdtName.setError("Enter your name");
                        }
                        if(PersonalInfoFragment.mState==null || PersonalInfoFragment.mState.isEmpty())
                        {
                            validData=1;
                            Toast.makeText(RegisterActivity.this, "Please select state", Toast.LENGTH_SHORT).show();
                        }
                        if(PersonalInfoFragment.mCity==null || PersonalInfoFragment.mCity.isEmpty())
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
                            ConfirmFragment.mEdtOtp.setError("Enter OTP");
                        }
                    }

                    if(validData==0) {

                        if(pagerCount==3)
                        {
                            RegisterUser();

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
                            //Toast.makeText(RegisterActivity.this, "Registartion Successfull", Toast.LENGTH_SHORT).show();
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
        pagerAdapter.addFragment(new PersonalInfoFragment(),"Personal Info");
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

    public void RegisterUser() {
        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setTitle(Constants.PLEASE_WAIT);
        dialog.setMessage(Constants.REGISTER_DIALOG_TITLE);
        dialog.setCancelable(false);
        dialog.show();

        Register register=new Register();
        register.setState(SelectLocationFragment.mState);
        register.setDistrict(SelectLocationFragment.mDistrict);
        register.setCity(SelectLocationFragment.mCity);
        register.setName(mName);
        register.setPassword(mPassword);
        register.setMobile(mMobile);
        register.setExamState(PersonalInfoFragment.mState);
        register.setExamCity(PersonalInfoFragment.mCity);

        Gson gson=new Gson();
        String json_string =gson.toJson(register);
        Log.e("Json_string",json_string);

      /*  String url= EndPoints.USER_LOGIN;

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                           // Log.e("Cat Json_string",response.toString());
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
        mVolleyRequest.add(obreq);*/
    }

    public void showNoConnectionDialog() {
        mNoConnectionDialog.setContentView(R.layout.dialog_no_internet);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button tryAgain = mNoConnectionDialog.findViewById(R.id.txt_retry);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });
    }
}
