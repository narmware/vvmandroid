package com.narmware.vvmexam;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.narmware.vvmexam.support.SharedPreferencesHelper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {
    int count = 0;

    public static void config_realm(Context context) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);


    }

    public static String getLocation(final Activity activity) throws IOException {
        final Double[] latitude = new Double[1];
        final Double[] longitude= new Double[1];
        final String[] address = {null};
        final String[] city = {null};

        LocationManager mLocationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //your code here

                latitude[0] =location.getLatitude();
                longitude[0] =location.getLongitude();

                try {
                    Geocoder geocoder;
                    List<Address> addresses = new ArrayList<>();
                    geocoder = new Geocoder(activity, Locale.getDefault());

                    addresses = geocoder.getFromLocation(latitude[0], longitude[0], 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    address[0] = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    city[0] = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    Log.e("Current location", address[0]);
                }catch (Exception e)
                {

                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,
                0, mLocationListener);

       return address[0] +""+ city[0];
    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String macAddress()    {
        String macAddress = "N/A";

        try {
            // get all the interfaces
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            //find network interface wlan0
            for (NetworkInterface networkInterface : all) {
                if (!networkInterface.getName().equalsIgnoreCase("wlan0")) continue;
                //get the hardware address (MAC) of the interface
                byte[] macBytes = networkInterface.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }


                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //gets the last byte of b
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                macAddress= res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return macAddress;
    }


    public static String ipAddress() {
        String ipAddress = "N/A";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = inetAddress.getHostAddress().toString();
                    }
                }
            }

        } catch (SocketException ex) {
        }

        return ipAddress;
    }

    public static String imei(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        System.out.println("IMEI::" + telephonyManager.getDeviceId());
        return telephonyManager.getDeviceId();
    }

    public static String getCurrentDate() {

        Calendar calander = Calendar.getInstance();
        long cDay = calander.get(Calendar.DAY_OF_MONTH);
        long cMonth = calander.get(Calendar.MONTH) + 1;
        long cYear = calander.get(Calendar.YEAR);
        long cHour = calander.get(Calendar.HOUR);
        long cMinute = calander.get(Calendar.MINUTE);
        long cSecond = calander.get(Calendar.AM_PM);
        String am_pm = null;
        if (cSecond == 1) {
            am_pm = "pm";
        }
        if (cSecond == 0) {
            am_pm = "am";
        }
        String current_time = cDay + "-" + cMonth + "-" + cYear + " at " + cHour + ":" + cMinute + " " + am_pm;
        Log.e("Current date",current_time);

        return current_time;
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
