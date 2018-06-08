package com.app.yuqing.net.bean;

import com.app.yuqing.bean.UserBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 */

public class QueryUserResponseBean extends BaseResponseBean {
    private List<UserBean> data;

    public List<UserBean> getData() {
        return data;
    }

    public void setData(List<UserBean> data) {
        this.data = data;
    }
}
