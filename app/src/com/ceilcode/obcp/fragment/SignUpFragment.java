package com.ceilcode.obcp.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ceilcode.obcp.BaseActivity;
import com.ceilcode.obcp.R;
import com.ceilcode.obcp.constant.Constants;
import com.ceilcode.obcp.item.CountryCodeProvider;
import com.ceilcode.obcp.item.CountryCodeProvider.Country;
import com.ceilcode.obcp.ui.TypefaceUtil;

public class SignUpFragment extends BaseFragment implements OnClickListener {
	private EditText phonetext;
	private Button login;
	private ProgressDialog dialog;
	private static final int taskId = 0;
	private CheckBox rememberMe;
	private AlertDialog errorDialog;
	private TextView signUp;
	private TextView areaText;
	private TextView welcomeTitle;

	private TypefaceUtil typefaceUtil;
	private TextView verifyYourAccount;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// getActivity().getActionBar().show();
		getActivity().getActionBar().hide();
		View rootView = inflater.inflate(R.layout.fragment_signup, container,
				false);

		welcomeTitle = (TextView) rootView.findViewById(R.id.textView_welcome);
		verifyYourAccount = (TextView) rootView
				.findViewById(R.id.textView_verify_account);

		areaText = (TextView) rootView.findViewById(R.id.editText_areacode);
		areaText.setOnClickListener(this);
		phonetext = (EditText) rootView.findViewById(R.id.editText_phone);

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

		setTypeface();

		verifyYourAccount.setVisibility(View.INVISIBLE);

		return rootView;
	}

	private void setTypeface() {
		welcomeTitle.setTypeface(typefaceUtil.getTypeface());
		signUp.setTypeface(typefaceUtil.getTypeface());
		areaText.setTypeface(typefaceUtil.getTypeface());
		phonetext.setTypeface(typefaceUtil.getTypeface());
		verifyYourAccount.setTypeface(typefaceUtil.getTypeface());

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		typefaceUtil = ((BaseActivity) activity).typefaceUtil;
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

		return true;
	}

	private class MyLoginTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				// required for connection
				HttpPost request = new HttpPost(Constants.URL_SIGNUP);
				request.setHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();

				pairs.add(new BasicNameValuePair("UserID", params[0].trim()));
				pairs.add(new BasicNameValuePair("api_key", Constants.API_KEY));
				pairs.add(new BasicNameValuePair("Password", ""));
				request.setEntity(new UrlEncodedFormEntity(pairs));
				HttpParams httpParams = new BasicHttpParams();

				HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(httpParams,
						HTTP.DEFAULT_CONTENT_CHARSET);
				HttpProtocolParams.setUseExpectContinue(httpParams, true);

				HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
				HttpConnectionParams
						.setConnectionTimeout(httpParams, 999999999);
				HttpConnectionParams.setSoTimeout(httpParams, 999999999);
				HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				schReg.register(new Scheme("https", SSLSocketFactory
						.getSocketFactory(), 443));
				ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
						httpParams, schReg);

				DefaultHttpClient client = new DefaultHttpClient(conMgr,
						httpParams);

				HttpResponse response = client.execute(request);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));

				StringBuilder responseBuilder = new StringBuilder();
				for (String line = reader.readLine(); line != null; line = reader
						.readLine()) {

					responseBuilder.append(line);

				}

				Log.d("response", responseBuilder.toString());
				return responseBuilder.toString().trim();

			} catch (Exception e) {
				e.printStackTrace();
				return null;

			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				dialog.dismiss();
				if (result.equalsIgnoreCase("\"success\"")) {

					Preference preference = new Preference(getActivity());
					

					// MyRequestWrapper params = new MyRequestWrapper(
					// Constants.URL_USER_DETAIL);

					// new AbstractFetchTask(taskId, SignInFragment.this) {
					//
					// @Override
					// public void overrideOnProgressUpdate(String... values) {
					//
					// }
					// }.execute(params);
					AlertDialog alertDialog = new AlertDialog.Builder(
							getActivity()).create();

					alertDialog
							.setMessage("Thank you for for register with dbill.me. we just sent you sms to verify your phone number. Please check your sms and type the code in dbill application.");
					alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									getActivity()
											.getSupportFragmentManager()
											.beginTransaction()
											.replace(R.id.content,
													SignInFragment.create(areaText.getText().toString()
															.trim(), phonetext.getText().toString()
															.trim(),true))
											.commit();
//									VerifyAccountFragment dialogFragment = new VerifyAccountFragment();
//									dialogFragment.setStyle(
//											DialogFragment.STYLE_NORMAL,
//											R.style.You_Dialog);
//									dialogFragment.show(getActivity()
//											.getSupportFragmentManager(),
//											"verify");

								}
							});

					alertDialog.show();

				} else if (result.equalsIgnoreCase("\"fail\"")) {
					errorDialog.setMessage("Invalid Login Information");
					errorDialog.show();
				} else {
					errorDialog
							.setMessage("Please check your internet connection.");
					errorDialog.show();
				}

			} catch (Exception exception) {
				exception.printStackTrace();
				errorDialog
						.setMessage("Please check your internet connection.");
				errorDialog.show();
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_signin:
			if (validate()) {
				new MyLoginTask().execute(areaText.getText().toString().trim()
						+ phonetext.getText().toString());
			}

			break;
			
		case R.id.button_signup:

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new SignInFragment())
					.addToBackStack("signup").commit();

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
}
