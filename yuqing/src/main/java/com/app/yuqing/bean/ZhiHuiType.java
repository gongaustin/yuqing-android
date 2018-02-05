package com.app.yuqing.bean;

public enum ZhiHuiType {
	SB("上报"),YJZH("应急指挥"),JX("绩效"),BL("办理"),XP("下派");
	private String type;
	ZhiHuiType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
