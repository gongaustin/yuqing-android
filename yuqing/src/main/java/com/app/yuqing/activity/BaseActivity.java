package com.app.yuqing.activity;

import java.util.HashMap;
import java.util.Locale;

import com.app.yuqing.AppContext;
import com.app.yuqing.MyApplication;
import com.app.yuqing.R;
import com.app.yuqing.net.AndroidEventManager;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventManager.OnEventListener;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.service.YuQingIntentService;
import com.app.yuqing.service.YuQingPushService;
import com.app.yuqing.utils.CommonUtils;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.ViewUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi") public class BaseActivity extends FragmentActivity implements OnClickListener,OnEventListener {
	//逻辑部分
	protected Context context = null;
	protected ProgressDialog 	mProgressDialog;
	protected MyApplication mApplication;
	protected NotificationManager notificationManager;
	protected InputMethodManager manager;
	protected AndroidEventManager mEventManager = AndroidEventManager.getInstance();
	protected Gson gson;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		String strClassName = this.getClass().getName();
		int nIndex = strClassName.lastIndexOf(".");
		if(nIndex != -1){
			final String cName = strClassName.substring(nIndex + 1);
			String strResourceName = "activity_" + cName.replaceFirst("Activity", "");
			strResourceName = strResourceName.toLowerCase(Locale.getDefault());
			final int nLayoutId = getResources().getIdentifier(strResourceName, 
					"layout", getPackageName());
			if(nLayoutId != 0){
				setContentView(nLayoutId);
			}
		}
		ViewUtils.inject(this); //注入事件
		
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			//透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
		
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		context = this;
		mApplication = (MyApplication) getApplication();
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mApplication.addActivity(this);
		gson = new Gson();
		
		PushManager.getInstance().initialize(this.getApplicationContext(), YuQingPushService.class);
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), YuQingIntentService.class);
	}
	
	protected void addBack(int resId) {
		View view = findViewById(resId);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	protected void setTitle(String title) {
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(title);
	}
	
	protected void setRightResource(int resId) {
		ImageView ivRight = (ImageView) findViewById(R.id.right_image);
		ivRight.setImageResource(resId);
	}
	
	protected void setRightClickListener(OnClickListener listener) {
		View view = findViewById(R.id.right_layout);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(listener);
	}
	
	protected void setRightTextClickListener(OnClickListener listener) {
		View view = findViewById(R.id.right_textlayout);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(listener);
	}	
	
	protected void setRightTextClickListener(String str , OnClickListener listener) {
		View view = setRightText(str);
		view.setOnClickListener(listener);
	}	
	
	protected View setRightText(String str) {
		TextView tvRight = (TextView) findViewById(R.id.tv_right);
		tvRight.setText(str);
		View view = findViewById(R.id.right_textlayout);
		view.setVisibility(View.VISIBLE);
		return view;
	}
	
	@Override  
	public Resources getResources() {  
	    Resources res = super.getResources();    
	    Configuration config=new Configuration();    
	    config.setToDefaults();    
	    res.updateConfiguration(config,res.getDisplayMetrics() );  
	    return res;  
	} 
	
	public boolean hasInternetConnected() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		while(mIsXProgressDialogShowing){
			dismissXProgressDialog();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mIsXProgressDialogShowing){
			mViewXProgressDialog.setVisibility(View.VISIBLE);
		}
		PushManager.getInstance().initialize(this.getApplicationContext(), YuQingPushService.class);
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), YuQingIntentService.class);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mIsXProgressDialogShowing){
			mViewXProgressDialog.setVisibility(View.GONE);
		}
	}
	
	@Override  
	 public boolean onTouchEvent(MotionEvent event) {  
	  // TODO Auto-generated method stub  
	  if(event.getAction() == MotionEvent.ACTION_DOWN){  
	     if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){  
	       manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
	     }  
	  }  
	  return super.onTouchEvent(event);  
	 } 
	
	//网络请求模块
	private HashMap<Event, Boolean>				mMapEventToProgressBlock;
	protected Event pushEvent(int eventCode,Object...params){
		return pushEventEx(eventCode, true, false,null,params);
	}
	
	protected Event pushEventBlock(int eventCode,Object...params){
		return pushEventEx(eventCode, true, true, null, params);
	}
	
	protected Event pushEventNoProgress(int eventCode,Object...params){
		return pushEventEx(eventCode, false, false, null, params);
	}
	
	@SuppressLint("UseSparseArrays")
	protected Event pushEventEx(int eventCode,
			boolean bShowProgress,boolean bBlock,String progressMsg,
			Object... params){
		Event e = null;
		e = mEventManager.pushEventEx(eventCode,this,params);
		
		if(mMapEventToProgressBlock == null){
			mMapEventToProgressBlock = new HashMap<Event, Boolean>();
		}
		
		if( !mMapEventToProgressBlock.containsKey(e)){
			if(bShowProgress){
				if(bBlock){
					showProgressDialog(null,progressMsg);
				}else{
					showXProgressDialog();
				}
				mMapEventToProgressBlock.put(e, bBlock);
			}
		}
		
		return e;
	}
	
	//进度条显�?
	protected boolean			mIsXProgressDialogShowing;
	protected int				mXProgressDialogShowCount;
	protected View				mViewXProgressDialog;
	
	protected void showProgressDialog(){
		showProgressDialog(null, null);
	}
	
	protected void showProgressDialog(String strTitle,int nStringId){
		showProgressDialog(strTitle, getString(nStringId));
	}
	
	protected void showProgressDialog(String strTitle,String strMessage){
		if(mProgressDialog == null){
			mProgressDialog = ProgressDialog.show(this, strTitle, strMessage, true, false);
		}
	}
	
	protected void showXProgressDialog(){
		++mXProgressDialogShowCount;
		if(mIsXProgressDialogShowing){
			return;
		}
		final Context context = getParent() == null ? this : getParent();
		FrameLayout layout = new FrameLayout(context);
//		layout.setBackgroundColor(Color.rgb(223, 230, 238));
		layout.setBackgroundColor(getResources().getColor(R.color.color_main));
		View view = (View) LayoutInflater.from(context).inflate(R.layout.progressbar, null);
		int pbSize = FrameLayout.LayoutParams.WRAP_CONTENT;
		FrameLayout.LayoutParams lpPb = new FrameLayout.LayoutParams(pbSize, pbSize);
		lpPb.gravity = Gravity.CENTER;
		layout.addView(view, lpPb);
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
	    int width = dm.widthPixels;
	    int height = dm.heightPixels - CommonUtils.dip2px(this, 44) - CommonUtils.getSystemStatusHeight(this);
	    WindowManager windowManager = getParent() == null ? getWindowManager() : getParent().getWindowManager();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				width,
				height, 
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.RGBA_8888);
		lp.gravity = Gravity.BOTTOM;
		
		windowManager.addView(layout, lp);
		mViewXProgressDialog = layout;
		mIsXProgressDialogShowing = true;
	}
	
	protected ProgressBar onCreateXProgressBar(){
		ProgressBar pb = new ProgressBar(this);
		pb.setIndeterminate(true);
		return pb;
	}
	
	protected void dismissXProgressDialog(){
		if(mIsXProgressDialogShowing){
			if(--mXProgressDialogShowCount == 0){
				WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
				windowManager.removeView(mViewXProgressDialog);
				mViewXProgressDialog = null;
				mIsXProgressDialogShowing = false;
			}
		}
	}
	
	protected void dismissProgressDialog(){
		try{
			if(mProgressDialog != null){
				mProgressDialog.dismiss();
			}
		}catch(Exception e){
		}
		mProgressDialog = null;
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		if(mMapEventToProgressBlock != null){
			Boolean block = mMapEventToProgressBlock.remove(event);
			if(block != null){
				if(block.booleanValue()){
					dismissProgressDialog();
				}else{
					dismissXProgressDialog();
				}
			}
		}
		if (!event.isSuccess()) {
			BaseResponseBean bean = (BaseResponseBean) event.getReturnParamAtIndex(0);
			if (bean != null && AppContext.KEY_NOLOGIN.equals(bean.getMsg())) {
				CommonUtils.showToast("被其它设备登录");
				Intent intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
				MyApplication.instance.exit();
			}
		}
		if(!event.isSuccess()){
			if (!TextUtils.isEmpty(event.getmErrorCode())
					&& AppContext.CODE_NOLOGIN.equals(event.getmErrorCode())) {
				CommonUtils.showToast("被其它设备登录");
				Intent intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
				MyApplication.instance.exit();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	protected Activity getMyself() {
		return this;
	}
	
}
