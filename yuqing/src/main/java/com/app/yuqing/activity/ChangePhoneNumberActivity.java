package com.app.yuqing.activity;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.bean.UserOldBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.UpdatePhoneResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ChangePhoneNumberActivity extends BaseActivity{

	@ViewInject(R.id.edt_phone)
	private EditText edtPhone;
	@ViewInject(R.id.edt_pwd)
	private EditText edtPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView() {
		addBack(R.id.rl_back);
		setTitle("修改手机号");
		
		UserOldBean bean = PreManager.get(getApplicationContext(), AppContext.KEY_LOGINUSER,UserOldBean.class);
		if (bean != null && !TextUtils.isEmpty(bean.getMobile())) {
			edtPhone.setText(bean.getMobile());
		}
		
		setRightTextClickListener("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(edtPhone.getText().toString())
						|| TextUtils.isEmpty(edtPwd.getText().toString())) {
					CommonUtils.showToast("请输入完整");
					return;
				}
				pushEventBlock(EventCode.HTTP_MODIFYPHONENUMBER, edtPhone.getText().toString(),edtPwd.getText().toString());
			}
		});
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_MODIFYPHONENUMBER) {
			if (event.isSuccess()) {
				BaseResponseBean bean = (BaseResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					CommonUtils.showToast(bean.getMsg());
				}
				finish();
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}	
}
