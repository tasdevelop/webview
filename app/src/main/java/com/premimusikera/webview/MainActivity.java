package com.premimusikera.webview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onesignal.OneSignal;
import com.wessam.library.NoInternetLayout;

public class MainActivity extends AppCompatActivity {
    private static final String ONESIGNAL_APP_ID = "62c870d8-e9de-4ab7-963c-22d020ccb984";
    private WebView webView;

    private float m_downX;

    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        if(isActivityVisible() == 0){
            url = "https://premierpro.co.id/";
        }
        else{
            url = getIntent().getStringExtra("url");
        }

        webView = (WebView) findViewById(R.id.webView);

        initWebView();
        webView.loadUrl(url);
    }
    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                invalidateOptionsMenu();
                new NoInternetLayout.Builder(MainActivity.this, R.layout.activity_main);

            }

        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void notificationOpened(OSNotificationOpenResult result) {
//        if (result.notification.payload.launchURL != null) {
//            if (isActivityVisible() == 1){
//                Intent i = new Intent(this, MainActivity.class);
//                i.putExtra("url", result.notification.payload.launchURL);
//                startActivity(i);
//            }
//            else{
//                initWebView();
//                webView.loadUrl(result.notification.payload.launchURL);
//            }
//        }
//    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

//    @Override
//    public void notificationOpened(OSNotificationOpenResult result) {
//        if (result.notification.payload.launchURL != null) {
//            if (isActivityVisible() == 1){
//                Intent i = new Intent(this, MainActivity.class);
//                i.putExtra("url", result.notification.payload.launchURL);
//                startActivity(i);
//            }
//            else{
//                initWebView();
//                webView.loadUrl(result.notification.payload.launchURL);
//            }
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.activityStarted();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.activityResumed();
    }

    public static int isActivityVisible() {
        return activityVisible;
    }

    public static void activityStarted() {
        activityVisible = 0;
    }

    public static void activityPaused() {
        activityVisible = 1;
    }

    public static void activityResumed() {
        activityVisible = 2;
    }

    private static int activityVisible;
}