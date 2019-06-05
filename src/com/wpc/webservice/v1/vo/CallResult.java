package com.wpc.webservice.v1.vo;

/**
 * 统一返回对象
 * 
 * @author imlzw
 *
 */
public class CallResult {
	private int code;
	private String message;
	private Object data;
	
	public CallResult(){
		code = -1;
		message = "服务器未知错误!";
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	

	
}

