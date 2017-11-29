package com.app.yuqing.view;

import com.app.yuqing.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChangeHeadDialog extends BaseDialog {
	private TextView tvCapture;
	private TextView tvPic;
	private TextView tvCancel;
	public ChangeHeadDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_changhead);
		initView();
	}
	
	private void initView() {
		addClickCancel();
		tvCapture = (TextView) findViewById(R.id.tv_capture);
		tvCancel = (TextView) findViewById(R.id.tv_cancel);
		tvPic = (TextView) findViewById(R.id.tv_pic);
		
		tvCapture.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.capture();
					dismiss();
				}
			}
		});
		
		tvCancel.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		tvPic.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.pic();
					dismiss();
				}
			}
		});
	}
	
	public interface ChangeHeadDialogListener {
		void capture();
		void pic();
	}
	
	private ChangeHeadDialogListener mListener;

	public void setListener(ChangeHeadDialogListener listener) {
		mListener = listener;
	}

}
