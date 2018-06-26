package com.app.yuqing.net.httprunner;

import android.text.TextUtils;
import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.VersionResponseBean;
import com.app.yuqing.utils.CommonUtils;

/**
 * Created by Administrator on 2018/6/6.
 */

public class QueryUpgrageHttpRunner extends HttpRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String id = (String) event.getParamAtIndex(0);
        String version = (String) event.getParamAtIndex(1);

        String result = OKHttpUtil.getInstance().request(URLUtils.QUERYUPGRAGE+"?id="+id+"&&version="+version, null, "get",false);

        Log.i(AppContext.LOG_NET, result);
        System.out.println("result : "+result);
        if (CommonUtils.isEmpty(result)) {
            event.setSuccess(false);
            event.setFailException(new Exception("网络错误"));
            return;
        }

        VersionResponseBean bean = gson.fromJson(result, VersionResponseBean.class);
        if (!bean.isSuccess()) {
            event.setSuccess(false);
            if (!TextUtils.isEmpty(bean.getMsg())) {
                event.setFailException(new Exception(bean.getMsg()));
            } else {
                event.setFailException(new Exception("未知错误"));
            }
            event.setmErrorCode(bean.getCode());
        } else {
            event.setSuccess(true);
            event.addReturnParam(bean);
        }
    }
}
