package com.ceilcode.obcp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.constant.Constants;
import com.ceilcode.obcp.ui.TypefaceUtil;
import com.ceilcode.obcp.util.JsonKeys;
import com.ceilcode.obcp.util.Preference;
import com.ceilcode.obcp.webservice.AbstractFetchTask;
import com.ceilcode.obcp.webservice.JsonResponseWrapper;
import com.ceilcode.obcp.webservice.MyRequestWrapper;
import com.ceilcode.obcp.webservice.OnTaskPerformListner;

public class ProfileFragmentEdit extends Fragment implements
		OnTaskPerformListner, OnClickListener {

	private Button editButton;
	private TextView emailTitle;
	private TextView maritalStatusTitle;
	private TextView genderTitle;
	private TextView ageTitle;

	private EditText emailView;
	private Spinner genderView;
	private EditText ageView;
	private Spinner maritalStatusView;

	private EditText firstNameTextView;
	private EditText lastNameTextView;

	private TypefaceUtil typefaceUtil;

	private int update_profile_task = 0, fetch_profile_task = 1;

	private ProgressDialog loading;

	private OnSaveCompleteListner saveCompleteListner;

	public static ProfileFragmentEdit create(
			OnSaveCompleteListner saveCompleteListner) {
		ProfileFragmentEdit fragmentEdit = new ProfileFragmentEdit();

		fragmentEdit.saveCompleteListner = saveCompleteListner;

		return fragmentEdit;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		typefaceUtil = TypefaceUtil.initiate(getActivity());
		View rootView = inflater.inflate(R.layout.fragment_profile_edit, null);

		firstNameTextView = (EditText) rootView
				.findViewById(R.id.editText_first_name);
		lastNameTextView = (EditText) rootView
				.findViewById(R.id.editText_last_name);
		emailView = (EditText) rootView.findViewById(R.id.editText_email);
		emailTitle = (TextView) rootView
				.findViewById(R.id.textView_title_email);

		maritalStatusView = (Spinner) rootView
				.findViewById(R.id.spinner_status);
		maritalStatusTitle = (TextView) rootView
				.findViewById(R.id.textView_title_status);

		genderView = (Spinner) rootView.findViewById(R.id.spinner_gender);
		genderTitle = (TextView) rootView
				.findViewById(R.id.textView_title_gender);

		ageView = (EditText) rootView.findViewById(R.id.editText_age);
		ageTitle = (TextView) rootView.findViewById(R.id.textView_title_age);

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

		firstNameTextView.setText("" + preference.getFirstName());

		lastNameTextView.setText("" + preference.getLastName());

		emailView.setText("" + preference.getEmail());
		// maritalStatusView.setText("" + preference.getMaritalStatus());
		// genderView.setText("" + preference.getGender());
		ageView.setText("" + preference.getAge());

		genderView.setSelection((preference.getGender() - 1));
	}

	private void setTypeface() {
		editButton.setTypeface(typefaceUtil.getTypeface());
		firstNameTextView.setTypeface(typefaceUtil.getTypeface());
		lastNameTextView.setTypeface(typefaceUtil.getTypeface());
		emailTitle.setTypeface(typefaceUtil.getTypeface());
		// phoneTextView.setTypeface(typefaceUtil.getTypeface());
		emailTitle.setTypeface(typefaceUtil.getTypeface());
		emailView.setTypeface(typefaceUtil.getTypeface());
		maritalStatusTitle.setTypeface(typefaceUtil.getTypeface());
		ageTitle.setTypeface(typefaceUtil.getTypeface());
		ageView.setTypeface(typefaceUtil.getTypeface());
		genderTitle.setTypeface(typefaceUtil.getTypeface());

	}

	@Override
	public void onTaskStart(int taskId) {
		loading.show();

	}

	@Override
	public void onTaskComplet(int taskId, JsonResponseWrapper result) {
		loading.dismiss();

		if (taskId == update_profile_task) {

			String email = emailView.getText().toString().trim();
			String firstname = firstNameTextView.getText().toString().trim();
			String lastname = lastNameTextView.getText().toString().trim();

			Integer MeritalStatus = (int) maritalStatusView.getSelectedItemId();
			Integer age = Integer.parseInt(ageView.getText().toString().trim());
			Integer gender = genderView.getSelectedItemPosition() + 1;

			Preference preference = new Preference(getActivity());

			preference.setEmail(email);
			preference.setFirstName(firstname);
			preference.setLastName(lastname);
			preference.setMaritalStatus(MeritalStatus);
			preference.setAge(age);
			preference.setGender(gender);

			if (saveCompleteListner != null) {
				saveCompleteListner.onsaveCompleted();
			}

		}

	}

	private boolean validateEmail() {

		if (emailView.getText() == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(
					emailView.getText()).matches();
		}

	}

	@Override
	public void onClick(View v) {

		if (validateEmail()) {
			Preference preference = new Preference(getActivity());

			MyRequestWrapper params = new MyRequestWrapper(
					Constants.URL_USER_DETAIL);

			params.setGetRequest(false);
			params.setPreLoginRequired(true);
			params.setLoginCredentials(new Preference(getActivity())
					.getUserId(), new Preference(getActivity())
					.getSessionToken());

			params.addParam(JsonKeys.UserItemKeys.KEY_AGE, ageView.getText()
					.toString());
			params.addParam(JsonKeys.UserItemKeys.KEY_EMAIL, emailView
					.getText().toString());
			params.addParam(JsonKeys.UserItemKeys.KEY_FIRST_NAME,
					firstNameTextView.getText().toString());
			params.addParam(JsonKeys.UserItemKeys.KEY_GENDER,
					(genderView.getSelectedItemPosition() + 1) + "");
			params.addParam(JsonKeys.UserItemKeys.KEY_LAST_NAME,
					lastNameTextView.getText().toString());
			params.addParam(
					JsonKeys.UserItemKeys.KEY_MARITAL_STATUS,
					maritalStatusView.getAdapter().getItemId(
							maritalStatusView.getSelectedItemPosition())
							+ "");

			params.addParam(JsonKeys.UserItemKeys.KEY_PHONE,
					preference.getPhone());
			params.addParam(JsonKeys.UserItemKeys.KEY_USER_ID,
					preference.getUserId());
			params.addParam(JsonKeys.UserItemKeys.KEY_ALT_ID,
					preference.getAltUserId());

			new AbstractFetchTask(update_profile_task, ProfileFragmentEdit.this) {

				@Override
				public void overrideOnProgressUpdate(String... values) {
					// TODO Auto-generated method stub

				}
			}.execute(params);
		} else {
			emailView.setError("invalid email address");
		}

	}

	public interface OnSaveCompleteListner {

		public void onsaveCompleted();

	}

}
