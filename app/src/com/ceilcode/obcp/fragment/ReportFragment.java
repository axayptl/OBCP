package com.ceilcode.obcp.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.adapter.ReportAdapter;
import com.ceilcode.obcp.constant.Constants;
import com.ceilcode.obcp.dialog.MyDatePickerDialog;
import com.ceilcode.obcp.item.ReceiptItem;
import com.ceilcode.obcp.item.ReceiptSubItem;
import com.ceilcode.obcp.util.DateUtils;
import com.ceilcode.obcp.util.Preference;
import com.ceilcode.obcp.webservice.JsonResponseWrapper;
import com.ceilcode.obcp.webservice.LoginApi;
import com.ceilcode.obcp.webservice.MyJsonPack;
import com.ceilcode.obcp.webservice.OnTaskPerformListner;

public class ReportFragment extends BaseFragment implements OnDateSetListener,
		OnClickListener, OnTaskPerformListner, OnCheckedChangeListener {

	private static final int taskId = 0;

	private ExpandableListView listView;

	private TextView monthSelectView;
	private TextView monthlyExpanseView;

	private MyDatePickerDialog datePickerDialog;

	private AlertDialog errorDialog;
	private ProgressDialog dialog;

	private Button share;

	private View shareView;

	private Button printReport, emailReport, pdfReport, excelReport;;

	private final String MONTHS[] = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	private ArrayList<ReceiptItem> items;

	private Calendar selectedMonth;

	private ReportAdapter adapter;

	private ToggleButton privateWork;

	private int fetch_task = 0;

	private int email_task = 1;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_report, container,
				false);

		monthlyExpanseView = (TextView) rootView
				.findViewById(R.id.textView_monthly_expense);
		monthSelectView = (TextView) rootView.findViewById(R.id.textView_month);
		privateWork = (ToggleButton) rootView
				.findViewById(R.id.toggleButton_private_work);

		share = (Button) rootView.findViewById(R.id.button_share);
		printReport = (Button) rootView
				.findViewById(R.id.textView_print_report);
		printReport.setOnClickListener(this);
		pdfReport = (Button) rootView.findViewById(R.id.textView_pdf_report);
		pdfReport.setOnClickListener(this);
		excelReport = (Button) rootView
				.findViewById(R.id.textView_excel_report);
		excelReport.setOnClickListener(this);
		emailReport = (Button) rootView
				.findViewById(R.id.textView_email_report);
		emailReport.setOnClickListener(this);

		shareView = rootView.findViewById(R.id.layout_share);
		shareView.setVisibility(View.GONE);
		share.setOnClickListener(this);
		privateWork.setOnCheckedChangeListener(this);

		monthSelectView.setOnClickListener(this);

		monthlyExpanseView.setText("Monthly Total  $0.0");

		listView = (ExpandableListView) rootView
				.findViewById(R.id.expandableListView);

		listView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					listView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});

		selectedMonth = Calendar.getInstance(Locale.getDefault());
		monthSelectView.setText(MONTHS[selectedMonth.get(Calendar.MONTH)] + " "
				+ selectedMonth.get(Calendar.YEAR));
		datePickerDialog = new MyDatePickerDialog(getActivity(), this,
				selectedMonth.get(Calendar.YEAR),
				selectedMonth.get(Calendar.MONTH),
				selectedMonth.get(Calendar.DATE));

		datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// listView.setAdapter(new ReceiptAdapter(getActivity(), MyDataProvider
		// .getReceiptByMonth(calendar.get(Calendar.MONTH),
		// calendar.get(Calendar.YEAR))));

		dialog = new ProgressDialog(getActivity());
		dialog.setCancelable(false);

		errorDialog = new AlertDialog.Builder(getActivity()).create();
		errorDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		setTypeface();
		refresh();

		return rootView;

	}

	private void setTypeface() {
		monthlyExpanseView.setTypeface(typefaceUtil.getTypeface());
		monthSelectView.setTypeface(typefaceUtil.getTypefaceRegular());
		pdfReport.setTypeface(typefaceUtil.getTypefaceRegular());
		excelReport.setTypeface(typefaceUtil.getTypefaceRegular());

		emailReport.setTypeface(typefaceUtil.getTypefaceRegular());
		printReport.setTypeface(typefaceUtil.getTypefaceRegular());

	}

	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		selectedMonth.set(Calendar.YEAR, arg1);
		selectedMonth.set(Calendar.MONTH, arg2);
		selectedMonth.set(Calendar.DAY_OF_MONTH, arg3);
		monthSelectView.setText(MONTHS[selectedMonth.get(Calendar.MONTH)] + " "
				+ selectedMonth.get(Calendar.YEAR));
		refresh();
	}

	private void refresh() {

		if (!dialog.isShowing()) {

			new FetchBills().execute();
			;
		}
	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == R.id.textView_month) {

			datePickerDialog.setTitle("Select Month");
			datePickerDialog.show();

			((ViewGroup) datePickerDialog.getDatePicker())
					.findViewById(
							Resources.getSystem().getIdentifier("day", "id",
									"android")).setVisibility(View.GONE);
		} else if (arg0.getId() == R.id.button_share) {

			shareView
					.setVisibility(shareView.getVisibility() == View.GONE ? View.VISIBLE
							: View.GONE);

		} else if (arg0.getId() == R.id.textView_print_report) {

			shareView
					.setVisibility(shareView.getVisibility() == View.GONE ? View.VISIBLE
							: View.GONE);

		} else if (arg0.getId() == R.id.textView_email_report) {

			shareView
					.setVisibility(shareView.getVisibility() == View.GONE ? View.VISIBLE
							: View.GONE);

			new EmailReport().execute();

		} else if (arg0.getId() == R.id.textView_excel_report) {

			shareView
					.setVisibility(shareView.getVisibility() == View.GONE ? View.VISIBLE
							: View.GONE);

			emailVarification();

		} else if (arg0.getId() == R.id.textView_pdf_report) {

			shareView
					.setVisibility(shareView.getVisibility() == View.GONE ? View.VISIBLE
							: View.GONE);

		}

	}

	private void emailVarification() {
		Preference preference = new Preference(getActivity());

		String email = preference.getEmail();

		if (email == null || email.equals("")) {
			AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
					.create();

			alertDialog.setTitle("Check Email");
			alertDialog.setTitle("No eamil account is registered.");

			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Go to profile",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							FragmentManager manager = getActivity()
									.getSupportFragmentManager();
							manager.beginTransaction()
									.replace(R.id.content,
											new ProfileFragment())
									.addToBackStack("").commit();

						}
					});

			alertDialog.show();

		}

	}

	@Override
	public void onTaskComplet(int taskId, JsonResponseWrapper result) {
		dialog.dismiss();
		try {

			if (taskId == fetch_task) {

				if (result.getCode() == JsonResponseWrapper.RESPONSE_OK) {

					try {
						MyJsonPack myJsonPack = result.getJsonPack();
						items = new ArrayList<ReceiptItem>();
						for (int i = 0, lim = myJsonPack.getJsonArraySize(); i < lim; i++) {
							JSONObject object = myJsonPack.getFromArray(i);
							ReceiptItem item = ReceiptItem
									.createFromJson(object);
							items.add(item);
						}
						new FetchBills().execute();
					} catch (Exception e) {
						errorDialog
								.setMessage("Something Wrong With Connection.");
						errorDialog.show();
					}
				} else {
					errorDialog.setMessage("Something Wrong With Connection.");
//					errorDialog.show();
				}
			} else if (taskId == email_task) {

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onTaskStart(int taskId) {
		dialog.show();
	}

	// fetch bill
	private class FetchBills extends
			AsyncTask<Integer, String, ArrayList<ReceiptItem>> {

		private ProgressDialog dialog;
		private Preference preference;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.show();
			preference = new Preference(getActivity());

		}

		@Override
		protected ArrayList<ReceiptItem> doInBackground(Integer... params) {

			try {

				// required for connection

				DefaultHttpClient client = LoginApi.getSessionClient(
						preference.getUserId(), preference.getSessionToken());

				ArrayList<ReceiptItem> adapterList = new ArrayList<ReceiptItem>();

				ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();

				selectedMonth.set(Calendar.DATE, 1);

				postParams.add(new BasicNameValuePair("StartDate", DateUtils
						.formatDate(selectedMonth, DateUtils.MM_dd_yyyy)));

				Log.d("StartDate", DateUtils.formatDate(selectedMonth,
						DateUtils.MM_dd_yyyy));
				selectedMonth.add(Calendar.MONTH, 1);
				selectedMonth.add(Calendar.DATE, -1);

				postParams.add(new BasicNameValuePair("EndDate", DateUtils
						.formatDate(selectedMonth, DateUtils.MM_dd_yyyy)));

				Log.d("EndDate", DateUtils.formatDate(selectedMonth,
						DateUtils.MM_dd_yyyy));
				HttpPost requestPost = new HttpPost(Constants.URL_RECEPTS);
				requestPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				requestPost.setEntity(new UrlEncodedFormEntity(postParams));
				HttpResponse responseList = client.execute(requestPost);
				BufferedReader readerList = new BufferedReader(
						new InputStreamReader(responseList.getEntity()
								.getContent()));
				StringBuilder responseListText = new StringBuilder();
				for (String line = readerList.readLine(); line != null; line = readerList
						.readLine()) {
					responseListText.append(line);
				}

				readerList.close();
				Log.d("response", responseListText.toString());
				JSONArray arrayList = new JSONArray(responseListText.toString()
						.trim());

				items = new ArrayList<ReceiptItem>();
				for (int i = 0, lim = arrayList.length(); i < lim; i++) {
					JSONObject object = arrayList.getJSONObject(i);
					ReceiptItem item = ReceiptItem.createFromJson(object);
					items.add(item);
				}

				for (Iterator<ReceiptItem> iterator = items.iterator(); iterator
						.hasNext();) {
					ReceiptItem receiptItem = iterator.next().getClone();
					ArrayList<NameValuePair> getParams = new ArrayList<NameValuePair>();
					getParams.add(new BasicNameValuePair("api_key",
							Constants.API_KEY));
					getParams.add(new BasicNameValuePair("receipt_id", ""
							+ receiptItem.getReceiptId()));

					HttpGet request = new HttpGet(Constants.URL_RECEPTS_DETAIL
							+ "/" + receiptItem.getReceiptId());
					Log.d("request", Constants.URL_RECEPTS_DETAIL + "/"
							+ receiptItem.getReceiptId());
					HttpResponse response = client.execute(request);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					StringBuilder responseBuilder = new StringBuilder();
					for (String line = reader.readLine(); line != null; line = reader
							.readLine()) {
						responseBuilder.append(line);
					}
					Log.d("response", responseBuilder.toString());
					JSONArray array = new JSONArray(responseBuilder.toString()
							.trim());
					ArrayList<ReceiptSubItem> subItems = new ArrayList<ReceiptSubItem>();
					for (int j = 0, lim = array.length(); j < lim; j++) {
						ReceiptSubItem subItem = ReceiptSubItem
								.createFromJson(array.getJSONObject(j));
						if (subItem != null) {
							subItems.add(subItem);
						}
					}
					receiptItem.setSubItems(subItems);
					adapterList.add(receiptItem);
				}
				return adapterList;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<ReceiptItem> result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result != null && getActivity() != null) {
				adapter = new ReportAdapter(getActivity(), result,
						privateWork.isChecked());
				listView.setAdapter(adapter);
				monthlyExpanseView.setText("Monthly Total  $"
						+ adapter.getTotalCost());

			} else {
				errorDialog.show();

			}

		}
	}

	// fetch bill
	private class EmailReport extends AsyncTask<Integer, String, String> {

		private ProgressDialog dialog;
		private Preference preference;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.show();
			preference = new Preference(getActivity());

		}

		@Override
		protected String doInBackground(Integer... params) {

			try {

				// required for connection

				DefaultHttpClient client = LoginApi.getSessionClient(
						preference.getUserId(), preference.getSessionToken());

				ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();

				selectedMonth.set(Calendar.DATE, 1);

				postParams.add(new BasicNameValuePair("Month", ""
						+ (selectedMonth.get(Calendar.MONTH) + 1)));

				selectedMonth.add(Calendar.MONTH, 1);
				selectedMonth.add(Calendar.DATE, -1);

				postParams.add(new BasicNameValuePair("Year", ""
						+ (selectedMonth.get(Calendar.YEAR))));

				Log.d("EndDate", DateUtils.formatDate(selectedMonth,
						DateUtils.MM_dd_yyyy));
				HttpPost requestPost = new HttpPost(
						Constants.URL_EMAIL_MONTHLY_REPORT);
				requestPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				requestPost.setEntity(new UrlEncodedFormEntity(postParams));
				HttpResponse responseList = client.execute(requestPost);
				BufferedReader readerList = new BufferedReader(
						new InputStreamReader(responseList.getEntity()
								.getContent()));
				StringBuilder responseListText = new StringBuilder();
				for (String line = readerList.readLine(); line != null; line = readerList
						.readLine()) {
					responseListText.append(line);
				}

				readerList.close();
				Log.d("response", responseListText.toString());

				return responseListText.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result != null && getActivity() != null) {

				if (!result.equalsIgnoreCase("\"success\"")) {
					emailVarification();
				} else {
					Toast.makeText(getActivity(),
							"Email is sent to your registered email address.",
							Toast.LENGTH_SHORT).show();
				}

			} else {
//				errorDialog.show();

			}

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (adapter != null) {
			adapter.showPrivate(isChecked);
		}
	}

}
