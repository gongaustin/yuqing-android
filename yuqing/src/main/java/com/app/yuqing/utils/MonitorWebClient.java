package com.app.yuqing.utils;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import com.app.yuqing.MyApplication;
import com.app.yuqing.net.OKHttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MonitorWebClient extends WebViewClient{
	private WebView webView;
	private Activity activity;
	public MonitorWebClient(WebView webView,Activity activity){
		this.webView = webView;
		this.activity = activity;
	}
	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		// TODO Auto-generated method stub
		Log.i(getClass().getName(), description);
		try{
			webView.stopLoading();
			webView.clearView();
		}catch(Exception e){
			
		}
		if(webView.canGoBack()){
			webView.goBack();
		}
	}
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		// TODO Auto-generated method stub
		handler.proceed();
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO Auto-generated method stub
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		super.onPageFinished(view, url);
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(final WebView view, String url) {
		// TODO Auto-generated method stub
        return super.shouldOverrideUrlLoading(view, url); 
	}
	
	@Override
	@Deprecated
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		// TODO Auto-generated method stub
//		synchronousWebCookies(MyApplication.instance.getApplicationContext(),url);
		return super.shouldInterceptRequest(view, url);
	}
	
	
	
	@SuppressLint("NewApi") @Override
	public WebResourceResponse shouldInterceptRequest(WebView view,
			WebResourceRequest request) {
		// TODO Auto-generated method stub
		String url = request.getUrl().toString();  
//		synchronousWebCookies(MyApplication.instance.getApplicationContext(),url);
		return super.shouldInterceptRequest(view, request);
	}
	
//    /**
//     * 同步一下cookie
//     */
//    public static void synchronousWebCookies(Context context,String url){
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//            CookieSyncManager.createInstance( context);
//        }
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie( true );
//        cookieManager.removeSessionCookie();// 移除
//        cookieManager.setCookie(url, PreManager.getCookieValue(MyApplication.instance.getApplicationContext()));//为url设置cookie
//        CookieSyncManager.getInstance().sync();//同步cookie
//    }
    
    
}
