package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.BaseRongYunResponseBean;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.utils.CommonUtils;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/2/7.
 */

public class GetuiTestHttpRunner extends HttpRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String clientId = (String) event.getParamAtIndex(0);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("clientId", clientId);

//		String result = HttpUtils.doPost(URLUtils.GETTOKEN, map,false);
//		String result = HttpsUtil.doPost(URLUtils.GETTOKEN, map);

        RequestBody body=new FormBody.Builder().build();

        String result = OKHttpUtil.getInstance().request(URLUtils.GETUITEST+"?clientId="+clientId, body, "get",false);

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
            event.setSuccess(true);
//            BaseRongYunResponseBean bean = gson.fromJson(result, BaseRongYunResponseBean.class);
//            if (bean != null) {
//                if (checkRongYun(bean)) {
//                    event.setSuccess(true);
//                    event.addReturnParam(bean);
//                    return;
//                }
//            }
//            event.setSuccess(false);
        }
    }
}
