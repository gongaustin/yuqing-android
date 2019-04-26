package com.app.yuqing.bean;

public enum YQType {
	YQZX("舆情中心"),SJLB("时间列表"),SJFX("事件分析"),YQZZ("舆情追踪"),YQYJ("舆情预警"),YQBG("舆情报告"),DXJZ("定向监测"),ALK("案例库");
	private String type;
	YQType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}
