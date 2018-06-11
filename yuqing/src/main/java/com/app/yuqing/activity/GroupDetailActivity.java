package com.app.yuqing.activity;

import java.util.ArrayList;
import java.util.List;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.adapter.GroupUserAdapter;
import com.app.yuqing.bean.GroupBean;
import com.app.yuqing.bean.GroupInfoBean;
import com.app.yuqing.bean.GroupMemberBean;
import com.app.yuqing.bean.GroupUserBean;
import com.app.yuqing.bean.PersonalBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.GroupInfoResponseBean;
import com.app.yuqing.net.bean.GroupUserResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.view.MyGridView;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

public class GroupDetailActivity extends BaseActivity {
	@ViewInject(R.id.mgv_users)
	private MyGridView mvUser;
	@ViewInject(R.id.btn_exit)
	private Button btnExit;
	
	private List<GroupMemberBean> dataList = new ArrayList<GroupMemberBean>();
	private GroupUserAdapter adapter;
	public static final String KEY_GROUP = "key_GroupDetailActivity_group";
	private GroupInfoBean groupBean;
	
	public static final String KEY_ADDRESULT = "key_GroupDetailActivity_addresult";
	private static final int CODE_ADDUSER = 0x11;
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
		setTitle("群成员");
		
		adapter = new GroupUserAdapter(GroupDetailActivity.this, dataList);
		mvUser.setAdapter(adapter);
	}
	
	private void initListener() {
		mvUser.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == dataList.size() - 1) {
					Intent intent = new Intent(GroupDetailActivity.this,CreateGroupActivity.class);
					intent.putExtra(CreateGroupActivity.KEY_ADDUSER, CreateGroupActivity.TYPE_ADDUSER);
					startActivityForResult(intent, CODE_ADDUSER);
				}
			}
		});
		
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PersonalBean bean = PreManager.get(getApplicationContext(), AppContext.KEY_LOGINUSER, PersonalBean.class);
				if (bean != null && !TextUtils.isEmpty(bean.getUserId()) && groupBean != null && !TextUtils.isEmpty(groupBean.getGroupId()) ) {
					if (bean.getUserId().equals(groupBean.getCreateUser())) {
						pushEventBlock(EventCode.HTTP_DISMISSGROUP, groupBean.getGroupId(),bean.getUserId());
						return;
					} else {
						pushEventBlock(EventCode.HTTP_QUITGROUP, groupBean.getGroupId());
					}
				}

			}
		});
	}	
	
	private void initData() {
		groupBean = (GroupInfoBean) getIntent().getSerializableExtra(KEY_GROUP);
		refreshData();
	}

	private void refreshData() {
		if (groupBean != null && groupBean.getMembers() != null) {
			GroupMemberBean groupMemberBean = new GroupMemberBean();
			groupMemberBean.setRealname("添加新成员");
			groupBean.getMembers().add(groupMemberBean);
			adapter.updateData(groupBean.getMembers());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == CODE_ADDUSER) {
				if (data != null) {
					String userId = data.getStringExtra(KEY_ADDRESULT);
					if (!TextUtils.isEmpty(userId) && groupBean != null && !TextUtils.isEmpty(groupBean.getGroupId())) {
						pushEventBlock(EventCode.HTTP_GROUPJOIN,groupBean.getGroupId(),userId);
					}
				}
			}
		}
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		
		if (event.getEventCode() == EventCode.HTTP_GROUPJOIN) {
			if (event.isSuccess()) {
				if (groupBean != null && !TextUtils.isEmpty(groupBean.getGroupId())) {
					pushEventNoProgress(EventCode.HTTP_GETGROUPINFO, groupBean.getGroupId());			
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}

		if (event.getEventCode() == EventCode.HTTP_GETGROUPINFO) {
			if (event.isSuccess()) {
				GroupInfoResponseBean bean = (GroupInfoResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null) {
					groupBean = bean.getData();
					refreshData();
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		
		if (event.getEventCode() == EventCode.HTTP_QUITGROUP) {
			if (event.isSuccess()) {
				Intent intent = new Intent(GroupDetailActivity.this,MainActivity.class);
				startActivity(intent);
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}	
		
		if (event.getEventCode() == EventCode.HTTP_DISMISSGROUP) {
			if (event.isSuccess()) {
				Intent intent = new Intent(GroupDetailActivity.this,MainActivity.class);
				startActivity(intent);
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}			
	}

}
