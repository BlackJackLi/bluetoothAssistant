package com.gizwits.energy.android.lib.web.base;

/**
 * WebResponse
 *
 * @author Joke Huang
 * @version 1.0.0
 * @Description
 * @createDate 2014年7月8日
 */

public class WebResponse<T> {
	// code :
	private String code;
	// message : 提示语，或者错误描述
	private String message;
	// result : 请求返回数据
	private String result;
	private boolean isHaveResultObject;

	public boolean isHaveResultObject() {
		return isHaveResultObject;
	}

	public void setHaveResultObject(boolean isHaveResultObject) {
		this.isHaveResultObject = isHaveResultObject;
	}

	private T resultObj;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCode(int code) {
		this.code = String.valueOf(code);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public T getResultObj() {
		return resultObj;
	}

	public void setResultObj(T resultObj) {
		this.resultObj = resultObj;

	}

	@Override
	public String toString() {
		return "WebResponse [code=" + code + ", message=" + message + ", result=" + result + ", resultObj=" + resultObj + "]";
	}

}
