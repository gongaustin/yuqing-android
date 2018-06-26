package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.QueryUserResponseBean;
import com.app.yuqing.utils.CommonUtils;

/**
 * Created by Administrator on 2018/6/5.
 */

public class QueryUserHttpRunner extends HttpRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String result = OKHttpUtil.getInstance().request(URLUtils.QUERYUSER, null, "get",false);

        Log.i(AppContext.LOG_NET, result);
        System.out.println("result : "+result);
        if (CommonUtils.isEmpty(result)) {
            event.setSuccess(false);
            event.setFailException(new Exception("网络错误"));
            return;
        }

        QueryUserResponseBean bean = gson.fromJson(result, QueryUserResponseBean.class);
        if (!bean.isSuccess()) {
            event.setSuccess(false);
            event.setmErrorCode(bean.getCode());
        } else {
            event.setSuccess(true);
            event.addReturnParam(bean);
        }
    }
}
