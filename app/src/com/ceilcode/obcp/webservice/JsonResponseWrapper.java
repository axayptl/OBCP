package com.ceilcode.obcp.webservice;

public class JsonResponseWrapper {

	/*
	 * constants
	 */

	public static final int CONNECTION_ERROR = -1;
	public static final int RESPONSE_ERROR = -2;
	public static final int RESPONSE_OK = 0;
	public static final int PARSING_ERROR = -3;

	private MyJsonPack jsonPack;
	private Exception exception;
	private int code;

	public MyJsonPack getJsonPack() {
		return jsonPack;
	}

	public void setJsonPack(MyJsonPack jsonPack) {
		this.jsonPack = jsonPack;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
