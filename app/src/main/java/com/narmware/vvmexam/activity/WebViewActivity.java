package com.narmware.vvmexam.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.support.Constants;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{

    String mUrl;
    WebView mWebView;
    TextView mTxtTitle;
    ImageButton mImgBtnBack;
    protected ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().hide();

        Intent intent=getIntent();
        mUrl=intent.getStringExtra(Constants.LINK);
        init();
    }

    private void init() {

        mImgBtnBack=findViewById(R.id.btn_back);
        mTxtTitle=findViewById(R.id.web_title);
        mImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebView= (WebView) findViewById(R.id.webview);
        setWebView();
        mWebView.loadUrl(mUrl);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

       /* Intent intent=new Intent(WebViewActivity.this, HomeActivityTab.class);
        intent.putExtra("isDone",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();*/

    }

    public void setWebView() {

        mProgress = new ProgressDialog(WebViewActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setIndeterminate(true);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        //   webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
    }


    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                if (mProgress.isShowing() == false) {
                    mProgress.show();
                }
                else {
                    Log.d("Page Error : ","");
                    mProgress.dismiss();
                }
            }
            catch (Exception e) {
                Log.d("Page Exception : ","");
                mProgress.dismiss();
            }

           /* if(MyApplication.LOGOUT_URL.equals(url)){
                mWebView.loadUrl(mUrl);
                mProgress.dismiss();
            }*/
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            if(mProgress.isShowing()) {
                mProgress.dismiss();
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            // TODO Auto-generated method stub

            view.loadUrl(url);
            mProgress.dismiss();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("Page loaded : ", url);
            mTxtTitle.setText(mWebView.getTitle());
            mProgress.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                 Log.e("errorCode" ,description);
        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            mProgress.dismiss();
            return super.onRenderProcessGone(view, detail);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            //mHorizontalProgress.setProgress(progress);
        }


    }

}
