package com.ceilcode.obcp.webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJsonPack {

	/*
	 * public constants
	 */
	public static final int JSON_ARRAY = 0;
	public static final int JSON_OBJECT = 1;
	public static final int PARSING_ERROR = 9;
	/*
	 * private fields
	 */
	private JSONObject jsonObject;
	private JSONArray jsonArray;

	private String rawResult;

	public MyJsonPack(String jsonString) {

		rawResult = jsonString;

		try {
			jsonObject = new JSONObject(jsonString);
		} catch (Exception e) {
			jsonObject = null;
		}

		try {
			jsonArray = new JSONArray(jsonString);
		} catch (Exception e) {
			jsonArray = null;
		}
	}

	public String getRawResult() {
		return rawResult;
	}

	public void setRawResult(String rawResult) {
		this.rawResult = rawResult;
	}

	public Object getWholeJson() {
		if (jsonObject != null) {
			return jsonObject;
		} else if (jsonArray != null) {
			return jsonArray;
		} else {
			return null;
		}
	}

	public Object getFromObject(String key) throws JSONException {
		return jsonObject.get(key);
	}

	public JSONObject getFromArray(int index) throws JSONException {
		return jsonArray.getJSONObject(index);
	}

	public int getJsonArraySize() {
		return jsonArray.length();
	}

	public int getType() {

		if (jsonArray != null)
			return JSON_ARRAY;
		else if (jsonObject != null)
			return JSON_OBJECT;
		else
			return PARSING_ERROR;

	}

}
