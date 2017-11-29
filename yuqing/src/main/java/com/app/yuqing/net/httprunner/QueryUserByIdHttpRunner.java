package com.app.yuqing.net.httprunner;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.UserDetailResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class QueryUserByIdHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
		String id = (String) event.getParamAtIndex(0);
		
        RequestBody body=new FormBody.Builder()
        .add("id",id).build();
        
        String result = OKHttpUtil.getInstance().request(URLUtils.QUERYUSERBYID, body, "post",false);
		
		Log.i(AppContext.LOG_NET, result);
		System.out.println("result : "+result);
		if (CommonUtils.isEmpty(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("网络错误"));
			return;
		}
		
		if ("false".equals(result)) {
			event.setSuccess(false);
		} else {
			UserDetailResponseBean bean = gson.fromJson(result, UserDetailResponseBean.class);
			if ("true".equals(bean.getFlag())) {
				event.setSuccess(true);
				event.addReturnParam(bean);				
			} else {
				event.setSuccess(false);	
			}
		}	
	}

}
