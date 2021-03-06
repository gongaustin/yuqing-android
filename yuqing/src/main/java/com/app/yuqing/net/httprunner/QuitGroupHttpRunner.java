package com.app.yuqing.net.httprunner;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.GroupUserResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class QuitGroupHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
		String gid = (String) event.getParamAtIndex(0);
		
        RequestBody body=new FormBody.Builder()
        .add("gid",gid).build();
        
        String result = OKHttpUtil.getInstance().request(URLUtils.QUITGROUP, body, "post",false);
		
		Log.i(AppContext.LOG_NET, result);
		System.out.println("result : "+result);
		if (CommonUtils.isEmpty(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("网络错误"));
			return;
		}

		BaseResponseBean bean = gson.fromJson(result, BaseResponseBean.class);
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
