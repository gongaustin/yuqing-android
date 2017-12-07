package com.app.yuqing.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
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
import com.lidroid.xutils.view.annotation.ViewInject;

public class ContactFragment extends BaseFragment {

	@ViewInject(R.id.ll_tab)
	private LinearLayout llTab;

	private TextView tvTitle;
	private ListView lvContact;
	private ContactAdapter adapter;
	private List<ContactUser> userList = new ArrayList<ContactUser>();
	
	private List<TreeDatabean> dataList = new ArrayList<TreeDatabean>();
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
		tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
		lvContact = (ListView) contentView.findViewById(R.id.lv_data);
		
		adapter = new ContactAdapter(getActivity(), userList);
		lvContact.setAdapter(adapter);
		
		tvTitle.setText("联系人");
	}
	
	private void initListener() {
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
	
	private void getData() {
		pushEventNoProgress(EventCode.HTTP_TREEDATA);
	}

	private void refreshClick(int id) {
		System.out.println("点击ID："+id);
		int index = -1;
		for(int i = 0 ; i<llTab.getChildCount() ; i++) {
			if (llTab.getChildAt(i) instanceof  TextView) {
				if (llTab.getChildAt(i).getId() == id) {
					index = i;
					((TextView)llTab.getChildAt(i)).setTextColor(getResources().getColor(R.color.color_banner));
				}
			}
		}

		if (index != -1) {
			for(int i = 0 ; i<llTab.getChildCount() ; i++) {
				if (llTab.getChildAt(i) instanceof  TextView) {
					if (i != index) {
						((TextView)llTab.getChildAt(i)).setTextColor(getResources().getColor(R.color.color_worktext_gray));
					}
				}
			}
		}
	}

	private void refreshView() {
		llTab.removeAllViews();
		int index = 0;
		for(TreeDatabean bean : dataList) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
			params.weight = 1;
			params.gravity = Gravity.CENTER_VERTICAL;

			TextView tv = new TextView(llTab.getContext());
			tv.setTextSize(getResources().getDimension(R.dimen.textsize_mini));
			tv.setTextColor(getResources().getColor(R.color.color_worktext_gray));
			tv.setText(bean.getName());
			tv.setGravity(Gravity.CENTER);
			tv.setId(index);
			index++;

			final String id = bean.getId();
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					refreshClick(v.getId());
					userList.clear();
					adapter.notifyDataSetChanged();
					pushEventNoProgress(EventCode.HTTP_QUERYUSERBYOFFICEID, id);
				}
			});
			llTab.addView(tv,params);
		}

		if (llTab.getChildCount() != 0 && llTab.getChildAt(0) instanceof TextView) {
			((TextView)llTab.getChildAt(0)).performClick();
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
