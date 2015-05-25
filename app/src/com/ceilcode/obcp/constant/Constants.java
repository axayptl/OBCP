package com.ceilcode.obcp.constant;

public class Constants {

	public static final String API_KEY = "4e1226b9fcb140919839fae48bbbd7e3";

	private static final String URL_BASE = "https://secure.dbill.me/api/";
	public static final String URL_LOGIN = URL_BASE + "account";
	public static final String URL_SIGNUP = URL_BASE + "createaccount";
	public static final String URL_RECEPT = URL_BASE + "account";
	public static final String URL_REPORT = URL_BASE + "bill";
	public static final String URL_USER_DETAIL = URL_BASE + "userdetails";
	public static final String URL_RECEPTS = URL_BASE + "bill";
	public static final String URL_RECEPTS_DETAIL = URL_BASE + "bill";
	public static final String URL_VERIFY_USER = URL_BASE + "ActivateUser";
	public static final String URL_CHANGE_DETAILS = URL_BASE + "userdetails";

	public static final String URL_NEW_LOGIN = "https://secure.dbill.me/api/twofaaccount";

	public static final String URL_NEW_VERIFY = "https://secure.dbill.me/api/validateotp";
	public static final String URL_SESSION_LOGIN = "https://secure.dbill.me/api/loginbysessiontoken";
	public static final String URL_GET_RECEIPT_TYPES = "https://secure.dbill.me/api/receipttypes";
	public static final String URL_GET_MARITAL_STATUS_TYPES = "https://secure.dbill.me/api/maritalstatusoptions";
	public static final String URL_SEND_PUSH_ID = "https://secure.dbill.me/api/registerpushdevice";
	public static final String URL_CHECK_PUSH_ID = "https://secure.dbill.me/api/checkpushdeviceregistered";
	public static final String URL_UPDATE_RECEIPT_TYPE = "https://secure.dbill.me/api/updatebilltype";
	public static final String URL_EMAIL_MONTHLY_REPORT = "https://secure.dbill.me/api/emailmonthlyreport";

}
