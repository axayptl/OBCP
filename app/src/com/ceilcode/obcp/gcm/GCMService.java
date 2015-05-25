package com.ceilcode.obcp.gcm;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ceilcode.obcp.MainActivity;
import com.ceilcode.obcp.R;
import com.ceilcode.obcp.util.JsonKeys;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GCMService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) // has effect of unparcelling Bundle
		{
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString(), null);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification(
						"Deleted messages on server: " + extras.toString(),
						null);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// Post notification of received message.
				String message = ((intent.getExtras() == null) ? "Empty Bundle"
						: intent.getExtras().getString("Notice"));

				Bundle bundle = intent.getExtras();
				for (String key : bundle.keySet()) {
					Object value = bundle.get(key);
					Log.e("Notification", String.format("%s %s (%s)", key,
							value.toString(), value.getClass().getName()));
				}

				Log.i(Globals.TAG, "Received: " + message);
				if (intent.getExtras() != null
						&& "com.fallenapps.gcm.client.CLEAR_NOTIFICATION"
								.equals(intent.getExtras().getString("action"))) {
					clearNotification();
				} else {
					sendNotification(message, extras);

				}
				// notification received

			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	/**
	 * Put the message into a notification and post it. This is just one simple
	 * example of what you might choose to do with a GCM message.
	 * 
	 * @param msg
	 * @param extras
	 */
	private void sendNotification(String msg, Bundle extras) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, MainActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// If this is a notification type message include the data from the
		// message
		// with the intent
		if (extras != null) {
			intent.putExtras(extras);
			intent.setAction("com.fallenapps.dbill.NOTIFICATION");
		}

		try {
			JSONObject object = new JSONObject(msg.toString().trim());
			String formattedText = object
					.getString(JsonKeys.NotificationItemKeys.KEY_ALERT)
					+ " Total Items = "+ object.getString(JsonKeys.NotificationItemKeys.KEY_TOTAL_ITEMS)+", Receipt Total = "
					+ object.getString(JsonKeys.NotificationItemKeys.KEY_TOTAL);

			String bigText = object
					.getString(JsonKeys.NotificationItemKeys.KEY_ALERT);

			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(bigText)
					.setStyle(
							new NotificationCompat.BigTextStyle()
									.bigText(formattedText))
					.setContentText(formattedText).setTicker(formattedText)
					.setAutoCancel(true)
					.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

			Uri path = Uri.parse("android.resource://com.ceilcode.obcp/"
					+ R.raw.beep);
			mBuilder.setContentIntent(contentIntent);
			Notification notificationCompat = mBuilder.build();
			notificationCompat.sound = path;
			mNotificationManager.notify(NOTIFICATION_ID, notificationCompat);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remove the app's notification
	 */
	private void clearNotification() {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(NOTIFICATION_ID);
	}

	public static void setBadge(Context context, int count) {
		String launcherClassName = getLauncherClassName(context);
		if (launcherClassName == null) {
			return;
		}
		Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
		intent.putExtra("badge_count", count);
		intent.putExtra("badge_count_package_name", context.getPackageName());
		intent.putExtra("badge_count_class_name", launcherClassName);
		context.sendBroadcast(intent);
	}

	public static String getLauncherClassName(Context context) {

		PackageManager pm = context.getPackageManager();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo resolveInfo : resolveInfos) {
			String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
			if (pkgName.equalsIgnoreCase(context.getPackageName())) {
				String className = resolveInfo.activityInfo.name;
				return className;
			}
		}
		return null;
	}

}
