package com.app.yuqing.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.app.yuqing.R;
import com.app.yuqing.activity.UserDetailActivity;
import com.app.yuqing.adapter.ContactAdapter;
import com.app.yuqing.bean.ContactUser;
import com.app.yuqing.bean.TreeDatabean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.ContactUserResponsebean;
import com.app.yuqing.net.bean.TreeDataResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class ContactFragment extends BaseFragment {

	private TextView tvFGLD;
	private TextView tvXCB;
	private TextView tvXZ;
	private TextView tvTitle;
	private ListView lvContact;
	private ContactAdapter adapter;
	private List<ContactUser> userList = new ArrayList<ContactUser>();
	
	private List<TreeDatabean> dataList = new ArrayList<TreeDatabean>();
	private int currentIndex = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_contact);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		initListener();
		getData();
		super.onActivityCreated(savedInstanceState);
	}
	
	private void initView() {
		tvFGLD = (TextView) contentView.findViewById(R.id.tv_fgld);
		tvXCB = (TextView) contentView.findViewById(R.id.tv_xcb);
		tvXZ = (TextView) contentView.findViewById(R.id.tv_xz);
		tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
		lvContact = (ListView) contentView.findViewById(R.id.lv_data);
		
		adapter = new ContactAdapter(getActivity(), userList);
		lvContact.setAdapter(adapter);
		
		tvTitle.setText("联系人");
	}
	
	private void initListener() {
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
				Intent intent = new Intent(getActivity(),UserDetailActivity.class);
				intent.putExtra(UserDetailActivity.KEY_ID, userList.get(position).getId());
				getActivity().startActivity(intent);
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
					adapter.updateData(bean.getData());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}
	
}
