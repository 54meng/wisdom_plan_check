package com.wpc.webapp.controller.vo;

/**
 * 接口请求返回对象
 * 
 * @author imlzw
 *
 */
public class CallResult {
	private int ret;

	private String retValue;

	private String msg;
	
	private String js;//返回js，可以返回时，被浏览器调用

	public CallResult(){
		
	}
	public CallResult(int ret,String retValue,String msg){
		this.ret = ret;
		this.retValue = retValue;
		this.msg = msg;
	}
	public CallResult(int ret,String retValue,String msg,String js){
		this.ret = ret;
		this.retValue = retValue;
		this.msg = msg;
		this.js = js;
	}
	
	/**
	 * 返回代码
	 */
	public int getRet() {
		return ret;
	}

	/**
	 * 返回代码
	 */
	public void setRet(int ret) {
		this.ret = ret;
	}

	/**
	 * 返回信息
	 */
	public String getMsg() {
		return msg!= null?msg:"";
	}
	
	/**
	 * 返回信息
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 获取返回值
	 * @return
	 */
	public String getRetValue() {
		return retValue;
	}
	
	/**
	 * 设置返回值
	 * @param retValue
	 */
	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}
	/**
	 * 获取JS
	 * @return
	 */
	public String getJs() {
		return js;
	}
	/**
	 * 设置JS
	 * @param js
	 */
	public void setJs(String js) {
		this.js = js;
	}
}
