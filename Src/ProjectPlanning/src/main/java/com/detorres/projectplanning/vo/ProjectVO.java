package com.detorres.projectplanning.vo;

import org.apache.commons.collections4.map.LinkedMap;

public class ProjectVO {

	private String duration;

	private String endDate;

	private String name;

	private String startDate;

	private String status;

	private LinkedMap<Integer, TaskVO> branches = new LinkedMap<Integer, TaskVO>();

	int id;

	public String getDuration() {
		return duration;
	}

	public String getEndDate() {
		return endDate;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStartDate() {
		return startDate;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public boolean hasBranches() {
		return !branches.isEmpty();
	}

	public LinkedMap<Integer, TaskVO> getBranches() {
		return branches;
	}

	public void setBranches(LinkedMap<Integer, TaskVO> branches) {
		this.branches = branches;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.getName() + " " + this.getDuration() + " [" + this.getStartDate() + " -> " + this.getEndDate() + "] - " + this.getStatus();
	}

}
