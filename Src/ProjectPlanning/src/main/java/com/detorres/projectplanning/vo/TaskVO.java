package com.detorres.projectplanning.vo;

import java.util.List;

public class TaskVO {

	private List<TaskVO> dependentVOs;

	private long duration;

	private String endDate;

	private String startDate;

	public List<TaskVO> getDependentVOs() {
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

	public void setDependentVOs(List<TaskVO> dependentVOs) {
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

}
