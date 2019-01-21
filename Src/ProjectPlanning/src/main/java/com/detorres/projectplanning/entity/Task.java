package com.detorres.projectplanning.entity;

import java.util.Date;

import org.apache.commons.collections4.map.LinkedMap;

import com.detorres.projectplanning.constants.DefaultValConstants;

public class Task {

	private static int idCounter = 0;

	private LinkedMap<Integer, Task> branches = new LinkedMap<Integer, Task>();

	private long duration;

	private Date endDate;

	private int id;

	private int status;

	private String name;

	private int parentProjectId;

	private int parentTaskId;

	private Date startDate;

	public Task() {
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

	public String getName() {
		return name;
	}

	public int getParentProjectId() {
		return parentProjectId;
	}

	public int getParentTaskId() {
		return parentTaskId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public boolean hasBranches() {
		return !branches.isEmpty();
	}

	public boolean hasParentTask() {
		return parentTaskId == 0 ? false : true;
	}

	public boolean isComplete() {
		return status == DefaultValConstants.STATUS_COMPLETE;
	}

	public boolean isBranch() {
		return !getBranches().isEmpty();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setParentProjectId(int parentProjectId) {
		this.parentProjectId = parentProjectId;
	}

	public void setParentTaskId(int parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean inProgress() {
		return status == DefaultValConstants.STATUS_IN_PROGRESS;
	}

	public boolean isWaiting() {
		return status == DefaultValConstants.STATUS_WAITING;
	}

}
