package com.ceilcode.obcp.webservice;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

public class MyRequestWrapper {

	private boolean isGetRequest = true;
	private boolean isPreLoginRequired = false;
	String userId;
	String password;

	public void setLoginCredentials(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public boolean isPreLoginRequired() {
		return isPreLoginRequired;
	}

	public void setPreLoginRequired(boolean isPreLoginRequired) {
		this.isPreLoginRequired = isPreLoginRequired;
	}

	public boolean isGetRequest() {
		return isGetRequest;
	}

	public void setGetRequest(boolean isGetRequest) {
		this.isGetRequest = isGetRequest;
	}

	private HashMap<String, String> params;

	private String url;
	private Context context;

	public Context getContext() {
		return context;
	}

	public MyRequestWrapper(String url) {

		this.url = url;
		params = new HashMap<String, String>();
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void addParam(String key, String value) {
		params.put(key, value);
	}

	public HttpPost getPostRequest() throws UnsupportedEncodingException {

		HttpPost post = new HttpPost(url);

		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();

		for (Iterator<Entry<String, String>> iterator = params.entrySet()
				.iterator(); iterator.hasNext();) {

			Entry<String, String> entry = iterator.next();
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

		}

		post.setEntity(new UrlEncodedFormEntity(pairs));

		return post;
	}

	public HttpGet getGetRequest() throws UnsupportedEncodingException {

		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();

		for (Iterator<Entry<String, String>> iterator = params.entrySet()
				.iterator(); iterator.hasNext();) {

			Entry<String, String> entry = iterator.next();
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

		}
		HttpGet get = new HttpGet(url + "?"
				+ URLEncodedUtils.format(pairs, "utf-8"));

		return get;
	}

	public void setContext(Context context) {
		this.context = context;

	}
}
