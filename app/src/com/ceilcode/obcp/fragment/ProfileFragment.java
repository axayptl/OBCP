package com.ceilcode.obcp.fragment;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.fragment.ProfileFragmentEdit.OnSaveCompleteListner;
import com.ceilcode.obcp.ui.TypefaceUtil;
import com.ceilcode.obcp.util.JsonKeys;
import com.ceilcode.obcp.util.Preference;
import com.ceilcode.obcp.webservice.JsonResponseWrapper;
import com.ceilcode.obcp.webservice.OnTaskPerformListner;

public class ProfileFragment extends Fragment implements OnClickListener {

	private Button editButton;
	private TextView emailTitle;
	private TextView maritalStatusTitle;
	private TextView genderTitle;
	private TextView ageTitle;
	private TextView monthlyBudgetTitle;

	private TextView emailView;
	private TextView maritalStatusView;
	private TextView genderView;
	private TextView ageView;
	private TextView monthlyBudgetView;

	private TextView nameTextView;

	private TypefaceUtil typefaceUtil;

	private int task_id;

	private ProgressDialog loading;

	private static final String[] GENDER = { "Male", "Female" };

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		typefaceUtil = TypefaceUtil.initiate(getActivity());
		View rootView = inflater.inflate(R.layout.fragment_profile, null);

		nameTextView = (TextView) rootView.findViewById(R.id.textView_name);
		emailView = (TextView) rootView.findViewById(R.id.textView_email);
		emailTitle = (TextView) rootView
				.findViewById(R.id.textView_title_email);

		maritalStatusView = (TextView) rootView
				.findViewById(R.id.textView_status);
		maritalStatusTitle = (TextView) rootView
				.findViewById(R.id.textView_title_status);

		genderView = (TextView) rootView.findViewById(R.id.textView_gender);
		genderTitle = (TextView) rootView
				.findViewById(R.id.textView_title_gender);

		ageView = (TextView) rootView.findViewById(R.id.textView_age);
		ageTitle = (TextView) rootView.findViewById(R.id.textView_title_age);
		monthlyBudgetView = (TextView) rootView
				.findViewById(R.id.textView_monthly);
		monthlyBudgetTitle = (TextView) rootView
				.findViewById(R.id.textView_title_monthly);

		setDataFromPreference();

		editButton = (Button) rootView.findViewById(R.id.button_edit);

		editButton.setOnClickListener(this);

		setTypeface();

		loading = new ProgressDialog(getActivity());

		// MyRequestWrapper params = new MyRequestWrapper(
		// Constants.URL_USER_DETAIL);
		// params.setPreLoginRequired(true);
		// params.setLoginCredentials(new Preference(getActivity()).getUserId(),
		// new Preference(getActivity()).getPassword());
		// new AbstractFetchTask(task_id, this) {
		//
		// @Override
		// public void overrideOnProgressUpdate(String... values) {
		// // TODO Auto-generated method stub
		//
		// }
		// }.execute(params);
		return rootView;
	}

	private void setDataFromPreference() {

		Preference preference = new Preference(getActivity());

		nameTextView.setText("User Test ");

		emailView.setText("" + preference.getEmail());

		genderView.setText(""
				+ ((preference.getGender() > 0) ? GENDER[(preference
						.getGender() - 1)] : ""));
		ageView.setText("" + preference.getAge());
		monthlyBudgetView.setText("" + preference.getMonthlyBudget() + "$");
	}

	private void setTypeface() {
		editButton.setTypeface(typefaceUtil.getTypeface());
		nameTextView.setTypeface(typefaceUtil.getTypeface());
		emailTitle.setTypeface(typefaceUtil.getTypeface());
		// phoneTextView.setTypeface(typefaceUtil.getTypeface());
		emailTitle.setTypeface(typefaceUtil.getTypeface());
		emailView.setTypeface(typefaceUtil.getTypeface());
		maritalStatusTitle.setTypeface(typefaceUtil.getTypeface());
		maritalStatusView.setTypeface(typefaceUtil.getTypeface());
		ageTitle.setTypeface(typefaceUtil.getTypeface());
		ageView.setTypeface(typefaceUtil.getTypeface());
		monthlyBudgetTitle.setTypeface(typefaceUtil.getTypeface());
		monthlyBudgetView.setTypeface(typefaceUtil.getTypeface());
		genderTitle.setTypeface(typefaceUtil.getTypeface());
		genderTitle.setTypeface(typefaceUtil.getTypeface());

	}

	@Override
	public void onClick(View v) {

		// Preference preference = new Preference(getActivity());
		//
		// EditProfileFragment dialogFragment = EditProfileFragment
		// .createInstance(preference.getUserId(), preference.getEmail(),
		// preference.getFirstName(), preference.getLastName(),
		// preference.getPhone(), saveCompleteListner);
		// dialogFragment
		// .setStyle(DialogFragment.STYLE_NORMAL, R.style.You_Dialog);
		// dialogFragment.show(getActivity().getSupportFragmentManager(),
		// "edit");

		FragmentManager manager = getActivity().getSupportFragmentManager();
		manager.beginTransaction()
				.replace(R.id.content,
						(ProfileFragmentEdit.create(saveCompleteListner)))
				.addToBackStack("edit").commit();

	}

	private OnSaveCompleteListner saveCompleteListner = new OnSaveCompleteListner() {

		@Override
		public void onsaveCompleted() {

			setDataFromPreference();
			FragmentManager manager = getActivity().getSupportFragmentManager();
			manager.popBackStack();

		}
	};

}
