package com.app.yuqing.view;

import com.app.yuqing.R;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VersionUpdateDialog extends BaseDialog {

	private RelativeLayout rlCancel;
	private RelativeLayout rlSubmit;
	private TextView tvContent;
	private String content;
	public VersionUpdateDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_versionupdate);
		initView();
	}
	
	private void initView() {
		addClickCancel();
		rlCancel = (RelativeLayout) findViewById(R.id.mrl_dialog_cancel);
		rlSubmit = (RelativeLayout) findViewById(R.id.mrl_dialog_confirm);
		tvContent = (TextView) findViewById(R.id.tv_content);
		if (!TextUtils.isEmpty(content)) {
			tvContent.setText(content);
		}
		rlCancel.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		rlSubmit.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mListener != null) {
					mListener.update("true");
				}
				dismiss();
			}
		});
	}
	
	public void setData(String cotent) {
		this.content = cotent;
		if (tvContent != null) {
			tvContent.setText(content);
		}
	}

}
