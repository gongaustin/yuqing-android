package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.ContactUserResponsebean;
import com.app.yuqing.net.bean.VersionBean;
import com.app.yuqing.utils.CommonUtils;

public class QueryUserByOfficeIdHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
		String officeId = (String) event.getParamAtIndex(0);
        String result = OKHttpUtil.getInstance().request(URLUtils.QUERYUSERBYOFFICEID+"?office.id="+officeId, null, "get",false);
		
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
			ContactUserResponsebean bean = gson.fromJson(result, ContactUserResponsebean.class);
			if ("true".equals(bean.getFlag())) {
				event.setSuccess(true);
				event.addReturnParam(bean);
			} else {
				event.setSuccess(false);
				event.setFailException(new Exception("登录失败"));
			}
		}
	}

}
