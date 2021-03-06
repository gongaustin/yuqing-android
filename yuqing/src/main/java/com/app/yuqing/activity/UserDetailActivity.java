package com.app.yuqing.activity;

import io.rong.imkit.RongIM;

import com.app.yuqing.R;
import com.app.yuqing.bean.PersonalBean;
import com.app.yuqing.bean.UserOldBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.PersonalInfoResponseBean;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.net.bean.UserDetailResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.ImageLoaderUtil;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDetailActivity extends BaseActivity {

	@ViewInject(R.id.iv_head)
	private ImageView ivHead;
	@ViewInject(R.id.tv_username)
	private TextView tvUserName;
	@ViewInject(R.id.tv_role)
	private TextView tvRole;
	@ViewInject(R.id.tv_number)
	private TextView tvNumber;	
	
	public static final String KEY_ID = "key_UserDetailActivity_id";
	private String userId;
	private PersonalBean userBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		addBack(R.id.rl_back);
		
	}
	
	private void initListener() {
		ivHead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		setRightTextClickListener("发消息", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (userBean == null) {
					CommonUtils.showToast("信息为空");
					return;
				}
				RongIM.getInstance().startPrivateChat(UserDetailActivity.this, userBean.getUserId(), userBean.getRealname());
			}
		});
	}
	
	private void initData() {
		userId = getIntent().getStringExtra(KEY_ID);
		if (!TextUtils.isEmpty(userId)) {
			pushEvent(EventCode.HTTP_USERINFOBYUSERID, userId);
		}
	}
	
	private void refreshView(PersonalBean bean) {
		userBean = bean;
		if (!TextUtils.isEmpty(bean.getAvatar())) {
			ImageLoaderUtil.display(bean.getAvatar(),ivHead);
		}
		if (!TextUtils.isEmpty(bean.getRealname())) {
			tvUserName.setText(bean.getRealname());
			setTitle(bean.getRealname());
		}
		if (!TextUtils.isEmpty(bean.getPhone())) {
			tvNumber.setText(bean.getPhone());
		}
		if (!TextUtils.isEmpty(bean.getDeptName())) {
			tvRole.setText(bean.getDeptName());
		}
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_USERINFOBYUSERID) {
			if (event.isSuccess()) {
				PersonalInfoResponseBean bean = (PersonalInfoResponseBean) event.getReturnParamAtIndex(0);
				if (bean.getData() != null) {
					refreshView(bean.getData());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}
}
