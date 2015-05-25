package com.ceilcode.obcp.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.util.Log;

public abstract class AbstractFetchTask extends
		AsyncTask<MyRequestWrapper, String, JsonResponseWrapper> {

	protected OnTaskPerformListner onTaskPerformListner;
	private int taskId;

	public AbstractFetchTask(int taskId,
			OnTaskPerformListner onTaskPerformListner) {
		this.onTaskPerformListner = onTaskPerformListner;
		this.taskId = taskId;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		overrideOnPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected JsonResponseWrapper doInBackground(MyRequestWrapper... params) {

		MyRequestWrapper myRequestWrapper = params[0];
		MyJsonPack myJsonPack;
		JsonResponseWrapper jsonResponseWrapper = new JsonResponseWrapper();

		try {
			// required for connection
			HttpUriRequest request;
			if (myRequestWrapper.isGetRequest()) {
				request = myRequestWrapper.getGetRequest();
			} else {
				request = myRequestWrapper.getPostRequest();
			}

			DefaultHttpClient client = null;
			if (!params[0].isPreLoginRequired()) {
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

				client = new DefaultHttpClient(conMgr, httpParams);
			} else {
				client = LoginApi.getSessionClient(myRequestWrapper.userId,
						myRequestWrapper.password);
			}

			HttpResponse response = client.execute(request);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuilder responseBuilder = new StringBuilder();
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {

				responseBuilder.append(line);

			}

			Log.d("response", responseBuilder.toString());

			myJsonPack = new MyJsonPack(responseBuilder.toString());
			jsonResponseWrapper.setCode(JsonResponseWrapper.RESPONSE_OK);
			jsonResponseWrapper.setJsonPack(myJsonPack);

		} catch (Exception exception) {
			exception.printStackTrace();
			jsonResponseWrapper.setException(exception);
			jsonResponseWrapper.setCode(JsonResponseWrapper.RESPONSE_ERROR);
		}

		return jsonResponseWrapper;
	}

	@Override
	protected void onPostExecute(JsonResponseWrapper result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		overrideOnPostExecute(result);
	}

	// should override this three method
	public void overrideOnPostExecute(JsonResponseWrapper result) {
		this.onTaskPerformListner.onTaskComplet(taskId, result);
	}

	public abstract void overrideOnProgressUpdate(String... values);

	public void overrideOnPreExecute() {
		this.onTaskPerformListner.onTaskStart(taskId);

	}
}
