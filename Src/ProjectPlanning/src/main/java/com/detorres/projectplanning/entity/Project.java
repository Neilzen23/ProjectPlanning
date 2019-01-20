package com.detorres.projectplanning.entity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Project {

	private static int idCounter = 0;

	private int id;

	private long duration;

	private Date endDate;

	private String projectName;

	private Date startDate;

	private Map<Integer, Task> tasks = new LinkedHashMap<Integer, Task>();

	public Project() {
		idCounter++;
		id = idCounter;
	}

	public long getDuration() {
		return duration;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getId() {
		return id;
	}

	public String getProjectName() {
		return projectName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Map<Integer, Task> getTasks() {
		return tasks;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setTasks(Map<Integer, Task> tasks) {
		this.tasks = tasks;
	}

}
