package com.app.yuqing.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.adapter.CreateGroupAdapter;
import com.app.yuqing.bean.ContactUser;
import com.app.yuqing.bean.TreeDatabean;
import com.app.yuqing.bean.UserBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.ContactUserResponsebean;
import com.app.yuqing.net.bean.QueryUserResponseBean;
import com.app.yuqing.net.bean.TreeDataResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.view.ChangeGroupNameDialog;
import com.app.yuqing.view.BaseDialog.DialogListener;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CreateGroupActivity extends BaseActivity {

	private ListView lvContact;
	private CreateGroupAdapter adapter;
	private List<UserBean> userList = new ArrayList<UserBean>();
	private List<UserBean> cacheList = new ArrayList<UserBean>();
	
	public static final String TYPE_ADDUSER = "addUser";
	public static final String KEY_ADDUSER = "key_CreateGroupActivity_addUser";
	private String type;
	
	private ChangeGroupNameDialog changeGroupNameDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		type = getIntent().getStringExtra(KEY_ADDUSER);
		initView();
		initListener();
		getData();
	}
	
	private void initView() {
		addBack(R.id.rl_back);
		if (TYPE_ADDUSER.equals(type)) {
			setTitle("选择群成员");
		} else {
			setTitle("选择群成员");			
		}

		lvContact = (ListView) findViewById(R.id.lv_data);
		
		adapter = new CreateGroupAdapter(CreateGroupActivity.this, userList);
		lvContact.setAdapter(adapter);
		
	}
	
	private void initListener() {
		setRightTextClickListener("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (cacheList.size() > 0) {
					if (TYPE_ADDUSER.equals(type)) {
						StringBuffer sbID = new StringBuffer();
						for(UserBean cu : cacheList) {
							sbID.append(cu.getUserId()).append(",");
						}
						String userIds = sbID.toString();
						if (userIds.endsWith(",")) {
							userIds = userIds.substring(0, userIds.length() - 1);
						}
						Intent data = new Intent();
						data.putExtra(GroupDetailActivity.KEY_ADDRESULT, userIds);
						setResult(Activity.RESULT_OK, data);
						finish();
					} else {
						if (changeGroupNameDialog == null) {
							changeGroupNameDialog = new ChangeGroupNameDialog(CreateGroupActivity.this);
						}
						changeGroupNameDialog.setListener(new DialogListener() {
							
							@Override
							public void update(Object object) {
								String name = (String) object;
								if (!TextUtils.isEmpty(name)) {
									String userId = "";
									StringBuffer sbID = new StringBuffer();
									for(UserBean cu : cacheList) {
										sbID.append(cu.getUserId()).append(",");
									}
									userId = sbID.toString();
									
									if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(name)) {
										CommonUtils.showToast("信息不完整");
										return;
									}
									
									pushEventBlock(EventCode.HTTP_GROUPCREATE, name,userId);
								}
							}
						});
						changeGroupNameDialog.show();
					}
				} else {
					CommonUtils.showToast("请选择群成员");
				}
			}
		});
		
		lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserBean user = userList.get(position);
				if (user.isSelected()) {
					user.setSelected(false);
					Iterator<UserBean> itrator = cacheList.iterator();
					while(itrator.hasNext()) {
						UserBean cu = itrator.next();
						if (user.getUserId().equals(cu.getUserId())) {
							itrator.remove();
						}
					}
				} else {
					user.setSelected(true);
					if (!cacheList.contains(user)) {
						cacheList.add(user);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void getData() {
		pushEventNoProgress(EventCode.HTTP_QUERYUSER);
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_QUERYUSER) {
			if (event.isSuccess()) {
				QueryUserResponseBean bean = (QueryUserResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null && bean.getData().size() > 2) {
					userList.clear();
					userList.addAll(bean.getData());
					adapter.notifyDataSetChanged();
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		if (event.getEventCode() == EventCode.HTTP_GROUPCREATE) {
			if (event.isSuccess()) {
				finish();
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}		
	}
}
