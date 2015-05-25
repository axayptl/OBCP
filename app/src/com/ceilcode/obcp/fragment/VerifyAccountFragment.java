package com.ceilcode.obcp.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ceilcode.obcp.R;
import com.ceilcode.obcp.constant.Constants;
import com.ceilcode.obcp.fragment.SignInFragment.OnVerificationCompleteListner;
import com.ceilcode.obcp.gcm.Globals;
import com.ceilcode.obcp.util.JsonKeys;
import com.ceilcode.obcp.util.Preference;

import com.ceilcode.obcp.webservice.AbstractFetchTask;
import com.ceilcode.obcp.webservice.JsonResponseWrapper;
import com.ceilcode.obcp.webservice.LoginApi;
import com.ceilcode.obcp.webservice.MyRequestWrapper;
import com.ceilcode.obcp.webservice.OnTaskPerformListner;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class VerifyAccountFragment extends DialogFragment implements
		OnClickListener,OnTaskPerformListner {

	private static final int VERIFY_TASK = 1;
	private static final int USER_DETAIL_TASK = 2;
	private View rootView;
	private ImageButton close;
	private ImageButton verifyButton;
	private EditText verificationCode;
	private ProgressDialog dialog;
	private AlertDialog errorDialog;
	private String userId;
	private Activity context;

	/*
	 * GCM constants
	 */

	public static final String EXTRA_MESSAGE = "message";
	private static final String PROPERTY_APP_VERSION = "1.0";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/*
	 * GCM Fields
	 */
	private GoogleCloudMessaging gcm;
	private String regid;
	private AtomicInteger msgId = new AtomicInteger();

	private OnVerificationCompleteListner onVerificationCompleteListner;

	public static VerifyAccountFragment create(Activity context,
			OnVerificationCompleteListner onVerificationCompleteListner,
			String userId) {
		VerifyAccountFragment fragment = new VerifyAccountFragment();

		fragment.userId = userId;
		fragment.onVerificationCompleteListner = onVerificationCompleteListner;

		fragment.context = context;

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_verify_account,
				container, false);
		dialog = new ProgressDialog(getActivity());

		verificationCode = (EditText) rootView
				.findViewById(R.id.editText_verication_code);

		close = (ImageButton) rootView.findViewById(R.id.imageButton_close);
		verifyButton = (ImageButton) rootView
				.findViewById(R.id.imageButton_verify);

		verifyButton.setOnClickListener(this);

		close.setOnClickListener(this);

		// creating the fullscreen dialog
		Dialog dialog = getDialog();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		errorDialog = new AlertDialog.Builder(getActivity()).create();
		errorDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		return rootView;
	}

	private boolean validate() {

		if (verificationCode.getText().toString().trim().length() < 1) {
			errorDialog.setMessage("Verification code is required");
			errorDialog.show();
			return false;
		}

		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageButton_verify:

			if (!validate()) {
				return;
			}

		
			break;
		case R.id.imageButton_close:
			this.dismiss();

			break;

		}

	}

	@Override
	public void onTaskStart(int taskId) {

		dialog.show();

	}

	@Override
	public void onTaskComplet(int taskId, JsonResponseWrapper result) {
		dialog.dismiss();

		if (taskId == VERIFY_TASK) {
			try {

				JSONObject jsonObject = (JSONObject) result.getJsonPack()
						.getWholeJson();
				if (jsonObject.getString(JsonKeys.VerificationKeys.KEY_STATUS)
						.equals("success")) {
					this.dismiss();

					new Preference(context).setSessionToken(jsonObject
							.getString(JsonKeys.VerificationKeys.KEY_TOKEN));

					MyRequestWrapper params = new MyRequestWrapper(
							Constants.URL_USER_DETAIL);
					params.setPreLoginRequired(true);
					params.setContext(context);
					params.setLoginCredentials(userId, new Preference(
							getActivity()).getSessionToken());

					Log.e("Login Detail", userId + " 1234 ");
					new AbstractFetchTask(USER_DETAIL_TASK,
							VerifyAccountFragment.this) {

						@Override
						public void overrideOnProgressUpdate(String... values) {

						}
					}.execute(params);
				} else {
					Toast.makeText(getActivity(), "Invalid Varification Code",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (taskId == USER_DETAIL_TASK) {
			try {
				String userId = (String) result.getJsonPack().getFromObject(
						JsonKeys.UserItemKeys.KEY_USER_ID);
				String phone = (String) result.getJsonPack().getFromObject(
						JsonKeys.UserItemKeys.KEY_PHONE);
				String email = (String) result.getJsonPack().getFromObject(
						JsonKeys.UserItemKeys.KEY_EMAIL);
				String firstname = (String) result.getJsonPack().getFromObject(
						JsonKeys.UserItemKeys.KEY_FIRST_NAME);
				String lastname = (String) result.getJsonPack().getFromObject(
						JsonKeys.UserItemKeys.KEY_LAST_NAME);
				String altId = (String) result.getJsonPack().getFromObject(
						JsonKeys.UserItemKeys.KEY_ALT_ID);
				Integer MeritalStatus = Integer
						.parseInt(result
								.getJsonPack()
								.getFromObject(
										JsonKeys.UserItemKeys.KEY_MARITAL_STATUS)
								.toString().trim());
				Integer age = Integer.parseInt(result.getJsonPack()
						.getFromObject(JsonKeys.UserItemKeys.KEY_AGE)
						.toString().trim());
				Integer gender = Integer.parseInt(result.getJsonPack()
						.getFromObject(JsonKeys.UserItemKeys.KEY_GENDER)
						.toString().trim());
				float monthlyBudget = Float
						.parseFloat(result
								.getJsonPack()
								.getFromObject(
										JsonKeys.UserItemKeys.KEY_MONTHLY_BUDGET)
								.toString().trim());

				Preference preference = new Preference(context);
				preference.setUserId(userId);

				preference.setAtlUserId(altId);
				preference.setEmail(email);
				preference.setFirstName(firstname);
				preference.setLastName(lastname);
				preference.setPhone(phone);
				preference.setMaritalStatus(MeritalStatus);
				preference.setAge(age);
				preference.setGender(gender);
				preference.setMonthlyBudget(monthlyBudget);
				preference.setPassword(verificationCode.getText().toString()
						.trim());

				if (checkPlayServices()) {
					registerInBackground();

				} else {
					Log.e(Globals.TAG,
							"No valid Google Play Services APK found.");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(Globals.PREFS_PROPERTY_REG_ID,
				"");
		if (registrationId == null || registrationId.equals("")) {
			Log.i(Globals.TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(Globals.TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but how you store the regID in your app is up to you.
		return context.getSharedPreferences(Globals.PREFS_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, context,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(Globals.TAG, "This device is not supported.");
				dismiss();
			}
			return false;
		}
		return true;
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<String, Void, String>() {

			protected void onPreExecute() {
				dialog.show();
			};

			@Override
			protected String doInBackground(String... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(Globals.GCM_SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// required for connection

					Preference preference = new Preference(context);
					DefaultHttpClient client = LoginApi.getSessionClient(
							preference.getUserId(),
							preference.getSessionToken());

					ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();

					postParams
							.add(new BasicNameValuePair("DeviceToken", regid));
					postParams
							.add(new BasicNameValuePair("Platform", "android"));

					HttpPost requestPost = new HttpPost(
							Constants.URL_SEND_PUSH_ID);

					requestPost.setHeader("Content-Type",
							"application/x-www-form-urlencoded;charset=UTF-8");
					requestPost.setEntity(new UrlEncodedFormEntity(postParams));
					HttpResponse responseList = client.execute(requestPost);
					BufferedReader readerList = new BufferedReader(
							new InputStreamReader(responseList.getEntity()
									.getContent()));
					StringBuilder responseListText = new StringBuilder();
					for (String line = readerList.readLine(); line != null; line = readerList
							.readLine()) {
						responseListText.append(line);
					}

					readerList.close();
					Log.e("gcm id", responseListText.toString());

					if (responseListText.toString().equals("\"success\"")) {
						storeRegistrationId(context, regid);
					}
					return regid;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				dialog.dismiss();
			};

			@Override
			protected void onPostExecute(String msg) {

				// complete register id
				regid = msg;
				onVerificationCompleteListner.onVerificationComplete();

				Log.e("ID", regid);

			}
		}.execute();

	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(Globals.TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Globals.PREFS_PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();

	}

}
