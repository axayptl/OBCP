package com.ceilcode.obcp.item;

import org.json.JSONException;
import org.json.JSONObject;

import com.ceilcode.obcp.util.JsonKeys;

public class MaritalStatusItem {
	private int id;
	private String name;

	public MaritalStatusItem(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return name;
	}

	public void setTitle(String title) {
		this.name = title;
	}

	public static MaritalStatusItem createFromJson(JSONObject object) {
		MaritalStatusItem item = null;
		try {
			item = new MaritalStatusItem(Integer.parseInt(object
					.getString(JsonKeys.MaritalStatusKeys.KEY_ID)),
					object.getString(JsonKeys.MaritalStatusKeys.KEY_NAME));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return item;
	}

}
