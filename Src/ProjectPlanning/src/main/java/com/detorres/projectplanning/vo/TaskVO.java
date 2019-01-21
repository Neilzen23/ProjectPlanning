package com.detorres.projectplanning.vo;

import org.apache.commons.collections4.map.LinkedMap;

public class TaskVO {

	private LinkedMap<Integer, TaskVO> branches = new LinkedMap<Integer, TaskVO>();

	private String duration;

	private String endDate;

	private int id;

	private String startDate;

	private String status;

	private String name;

	private boolean isParent;

	private int parentTaskId;

	private int projectId;

	public LinkedMap<Integer, TaskVO> getBranches() {
		return branches;
	}

	public String getDuration() {
		return duration;
	}

	public String getEndDate() {
		return endDate;
	}

	public int getId() {
		return id;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setBranches(LinkedMap<Integer, TaskVO> branches) {
		this.branches = branches;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(int parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public boolean hasBranches() {
		return !branches.isEmpty();
	}

	@Override
	public String toString() {
		if (hasBranches()) {
			return "*** " + this.getName() + " " + this.getDuration() + " [" + this.getStartDate() + " -> " + this.getEndDate() + "] - " + this.getStatus();
		} else {
			return this.getName() + " " + this.getDuration() + " [" + this.getStartDate() + " -> " + this.getEndDate() + "] - " + this.getStatus();
		}
	}

}
