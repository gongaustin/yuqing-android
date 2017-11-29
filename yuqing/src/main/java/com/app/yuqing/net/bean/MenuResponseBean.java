package com.app.yuqing.net.bean;

import java.util.List;

import com.app.yuqing.bean.MenuBean;

public class MenuResponseBean extends BaseResponseBean {

	private String flag;
	private List<MenuBean> data;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<MenuBean> getData() {
		return data;
	}
	public void setData(List<MenuBean> data) {
		this.data = data;
	}
}
