package com.app.yuqing.net.bean;

import com.app.yuqing.bean.GroupBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */

public class GetGroupsResponseBean extends BaseResponseBean {

    private List<GroupBean> data;

    public List<GroupBean> getData() {
        return data;
    }

    public void setData(List<GroupBean> data) {
        this.data = data;
    }
}
