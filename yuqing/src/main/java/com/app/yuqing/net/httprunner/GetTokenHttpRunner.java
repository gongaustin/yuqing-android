package com.app.yuqing.net.httprunner;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.HttpsUtil;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.utils.CommonUtils;

public class GetTokenHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
		String userId = (String) event.getParamAtIndex(0);
		String name = (String) event.getParamAtIndex(1);
		String portraitUri = (String) event.getParamAtIndex(2);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("name", name);
		map.put("portraitUri", portraitUri);
		
//		String result = HttpUtils.doPost(URLUtils.GETTOKEN, map,false);
//		String result = HttpsUtil.doPost(URLUtils.GETTOKEN, map);
		
        RequestBody body=new FormBody.Builder()
        .add("userId",userId)
        .add("name",name)
        .add("portraitUri",portraitUri).build();
        
        String result = OKHttpUtil.getInstance().request(URLUtils.GETTOKEN, body, "post",false);
		
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
			TokenBean bean = gson.fromJson(result, TokenBean.class);
			if (bean != null) {
				if (checkRongYun(bean)) {
					event.setSuccess(true);
					event.addReturnParam(bean);
					return;
				}
			}
			event.setSuccess(false);
		}
	}

	
}
