package com.app.yuqing.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GroupInfoProvider;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

import java.util.ArrayList;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.bean.GroupBean;
import com.app.yuqing.fragment.BaseFragment;
import com.app.yuqing.fragment.ContactFragment;
import com.app.yuqing.fragment.MeFragment;
import com.app.yuqing.fragment.MessageFragment;
import com.app.yuqing.fragment.WorkFragment;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.GroupListResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.net.bean.VersionBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.view.BaseDialog.DialogListener;
import com.app.yuqing.view.VersionUpdateDialog;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	@ViewInject(R.id.message_icon)
	private RadioButton rbMessage;
	@ViewInject(R.id.work_icon)
	private RadioButton rbWork;
	@ViewInject(R.id.contact_icon)
	private RadioButton rbContact;
	@ViewInject(R.id.me_icon)
	private RadioButton rbMe;
	@ViewInject(R.id.tv_msg1)
	private TextView tvMsg;
	
	@ViewInject(R.id.container)
	private FrameLayout container;
	
	private FragmentManager fragmentManager;
	
	private MessageFragment messageFragment;
	private WorkFragment workFragment;
	private ContactFragment contactFragment;
	private MeFragment meFragment;
	
	private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
	
	private VersionUpdateDialog versionUpdateDialog;
	private UserResponseBean loginBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initFragment();
		initView();
		getData();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int count = RongIM.getInstance().getTotalUnreadCount();
		if (count > 0) {
			tvMsg.setVisibility(View.VISIBLE);
			tvMsg.setText(count + "");
		} else {
			tvMsg.setVisibility(View.GONE);
		}
	}
	
	private void initFragment() {
		fragmentManager = getSupportFragmentManager();
		
		messageFragment = new MessageFragment();
		workFragment = new WorkFragment();
		contactFragment = new ContactFragment();
		meFragment = new MeFragment();
		fragments.add(messageFragment);
		fragments.add(workFragment);
		fragments.add(contactFragment);
		fragments.add(meFragment);
		
		FragmentTransaction ft = fragmentManager.beginTransaction();
        for(BaseFragment fragment : fragments) {
            ft.add(R.id.container, fragment);
        }
        ft.commit();
	}
	
	private void initView() {
		rbMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageChanged(0);
			}
		});
		
		rbWork.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageChanged(1);
			}
		});
		
		rbContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageChanged(2);
			}
		});
		
		rbMe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageChanged(3);
			}
		});		
		
		rbMessage.performClick();
	}	
	
	private void pageChanged(int index) {
		showFragment(index);
		switch (index)
		{
	        case 0:
	        	rbMessage.setSelected(true);
	        	rbWork.setSelected(false);
	        	rbContact.setSelected(false);
	        	rbMe.setSelected(false);
	            break;
	        case 1:
	        	rbMessage.setSelected(false);
	        	rbWork.setSelected(true);
	        	rbContact.setSelected(false);
	        	rbMe.setSelected(false);
	            break;
	        case 2:
	        	rbMessage.setSelected(false);
	        	rbWork.setSelected(false);
	        	rbContact.setSelected(true);
	        	rbMe.setSelected(false);
	            break;
	        case 3:
	        	rbMessage.setSelected(false);
	        	rbWork.setSelected(false);
	        	rbContact.setSelected(false);
	        	rbMe.setSelected(true);
	            break;	 
        }
	}
	
    public void showFragment(int position) {
        hideFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragments.get(position));
        ft.commit();
    }
    
    public void hideFragment() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for(BaseFragment fragment : fragments) {
            if(fragment != null)
                ft.hide(fragment);
        }
        ft.commit();
    }
    
    public void getData() {
    	pushEventNoProgress(EventCode.HTTP_QUERYLASTVERSION);
    	
    	loginBean = PreManager.get(getApplicationContext(), AppContext.KEY_LOGINUSER, UserResponseBean.class);
		if (loginBean != null && loginBean.getUser() != null && !TextUtils.isEmpty(loginBean.getUser().getId())) {
			pushEvent(EventCode.HTTP_QUERYGROUP, loginBean.getUser().getId());			
		}
    }
    
    @Override
    public void onEventRunEnd(Event event) {
    	// TODO Auto-generated method stub
    	super.onEventRunEnd(event);
    	if (event.getEventCode() == EventCode.HTTP_QUERYLASTVERSION) {
			if (event.isSuccess()) {
				final VersionBean bean = (VersionBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					if (!CommonUtils.checkVersionIsSame(bean.getVersion())) {
						meFragment.tvVersion.setText("有新版本");
						if (versionUpdateDialog == null) {
							versionUpdateDialog = new VersionUpdateDialog(MainActivity.this);
						}
						versionUpdateDialog.setData(bean.getDesc());
						versionUpdateDialog.setListener(new DialogListener() {
							
							@Override
							public void update(Object object) {
								if ("true".equals((String)object)) {
									Intent intent = new Intent(Intent.ACTION_VIEW);
									intent.setData(Uri.parse(bean.getUrl()));
									startActivity(intent);
								}
							}
						});
						versionUpdateDialog.show();
					} else {
						meFragment.tvVersion.setText("已经是最新版本");
					}
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
    	
		if (event.getEventCode() == EventCode.HTTP_QUERYGROUP) {
			if (event.isSuccess()) {
				GroupListResponseBean bean = (GroupListResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getGroups() != null) {
					StringBuffer sbf = new StringBuffer();
					for(GroupBean gb : bean.getGroups()) {
						PreManager.putString(getApplicationContext(), gb.getGroupId(), gb.getGroupName());
						sbf.append(gb.getGroupId()).append(",");
					}
					String groupId = sbf.toString();
					if (groupId.endsWith(",")) {
						groupId = groupId.substring(0, groupId.length() - 1);
					}
					if (loginBean != null && loginBean.getUser() != null && !TextUtils.isEmpty(loginBean.getUser().getId())) {
						pushEvent(EventCode.HTTP_SYNC, groupId,loginBean.getUser().getId());			
					}
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		
		if (event.getEventCode() == EventCode.HTTP_SYNC) {
			if (event.isSuccess()) {
				CommonUtils.showToast("刷新成功");
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
    }
    
	private Long backCode = 0l;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if((keyCode == KeyEvent.KEYCODE_BACK)){
			if((System.currentTimeMillis() - backCode < 2000)){
				exitProgram();
			}else{
				backCode = System.currentTimeMillis();
				CommonUtils.showToast("再点一次退出程序");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void exitProgram(){
		Intent intent=new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}	
}