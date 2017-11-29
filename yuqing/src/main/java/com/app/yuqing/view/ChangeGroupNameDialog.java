package com.app.yuqing.view;

import com.app.yuqing.R;
import com.app.yuqing.utils.CommonUtils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class ChangeGroupNameDialog extends BaseDialog {

	private RelativeLayout rlCancel;
	private RelativeLayout rlSubmit;
	private EditText edtName;
	public ChangeGroupNameDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_changegroupname);
		initView();
	}
	
	private void initView() {
		addClickCancel();
		rlCancel = (RelativeLayout) findViewById(R.id.mrl_dialog_cancel);
		rlSubmit = (RelativeLayout) findViewById(R.id.mrl_dialog_confirm);
		edtName = (EditText) findViewById(R.id.edt_name);
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
				if (TextUtils.isEmpty(edtName.getText().toString())) {
					CommonUtils.showToast("请输入群名称");
					return;
				}
				if (mListener != null) {
					mListener.update(edtName.getText().toString());
				}
				dismiss();
			}
		});
	}

}
