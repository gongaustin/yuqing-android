package com.app.yuqing.activity;

import com.app.yuqing.R;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.ChangePwdResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ChangePwdActivity extends BaseActivity {

	@ViewInject(R.id.edt_newpwdagain)
	private EditText edtNewPwdAgain;
	@ViewInject(R.id.edt_oldpwd)
	private EditText edtOldPwd;
	@ViewInject(R.id.edt_newpwd)
	private EditText edtNewPwd;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView() {
		addBack(R.id.rl_back);
		setTitle("修改密码");
		setRightTextClickListener("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(edtOldPwd.getText().toString()) 
						|| TextUtils.isEmpty(edtNewPwd.getText().toString())
						|| TextUtils.isEmpty(edtNewPwdAgain.getText().toString())) {
					CommonUtils.showToast("请输入完整");
					return;
				}
				if (!(edtNewPwd.getText().toString()).equals(edtNewPwdAgain.getText().toString())) {
					CommonUtils.showToast("两次密码输入不一致");
					return;
				}
				pushEventBlock(EventCode.HTTP_UPDATEPASSWORD, edtOldPwd.getText().toString(),edtNewPwd.getText().toString());
			}
		});
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_UPDATEPASSWORD) {
			if (event.isSuccess()) {
				ChangePwdResponseBean bean = (ChangePwdResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					CommonUtils.showToast(bean.getMessage());
				}
				finish();
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}
}
