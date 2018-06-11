package com.app.yuqing.net.httprunner;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.GroupListResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class QueryGroupHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		String userId = (String) event.getParamAtIndex(0);
		
        RequestBody body=new FormBody.Builder()
        .add("userId",userId).build();
        
        String result = OKHttpUtil.getInstance().request(URLUtils.QUERYGROUP, body, "post",false);
		
		Log.i(AppContext.LOG_NET, result);
		System.out.println("result : "+result);
		if (CommonUtils.isEmpty(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("网络错误"));
			return;
		}

		
		if ("false".equals(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("创建失败"));
		} else {
			GroupListResponseBean bean = gson.fromJson(result, GroupListResponseBean.class);
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
