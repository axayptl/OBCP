package com.ceilcode.obcp.webservice;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

import com.ceilcode.obcp.constant.Constants;

import android.util.Log;

public class LoginApi {

	public static DefaultHttpClient getSessionClient(String userId,
			String password) throws ClientProtocolException, IOException {
		// required for connection
		HttpPost request = new HttpPost(Constants.URL_SESSION_LOGIN);
		request.setHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();

		pairs.add(new BasicNameValuePair("UserID", userId));

		pairs.add(new BasicNameValuePair("SessionToken", password));

		Log.d("get session", userId + " " + password);

		request.setEntity(new UrlEncodedFormEntity(pairs));
		HttpParams httpParams = new BasicHttpParams();

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(httpParams, true);

		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		HttpConnectionParams.setConnectionTimeout(httpParams, 999999999);
		HttpConnectionParams.setSoTimeout(httpParams, 999999999);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				httpParams, schReg);

		DefaultHttpClient client = new DefaultHttpClient(conMgr, httpParams);

		client.execute(request);

		return client;

	}

}
