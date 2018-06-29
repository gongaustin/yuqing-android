package com.app.yuqing.activity;

import com.app.yuqing.AppContext;
import com.app.yuqing.fragment.WorkNewFragment;
import com.app.yuqing.utils.ImageUtil;
import com.app.yuqing.utils.MonitorWebClient;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.utils.webview.MyWebChomeClient;
import com.app.yuqing.utils.webview.PermissionUtil;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.app.yuqing.MyApplication;
import com.app.yuqing.R;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UrlWebClientActivity extends BaseActivity implements MyWebChomeClient.OpenFileChooserCallBack {

	@ViewInject(R.id.webview)
	private WebView webView;

	public static final String KEY_URL = "url";
	private static final String KEY_PRE = "http://";

	private static final int REQUEST_CODE_PICK_IMAGE = 0;
	private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;

	private Intent mSourceIntent;
	private ValueCallback<Uri> mUploadMsg;
	public ValueCallback<Uri[]> mUploadMsgForAndroid5;

	// permission Code
	private static final int P_CODE_PERMISSIONS = 101;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		requestPermissionsAndroidM();
		String url = getIntent().getStringExtra(KEY_URL);
		url = dealUrl(url);
		WebSettings webSettings = webView.getSettings();
		initWebviewSet(webSettings);
		initWebView(webView);
		fixDirPath();
		if (TextUtils.isEmpty(url)) {
			Toast.makeText(UrlWebClientActivity.this, "链接为空", Toast.LENGTH_LONG).show();
		} else {
			String token = PreManager.getString(UrlWebClientActivity.this.getApplicationContext(),AppContext.KEY_TOKEN);
			webView.loadUrl("javascript:document.cookie='token="+token+"';");
			webView.loadUrl(url);
		}
	}

	private void initWebviewSet(WebSettings webSettings) {
		//设置支持JS
		webSettings.setJavaScriptEnabled(true);
		// 设置支持本地存储
		webSettings.setDatabaseEnabled(true);
		//取得缓存路径
		String path = UrlWebClientActivity.this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		//设置路径
		webSettings.setDatabasePath(path);
		//设置支持DomStorage
		webSettings.setDomStorageEnabled(true);
		//设置存储模式
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		//设置适应屏幕
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setSupportZoom(true);
		webSettings.setAllowContentAccess(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDisplayZoomControls(false);
		//设置缓存
		webSettings.setAppCacheEnabled(true);

		//如果访问的页面中有Javascript，则webview必须设置支持Javascript
		webSettings.setAllowFileAccess(true);
	}

	private void initWebView(WebView wView) {
		wView.requestFocus();
		//下面三个各种监听
		wView.setWebChromeClient(new MyWebChomeClient(this));
		wView.setWebViewClient(new MyWebViewClient());
		wView.setDownloadListener(new MyWebViewDownLoadListener());
//		wView.setWebViewClient(new MonitorWebClient(webView,this));
		webView.addJavascriptInterface(new JSObject(),"native");
	}

	public String dealUrl(String url) {
		if (!TextUtils.isEmpty(url)) {
			if (!url.startsWith(KEY_PRE)) {
				url = KEY_PRE+url;
			}
		}
		return url;
	}

	private void fixDirPath() {
		String path = com.app.yuqing.utils.webview.ImageUtil.getDirPath();
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	//*************************   权限开始    ****************************//
	private void requestPermissionsAndroidM() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			List<String> needPermissionList = new ArrayList<>();
			needPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			needPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			needPermissionList.add(Manifest.permission.CAMERA);

			PermissionUtil.requestPermissions(UrlWebClientActivity.this, P_CODE_PERMISSIONS, needPermissionList);

		} else {
			return;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case P_CODE_PERMISSIONS:
				requestResult(permissions, grantResults);
				restoreUploadMsg();
				break;

			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	public void requestResult(String[] permissions, int[] grantResults) {
		ArrayList<String> needPermissions = new ArrayList<String>();

		for (int i = 0; i < grantResults.length; i++) {
			if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
				if (PermissionUtil.isOverMarshmallow()) {

					needPermissions.add(permissions[i]);
				}
			}
		}

		if (needPermissions.size() > 0) {
			StringBuilder permissionsMsg = new StringBuilder();

			for (int i = 0; i < needPermissions.size(); i++) {
				String strPermissons = needPermissions.get(i);

				if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(strPermissons)) {
					permissionsMsg.append("," + getString(R.string.permission_storage));

				} else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(strPermissons)) {
					permissionsMsg.append("," + getString(R.string.permission_storage));

				} else if (Manifest.permission.CAMERA.equals(strPermissons)) {
					permissionsMsg.append("," + getString(R.string.permission_camera));

				}
			}

			String strMessage = "请允许使用\"" + permissionsMsg.substring(1).toString() + "\"权限, 以正常使用APP的所有功能.";

			Toast.makeText(UrlWebClientActivity.this, strMessage, Toast.LENGTH_SHORT).show();

		} else {
			return;
		}
	}

	private void restoreUploadMsg() {
		if (mUploadMsg != null) {
			mUploadMsg.onReceiveValue(null);
			mUploadMsg = null;

		} else if (mUploadMsgForAndroid5 != null) {
			mUploadMsgForAndroid5.onReceiveValue(null);
			mUploadMsgForAndroid5 = null;
		}
	}

	//****************************   开始WEBChomeClient   **************************//


	@Override
	public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
		mUploadMsg = uploadMsg;
		showOptions();
	}

	@Override
	public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
		mUploadMsgForAndroid5 = filePathCallback;
		showOptions();

		return true;
	}

	public void showOptions() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(UrlWebClientActivity.this);
		alertDialog.setOnCancelListener(new DialogOnCancelListener());

		alertDialog.setTitle("请选择操作");
		// gallery, camera.
		String[] options = {"文档", "拍照"};

		alertDialog.setItems(options, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							if (PermissionUtil.isOverMarshmallow()) {
								if (!PermissionUtil.isPermissionValid(UrlWebClientActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
									Toast.makeText(UrlWebClientActivity.this,
											"请去\"设置\"中开启本应用的图片媒体访问权限",
											Toast.LENGTH_SHORT).show();

									restoreUploadMsg();
									requestPermissionsAndroidM();
									return;
								}

							}

							try {
								mSourceIntent = com.app.yuqing.utils.webview.ImageUtil.choosePicture();
								startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(UrlWebClientActivity.this,
										"请去\"设置\"中开启本应用的图片媒体访问权限",
										Toast.LENGTH_SHORT).show();
								restoreUploadMsg();
							}

						} else {
							if (PermissionUtil.isOverMarshmallow()) {
								if (!PermissionUtil.isPermissionValid(UrlWebClientActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
									Toast.makeText(UrlWebClientActivity.this,
											"请去\"设置\"中开启本应用的图片媒体访问权限",
											Toast.LENGTH_SHORT).show();

									restoreUploadMsg();
									requestPermissionsAndroidM();
									return;
								}

								if (!PermissionUtil.isPermissionValid(UrlWebClientActivity.this, Manifest.permission.CAMERA)) {
									Toast.makeText(UrlWebClientActivity.this,
											"请去\"设置\"中开启本应用的相机权限",
											Toast.LENGTH_SHORT).show();

									restoreUploadMsg();
									requestPermissionsAndroidM();
									return;
								}
							}

							try {
								mSourceIntent = com.app.yuqing.utils.webview.ImageUtil.takeBigPicture();
								startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);

							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(UrlWebClientActivity.this,
										"请去\"设置\"中开启本应用的相机和图片媒体访问权限",
										Toast.LENGTH_SHORT).show();

								restoreUploadMsg();
							}
						}
					}
				}
		);

		alertDialog.show();
	}

	private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
		@Override
		public void onCancel(DialogInterface dialogInterface) {
			restoreUploadMsg();
		}
	}

	//****************************   WEBChomeClient  结束 **************************//

	//****************************   开始定义内部类   **************************//
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

	public class MyWebViewClient extends WebViewClient {

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			return super.shouldInterceptRequest(view, url);
		}

		@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
			String url = request.getUrl().toString();
			return super.shouldInterceptRequest(view, request);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			System.out.println("WebView url:"+url);
			String token = PreManager.getString(UrlWebClientActivity.this.getApplicationContext(),AppContext.KEY_TOKEN);
			view.loadUrl("javascript:document.cookie='token="+token+"';");
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                CookieSyncManager.getInstance().sync();
//            } else {
//                CookieManager.getInstance().flush();
//            }
		}
	}

	//****************************   结束定义内部类   **************************//

	//****************************   开始处理回调数据   **************************//
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			if (mUploadMsg != null) {
				mUploadMsg.onReceiveValue(null);
			}

			if (mUploadMsgForAndroid5 != null) {         // for android 5.0+
				mUploadMsgForAndroid5.onReceiveValue(null);
			}
			return;
		}
		switch (requestCode) {
			case REQUEST_CODE_IMAGE_CAPTURE:
			case REQUEST_CODE_PICK_IMAGE: {
				try {
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
						if (mUploadMsg == null) {
							return;
						}

						String sourcePath = com.app.yuqing.utils.webview.ImageUtil.retrievePath(UrlWebClientActivity.this, mSourceIntent, data);

						if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
							Log.e(AppContext.TAG, "sourcePath empty or not exists.");
							break;
						}
						Uri uri = Uri.fromFile(new File(sourcePath));
						mUploadMsg.onReceiveValue(uri);

					} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						if (mUploadMsgForAndroid5 == null) {        // for android 5.0+
							return;
						}

						String sourcePath = com.app.yuqing.utils.webview.ImageUtil.retrievePath(UrlWebClientActivity.this, mSourceIntent, data);

						if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
							Log.e(AppContext.TAG, "sourcePath empty or not exists.");
							break;
						}
						Uri uri = Uri.fromFile(new File(sourcePath));
						mUploadMsgForAndroid5.onReceiveValue(new Uri[]{uri});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	//****************************   结束处理回调数据   **************************//

	//****************************   处理cookie开始   **************************//
	private void syncCookie(Context context, String url){
		String token = PreManager.getString(context,AppContext.KEY_TOKEN);
		CookieManager cookieManager = CookieManager.getInstance();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			cookieManager.removeSessionCookies(null);
			cookieManager.flush();
		} else {
			cookieManager.removeSessionCookie();
			CookieSyncManager.getInstance().sync();
		}
		cookieManager.setAcceptCookie(true);
		//设置Cookie
		CookieSyncManager.getInstance().sync();
	}

	public class JSObject {

		@JavascriptInterface
		public void loginOut() {
			Intent intent = new Intent(UrlWebClientActivity.this,LoginActivity.class);
			startActivity(intent);
			MyApplication.instance.exit();
		}
	}

}
