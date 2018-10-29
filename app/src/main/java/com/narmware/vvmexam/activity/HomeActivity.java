package com.narmware.vvmexam.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.broadcast.SingleUploadBroadcastReceiver;
import com.narmware.vvmexam.db.RealmController;
import com.narmware.vvmexam.fragment.ExamCenterFragment;
import com.narmware.vvmexam.fragment.ExamInfoFragment;
import com.narmware.vvmexam.fragment.ProfilesFragment;
import com.narmware.vvmexam.fragment.NotificationFragment;
import com.narmware.vvmexam.fragment.OtherFragment;
import com.narmware.vvmexam.fragment.StudentProfileFragment;
import com.narmware.vvmexam.pojo.ImageUploadResponse;
import com.narmware.vvmexam.pojo.Login;
import com.narmware.vvmexam.support.Constants;
import com.narmware.vvmexam.support.EndPoints;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class HomeActivity extends AppCompatActivity implements ProfilesFragment.OnFragmentInteractionListener,StudentProfileFragment.OnFragmentInteractionListener,
        ExamCenterFragment.OnFragmentInteractionListener,NotificationFragment.OnFragmentInteractionListener,OtherFragment.OnFragmentInteractionListener,ExamInfoFragment.OnFragmentInteractionListener
    ,SingleUploadBroadcastReceiver.Delegate
{

    @BindView(R.id.btn_exam) Button mBtnExam;
    private TextView mTextMessage;
    Realm realm;
    Dialog dialog;

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

   /*     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }*/

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
        realm=Realm.getInstance(HomeActivity.this);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(new ProfilesFragment(),"Edit");

        mBtnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,InvigilatorActivity.class);
                startActivity(intent);
            }
        });
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
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                File finalFile = new File(getRealPathFromURI(tempUri));
                System.out.println(finalFile);

                String[] filePathColumn = { MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(tempUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Log.e("Image path",picturePath);
                uploadMultipart(picturePath);

                cursor.close();

            } catch (IOException e) {
                e.printStackTrace();
            }



         /*   Uri selectedImage = data.getData();
            Log.e("Image path",selectedImage.toString());

            String[] filePathColumn = { MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();*/
            StudentProfileFragment.mImgProf.setImageURI(resultUri);
        }

        if (requestCode == Constants.CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

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
            uploadMultipart(picturePath);

            //Log.e("Image path",picturePath);

            StudentProfileFragment.mImgProf.setImageBitmap(photo);
        }
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
        dialog = new ProgressDialog(HomeActivity.this);
        dialog.setTitle("Uploading...");
        dialog.setCancelable(false);
        dialog.show();
        Log.e("Progress",""+progress);
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
       // Log.e("ServerProgress",uploadedBytes+" ");
    }

    @Override
    public void onError(Exception exception) {
        //Log.e("ServerError","Errrrrorrrr!!!!");
        Toast.makeText(this, "Oop-s! Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        //Log.e("ServerResponse", new String(serverResponseBody) + "   " + serverResponseCode);

        dialog.dismiss();
       // Log.e("ServerResponse", new String(serverResponseBody) + "   " + serverResponseCode);
        Gson gson=new Gson();
        ImageUploadResponse imageUploadResponse=gson.fromJson(new String(serverResponseBody),ImageUploadResponse.class);
      //  Log.e("ServerUrl",imageUploadResponse.getUrl());

        realm.beginTransaction();
        Login login=RealmController.with(HomeActivity.this).getStudentDetails();
        login.setProfile_path(imageUploadResponse.getUrl());
        realm.commitTransaction();

        Picasso.with(HomeActivity.this)
                .load(imageUploadResponse.getUrl())
                .into(StudentProfileFragment.mImgProf);
    }

    @Override
    public void onCancelled() {
    }

    public void uploadMultipart(String path) {

        String uploadId = UUID.randomUUID().toString();
        String student_id=null;
        String student_uname=null;

        Login login= RealmController.with(HomeActivity.this).getStudentDetails();

        if(login!=null) {
            student_id=login.getStudent_id();
            student_uname=login.getUsername();
        }

        uploadReceiver.setDelegate(this);
        uploadReceiver.setUploadID(uploadId);

        //Uploading code
        try {
            //Creating a multi part request
            new MultipartUploadRequest(HomeActivity.this,uploadId, EndPoints.UPLOAD_IMAGE)
                    .addFileToUpload(path, Constants.PROFILE_IMAGE) //Adding file
                    .addParameter(Constants.USER_ID,student_id)
                    .addParameter(Constants.USERNAME,student_uname)//Adding text parameter to the request
                    .setMaxRetries(2)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(HomeActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
