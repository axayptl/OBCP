package com.ceilcode.obcp.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.constant.Constants;
import com.ceilcode.obcp.item.ReceiptItem;
import com.ceilcode.obcp.ui.TypefaceUtil;
import com.ceilcode.obcp.util.Preference;
import com.ceilcode.obcp.webservice.AbstractFetchTask;
import com.ceilcode.obcp.webservice.JsonResponseWrapper;
import com.ceilcode.obcp.webservice.MyRequestWrapper;
import com.ceilcode.obcp.webservice.OnTaskPerformListner;

public class ReceiptAdapter extends BaseExpandableListAdapter implements
		OnCheckedChangeListener, OnTaskPerformListner {

	private static final int GROUP_VIEW = R.layout.item_problem;
	private static final int CHILD_VIEW = R.layout.item_solution;

	private LayoutInflater inflater;
	private Context context;

	private ArrayList<ReceiptItem> items;

	private TypefaceUtil typefaceUtil;

	private OnClickListener shareCLickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, v.getTag().toString());
			context.startActivity(Intent.createChooser(shareIntent, "Share..."));

		}
	};
	private int type_change_task = 0;
	private ProgressDialog progressDialog;

	public ReceiptAdapter(Context context, ArrayList<ReceiptItem> items) {
		this.context = context;
		inflater = LayoutInflater.from(context);

		typefaceUtil = TypefaceUtil.initiate(context);

		progressDialog = new ProgressDialog(context);
	}

	@Override
	public Object getChild(int arg0, int arg1) {

		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {

		if (arg3 == null) {

			arg3 = inflater.inflate(CHILD_VIEW, arg4, false);
		}

		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {

		if (arg2 == null) {
			arg2 = inflater.inflate(GROUP_VIEW, arg3, false);

		}

		return arg2;
	}

	private void setGroupView(View view, boolean isExpanded) {

		ImageView date = (ImageView) view.findViewById(R.id.imageView1);
		ImageView time = (ImageView) view.findViewById(R.id.imageView2);

		if (isExpanded) {
			view.setBackgroundResource(R.drawable.bg_group_expanded);
			date.setImageResource(R.drawable.ic_location);
			time.setImageResource(R.drawable.ic_time);

		} else {
			view.setBackgroundResource(R.drawable.bg_receipt_color);
			date.setImageResource(R.drawable.ic_blue_location);
			time.setImageResource(R.drawable.ic_blue_time);
		}
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getTotalCost() {
		double totalCost = 0;
		for (Iterator<ReceiptItem> iterator = items.iterator(); iterator
				.hasNext();) {
			ReceiptItem item = (ReceiptItem) iterator.next();

			totalCost += item.getTotal();

		}

		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		return df.format(totalCost);
	}

	private ReceiptItem inProcessReceiptItem;

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		inProcessReceiptItem = (ReceiptItem) buttonView.getTag();

		int newTypeId = ReceiptItem
				.getReceiptTypeFromName(isChecked ? "private" : "work");

		MyRequestWrapper params = new MyRequestWrapper(
				Constants.URL_UPDATE_RECEIPT_TYPE);
		params.setPreLoginRequired(true);
		params.setLoginCredentials(new Preference(context).getUserId(),
				new Preference(context).getSessionToken());
		params.setGetRequest(false);
		params.addParam("ReceiptID", "" + inProcessReceiptItem.getReceiptId());
		params.addParam("ReceiptTypeID", "" + (newTypeId));

		new AbstractFetchTask(type_change_task, this) {

			@Override
			public void overrideOnProgressUpdate(String... values) {
				// TODO Auto-generated method stub

			}
		}.execute(params);
	}

	@Override
	public void onTaskStart(int taskId) {
		progressDialog.show();
	}

	@Override
	public void onTaskComplet(int taskId, JsonResponseWrapper result) {
		progressDialog.dismiss();
		try {

			if (result.getJsonPack().getRawResult().equals("\"success\"")) {

				if (inProcessReceiptItem != null) {
					inProcessReceiptItem.changeReceiptType();

				}
			}
			notifyDataSetChanged();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

}
