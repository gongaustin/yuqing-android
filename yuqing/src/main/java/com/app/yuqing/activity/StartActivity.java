package com.app.yuqing.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GroupInfoProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

public class StartActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
	                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		super.onCreate(savedInstanceState);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				String account = PreManager.getString(getApplicationContext(), AppContext.USER_ACCOUNT);
				String password = PreManager.getString(getApplicationContext(), AppContext.USER_KEY);
				if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
					Intent intent = new Intent(StartActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					pushEventNoProgress(EventCode.HTTP_LOGIN, account,password);
				}

			}
		}, 3000);
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_LOGIN) {
			if (event.isSuccess()) {
				UserResponseBean bean = (UserResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					PreManager.put(getApplicationContext(), AppContext.KEY_LOGINUSER, bean);
					
					Uri headUri = Uri.parse(bean.getUser().getPhoto());
	                UserInfo userInfo = new UserInfo(bean.getUser().getId(), bean.getUser().getName(),headUri);
	                RongIM.getInstance().setCurrentUserInfo(userInfo);
					RongIM.setGroupInfoProvider(new GroupInfoProvider() {
						
						@Override
						public Group getGroupInfo(String id) {
							// TODO Auto-generated method stub
							String name = PreManager.getString(getApplicationContext(), id);
							Group mGroup = new Group(id, name, null);
							return mGroup;
						}
					}, false);
					
					pushEventNoProgress(EventCode.HTTP_GETTOKEN, bean.getUser().getId(),bean.getUser().getName(),bean.getUser().getPhoto());
				}
			} else {
				Intent intent = new Intent(StartActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
		
		if (event.getEventCode() == EventCode.HTTP_GETTOKEN) {
			if (event.isSuccess()) {
				TokenBean bean = (TokenBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					connect(bean.getToken());
				}
			} else {
				Intent intent = new Intent(StartActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}		
	}
	
	/**
	 * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
	 * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
	 * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
	 *
	 * @param token    从服务端获取的用户身份令牌（Token）。
	 * @param callback 连接回调。
	 * @return RongIM  客户端核心类的实例。
	 */
	private void connect(String token) {

	    if (getApplicationInfo().packageName.equals(CommonUtils.getCurProcessName(getApplicationContext()))) {

	        RongIM.connect(token, new RongIMClient.ConnectCallback() {

	            /**
	             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
	             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
	             */
	            @Override
	            public void onTokenIncorrect() {
					Intent intent = new Intent(StartActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
	            }

	            /**
	             * 连接融云成功
	             * @param userid 当前 token 对应的用户 id
	             */
	            @Override
	            public void onSuccess(String userid) {
	                Log.d("LoginActivity", "--onSuccess" + userid);
	                startActivity(new Intent(StartActivity.this, MainActivity.class));
	                finish();
	            }

	            /**
	             * 连接融云失败
	             * @param errorCode 错误码，可到官网 查看错误码对应的注释
	             */
	            @Override
	            public void onError(RongIMClient.ErrorCode errorCode) {
					Intent intent = new Intent(StartActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
	            }
	        });
	    }
	}
}
