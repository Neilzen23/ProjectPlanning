package com.detorres.projectplanning.entity;

import java.util.Date;

import org.apache.commons.collections4.map.LinkedMap;

import com.detorres.projectplanning.constants.DefaultValConstants;

public class Project {

	private static int idCounter = 0;

	private LinkedMap<Integer, Task> branches = new LinkedMap<Integer, Task>();

	private long duration;

	private Date endDate;

	private int id;

	private String projectName;

	private Date startDate;

	private int status;

	public Project() {
		idCounter++;
		id = idCounter;
	}

	public LinkedMap<Integer, Task> getBranches() {
		return branches;
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

	public int getStatus() {
		return status;
	}

	public boolean hasBranches() {
		return !branches.isEmpty();
	}

	public boolean inProgress() {
		return status == DefaultValConstants.STATUS_IN_PROGRESS;
	}

	public boolean isComplete() {
		return status == DefaultValConstants.STATUS_COMPLETE;
	}

	public boolean isWaiting() {
		return status == DefaultValConstants.STATUS_WAITING;
	}

	public void setBranches(LinkedMap<Integer, Task> branches) {
		this.branches = branches;
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

	public void setStatus(int status) {
		this.status = status;
	}

}
