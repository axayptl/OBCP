package com.ceilcode.obcp.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.adapter.ReceiptAdapter;
import com.ceilcode.obcp.constant.Constants;
import com.ceilcode.obcp.item.ReceiptItem;
import com.ceilcode.obcp.item.ReceiptSubItem;
import com.ceilcode.obcp.util.Preference;
import com.ceilcode.obcp.webservice.LoginApi;

public class ReceiptFragment extends Fragment {

	private Calendar dateCalendar;

	private ExpandableListView receiptView;

	private int lastRecordId = 0;
	private ProgressDialog dialog;
	private AlertDialog errorDialog;

	private View footerView;

	public static ReceiptFragment newInstance(Calendar dateCalendar) {

		ReceiptFragment fragment = new ReceiptFragment();
		fragment.dateCalendar = dateCalendar;

		return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		//outState.putLong("DATE", dateCalendar.getTimeInMillis());

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			dateCalendar = Calendar.getInstance();
			dateCalendar.setTimeInMillis(savedInstanceState.getLong("DATE"));
		}

		View rootView = inflater.inflate(R.layout.fragment_receipt, container,
				false);

		receiptView = (ExpandableListView) rootView
				.findViewById(R.id.expandableListView);

		receiptView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					receiptView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});

		receiptView.setAdapter(new ReceiptAdapter(getActivity(),
				new ArrayList<ReceiptItem>()));

		setFooterView();

		errorDialog = new AlertDialog.Builder(getActivity()).create();
		errorDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		dialog = new ProgressDialog(getActivity());

		// receiptView.setAdapter(new ReceiptAdapter(getActivity(),
		// MyDataProvider
		// .getReceptsByDate(5, dateCalendar.get(Calendar.MONTH),
		// dateCalendar.get(Calendar.YEAR))));

		return rootView;
	}

	private void setFooterView() {

		// if (footerView == null) {
		// footerView = LayoutInflater.from(getActivity()).inflate(
		// R.layout.item_footer_view, null);
		// TextView textView = (TextView) footerView
		// .findViewById(R.id.textView);
		// textView.setText("No receipt found");
		//
		// }
		//
		// receiptView.addFooterView(footerView);

	}

	private void setFooterViewText(String text) {

		// if (footerView == null) {
		// setFooterView();
		//
		// }
		//
		// TextView textView = (TextView)
		// footerView.findViewById(R.id.textView);
		// textView.setText(text);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		refreshReceipts();
	}

	public void refreshReceipts(Calendar calendar) {

		this.dateCalendar = calendar;

		if (getActivity() != null) {
			refreshReceipts();

		}

	}

	public void refreshReceipts() {

		Preference preference = new Preference(getActivity());

	}

}
