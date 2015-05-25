package com.ceilcode.obcp.util;

public class JsonKeys {

	public static final class NotificationItemKeys {
		public static final String KEY_ALERT = "alert";
		public static final String KEY_TOTAL_ITEMS = "Total Items";

		public static final String KEY_TOTAL = "Receipt Total";
		public static final String KEY_BADGE = "badge";
		public static final String KEY_SOUND = "sound";
	}

	public static final class UserItemKeys {
		/*
		 * {"UserID":"6592315604","FirstName":"Yaniv","LastName":"Cohen","Phone":
		 * "6592315604"
		 * ,"Email":"confis@gmail.com","MaritalStatus":0,"Age":0,"Gender"
		 * :0,"MonthlyBudget":0.0,"AlternativeID":"A12345"}
		 */

		public static final String KEY_USER_ID = "UserID";
		public static final String KEY_FIRST_NAME = "FirstName";
		public static final String KEY_LAST_NAME = "LastName";
		public static final String KEY_PHONE = "Phone";
		public static final String KEY_EMAIL = "Email";
		public static final String KEY_ALT_ID = "AlternativeID";
		public static final String KEY_MARITAL_STATUS = "MaritalStatus";
		public static final String KEY_AGE = "Age";
		public static final String KEY_GENDER = "Gender";
		public static final String KEY_MONTHLY_BUDGET = "MonthlyBudget";

	}

	public static final class ReceiptItemKeys {

		/*
		 * {"ReceiptID":136,"PosUserID":"GUESS","PosUserName":"fjb","Total":139.90
		 * ,"ReceiptTypeID":1,"ReceiptType":"Private","ShoppingCategoryID":2,
		 * "ShoppingCategory":"Clothing","RawDataPath":
		 * "C:\\web\\dbill\\App_Data\\upload/fjb_6591599564_20140909103056931.ps"
		 * ,"Date":"2014-09-09T10:30:57"}
		 */
		public static final String KEY_RECEIPT_ID = "ReceiptID";
		public static final String KEY_POS_USER_NAME = "PosUserID";
		public static final String KEY_RECEIPT_TYPE_ID = "ReceiptTypeID";
		public static final String KEY_RECEIPT_TYPE = "ReceiptType";
		public static final String KEY_SHOPPING_CATEGORY_ID = "ShoppingCategoryID";
		public static final String KEY_SHOPPING_CATEGORY = "ShoppingCategory";
		public static final String KEY_RAW_DATA_PATH = "RawDataPath";
		public static final String KEY_POS_USER_ID = "PosUserID";
		public static final String KEY_TOTAL = "Total";
		public static final String KEY_DATE = "Date";

	}

	public static final class ReceiptSubItemKeys {
		public static final String KEY_COST = "Cost";
		public static final String KEY_NAME = "Name";
		public static final String KEY_QUANTITY = "Quantity";
	}

	public static final class VerificationKeys {
		public static final String KEY_STATUS = "Status";
		public static final String KEY_TOKEN = "SessionToken";

	}

	public static final class MaritalStatusKeys {
		public static final String KEY_ID = "ID";
		public static final String KEY_NAME = "Name";

	}

	public static final class ReceiptTypeKeys {
		public static final String KEY_ID = "ID";
		public static final String KEY_NAME = "Name";

	}

}
