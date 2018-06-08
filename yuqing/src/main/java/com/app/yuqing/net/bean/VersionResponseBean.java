package com.app.yuqing.net.bean;

import com.app.yuqing.bean.VersionBean;

public class VersionResponseBean extends BaseResponseBean {

	private VersionBean data;

	public VersionBean getData() {
		return data;
	}

	public void setData(VersionBean data) {
		this.data = data;
	}
}
