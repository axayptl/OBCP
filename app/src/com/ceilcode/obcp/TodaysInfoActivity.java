package com.ceilcode.obcp;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ceilcode.obcp.adapter.SettingsDrawerAdapter;
import com.ceilcode.obcp.fragment.CalendarFragment;
import com.ceilcode.obcp.fragment.CalendarFragment.OnDateClickListner;
import com.ceilcode.obcp.fragment.ProfileFragment;
import com.ceilcode.obcp.fragment.ReceiptFragment;
import com.ceilcode.obcp.fragment.ReportFragment;
import com.ceilcode.obcp.util.Preference;

public class TodaysInfoActivity extends BaseActivity implements
		OnDateClickListner {

	private static final int fetch_bill_task = 0;
	private static final int fetch_receipt_type_task = 1;
	private static final int fetch_marital_status_task = 2;
	private ListView leftDrawerList;
	private RelativeLayout rightDrawer;
	private RelativeLayout leftDrawer;

	private DrawerLayout drawerLayout;

	private CalendarFragment calendarFragment;
	private ReceiptFragment receiptFragment;
	private ReportFragment reportFragment;
	private ProfileFragment profileFragment;

	private AlertDialog retryDialog;

	// refresh data
	// private RefreshDataTask refreshDataTask;
	// private Timer timer;
	private long delay = 0;
	private long period = 1000 * 60;
	private Handler handler;
	private ProgressDialog dialog;
	private ProgressDialog dialog1;

	private int lastRecordId = 0;
	private boolean isFistTime = true;
	private AlertDialog errorDialog;

	// actionbar items

	private TextView titleCalanderDate;
	private TextView userId, appTitle;

	public static String password = "";

	public static String getpassword() {
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		if (password.length() < 8) {

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < 8; i++) {
				double index = Math.random() * characters.length();
				buffer.append(characters.charAt((int) index));
			}
			buffer.toString();
			password = buffer.toString().trim();
		}

		Log.d("password", password);
		return password;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_todays_info);
		setupFragments();

		retryDialog = new AlertDialog.Builder(this).create();

		retryDialog.setMessage("Error in connection.");
		retryDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});

		retryDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		drawerLayout = (DrawerLayout) findViewById(R.id.sliding_pane_layout);
		leftDrawerList = (ListView) findViewById(R.id.left_drawer);

		leftDrawerList.setAdapter(new SettingsDrawerAdapter(this));
		leftDrawerList.setOnItemClickListener(navigationClickListener);

		leftDrawer = (RelativeLayout) findViewById(R.id.layout_left_drawer);

		rightDrawer = (RelativeLayout) findViewById(R.id.right_drawer);

		// title
		userId = (TextView) findViewById(R.id.textView_user_id);
		titleCalanderDate = (TextView) findViewById(R.id.textView_calendar);
		appTitle = (TextView) findViewById(R.id.textView_app_title);

		String formatedUserId = new Preference(this).getAltUserId();

		formatedUserId = "#5467";

		Log.e("session token ", new Preference(this).getSessionToken());

		userId.setText(formatedUserId);
		titleCalanderDate.setText(Calendar.getInstance(Locale.getDefault())
				.get(Calendar.DATE) + "");

		// left drawer open
		ImageButton settingButton = (ImageButton) findViewById(R.id.imageButton_settings);
		settingButton.setOnClickListener(toggleDrawerClickListner);

		// right drawer open
		ImageButton calendarButton = (ImageButton) findViewById(R.id.imageButton_calendar);
		calendarButton.setOnClickListener(toggleDrawerClickListner);

		// refresh data
		handler = new Handler();
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);

		dialog1 = new ProgressDialog(this);
		dialog1.setCancelable(false);

		isFistTime = true;
		errorDialog = new AlertDialog.Builder(this).create();
		errorDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		// timer = new Timer();
		// refreshDataTask = new RefreshDataTask();
		setypeface();

	}

	private void setypeface() {
		userId.setTypeface(typefaceUtil.getTypefaceBold());
		titleCalanderDate.setTypeface(typefaceUtil.getTypeface());
		appTitle.setTypeface(typefaceUtil.getTypefaceBold());
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// timer.cancel();
		// refreshDataTask.cancel();

	}

	private void setupFragments() {
		try {
			if (receiptFragment == null) {
				
				receiptFragment=new ReceiptFragment();

				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content, receiptFragment, "receipt")
						.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (calendarFragment == null) {

				calendarFragment=new CalendarFragment();
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.right_drawer, calendarFragment,
								"calendar").commit();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void refreshViews() {

		try {
			if (receiptFragment != null) {
				receiptFragment.refreshReceipts();
			}

			if (calendarFragment != null) {
				calendarFragment.refreshReceipts();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void makeSound(final int count) {

		final int playCount = isFistTime ? 1 : count;

		isFistTime = false;

		if (count > 0) {
			SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			final int sound = soundPool.load(this, R.raw.beep, 1);

			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					soundPool.play(sound, 1.0f, 1.0f, 0, playCount - 1, 1.0f);

				}
			});
		}

	}

	private OnItemClickListener navigationClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			FragmentManager manager;

			switch (arg2) {
			case 0:
				if (receiptFragment == null) {
					receiptFragment = new ReceiptFragment();
				}

				changeNavigationFragment(receiptFragment);
				break;
			case 1:

				// manager = getSupportFragmentManager();
				// manager.beginTransaction()
				// .replace(R.id.content,
				// (WebPageFragment.create("http://dbill.me/faq")))
				// .commit();

				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://obcp.ceilcode.com/faq"));
				startActivity(browserIntent);
				break;

			case 2:
				if (profileFragment == null) {
					profileFragment = new ProfileFragment();
				}

				changeNavigationFragment(profileFragment);

				break;

			// case 3:
			// manager = getSupportFragmentManager();
			// manager.beginTransaction()
			// .replace(R.id.content, (new HelpFragment())).commit();
			// break;
			case 3:

				// manager = getSupportFragmentManager();
				// manager.beginTransaction()
				// .replace(
				// R.id.content,
				// (WebPageFragment
				// .create("http://dbill.me/contact")))
				// .commit();
				browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://obcp.ceilcode.com/contact"));
				startActivity(browserIntent);
				break;
			case 4:

				if (reportFragment == null) {
					reportFragment = new ReportFragment();
				}

				changeNavigationFragment(reportFragment);

				break;

			}
			if (drawerLayout.isDrawerOpen(leftDrawer)) {
				drawerLayout.closeDrawer(leftDrawer);
			}
		}
	};

	// drawer toggle
	private OnClickListener toggleDrawerClickListner = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.imageButton_settings:

				if (drawerLayout.isDrawerOpen(rightDrawer)) {
					drawerLayout.closeDrawer(rightDrawer);
				}

				if (drawerLayout.isDrawerOpen(leftDrawer)) {
					drawerLayout.closeDrawer(leftDrawer);
				} else {
					drawerLayout.openDrawer(leftDrawer);
				}

				break;

			case R.id.imageButton_calendar:

				if (drawerLayout.isDrawerOpen(leftDrawer)) {
					drawerLayout.closeDrawer(leftDrawer);
				}

				if (drawerLayout.isDrawerOpen(rightDrawer)) {
					drawerLayout.closeDrawer(rightDrawer);
				} else {
					drawerLayout.openDrawer(rightDrawer);
				}

				break;

			}

		}
	};

	@Override
	public void onDateClick(int year, int month, int day) {

		if (drawerLayout.isDrawerOpen(rightDrawer)) {
			drawerLayout.closeDrawer(rightDrawer);
		}

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);

		calendar.set(Calendar.DATE, day);

		if (receiptFragment == null) {
			receiptFragment = ReceiptFragment.newInstance(calendar);
		}

		changeNavigationFragment(receiptFragment);
		receiptFragment.refreshReceipts(calendar);

		titleCalanderDate.setText("" + day);

	}

	private void changeNavigationFragment(Fragment fragment) {

		if (!fragment.isVisible()) {
			FragmentManager manager = getSupportFragmentManager();
			manager.beginTransaction().replace(R.id.content, fragment).commit();

		}
	}

}
