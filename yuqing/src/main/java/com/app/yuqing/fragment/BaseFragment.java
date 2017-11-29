package com.app.yuqing.fragment;

import java.util.HashMap;

import com.app.yuqing.R;
import com.app.yuqing.net.AndroidEventManager;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventManager.OnEventListener;
import com.app.yuqing.utils.CommonUtils;
import com.lidroid.xutils.ViewUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BaseFragment extends Fragment implements OnClickListener , OnEventListener{

	public View contentView;
	protected int layoutResId;
	protected AndroidEventManager mEventManager = AndroidEventManager.getInstance();
	protected ProgressDialog 	mProgressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 注入类属性
		contentView = inflater.inflate(layoutResId, container, false);
		ViewUtils.inject(this, contentView);
		return contentView;
	}
	
	public void setContentView(int layoutResId) {
		this.layoutResId = layoutResId;
	}
	
	public void showToast(CharSequence msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}
		
    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return false;
    }

    @Override
    public void onClick(View v)
    {
        
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
	
	//进度条显示
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
			mProgressDialog = ProgressDialog.show(getActivity(), strTitle, strMessage, true, false);
		}
	}
	
	protected void showXProgressDialog(){
		++mXProgressDialogShowCount;
		if(mIsXProgressDialogShowing){
			return;
		}
		final Context context = getActivity().getParent() == null ? getActivity() : getActivity().getParent();
		FrameLayout layout = new FrameLayout(context);
		layout.setBackgroundColor(Color.rgb(223, 230, 238));
		View view = (View) LayoutInflater.from(context).inflate(R.layout.progressbar, null);
		int pbSize = FrameLayout.LayoutParams.WRAP_CONTENT;
		FrameLayout.LayoutParams lpPb = new FrameLayout.LayoutParams(pbSize, pbSize);
		lpPb.gravity = Gravity.CENTER;
		layout.addView(view, lpPb);
		WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
	    int width = dm.widthPixels;
	    int height = dm.heightPixels - CommonUtils.dip2px(getActivity(), 44) - CommonUtils.getSystemStatusHeight(getActivity());
	    WindowManager windowManager = getActivity().getParent() == null ? getActivity().getWindowManager() : getActivity().getParent().getWindowManager();
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
		ProgressBar pb = new ProgressBar(getActivity());
		pb.setIndeterminate(true);
		return pb;
	}
	
	protected void dismissXProgressDialog(){
		if(mIsXProgressDialogShowing){
			if(--mXProgressDialogShowCount == 0){
				WindowManager windowManager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
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
		if(!event.isSuccess()){
			final Exception e = event.getFailException();
			if(e != null){
				CommonUtils.showToast(e.getMessage());
			}
		}
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
	}
}
