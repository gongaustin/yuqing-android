package com.app.yuqing.net.httprunner;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;

import android.util.Log;

public class LoginHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
		String username = (String) event.getParamAtIndex(0);
		String password = (String) event.getParamAtIndex(1);
		String validateCode = (String) event.getParamAtIndex(2);
		
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("username", username);
//		map.put("password", password);
//		map.put("isAjax", "true");
//		map.put("validateCode", validateCode);
//		
//		String result = HttpUtils.doPost(URLUtils.LOGIN, map,false);
		
        RequestBody body=new FormBody.Builder()
        .add("username",username)
        .add("password",password)
        .add("isAjax","true").build();
        
        System.out.println("username:"+username+" password:"+password);
        
        String result = OKHttpUtil.getInstance().request(URLUtils.LOGIN, body, "post",true);
		
		Log.i(AppContext.LOG_NET, result);
		System.out.println("result : "+result);
		if (CommonUtils.isEmpty(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("网络错误"));
			return;
		}
		
		if ("false".equals(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("登录失败"));
		} else {
			UserResponseBean bean = gson.fromJson(result, UserResponseBean.class);
			if (bean.isSuccess()) {
				event.setSuccess(true);
				event.addReturnParam(bean);
			} else {
				event.setSuccess(false);
				event.setFailException(new Exception("登录失败"));
			}
		}
	}

}
