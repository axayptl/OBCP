package com.ceilcode.obcp.item;

import org.json.JSONException;
import org.json.JSONObject;

import com.ceilcode.obcp.util.JsonKeys;

public class ReceiptTypeItem {
	private int id;
	private String name;

	public ReceiptTypeItem(int id, String name) {
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

	public static ReceiptTypeItem createFromJson(JSONObject object) {
		ReceiptTypeItem item = null;
		try {
			item = new ReceiptTypeItem(Integer.parseInt(object
					.getString(JsonKeys.ReceiptTypeKeys.KEY_ID)),
					object.getString(JsonKeys.ReceiptTypeKeys.KEY_NAME));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return item;
	}

}
