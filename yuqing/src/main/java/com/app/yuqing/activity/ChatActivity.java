package com.app.yuqing.activity;

import java.util.Locale;

import io.rong.imlib.model.Conversation;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.bean.GroupBean;
import com.app.yuqing.bean.GroupInfoBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.GroupInfoResponseBean;
import com.app.yuqing.net.bean.GroupListResponseBean;
import com.app.yuqing.net.bean.PersonalInfoResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class ChatActivity extends BaseActivity {

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    /**
     * 会话targetId
     */
    private String targetId;
    
    private GroupInfoBean groupBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		addBack(R.id.rl_back);
		targetId = getIntent().getData().getQueryParameter("targetId");//id
		mConversationType = Conversation.ConversationType.valueOf(getIntent().getData()
                .getLastPathSegment().toUpperCase(Locale.US));
		
		String nickName = getIntent().getData().getQueryParameter("title");//昵称
		if (!TextUtils.isEmpty(nickName)) {
			setTitle(nickName);
		}
	}
	
	private void initListener() {
		 if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
	            setRightTextClickListener("群信息", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (groupBean == null || TextUtils.isEmpty(groupBean.getGroupId())) {
							CommonUtils.showToast("数据有误");
							return;
						}
						Intent intent = new Intent(ChatActivity.this,GroupDetailActivity.class);
						intent.putExtra(GroupDetailActivity.KEY_GROUP, groupBean);
						startActivity(intent);
					}
				});
	        } else if (mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
	            setRightTextClickListener("个人信息", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (TextUtils.isEmpty(targetId)) {
							CommonUtils.showToast("数据有误");
							return;
						}
						Intent intent = new Intent(ChatActivity.this,UserDetailActivity.class);
						intent.putExtra(UserDetailActivity.KEY_ID, targetId);
						startActivity(intent);
					}
				});
	        }
	}	
	
	private void initData() {
		if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
			if (!TextUtils.isEmpty(targetId)) {
				pushEventNoProgress(EventCode.HTTP_GETGROUPINFO,targetId);
			}
		} else if (mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
			if (!TextUtils.isEmpty(targetId)) {
				pushEvent(EventCode.HTTP_USERINFOBYUSERID, targetId);
			}
		}

	}
	
	private void refreshView(GroupInfoBean bean) {
		setTitle(bean.getGroupName());
		groupBean = bean;
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_GETGROUPINFO) {
			if (event.isSuccess()) {
				GroupInfoResponseBean bean = (GroupInfoResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null) {
					refreshView(bean.getData());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}

		if (event.getEventCode() == EventCode.HTTP_USERINFOBYUSERID) {
			if (event.isSuccess()) {
				PersonalInfoResponseBean bean = (PersonalInfoResponseBean) event.getReturnParamAtIndex(0);
				if (bean.getData() != null) {
					setTitle(bean.getData().getRealname());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}	
}
