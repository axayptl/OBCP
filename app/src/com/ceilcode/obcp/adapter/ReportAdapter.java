package com.ceilcode.obcp.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.item.ReceiptItem;
import com.ceilcode.obcp.item.ReceiptSubItem;
import com.ceilcode.obcp.ui.TypefaceUtil;

public class ReportAdapter extends BaseExpandableListAdapter {

	private static final int GROUP_VIEW = R.layout.item_group_expense_expanded_with_date;
	private static final int CHILD_VIEW = R.layout.item_child_expense;

	private LayoutInflater inflater;
	private Context context;

	private ArrayList<ReceiptItem> itemsPrivate;
	private ArrayList<ReceiptItem> itemsWork;

	private ArrayList<ReceiptItem> items;

	private ArrayList<ReceiptItem> unFilteredItem;;

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

	public ReportAdapter(Context context, ArrayList<ReceiptItem> items,
			boolean showPrivate) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.unFilteredItem = items;
		itemDistribute();
		this.items = showPrivate ? itemsPrivate : itemsWork;
		typefaceUtil = TypefaceUtil.initiate(context);
	}

	private void itemDistribute() {
		itemsPrivate = new ArrayList<ReceiptItem>();
		itemsWork = new ArrayList<ReceiptItem>();
		for (Iterator<ReceiptItem> iterator = unFilteredItem.iterator(); iterator
				.hasNext();) {

			try {

				ReceiptItem item = (ReceiptItem) iterator.next();

				if (ReceiptItem.getReceiptTypeFromId(item.getReceiptTypeId())
						.equalsIgnoreCase("work")) {
					itemsWork.add(item);
				} else {
					itemsPrivate.add(item);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public Object getChild(int arg0, int arg1) {

		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {

		if (arg3 == null) {

			arg3 = inflater.inflate(CHILD_VIEW, arg4, false);
		}

		ReceiptSubItem item = items.get(arg0).getSubItems().get(arg1);

		ImageView shareView = (ImageView) arg3
				.findViewById(R.id.imageView_share);

		shareView.setTag("Check out this product - " + item.getTitle() + "  "
				+ item.getCost() + "$  " + items.get(arg0).getPosUserId()
				+ " \nsend from http://dbill.me");
		shareView.setOnClickListener(shareCLickListner);
		TextView textView1 = (TextView) arg3.findViewById(R.id.textView1);
		textView1.setText(item.getTitle());
		TextView textView2 = (TextView) arg3.findViewById(R.id.textView2);
		textView2.setText(item.getValue());
		TextView textView3 = (TextView) arg3.findViewById(R.id.textView3);
		textView3.setText("$" + item.getCost());
		TextView textView4 = (TextView) arg3.findViewById(R.id.textView4);
		textView4.setText("$" + item.getTotal());

		textView1.setTypeface(typefaceUtil.getTypefaceRegular());
		textView2.setTypeface(typefaceUtil.getTypefaceRegular());
		textView3.setTypeface(typefaceUtil.getTypefaceRegular());
		textView4.setTypeface(typefaceUtil.getTypefaceRegular());

		arg3.setBackgroundColor(context.getResources().getColor(
				(arg1 % 2 == 0) ? R.color.bg_gray : R.color.bg_white));
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0).getSubItems().size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return items.size();
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

		ReceiptItem item = items.get(arg0);

		TextView title = (TextView) arg2.findViewById(R.id.textView1);
		title.setText(item.getPosUserId());
		TextView date = (TextView) arg2.findViewById(R.id.textView3);
		date.setText(item.getPosUserId());
		TextView time = (TextView) arg2.findViewById(R.id.textView4);
		time.setText(item.getDateTime(new SimpleDateFormat("HH:MM", Locale
				.getDefault())));

		TextView totalCost = (TextView) arg2.findViewById(R.id.textView2);
		totalCost.setText(item.getTotal() + "$");
		TextView textView5 = (TextView) arg2
				.findViewById(R.id.textView_maindate);
		textView5.setText(item.getDateTime(new SimpleDateFormat("MM.dd", Locale
				.getDefault())));

		title.setTypeface(typefaceUtil.getTypefaceRegular());
		date.setTypeface(typefaceUtil.getTypefaceRegular());
		time.setTypeface(typefaceUtil.getTypefaceRegular());
		totalCost.setTypeface(typefaceUtil.getTypefaceRegular());
		textView5.setTypeface(typefaceUtil.getTypefaceRegular());

		setGroupView(arg2, arg1);

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

	public double getTotalCost() {
		double totalCost = 0;
		for (Iterator<ReceiptItem> iterator = items.iterator(); iterator
				.hasNext();) {
			ReceiptItem item = (ReceiptItem) iterator.next();

			totalCost += item.getTotal();

		}
		return totalCost;
	}

	public void showPrivate(boolean isChecked) {
		this.items = isChecked ? itemsPrivate : itemsWork;
		notifyDataSetChanged();

	}

}
