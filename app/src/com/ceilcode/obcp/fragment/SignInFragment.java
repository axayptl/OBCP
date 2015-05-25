package com.ceilcode.obcp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ceilcode.obcp.MainActivity;
import com.ceilcode.obcp.R;
import com.ceilcode.obcp.item.CountryCodeProvider;
import com.ceilcode.obcp.item.CountryCodeProvider.Country;

public class SignInFragment extends BaseFragment implements OnClickListener {

	private EditText phonetext, passwordText;
	private Button login;
	private TextView areaText;
	private TextView signUp, resendOtp;
	private ProgressDialog dialog;
	private static final int taskId = 0;

	/* all views */
	private TextView welcomeTitle;
	private TextView verifyYourAccount;

	private AlertDialog errorDialog;
	public int task_id;

	public String areaCode = "", number = "";

	public static interface OnVerificationCompleteListner {
		public void onVerificationComplete();
	}

	private OnVerificationCompleteListner completeListner = new OnVerificationCompleteListner() {

		@Override
		public void onVerificationComplete() {

			startActivity(new Intent(getActivity(), MainActivity.class));
			getActivity().finish();
		}
	};

	public static SignInFragment create(String areaCode, String number) {
		SignInFragment fragment = new SignInFragment();

		fragment.areaCode = areaCode;
		fragment.number = number;

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		getActivity().getActionBar().hide();

		View rootView = inflater.inflate(R.layout.fragment_signin, container,
				false);

		/* for text change */
		welcomeTitle = (TextView) rootView.findViewById(R.id.textView_welcome);
		verifyYourAccount = (TextView) rootView
				.findViewById(R.id.textView_verify_account);
		resendOtp = (TextView) rootView.findViewById(R.id.textView_resend_otp);

		verifyYourAccount.setOnClickListener(this);
		resendOtp.setOnClickListener(this);

		areaText = (TextView) rootView.findViewById(R.id.editText_areacode);
		phonetext = (EditText) rootView.findViewById(R.id.editText_phone);
		passwordText = (EditText) rootView.findViewById(R.id.editText_password);

		areaText.setText(areaCode);
		phonetext.setText(number);

		areaText.setOnClickListener(this);

		login = (Button) rootView.findViewById(R.id.button_signin);
		login.setOnClickListener(this);

		signUp = (TextView) rootView.findViewById(R.id.button_signup);
		signUp.setOnClickListener(this);
		signUp.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

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

		// areaText.setText("91");
		// phonetext.setText("9033934493");
		passwordText.setText("1234");

		setTypeface();

		if (callApi) {

			callApi = false;
		}

		return rootView;
	}

	private void setTypeface() {
		welcomeTitle.setTypeface(typefaceUtil.getTypeface());
		signUp.setTypeface(typefaceUtil.getTypeface());
		verifyYourAccount.setTypeface(typefaceUtil.getTypeface());
		areaText.setTypeface(typefaceUtil.getTypeface());
		phonetext.setTypeface(typefaceUtil.getTypeface());
		resendOtp.setTypeface(typefaceUtil.getTypeface());

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_signin:
			if (validate()) {

			}

			break;

		case R.id.textView_resend_otp:
			if (validate()) {

			}

			break;
		case R.id.button_signup:

			// getActivity().getSupportFragmentManager().beginTransaction()
			// .replace(R.id.content, new SignUpFragment()).commit();

			getActivity().getSupportFragmentManager().popBackStack();

			break;
		case R.id.textView_verify_account:

			if (validate()) {

				VerifyAccountFragment dialogFragment = VerifyAccountFragment
						.create(getActivity(), completeListner, areaText
								.getText().toString()
								+ phonetext.getText().toString());
				dialogFragment.setStyle(DialogFragment.STYLE_NORMAL,
						R.style.You_Dialog);
				dialogFragment.show(getActivity().getSupportFragmentManager(),
						"verify");
			} else {

			}
			break;

		case R.id.editText_areacode:

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Make your selection");
			Country[] items = CountryCodeProvider.ALL_COUNTRY;

			builder.setAdapter(new ArrayAdapter<Country>(getActivity(),
					android.R.layout.simple_list_item_1, items),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							areaText.setText(CountryCodeProvider.ALL_COUNTRY[which]
									.getCode());
						}
					});

			AlertDialog alert = builder.create();
			alert.show();

			break;

		}

	}

	private boolean validate() {

		if (areaText.getText().toString().trim().length() < 1) {
			errorDialog.setMessage("Area Code is required");
			errorDialog.show();
			return false;
		}

		if (phonetext.getText().toString().trim().length() < 1) {
			errorDialog.setMessage("Phone Number is required");
			errorDialog.show();
			return false;
		}

		// if (passwordText.getText().toString().trim().length() < 1) {
		// errorDialog.setMessage("Password is required");
		// errorDialog.show();
		//
		// return false;
		// }

		return true;
	}

	public boolean callApi = false;

	public static SignInFragment create(String areaCode, String number,
			boolean callApi) {
		SignInFragment fragment = new SignInFragment();
		fragment.areaCode = areaCode;
		fragment.number = number;
		fragment.callApi = callApi;
		return fragment;
	}
}