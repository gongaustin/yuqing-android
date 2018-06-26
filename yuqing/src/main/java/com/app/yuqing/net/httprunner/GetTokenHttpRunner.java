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
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.GroupInfoResponseBean;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class GetTokenHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
        String result = OKHttpUtil.getInstance().request(URLUtils.GETTOKEN, null, "get",false);
		
		Log.i(AppContext.LOG_NET, result);
		System.out.println("result : "+result);
		if (CommonUtils.isEmpty(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("网络错误"));
			return;
		}

		UserResponseBean bean = gson.fromJson(result, UserResponseBean.class);

		if (!bean.isSuccess()) {
			event.setSuccess(false);
			event.setFailException(new Exception(bean.getMsg()));
			event.setmErrorCode(bean.getCode());
		} else {
			event.setSuccess(true);
			event.addReturnParam(bean);
		}
	}

	
}
