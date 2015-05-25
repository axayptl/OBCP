package com.ceilcode.obcp.item;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptSubItem {
	private String title;
	private String value;
	private String cost;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getTotal() {

		try {
			return "" + (Double.parseDouble(cost) * Double.parseDouble(value));

		} catch (Exception e) {
			e.printStackTrace();

			return "0.0";
		}

	}

	public static ReceiptSubItem createFromJson(JSONObject jsonObject) {
		ReceiptSubItem item = new ReceiptSubItem();
		try {

			item.setCost(jsonObject.getString("Cost"));
			item.setTitle(jsonObject.getString("Name"));
			item.setValue(jsonObject.getString("Quantity"));

		} catch (JSONException e) {
			// TODO: handle exception
		}

		return item;
	}

}
