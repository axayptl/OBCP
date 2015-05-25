package com.ceilcode.obcp.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class MyDatePickerDialog extends DatePickerDialog {

	public MyDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		// TODO Auto-generated method stub
		super.onDateChanged(view, year, month, day);
		setTitle("Select Month");
	}

}
