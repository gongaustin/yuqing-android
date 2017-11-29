package com.app.yuqing.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.adapter.ContactAdapter;
import com.app.yuqing.adapter.CreateGroupAdapter;
import com.app.yuqing.bean.ContactUser;
import com.app.yuqing.bean.TreeDatabean;
import com.app.yuqing.bean.UserBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.ContactUserResponsebean;
import com.app.yuqing.net.bean.TreeDataResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.view.ChangeGroupNameDialog;
import com.app.yuqing.view.BaseDialog.DialogListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CreateGroupActivity extends BaseActivity {

	private TextView tvFGLD;
	private TextView tvXCB;
	private TextView tvXZ;
	private ListView lvContact;
	private CreateGroupAdapter adapter;
	private List<ContactUser> userList = new ArrayList<ContactUser>();
	private List<ContactUser> cacheList = new ArrayList<ContactUser>();
	
	private List<TreeDatabean> dataList = new ArrayList<TreeDatabean>();
	private int currentIndex = 0;
	
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
		
		tvFGLD = (TextView) findViewById(R.id.tv_fgld);
		tvXCB = (TextView) findViewById(R.id.tv_xcb);
		tvXZ = (TextView) findViewById(R.id.tv_xz);
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
						for(ContactUser cu : cacheList) {
							sbID.append(cu.getId()).append(",");
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
									String masterId = "";
									String userId = "";
									String groupId = CommonUtils.getAndroidId(getApplicationContext())+System.currentTimeMillis();
									
									UserResponseBean bean = PreManager.get(getApplicationContext(), AppContext.KEY_LOGINUSER, UserResponseBean.class);
									if (bean != null) {
										masterId = bean.getUser().getId();
									}
									StringBuffer sbID = new StringBuffer();
									for(ContactUser cu : cacheList) {
										sbID.append(cu.getId()).append(",");
									}
									sbID.append(masterId);
									userId = sbID.toString();
									
									if (TextUtils.isEmpty(masterId) || TextUtils.isEmpty(userId) || TextUtils.isEmpty(groupId) || TextUtils.isEmpty(name)) {
										CommonUtils.showToast("信息不完整");
										return;
									}
									
									pushEventBlock(EventCode.HTTP_CREATEGROUP, masterId,userId,groupId,name);
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
		
		tvFGLD.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageClick(0);
			}
		});
		
		tvXCB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageClick(1);
			}
		});	
		
		tvXZ.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageClick(2);
			}
		});	
		tvFGLD.performClick();
		
		lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContactUser user = userList.get(position);
				if (user.isSelected()) {
					user.setSelected(false);
					Iterator<ContactUser> itrator = cacheList.iterator();
					while(itrator.hasNext()) {
						ContactUser cu = itrator.next();
						if (user.getId().equals(cu.getId())) {
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
	
	private void pageClick(int index) {
		currentIndex = index;
		switch (index) {
		case 0:
			refreshView();
			tvFGLD.setSelected(true);
			tvXCB.setSelected(false);
			tvXZ.setSelected(false);
			break;
			
		case 1:
			refreshView();
			tvFGLD.setSelected(false);
			tvXCB.setSelected(true);
			tvXZ.setSelected(false);
			break;
			
		case 2:
			refreshView();
			tvFGLD.setSelected(false);
			tvXCB.setSelected(false);
			tvXZ.setSelected(true);
			break;			

		default:
			break;
		}
	}
	
	private void getData() {
		pushEventNoProgress(EventCode.HTTP_TREEDATA);
	}
	
	private void refreshView() {
		if (dataList != null && dataList.size() > 2) {
			tvFGLD.setText(dataList.get(0).getName());
			tvXCB.setText(dataList.get(1).getName());
			tvXZ.setText(dataList.get(2).getName());
			if (currentIndex == 0) {
				pushEventNoProgress(EventCode.HTTP_QUERYUSERBYOFFICEID, dataList.get(0).getId());
			} else if (currentIndex == 1) {
				pushEventNoProgress(EventCode.HTTP_QUERYUSERBYOFFICEID, dataList.get(1).getId());
			} else if (currentIndex == 2) {
				pushEventNoProgress(EventCode.HTTP_QUERYUSERBYOFFICEID, dataList.get(2).getId());
			}
		}
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_TREEDATA) {
			if (event.isSuccess()) {
				TreeDataResponseBean bean = (TreeDataResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null && bean.getData().size() > 2) {
					dataList.clear();
					dataList.addAll(bean.getData());
					refreshView();
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		if (event.getEventCode() == EventCode.HTTP_QUERYUSERBYOFFICEID) {
			if (event.isSuccess()) {
				ContactUserResponsebean bean = (ContactUserResponsebean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null) {
					for(ContactUser user : bean.getData()) {
						for(ContactUser cacheUser : cacheList) {
							if(user.getId().equals(cacheUser.getId())) {
								user.setSelected(true);
							}
						}
					}
					adapter.updateData(bean.getData());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		if (event.getEventCode() == EventCode.HTTP_CREATEGROUP) {
			if (event.isSuccess()) {
				finish();
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}		
	}
}
