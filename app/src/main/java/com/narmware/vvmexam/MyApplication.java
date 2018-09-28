package com.narmware.vvmexam;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.narmware.vvmexam.support.SharedPreferencesHelper;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks{
    int count=0;

    public static void config_realm(Context context)
    {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);


    }



    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        //Toast.makeText(activity, "Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResumed(Activity activity) {

        if(SharedPreferencesHelper.getMinimizeCount(activity)==2)
        {
            Toast.makeText(activity, "Exam finished", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        count++;
        SharedPreferencesHelper.setMinimizeCount(count,activity);
        Toast.makeText(activity, "Paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
