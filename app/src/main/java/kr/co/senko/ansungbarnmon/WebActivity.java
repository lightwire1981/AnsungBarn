package kr.co.senko.ansungbarnmon;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kr.co.senko.ansungbarnmon.util.PreferenceSetting;
import kr.co.senko.ansungbarnmon.util.Util;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        setWidget();
    }

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    private void setWidget() {
        WebView mWebView = findViewById(R.id.wvMain);
        // 자바스크립트 설정
        mWebView.getSettings().setJavaScriptEnabled(true);
        // webview 로컬 스토리지 설정
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
//        mWebView.getSettings().setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //https, http 호환 여부(https에서 http컨텐츠도 보여질수 있도록 함)
            mWebView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        String key = getIntent().getStringExtra("url");
        String url = "";
        switch (key) {
            case "login":
                url = "https://livestock.kr";
                break;
            case "privacy":
                url = "https://www.anseong.go.kr/portal/contents.do?mId=0604000000";
                break;
            default:
                break;
        }
        mWebView.setWebViewClient(new WebViewClients(key));
        mWebView.loadUrl(url);
    }

    static class WebViewClients extends WebViewClient {

        String key;
        public WebViewClients(String key) {
            this.key = key;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            view.loadUrl("http://121.147.225.140/senko/index.html");
            return true;
        }

        @SuppressLint("ObsoleteSdkInt")
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (key.equals("privacy")) return;
            String userID = PreferenceSetting.LoadPreference(view.getContext(), PreferenceSetting.PREFERENCE_KEY.USER_INFO);
            Log.d("<<<<<< User ID : ", userID);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.evaluateJavascript("window.sessionStorage.setItem('"+"key"+"','"+userID+"');", null);
//                view.evaluateJavascript("window.localStorage.setItem('"+"ID"+"','"+ Util.PHONE_NUMBER+"');", null);
            } else {
                view.loadUrl("javascript:sessionStorage.setItem('"+ "ID" +"','"+ userID +"');");
//                view.loadUrl("javascript:localStorage.setItem('"+ key2 +"','"+ val2 +"');");
            }
        }
    }
}