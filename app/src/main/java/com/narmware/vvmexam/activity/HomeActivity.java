package com.narmware.vvmexam.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.broadcast.SingleUploadBroadcastReceiver;
import com.narmware.vvmexam.fragment.ExamCenterFragment;
import com.narmware.vvmexam.fragment.ProfilesFragment;
import com.narmware.vvmexam.fragment.NotificationFragment;
import com.narmware.vvmexam.fragment.OtherFragment;
import com.narmware.vvmexam.fragment.SchoolProfileFragment;
import com.narmware.vvmexam.fragment.StudentProfileFragment;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.SharedPreferencesHelper;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class HomeActivity extends AppCompatActivity implements ProfilesFragment.OnFragmentInteractionListener,StudentProfileFragment.OnFragmentInteractionListener,
        SchoolProfileFragment.OnFragmentInteractionListener,NotificationFragment.OnFragmentInteractionListener,OtherFragment.OnFragmentInteractionListener,ExamCenterFragment.OnFragmentInteractionListener
    ,SingleUploadBroadcastReceiver.Delegate
{

    private TextView mTextMessage;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_notification:
                    setFragment(new NotificationFragment(),"Notification");
                    return true;
                case R.id.navigation_profile:
                    setFragment(new ProfilesFragment(),"Edit");
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

        setFragment(new ProfilesFragment(),"Edit");

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Constants.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();

            CropImage.activity(selectedImage)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                //uploadMultipart(picturePath);

                ExamCenterFragment.mImgProf.setImageURI(resultUri);
        }

        if (requestCode == Constants.CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //Log.e("result data",data.getExtras().get("data").toString());

            Uri tempUri = getImageUri(getApplicationContext(), photo);
            File finalFile = new File(getRealPathFromURI(tempUri));
            System.out.println(finalFile);

            String[] filePathColumn = { MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(tempUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //Log.e("Image path",picturePath);

            ExamCenterFragment.mImgProf.setImageBitmap(photo);        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String imagePath = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(imagePath);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

    @Override
    public void onProgress(int progress) {
     /*   dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();*/
        Log.e("Progress",""+progress);
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        Log.e("ServerProgress",uploadedBytes+" ");
    }

    @Override
    public void onError(Exception exception) {
        Log.e("ServerError","Errrrrorrrr!!!!");
        Toast.makeText(this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
     /*   dialog.dismiss();
        Log.e("ServerResponse", new String(serverResponseBody) + "   " + serverResponseCode);
        Gson gson=new Gson();
        ImageUploadResponse imageUploadResponse=gson.fromJson(new String(serverResponseBody),ImageUploadResponse.class);
        SharedPreferencesHelper.setUserProfileImage(imageUploadResponse.getUrl(),MainActivity.this);
        Picasso.with(MainActivity.this)
                .load(imageUploadResponse.getUrl())
                .placeholder(R.drawable.placeholder)
                .into(ProfileFragment.mImgProf);*/
    }

    @Override
    public void onCancelled() {
    }

   /* public void uploadMultipart(String path) {

        String uploadId = UUID.randomUUID().toString();

        uploadReceiver.setDelegate(this);
        uploadReceiver.setUploadID(uploadId);

        //Uploading code
        try {
            //Creating a multi part request
            new MultipartUploadRequest(HomeActivity.this,uploadId, EndPoints.SET_PROFILE_IMG)
                    .addFileToUpload(path, Constants.PROF_IMG) //Adding file
                    .addParameter(Constants.USER_ID, SharedPreferencesHelper.getUserId(MainActivity.this))//Adding text parameter to the request
                    .setMaxRetries(2)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(MainActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/

}
