package com.app.yuqing.net.bean;

import java.util.List;

import com.app.yuqing.bean.BaseBean;
import com.app.yuqing.bean.MenuBean;

public class MenuDataBean extends BaseBean {

	private List<MenuBean> sentiment;
	
	private List<MenuBean> other;
	
	private List<MenuBean> emergency;

	public List<MenuBean> getSentiment() {
		return sentiment;
	}

	public void setSentiment(List<MenuBean> sentiment) {
		this.sentiment = sentiment;
	}

	public List<MenuBean> getOther() {
		return other;
	}

	public void setOther(List<MenuBean> other) {
		this.other = other;
	}

	public List<MenuBean> getEmergency() {
		return emergency;
	}

	public void setEmergency(List<MenuBean> emergency) {
		this.emergency = emergency;
	}
}
