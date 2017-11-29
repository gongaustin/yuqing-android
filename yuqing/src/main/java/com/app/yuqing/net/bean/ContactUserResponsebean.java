package com.app.yuqing.net.bean;

import java.util.List;

import com.app.yuqing.bean.ContactUser;

public class ContactUserResponsebean extends BaseResponseBean {

	private String flag;
	private List<ContactUser> data;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<ContactUser> getData() {
		return data;
	}
	public void setData(List<ContactUser> data) {
		this.data = data;
	}
}
