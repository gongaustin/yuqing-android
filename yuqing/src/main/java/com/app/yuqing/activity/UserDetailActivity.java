package com.app.yuqing.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation.ConversationType;

import com.app.yuqing.R;
import com.app.yuqing.bean.UserBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.net.bean.UserDetailResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.ImageLoaderUtil;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
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
	private UserBean userBean;
	private TokenBean tokenBean;
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
				if (userBean == null || tokenBean == null) {
					CommonUtils.showToast("信息为空");
					return;
				}
				RongIM.getInstance().startPrivateChat(UserDetailActivity.this, tokenBean.getUserId(), userBean.getName());
//				RongIM.getInstance().startConversation(UserDetailActivity.this, ConversationType.PRIVATE, tokenBean.getUserId(), userBean.getName());
			}
		});
	}
	
	private void initData() {
		userId = getIntent().getStringExtra(KEY_ID);
		if (!TextUtils.isEmpty(userId)) {
			pushEvent(EventCode.HTTP_QUERYUSERBYID, userId);			
		}
	}
	
	private void refreshView(UserBean bean) {
		userBean = bean;
		if (!TextUtils.isEmpty(bean.getPhoto())) {
			ImageLoaderUtil.display(bean.getPhoto(),ivHead);
		}
		if (!TextUtils.isEmpty(bean.getLoginName())) {
			tvUserName.setText(bean.getName());
			setTitle(bean.getName());
		}
		if (!TextUtils.isEmpty(bean.getMobile())) {
			tvNumber.setText(bean.getMobile());
		}
		if (!TextUtils.isEmpty(bean.getRoleNames())) {
			tvRole.setText(bean.getRoleNames());
		}
		pushEventNoProgress(EventCode.HTTP_GETTOKEN, bean.getId(),bean.getName(),bean.getPhoto());
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_QUERYUSERBYID) {
			if (event.isSuccess()) {
				UserDetailResponseBean bean = (UserDetailResponseBean) event.getReturnParamAtIndex(0);
				if (bean.getData() != null) {
					refreshView(bean.getData());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		
		if (event.getEventCode() == EventCode.HTTP_GETTOKEN) {
			if (event.isSuccess()) {
				TokenBean bean = (TokenBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					tokenBean = bean;
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}			
	}
}
