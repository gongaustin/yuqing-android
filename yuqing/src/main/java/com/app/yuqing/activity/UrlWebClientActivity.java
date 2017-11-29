package com.app.yuqing.activity;

import com.app.yuqing.utils.MonitorWebClient;
import com.app.yuqing.utils.PreManager;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.app.yuqing.MyApplication;
import com.app.yuqing.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UrlWebClientActivity extends BaseActivity {

	@ViewInject(R.id.webview)
	private WebView webView;
	public static final String KEY_URL = "url";
	private static final String KEY_PRE = "http://";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = getIntent().getStringExtra(KEY_URL);
		url = dealUrl(url);
		WebSettings webSettings = webView.getSettings();
		initWebviewSet(webSettings);
		initWebView(webView);
		if (TextUtils.isEmpty(url)) {
			Toast.makeText(this, "链接为空", Toast.LENGTH_LONG).show();
		} else {
			webView.loadUrl(url);			
		}

	}

	private void initWebviewSet(WebSettings webSettings) {
		//设置支持JS
		webSettings.setJavaScriptEnabled(true);
		// 设置支持本地存储
		webSettings.setDatabaseEnabled(true);
		//取得缓存路径
		String path = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		//设置路径
		webSettings.setDatabasePath(path);
		//设置支持DomStorage
		webSettings.setDomStorageEnabled(true);
		//设置存储模式
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		//设置适应屏幕
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDisplayZoomControls(false);
		//设置缓存
		webSettings.setAppCacheEnabled(true);

	}

	private void initWebView(WebView wView) {
		wView.requestFocus();
		//下面三个各种监听
		wView.setWebChromeClient(new MyChromClient());
		wView.setDownloadListener(new MyWebViewDownLoadListener());
		wView.setWebViewClient(new MonitorWebClient(webView,this));
	}
	
	public String dealUrl(String url) {
		if (!TextUtils.isEmpty(url)) {
			if (!url.startsWith(KEY_PRE)) {
				url = KEY_PRE+url;
			}
		}
		return url;
	}
	
    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
        	webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

	public class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
			Log.i("tag", "url="+url);
			Log.i("tag", "userAgent="+userAgent);
			Log.i("tag", "contentDisposition="+contentDisposition);
			Log.i("tag", "mimetype="+mimetype);
			Log.i("tag", "contentLength="+contentLength);
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	public class MyChromClient extends WebChromeClient {

	}
}
