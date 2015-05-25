package com.ceilcode.obcp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.ui.TypefaceUtil;

public class SettingsDrawerAdapter extends BaseAdapter {

	private final int ICON_DRAWABLES_IDS[] = { R.drawable.ic_home,
			R.drawable.ic_yellow_help, R.drawable.ic_yellow_profile,
			R.drawable.ic_message, R.drawable.ic_yellow_report };
	private final String[] TEXTS = { "Home", "FAQ", "Profile", "Contact Us",
			"Problems" };

	private TypefaceUtil typefaceUtil;

	private LayoutInflater inflater;

	public SettingsDrawerAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		typefaceUtil = TypefaceUtil.initiate(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ICON_DRAWABLES_IDS.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		// reuse views
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.item_list_settings, arg2, false);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) arg1.findViewById(R.id.textView_text);
			viewHolder.image = (ImageView) arg1
					.findViewById(R.id.imageView_icon);
			viewHolder.text.setTypeface(typefaceUtil.getTypeface());
			arg1.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) arg1.getTag();
		holder.text.setText(TEXTS[arg0]);
		holder.image.setImageResource(ICON_DRAWABLES_IDS[arg0]);

		return arg1;
	}

	static class ViewHolder {
		public TextView text;
		public ImageView image;
	}
}
