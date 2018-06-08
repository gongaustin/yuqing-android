package com.app.yuqing.net.bean;

import java.io.Serializable;
import java.util.Map;

public class BaseResponseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		if("200".equals(code)) {
			return true;
		}
		return  false;
	}
}
