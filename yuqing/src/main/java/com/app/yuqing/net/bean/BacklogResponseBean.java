package com.app.yuqing.net.bean;

import java.util.List;

import com.app.yuqing.bean.BacklogBean;

public class BacklogResponseBean extends BaseResponseBean {
	
	private String flag;
	private List<BacklogBean> data;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<BacklogBean> getData() {
		return data;
	}
	public void setData(List<BacklogBean> data) {
		this.data = data;
	}
}
