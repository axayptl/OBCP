package com.ceilcode.obcp.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.ceilcode.obcp.BaseActivity;
import com.ceilcode.obcp.ui.TypefaceUtil;

public class BaseFragment extends Fragment {

	protected TypefaceUtil typefaceUtil;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		typefaceUtil = ((BaseActivity) activity).typefaceUtil;
	}

}
