package com.narmware.vvmexam.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.fragment.ConfirmFragment;
import com.narmware.vvmexam.fragment.MobileVarifyFragment;
import com.narmware.vvmexam.fragment.PersonalInfoFragment;
import com.narmware.vvmexam.fragment.SelectLocationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    String preffExamState,preffExamCity;

    /*pos 0 : select location
    pos 1 : select personal info
    pos 2 : mobile varfication
    pos 3 : confirm all details
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pagerCount<mViewPager.getChildCount()) {
                    validData=0;


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
                    }

                    if(pagerCount==1)
                    {
                        mName=PersonalInfoFragment.mEdtName.getText().toString().trim();
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
                        mViewPager.setCurrentItem(pagerCount + 1);
                        pagerCount++;
                    }
                }
            }
        });

        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pagerCount>0) {

                    mViewPager.setCurrentItem(pagerCount - 1);
                    pagerCount--;
                }
            }
        });
    }

    private void init() {
        ButterKnife.bind(this);

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
                int currentProgress = mProgressBar.getProgress();
                int progress = currentProgress + 30;
                final ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, progress);
                progressAnimator.setDuration(500);
                progressAnimator.setInterpolator(new LinearInterpolator());
                progressAnimator.start();

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

}
