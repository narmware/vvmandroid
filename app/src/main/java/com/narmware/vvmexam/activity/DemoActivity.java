package com.narmware.vvmexam.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.vvmexam.MyApplication;
import com.narmware.vvmexam.R;
import com.narmware.vvmexam.adapter.QNumAdapter;
import com.narmware.vvmexam.adapter.QuestionsAdapter;
import com.narmware.vvmexam.db.RealmController;
import com.narmware.vvmexam.pojo.Questions;
import com.narmware.vvmexam.services.NotificationService;
import com.narmware.vvmexam.support.Constants;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener{

    Realm realm;
    public static RecyclerView mRecyclerView,mQnumRecycler;
    QuestionsAdapter questionAdapter;
    QNumAdapter qNumAdapter;
    public static Button mBtnNext,mBtnPrevious;
    public static Button mBtnOptA,mBtnOptB,mBtnOptC,mBtnOptD;
    @BindView(R.id.btn_switch) ImageButton mImgSwitch;
   // @BindView(R.id.btn_mark_review) ImageButton mImgBtnMarkReview;
    private GestureDetector mDetector;
    @BindView(R.id.txt_timer) TextView mTxtTimer;
    @BindView(R.id.btn_switch_lang) Button mBtnLangSwitch;
    @BindView(R.id.btn_end_exam) Button mBtnEndExam;

    int position=0;
    public static DialogPlus dialog;
    RealmResults<Questions> qnumList;
    RealmResults<Questions> questionsList;

    long totalSeconds = 65;
    long intervalSeconds = 1;
    String key;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemUI();

        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);

        getSupportActionBar().hide();
        ButterKnife.bind(this);
        MyApplication.config_realm(DemoActivity.this);
        mRecyclerView=findViewById(R.id.recycler);

        Intent intent=getIntent();
        key=intent.getStringExtra("key");


        startService(new Intent(DemoActivity.this, NotificationService.class));


        ActivityCompat.requestPermissions(DemoActivity.this,
                new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        realm=Realm.getInstance(DemoActivity.this);
        RealmController.with(DemoActivity.this).clearAll();
        RealmResults<Questions> data=RealmController.with(DemoActivity.this).getQuestions();
        if(data.size()==0)
        {

            for(int i=1;i<4;i++)
            {
                realm.beginTransaction();
                Questions questions=realm.createObject(Questions.class);
                questions.setQid(String.valueOf(i));
                questions.setEngj("eng/j/ques_"+i+".txt");
                questions.setEngs("eng/s/ques_"+i+".txt");
                questions.setMarj("mar/j/ques_"+i+".txt");
                questions.setMars("mar/s/ques_"+i+".txt");
                questions.setTamj("tam/j/ques_"+i+".txt");
                questions.setTams("tam/s/ques_"+i+".txt");
                questions.setTelj("tel/j/ques_"+i+".txt");
                questions.setTels("tel/s/ques_"+i+".txt");
               // questions.setBitmap(BitMapToString(bitmap));
                questions.setqAnswertype(Constants.NOT_VIEWED);
                realm.commitTransaction();
            }



        }

        setQuestionAdapter();
        init();

        setDialog();

        String location=null;
       String ip= MyApplication.ipAddress();
        String imei= MyApplication.imei(DemoActivity.this);
        String mac= MyApplication.macAddress();
        String device_name= MyApplication.getDeviceName();
        try {
            MyApplication.getLocation(DemoActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Ip address",ip+"\n"+imei+"\n"+mac+"\n"+device_name);

    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void setQnumAdapter(RecyclerView.LayoutManager layoutManager)
    {
        qnumList=RealmController.with(DemoActivity.this).getQuestions();
        View view=dialog.getHolderView();
        mQnumRecycler=view.findViewById(R.id.recycler_view);
        mQnumRecycler.setLayoutManager(layoutManager);
        mQnumRecycler.setItemAnimator(new DefaultItemAnimator());
        mQnumRecycler.setNestedScrollingEnabled(false);
        mQnumRecycler.setFocusable(false);
        qNumAdapter=new QNumAdapter(DemoActivity.this,qnumList);
        mQnumRecycler.setAdapter(qNumAdapter);

        qNumAdapter.notifyDataSetChanged();
    }
    private void setDialog() {
        dialog = DialogPlus.newDialog(DemoActivity.this)
                .setExpanded(true, RelativeLayout.LayoutParams.WRAP_CONTENT)
                .setContentHolder(new ViewHolder(R.layout.lay_top_sheet))
                .setGravity(Gravity.TOP)
                .setMargin(30,0,30,100)
                .setPadding(10,10,10,10)
                .setBackgroundColorResId(R.color.grey_1000w)
                .create();
        setQnumAdapter(new GridLayoutManager(DemoActivity.this,10));


        //dialog.show();
    }

    private void init() {
        mBtnNext=findViewById(R.id.btn_next);
         mBtnPrevious=findViewById(R.id.btn_previous);

        mBtnOptA=findViewById(R.id.btn_opt_a);
         mBtnOptB=findViewById(R.id.btn_opt_b);
         mBtnOptC=findViewById(R.id.btn_opt_c);
        mBtnOptD=findViewById(R.id.btn_opt_d);

        mBtnNext.setOnClickListener(this);
        mBtnPrevious.setOnClickListener(this);
        mBtnOptA.setOnClickListener(this);
        mBtnOptB.setOnClickListener(this);
        mBtnOptC.setOnClickListener(this);
        mBtnOptD.setOnClickListener(this);
        mImgSwitch.setOnClickListener(this);
        //mImgBtnMarkReview.setOnClickListener(this);
        mBtnLangSwitch.setOnClickListener(this);
        mBtnEndExam.setOnClickListener(this);

        mDetector = new GestureDetector(this, new MyGestureListener());

        View view=getWindow().getDecorView();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });

        CountDownTimer timer = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {

            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                 mTxtTimer.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));

               // Log.e("seconds elapsed: " ,f.format(hour) + ":" + f.format(min) + ":" + f.format(sec) );

            }

            public void onFinish() {
                //Log.d( "done!", "Time's up!");
                stopService(new Intent(DemoActivity.this, NotificationService.class));
                finish();
            }

        };
        timer.start();

    }

    public void setQuestionAdapter()
    {

        SnapHelper snapHelper = new LinearSnapHelper();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);
        snapHelper.attachToRecyclerView(mRecyclerView);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(DemoActivity.this,    LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        questionsList=RealmController.with(DemoActivity.this).getQuestions();
        questionAdapter=new QuestionsAdapter(DemoActivity.this,questionsList);
        mRecyclerView.setAdapter(questionAdapter);

        questionAdapter.notifyDataSetChanged();

        //RecyclerView.OnItemTouchListener disabler = new RecyclerViewDisabler();
        //mRecyclerView.addOnItemTouchListener(disabler);    // scolling disable

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                position=linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(position==-1)
                {
                    setButtonColors("");
                }

                try {
                    if (position >= 0) {

                        if(questionsList.get(position).getqAnswertype() != Constants.ATTEMPTED)
                        {
                           /* if(questionsList.get(position).getqAnswertype() != Constants.REVIEW)
                            {*/
                                realm.beginTransaction();
                                questionsList.get(position).setqAnswertype(Constants.VIEWED);
                                realm.commitTransaction();
                        //}

                        }

                          setButtonColors(questionsList.get(position).getAnswer());
                    }
                }catch (Exception e)
                {}
                //Toast.makeText(DemoActivity.this,position+"", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class RecyclerViewDisabler implements RecyclerView.OnItemTouchListener {

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }

    public void setButtonColors(String answer)
    {
        if(answer.equals(""))
        {
            mBtnOptA.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptB.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptC.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptD.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));

            mBtnOptA.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptB.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptC.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptD.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        if (answer.equals(Constants.OPT_A)) {
            mBtnOptA.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round_selected));
            mBtnOptB.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptC.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptD.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));

            mBtnOptA.setTextColor(Color.WHITE);
            mBtnOptB.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptC.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptD.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        if (answer.equals(Constants.OPT_B)) {
            mBtnOptB.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round_selected));
            mBtnOptA.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptC.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptD.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));

            mBtnOptB.setTextColor(Color.WHITE);
            mBtnOptA.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptC.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptD.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        if (answer.equals(Constants.OPT_C)) {
            mBtnOptC.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round_selected));
            mBtnOptA.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptB.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptD.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));

            mBtnOptC.setTextColor(Color.WHITE);
            mBtnOptA.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptB.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptD.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        if (answer.equals(Constants.OPT_D)) {
            mBtnOptD.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round_selected));
            mBtnOptA.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptB.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));
            mBtnOptC.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round));

            mBtnOptD.setTextColor(Color.WHITE);
            mBtnOptA.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptB.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBtnOptC.setTextColor(getResources().getColor(R.color.colorPrimary));

        }
    }

    public void setAnswer(int position,String answer)
    {
        realm.beginTransaction();
        questionsList.get(position).setAnswer(answer);
        questionsList.get(position).setqAnswertype(Constants.ATTEMPTED);
        questionsList.get(position).setDate_time(MyApplication.getCurrentDate());
        realm.commitTransaction();

        questionAdapter.notifyDataSetChanged();
        qNumAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_next:

                if(position==RealmController.with(DemoActivity.this).getQuestions().size()-1)
                {
                    Toast.makeText(DemoActivity.this, "No questions", Toast.LENGTH_SHORT).show();
                }
                else{
                    position++;
                    mRecyclerView.smoothScrollToPosition(position);
                    realm.beginTransaction();
                    realm.commitTransaction();
                }

                break;

            case R.id.btn_previous:

                if(position==0)
                {
                    Toast.makeText(DemoActivity.this, "No questions", Toast.LENGTH_SHORT).show();
                }
                else{
                    position--;
                    mRecyclerView.smoothScrollToPosition(position);
                }

                break;

            case R.id.btn_opt_a:
                setAnswer(position,Constants.OPT_A);
                setButtonColors(Constants.OPT_A);

                //mBtnOptA.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round_selected));
                break;

            case R.id.btn_opt_b:
                setAnswer(position,Constants.OPT_B);
                setButtonColors(Constants.OPT_B);

                //mBtnOptB.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round_selected));
                break;

            case R.id.btn_opt_c:
                setAnswer(position,Constants.OPT_C);
                setButtonColors(Constants.OPT_C);

                // mBtnOptC.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round_selected));
                break;

            case R.id.btn_opt_d:
                setAnswer(position,Constants.OPT_D);
                setButtonColors(Constants.OPT_D);

                //mBtnOptD.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_round_selected));
                break;

            case R.id.btn_switch:
                dialog.show();
                break;

            case R.id.btn_end_exam:
                finish();
                Toast.makeText(this, "End Exam", Toast.LENGTH_SHORT).show();
                break;


            case R.id.btn_switch_lang:
                Toast.makeText(this, "Marathi", Toast.LENGTH_SHORT).show();
                break;

           /* case R.id.btn_mark_review:
                realm.beginTransaction();
                questionsList.get(position).setqAnswertype(Constants.REVIEW);
                realm.commitTransaction();

                questionAdapter.notifyDataSetChanged();
                qNumAdapter.notifyDataSetChanged();
                break;*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(DemoActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        // other 'case' lines to check for other
        // permissions this app might request
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG","onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
