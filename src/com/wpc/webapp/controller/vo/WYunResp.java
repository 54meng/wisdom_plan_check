package com.wpc.webapp.controller.vo;

public class WYunResp {
	private int code;
	private String tid;
	private Token info;
	
	public class Token{
		private String token;
		private String accid;
		private String name;
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getAccid() {
			return accid;
		}
		public void setAccid(String accid) {
			this.accid = accid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Token getInfo() {
		return info;
	}

	public void setInfo(Token info) {
		this.info = info;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
}
