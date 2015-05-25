package com.ceilcode.obcp;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.ceilcode.obcp.ui.TypefaceUtil;

public class BaseActivity extends ActionBarActivity {
	private static final String LOG_TAG = "actionbar";

	public TypefaceUtil typefaceUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		typefaceUtil = TypefaceUtil.initiate(this);

		if (Build.VERSION.SDK_INT >= 11) {
			requestFeature();
		}
		supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);

		ActionBar actionBar = getActionBar();
		actionBar.hide();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.actionbar_todays_activity1);
		actionBar.show();
	}

	private void requestFeature() {
		try {
			Field fieldImpl = ActionBarActivity.class.getDeclaredField("mImpl");
			fieldImpl.setAccessible(true);
			Object impl = fieldImpl.get(this);

			Class<?> cls = Class
					.forName("android.support.v7.app.ActionBarActivityDelegate");

			Field fieldHasActionBar = cls.getDeclaredField("mHasActionBar");
			fieldHasActionBar.setAccessible(true);
			fieldHasActionBar.setBoolean(impl, true);

		} catch (NoSuchFieldException e) {
			Log.e(LOG_TAG, e.getLocalizedMessage(), e);
		} catch (IllegalAccessException e) {
			Log.e(LOG_TAG, e.getLocalizedMessage(), e);
		} catch (IllegalArgumentException e) {
			Log.e(LOG_TAG, e.getLocalizedMessage(), e);
		} catch (ClassNotFoundException e) {
			Log.e(LOG_TAG, e.getLocalizedMessage(), e);
		}
	}

}
