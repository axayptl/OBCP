package com.ceilcode.obcp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ceilcode.obcp.fragment.SignUpFragment;
import com.ceilcode.obcp.util.Preference;

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content, new SignUpFragment()).commit();

		if (new Preference(this).getUserId().length() > 0) {

			startActivity(new Intent(this, MainActivity.class));
			finish();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
