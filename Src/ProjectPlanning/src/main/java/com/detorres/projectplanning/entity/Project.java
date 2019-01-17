package com.detorres.projectplanning.entity;

import java.util.ArrayList;
import java.util.Date;

public class Project extends BaseEntity {

	private String projectName;

	private Date startDate;

	private ArrayList<Task> tasks;

	public String getProjectName() {
		return projectName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

}
