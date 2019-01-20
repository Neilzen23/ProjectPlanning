package com.detorres.projectplanning.vo;

import java.util.List;

public class ProjectVO extends VO<ProjectVO> {

	private long duration;

	private String endDate;

	private String name;

	private String startDate;

	private List<TaskVO> tasks;

	public long getDuration() {
		return duration;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getName() {
		return name;
	}

	public String getStartDate() {
		return startDate;
	}

	public List<TaskVO> getTasks() {
		return tasks;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setTasks(List<TaskVO> tasks) {
		this.tasks = tasks;
	}

}
