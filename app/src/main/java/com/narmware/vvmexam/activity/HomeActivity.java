package com.narmware.vvmexam.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.fragment.EditFragment;
import com.narmware.vvmexam.fragment.NotificationFragment;
import com.narmware.vvmexam.fragment.OtherFragment;
import com.narmware.vvmexam.fragment.SchoolProfileFragment;
import com.narmware.vvmexam.fragment.StudentProfileFragment;

public class HomeActivity extends AppCompatActivity implements EditFragment.OnFragmentInteractionListener,StudentProfileFragment.OnFragmentInteractionListener,
        SchoolProfileFragment.OnFragmentInteractionListener,NotificationFragment.OnFragmentInteractionListener,OtherFragment.OnFragmentInteractionListener
{

    private TextView mTextMessage;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_notification:
                    setFragment(new NotificationFragment(),"Notification");
                    return true;
                case R.id.navigation_profile:
                    setFragment(new EditFragment(),"Edit");
                    return true;
                case R.id.navigation_other:
                    setFragment(new OtherFragment(),"Other");
                    return true;
            }
            return false;
        }
    };


    public void setFragment(Fragment fragment, String tag)
    {
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment,tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(new EditFragment(),"Edit");

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
