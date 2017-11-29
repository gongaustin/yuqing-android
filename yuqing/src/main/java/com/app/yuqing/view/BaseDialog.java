package com.app.yuqing.view;


import com.app.yuqing.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public abstract class BaseDialog extends Dialog {
	protected Context mContext;
	public BaseDialog(Context context) {
		super(context,R.style.ShareDialog);
		this.mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = LayoutParams.MATCH_PARENT;
		params.width = LayoutParams.MATCH_PARENT;
		params.gravity = Gravity.CENTER;
		getWindow().setAttributes(params);
		getWindow().setWindowAnimations(R.style.ConfirmDialogAnimstyle);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
	
	protected DialogListener mListener;
	public static interface DialogListener {
		void update(Object object);
	}
	
	public void setListener(DialogListener listener) {
		this.mListener = listener;
	}
	
	protected void addClickCancel() {
		View view = findViewById(R.id.dialog_content);
		if (view != null) {
			view.setOnClickListener(new android.view.View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});			
		}
	}
}
