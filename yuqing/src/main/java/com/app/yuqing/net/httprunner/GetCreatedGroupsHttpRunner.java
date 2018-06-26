package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.GetGroupsResponseBean;
import com.app.yuqing.utils.CommonUtils;

/**
 * Created by Administrator on 2018/6/12.
 */

public class GetCreatedGroupsHttpRunner extends HttpRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String url = URLUtils.GETCREATEDGROUPS;
        String result = OKHttpUtil.getInstance().request(url, null, "get",false);

        Log.i(AppContext.LOG_NET, result);
        System.out.println("result : "+result);
        if (CommonUtils.isEmpty(result)) {
            event.setSuccess(false);
            event.setFailException(new Exception("网络错误"));
            return;
        }

        GetGroupsResponseBean bean = gson.fromJson(result, GetGroupsResponseBean.class);

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
