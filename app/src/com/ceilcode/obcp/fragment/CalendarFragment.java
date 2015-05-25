package com.ceilcode.obcp.fragment;

import java.util.Calendar;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.util.DateUtils;

public class CalendarFragment extends BaseFragment implements OnClickListener {

	private ImageButton nextButton, prevButton;
	private TextView monthTextView;

	private TextView lastClickedTextView = null;

	public static final int TYPE_CURRENT_DATE = R.drawable.item_calendar_selected_bg;
	public static final int TYPE_CURRENT_DATE_WITH_RECEIPT = R.drawable.item_calendar_selected_bg;
	public static final int TYPE_DATE_WITH_RECEPT = R.drawable.item_calendar_dot;
	public static final int TYPE_DATE_WITHOUT_RECEPT = R.drawable.item_calendar_selected_bg;

	private final int ROW_IDS[] = { R.id.tableRow2, R.id.tableRow3,
			R.id.tableRow4, R.id.tableRow5, R.id.tableRow6, R.id.tableRow7 };

	private final String MONTHS[] = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	private TextView monthlyTitleText, monthlyText, monthlyAvarageTitleText,
			dailyAvarageText, weeklyAvarageText, weeklyAvarageTitleText;

	private final int DATE_TEXT_IDS[] = { R.id.textView_place_00,
			R.id.textView_place_01, R.id.textView_place_02,
			R.id.textView_place_03, R.id.textView_place_04,
			R.id.textView_place_05, R.id.textView_place_06,
			R.id.textView_place_10, R.id.textView_place_11,
			R.id.textView_place_12, R.id.textView_place_13,
			R.id.textView_place_14, R.id.textView_place_15,
			R.id.textView_place_16, R.id.textView_place_20,
			R.id.textView_place_21, R.id.textView_place_22,
			R.id.textView_place_23, R.id.textView_place_24,
			R.id.textView_place_25, R.id.textView_place_26,
			R.id.textView_place_30, R.id.textView_place_31,
			R.id.textView_place_32, R.id.textView_place_33,
			R.id.textView_place_34, R.id.textView_place_35,
			R.id.textView_place_36, R.id.textView_place_40,
			R.id.textView_place_41, R.id.textView_place_42,
			R.id.textView_place_43, R.id.textView_place_44,
			R.id.textView_place_45, R.id.textView_place_46,
			R.id.textView_place_50, R.id.textView_place_51,
			R.id.textView_place_52, R.id.textView_place_53,
			R.id.textView_place_54, R.id.textView_place_55,
			R.id.textView_place_56 };

	private Calendar mainCalendar = Calendar.getInstance(Locale.getDefault());

	private Calendar selectedDate = Calendar.getInstance(Locale.getDefault());
	private View rootView;

	public static CalendarFragment newInstance(
			OnDateClickListner onDateClickListner) {

		CalendarFragment calendarFragment = new CalendarFragment();
		calendarFragment.onDateClickListner = onDateClickListner;
		return calendarFragment;

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_calendar, container,
				false);
		monthTextView = (TextView) rootView.findViewById(R.id.textView_month);
		nextButton = (ImageButton) rootView.findViewById(R.id.imageButton_next);
		prevButton = (ImageButton) rootView.findViewById(R.id.imageButton_prev);

		nextButton.setOnClickListener(this);
		prevButton.setOnClickListener(this);
		monthTextView.setText(MONTHS[mainCalendar.get(Calendar.MONTH)] + " "
				+ mainCalendar.get(Calendar.YEAR));

		monthlyText = (TextView) rootView.findViewById(R.id.textView11);
		dailyAvarageText = (TextView) rootView.findViewById(R.id.textView22);
		weeklyAvarageText = (TextView) rootView.findViewById(R.id.textView33);

		monthlyTitleText = (TextView) rootView.findViewById(R.id.textView1);
		monthlyAvarageTitleText = (TextView) rootView
				.findViewById(R.id.textView2);
		weeklyAvarageTitleText = (TextView) rootView
				.findViewById(R.id.textView3);
		setDates(mainCalendar);

		setTypeFace();

		return rootView;
	}

	private void setTypeFace() {
		monthlyText.setTypeface(typefaceUtil.getTypefaceBold());
		dailyAvarageText.setTypeface(typefaceUtil.getTypefaceBold());
		weeklyAvarageText.setTypeface(typefaceUtil.getTypefaceBold());

		monthlyTitleText.setTypeface(typefaceUtil.getTypefaceRegular());
		monthlyAvarageTitleText.setTypeface(typefaceUtil.getTypefaceRegular());
		weeklyAvarageTitleText.setTypeface(typefaceUtil.getTypefaceRegular());

	}

	private void nextMonth() {
		mainCalendar.add(Calendar.MONTH, 1);
		monthTextView.setText(MONTHS[mainCalendar.get(Calendar.MONTH)] + " "
				+ mainCalendar.get(Calendar.YEAR));

		setReceipts();
		setDates(mainCalendar);

	}

	private void previousMonth() {
		mainCalendar.add(Calendar.MONTH, -1);
		monthTextView.setText(MONTHS[mainCalendar.get(Calendar.MONTH)] + " "
				+ mainCalendar.get(Calendar.YEAR));

		setReceipts();
		setDates(mainCalendar);
	}

	private void refreshView() {

		setReceipts();

		try {
			for (int i = 0; i < ROW_IDS.length; i++) {
				rootView.findViewById(ROW_IDS[i]).setVisibility(View.VISIBLE);

			}

			for (int i = 0; i < DATE_TEXT_IDS.length; i++) {

				TextView textView = (TextView) rootView
						.findViewById(DATE_TEXT_IDS[i]);
				textView.setVisibility(View.VISIBLE);
				textView.setTag(null);
				textView.setBackgroundColor(Color.TRANSPARENT);
				textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

				textView.setTypeface(typefaceUtil.getTypefaceRegular());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void hideStartingDays(Calendar calendar) {
		Calendar tempCalendar = (Calendar) calendar.clone();
		tempCalendar.set(Calendar.DATE, 1);
		int blankDays = tempCalendar.get(Calendar.DAY_OF_WEEK);
		for (int i = 0; i <= blankDays; i++) {
			rootView.findViewById(DATE_TEXT_IDS[i]).setVisibility(
					View.INVISIBLE);
		}

	}

	private void hideEndingDays(Calendar calendar) {
		Calendar tempCalendar = (Calendar) calendar.clone();
		tempCalendar.set(Calendar.DATE, 1);
		int fistDayStartFrom = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		int totalDays = tempCalendar.getMaximum(Calendar.DATE);
		int blankDaysStartFrom = fistDayStartFrom + totalDays;
		for (int i = blankDaysStartFrom; i < DATE_TEXT_IDS.length; i++) {
			rootView.findViewById(DATE_TEXT_IDS[i]).setVisibility(View.GONE);
		}
	}

	private void hideExtraRows(Calendar calendar) {

		int fistDayStartFrom = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int totalDays = calendar.getMaximum(Calendar.DATE);
		int lastRow = (fistDayStartFrom + totalDays) / 7;
		int blankRowsStartFrom = lastRow + 1;

		for (int i = blankRowsStartFrom; i < ROW_IDS.length; i++) {
			rootView.findViewById(ROW_IDS[i]).setVisibility(View.GONE);
		}
	}

	private void setDates(Calendar calendar) {

		Log.e("Set dates",
				DateUtils.formatDate(mainCalendar, DateUtils.DD_MMM_YYY));
		Calendar tempCalendar = (Calendar) calendar.clone();
		tempCalendar.set(Calendar.DATE, 1);
		refreshView();
		hideStartingDays(tempCalendar);
		int fistDayStartFrom = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		int totalDays = tempCalendar.getMaximum(Calendar.DATE);

		int limit = fistDayStartFrom + totalDays;
		for (int i = fistDayStartFrom, date = 1; i < limit; i++, date++) {
			TextView textView = (TextView) rootView
					.findViewById(DATE_TEXT_IDS[i]);
			textView.setVisibility(View.VISIBLE);
			textView.setText("" + date);
			textView.setTag(date);
			textView.setOnClickListener(dateClickListner);
		}

		// set receipt dot on date
		setReceiptOnDate();

		// set current date
		if (mainCalendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
				&& mainCalendar.get(Calendar.MONTH) == selectedDate
						.get(Calendar.MONTH)) {
			for (int i = 0, lim = DATE_TEXT_IDS.length; i < lim; i++) {
				TextView textView = (TextView) rootView
						.findViewById(DATE_TEXT_IDS[i]);
				if (textView.getTag() != null) {

					int date = (Integer) textView.getTag();
					if (selectedDate.get(Calendar.DATE) == date) {
						textView.setBackgroundResource(TYPE_CURRENT_DATE);
					
					}
				}

			}

		}

		hideEndingDays(calendar);
		hideExtraRows(calendar);

	}

	private void setReceiptOnDate() {

		int year = mainCalendar.get(Calendar.YEAR);
		int month = mainCalendar.get(Calendar.MONTH);
		for (int i = 0, lim = DATE_TEXT_IDS.length; i < lim; i++) {
			TextView textView = (TextView) rootView
					.findViewById(DATE_TEXT_IDS[i]);
			if (textView.getTag() != null) {

				int date = (Integer) textView.getTag();
				
			}

		}

	}

	

	private OnClickListener dateClickListner = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			if (arg0.getTag() == null) {

				return;

			}

			lastClickedTextView = (TextView) arg0;

			int date = (Integer) arg0.getTag();

			

			if (onDateClickListner != null) {
				onDateClickListner.onDateClick(mainCalendar.get(Calendar.YEAR),
						mainCalendar.get(Calendar.MONTH), date);

				

				selectedDate
						.set(Calendar.YEAR, mainCalendar.get(Calendar.YEAR));
				selectedDate.set(Calendar.MONTH,
						mainCalendar.get(Calendar.MONTH));
				selectedDate.set(Calendar.DATE, date);
				setDates(mainCalendar);
			}

		}
	};

	@Override
	public void onClick(View arg0) {
		lastClickedTextView = null;

		switch (arg0.getId()) {
		case R.id.imageButton_next:
			nextMonth();

			break;

		case R.id.imageButton_prev:
			previousMonth();

			break;

		default:
			break;
		}
	}

	// callback

	private OnDateClickListner onDateClickListner;

	public static interface OnDateClickListner {
		public void onDateClick(int year, int month, int day);

	}

	public void refreshReceipts() {
		setDates(mainCalendar);

	}

	private void setReceipts() {
	
	}

	public static CalendarFragment newInstance(Calendar dateOfRecentReceipt,
			OnDateClickListner dateClickListner) {
		CalendarFragment calendarFragment = new CalendarFragment();
		calendarFragment.onDateClickListner = dateClickListner;

		calendarFragment.mainCalendar = (Calendar) dateOfRecentReceipt.clone();
		calendarFragment.selectedDate = (Calendar) dateOfRecentReceipt.clone();
		return calendarFragment;
	}

}
