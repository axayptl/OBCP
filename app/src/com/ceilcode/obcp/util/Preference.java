package com.ceilcode.obcp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

	private SharedPreferences preferences;

	public Preference(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

	}

	public void setUser(String user) {
		preferences.edit().putString("user", user).commit();
	}

	public String getUser() {
		return preferences.getString("user", "");
	}
	
	
	public void setGcmId(String user) {
		preferences.edit().putString("gcm_id", user).commit();
	}

	public String getGcmId() {
		return preferences.getString("gcm_id", "");
	}

	public void setPassword(String Password) {
		preferences.edit().putString("password", Password).commit();
	}

	public String getPassword() {
		return preferences.getString("password", "");
	}

	public void setLastRecordId(int id) {
		preferences.edit().putInt("lastRecordId", id).commit();
	}

	public int getLastRecordId() {
		return preferences.getInt("lastRecordId", 0);
	}

	public void setSessionToken(String token) {
		preferences.edit().putString("token", token).commit();
	}

	public String getSessionToken() {
		return preferences.getString("token", "");
	}

	public void setUserId(String userid) {
		preferences.edit().putString("userid", userid).commit();
	}

	public String getUserId() {
		return preferences.getString("userid", "");
	}

	public void setAtlUserId(String userid) {
		preferences.edit().putString("alt_userid", userid).commit();
	}

	public String getAltUserId() {
		return preferences.getString("alt_userid", "");
	}

	public void setFirstName(String firstName) {
		preferences.edit().putString("firstName", firstName).commit();
	}

	public String getFirstName() {
		return preferences.getString("firstName", "");
	}

	public void setLastName(String lastName) {
		preferences.edit().putString("lastName", lastName).commit();
	}

	public String getLastName() {
		return preferences.getString("lastName", "");
	}

	public void setPhone(String phone) {
		preferences.edit().putString("phone", phone).commit();
	}

	public String getPhone() {
		return preferences.getString("phone", "");
	}

	public void setMaritalStatus(int maritalStatus) {
		preferences.edit().putInt("MaritalStatus", maritalStatus).commit();
	}

	public int getMaritalStatus() {
		return preferences.getInt("MaritalStatus", -1);
	}

	public void setAge(int maritalStatus) {
		preferences.edit().putInt("age", maritalStatus).commit();
	}

	public int getAge() {
		return preferences.getInt("age", -1);
	}

	public void setGender(int gender) {
		preferences.edit().putInt("gender", gender).commit();
	}

	public int getGender() {
		return preferences.getInt("gender", -1);
	}

	public void setMonthlyBudget(float maritalStatus) {
		preferences.edit().putFloat("monthlyBudget", maritalStatus).commit();
	}

	public float getMonthlyBudget() {
		return preferences.getFloat("monthlyBudget", -1);
	}

	public void setEmail(String email) {
		preferences.edit().putString("email", email).commit();
	}

	public String getEmail() {
		return preferences.getString("email", "");
	}

	public void logout() {
		preferences.edit().clear().commit();
	}

}
