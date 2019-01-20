package com.detorres.projectplanning.vo;

import java.util.HashMap;
import java.util.Map;

public class TaskVO extends VO<TaskVO> {

	private Map<Integer, TaskVO> dependentVOs = new HashMap<Integer, TaskVO>();

	private long duration;

	private String endDate;

	private String startDate;

	private boolean complete;

	public Map<Integer, TaskVO> getDependentVOs() {
		return dependentVOs;
	}

	public long getDuration() {
		return duration;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setDependentVOs(Map<Integer, TaskVO> dependentVOs) {
		this.dependentVOs = dependentVOs;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

}
