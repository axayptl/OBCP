package com.ceilcode.obcp.webservice;

public interface OnTaskPerformListner {
	public void onTaskStart(int taskId);

	public void onTaskComplet(int taskId, JsonResponseWrapper result);
}
