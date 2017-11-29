package com.app.yuqing.net.bean;

import java.util.List;

import com.app.yuqing.bean.TreeDatabean;

public class TreeDataResponseBean extends BaseResponseBean {

	private String flag;
	private List<TreeDatabean> data;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<TreeDatabean> getData() {
		return data;
	}
	public void setData(List<TreeDatabean> data) {
		this.data = data;
	}
}
