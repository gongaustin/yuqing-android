package com.app.yuqing.net.bean;

import com.app.yuqing.bean.PersonalBean;

/**
 * Created by Administrator on 2018/6/5.
 */

public class PersonalInfoResponseBean extends BaseResponseBean {

    private PersonalBean data;

    public PersonalBean getData() {
        return data;
    }

    public void setData(PersonalBean data) {
        this.data = data;
    }
}
