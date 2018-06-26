package com.app.yuqing.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GroupInfoProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.PersonalInfoResponseBean;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.btn_login)
	private Button btnLogin;
	@ViewInject(R.id.edt_username)
	private EditText edtUsername;
	@ViewInject(R.id.edt_pwd)
	private EditText edtPwd;	
	@ViewInject(R.id.rl_delete)
	private RelativeLayout rlDelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
	}
	
	private void initView() {
		String account = PreManager.getString(getApplicationContext(), AppContext.USER_ACCOUNT);
		if (!TextUtils.isEmpty(account)) {
			edtUsername.setText(account);		
		}
	}
	
	private void initListener() {
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(edtUsername.getText().toString()) || TextUtils.isEmpty(edtPwd.getText().toString())) {
					CommonUtils.showToast("请输入完整");
					return;
				}
				pushEventNoProgress(EventCode.HTTP_LOGIN, edtUsername.getText().toString(),edtPwd.getText().toString());
			}
		});
		
		rlDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtPwd.setText("");
			}
		});
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_LOGIN) {
			if (event.isSuccess()) {
				UserResponseBean bean = (UserResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					PreManager.putString(getApplication(),AppContext.KEY_TOKEN,bean.getData());
					PreManager.putString(getApplicationContext(), AppContext.USER_ACCOUNT, edtUsername.getText().toString());
					PreManager.putString(getApplicationContext(), AppContext.USER_KEY, edtPwd.getText().toString());

					pushEventNoProgress(EventCode.HTTP_GETTOKEN);
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}

		if (event.getEventCode() == EventCode.HTTP_GETTOKEN) {
			if (event.isSuccess()) {
				UserResponseBean bean = (UserResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					pushEventNoProgress(EventCode.HTTP_PERSONALINFO);
					if (!TextUtils.isEmpty(bean.getData())) {
						connect(bean.getData());
					}
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}

		if (event.getEventCode() == EventCode.HTTP_PERSONALINFO) {
			if (event.isSuccess()) {
				PersonalInfoResponseBean bean = (PersonalInfoResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null) {
					PreManager.put(getApplicationContext(),AppContext.KEY_LOGINUSER,bean.getData());

					Uri headUri = Uri.parse(bean.getData().getAvatar());
	                UserInfo userInfo = new UserInfo(bean.getData().getUserId(), bean.getData().getRealname(),headUri);
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
				}

			} else {
				CommonUtils.showToast(event.getFailMessage());
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

			showXProgressDialog();
	        RongIM.connect(token, new RongIMClient.ConnectCallback() {

	            /**
	             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
	             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
	             */
	            @Override
	            public void onTokenIncorrect() {
					dismissXProgressDialog();
	            }

	            /**
	             * 连接融云成功
	             * @param userid 当前 token 对应的用户 id
	             */
	            @Override
	            public void onSuccess(String userid) {
					dismissXProgressDialog();
	                Log.d("LoginActivity", "--onSuccess" + userid);
					CommonUtils.showToast("登录成功");
	                startActivity(new Intent(LoginActivity.this, MainActivity.class));
	                finish();
	            }

	            /**
	             * 连接融云失败
	             * @param errorCode 错误码，可到官网 查看错误码对应的注释
	             */
	            @Override
	            public void onError(RongIMClient.ErrorCode errorCode) {
					dismissXProgressDialog();
	            }
	        });
	    }
	}
}
