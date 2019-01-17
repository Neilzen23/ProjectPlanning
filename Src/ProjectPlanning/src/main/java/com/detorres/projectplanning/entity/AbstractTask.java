package com.detorres.projectplanning.entity;

import java.util.List;

public abstract class AbstractTask extends BaseEntity {

	protected int dependency;

	protected List<Integer> dependentTasks;

	protected long duration;

	protected boolean isComplete;

	protected String name;

	public int getDependency() {
		return dependency;
	}

	public List<Integer> getDependentTasks() {
		return dependentTasks;
	}

	public long getDuration() {
		return duration;
	}

	public String getName() {
		return name;
	}

	public abstract boolean hasDependency();

	public abstract boolean hasDependentTask();

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public void setDependency(int dependency) {
		this.dependency = dependency;
	}

	public void setDependentTasks(List<Integer> dependentTasks) {
		this.dependentTasks = dependentTasks;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setName(String name) {
		this.name = name;
	}

}
