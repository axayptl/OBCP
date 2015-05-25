package com.ceilcode.obcp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ceilcode.obcp.R;

public class ForgotPasswordFragment extends DialogFragment {

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_forgot_password,
				container, false);
		return rootView;
	}

}
