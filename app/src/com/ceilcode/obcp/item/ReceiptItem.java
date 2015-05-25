package com.ceilcode.obcp.item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import static com.ceilcode.obcp.util.JsonKeys.ReceiptItemKeys.*;
import org.json.JSONObject;

import android.util.Log;

//{"ReceiptID":38,"PosUserID":"EliteKosherShop","Total":117.00,"Date":"2014-06-28T22:50:41"}

public class ReceiptItem {
	private int receiptId;
	private String posUserId;
	private double total;
	private Date date;
	private String posUserName;
	private int receiptTypeId;
	private String receiptType;
	private int shoppingCategoryId;
	private String shoppingCategory;
	private String rawDataPath;

	private static ArrayList<MaritalStatusItem> maritalStatusItems;
	private static ArrayList<ReceiptTypeItem> receiptTypeItems;

	public static ArrayList<MaritalStatusItem> getMaritalStatusItems() {
		return maritalStatusItems;
	}

	public static void setMaritalStatusItems(
			ArrayList<MaritalStatusItem> maritalStatusItems) {
		ReceiptItem.maritalStatusItems = maritalStatusItems;
	}

	public static ArrayList<ReceiptTypeItem> getReceiptTypeItems() {
		return receiptTypeItems;
	}

	public static void setReceiptTypeItems(
			ArrayList<ReceiptTypeItem> receiptTypeItems) {
		ReceiptItem.receiptTypeItems = receiptTypeItems;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPosUserName() {
		return posUserName;
	}

	public void setPosUserName(String posUserName) {
		this.posUserName = posUserName;
	}

	public int getReceiptTypeId() {
		return receiptTypeId;
	}

	public void setReceiptTypeId(int receiptTypeId) {
		this.receiptTypeId = receiptTypeId;
	}

	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	public int getShoppingCategoryId() {
		return shoppingCategoryId;
	}

	public void setShoppingCategoryId(int shoppingCategoryId) {
		this.shoppingCategoryId = shoppingCategoryId;
	}

	public String getShoppingCategory() {
		return shoppingCategory;
	}

	public void setShoppingCategory(String shoppingCategory) {
		this.shoppingCategory = shoppingCategory;
	}

	public String getRawDataPath() {
		return rawDataPath;
	}

	public void setRawDataPath(String rawDataPath) {
		this.rawDataPath = rawDataPath;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	private ArrayList<ReceiptSubItem> subItems;

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss", Locale.getDefault());

	public static final SimpleDateFormat FORMAT_HH_MM = new SimpleDateFormat(
			"hh:mm", Locale.getDefault());

	public ReceiptItem(int receptId, String posUserId, double total, String date) {
		this.receiptId = receptId;
		this.posUserId = posUserId;
		this.total = total;

		// remove T from date and parse it
		try {
			this.date = SIMPLE_DATE_FORMAT.parse(date.replace("T", " "));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public ReceiptItem(int receptId, String posUserId, double total, Date date) {
		this.receiptId = receptId;
		this.posUserId = posUserId;
		this.total = total;

		// remove T from date and parse it
		this.date = date;

	}

	public ReceiptItem getClone() {

		// ReceiptItem item = new ReceiptItem(receiptId, posUserId, total,
		// date);
		// item.posUserName = this.posUserName;
		// item.rawDataPath = this.rawDataPath;
		// item.receiptType = this.receiptType;
		// item.receiptTypeId = this.receiptTypeId;
		// item.shoppingCategory = this.shoppingCategory;
		// item.shoppingCategoryId = this.shoppingCategoryId;
		// item.subItems = (ArrayList<ReceiptSubItem>) this.subItems.clone();
		return this;
	}

	public ArrayList<ReceiptSubItem> getSubItems() {
		return (subItems != null) ? subItems : new ArrayList<ReceiptSubItem>();
	}

	public void setSubItems(ArrayList<ReceiptSubItem> subItems) {
		this.subItems = subItems;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

	public String getPosUserId() {
		return posUserId;
	}

	public void setPosUserId(String posUserId) {
		this.posUserId = posUserId;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Date getDateTime() {
		return date;
	}

	public String getDateTime(SimpleDateFormat format) {
		return format.format(date);
	}

	public void setDateTime(Date date) {
		this.date = date;
	}

	public void setDateTime(String date, SimpleDateFormat format) {
		try {
			this.date = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ReceiptItem createFromJson(JSONObject object) {
		ReceiptItem item = null;
		try {

			item = new ReceiptItem(object.getInt(KEY_RECEIPT_ID),
					object.getString(KEY_POS_USER_ID),
					object.getDouble(KEY_TOTAL), object.getString(KEY_DATE));
			item.setPosUserName(object.getString(KEY_POS_USER_NAME));
			item.setReceiptTypeId(object.getInt(KEY_RECEIPT_TYPE_ID));
			item.setReceiptType(object.getString(KEY_RECEIPT_TYPE));
			item.setShoppingCategoryId(object.getInt(KEY_SHOPPING_CATEGORY_ID));
			item.setShoppingCategory(object.getString(KEY_SHOPPING_CATEGORY));
			item.setRawDataPath(object.getString(KEY_RAW_DATA_PATH));

			Log.e("receipt type", item.getReceiptType() + "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	public static String getReceiptTypeFromId(int id) {
		for (Iterator<ReceiptTypeItem> iterator = receiptTypeItems.iterator(); iterator
				.hasNext();) {
			ReceiptTypeItem type = iterator.next();

			if (type.getId() == id) {
				return type.getTitle();
			}

		}

		return "";
	}

	public static int getReceiptTypeFromName(String name) {
		for (Iterator<ReceiptTypeItem> iterator = receiptTypeItems.iterator(); iterator
				.hasNext();) {
			ReceiptTypeItem type = iterator.next();

			if (type.getTitle().equalsIgnoreCase(name)) {
				return type.getId();
			}

		}

		return -1;
	}

	public void changeReceiptType() {

		String type = this.receiptType.equalsIgnoreCase("private") ? "work"
				: "private";
		this.receiptTypeId = getReceiptTypeFromName(type);

		this.receiptType = getReceiptTypeFromId(receiptTypeId);
	}

	public void setReceiptTypeAndId(String receiptType) {
		this.receiptTypeId = getReceiptTypeFromName(receiptType);

		this.receiptType = getReceiptTypeFromId(receiptTypeId);

	}

	public static String getMaritalStatusFromId(int maritalStatus) {
		for (Iterator<MaritalStatusItem> iterator = maritalStatusItems
				.iterator(); iterator.hasNext();) {
			MaritalStatusItem type = iterator.next();

			if (type.getId() == maritalStatus) {
				return type.getTitle();
			}

		}

		return "";
	}

}
