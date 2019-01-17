package com.detorres.projectplanning.entity;

public class Task extends AbstractTask {

	@Override
	public boolean hasDependency() {
		return super.dependency > 0 ? true : false;
	}

	@Override
	public boolean hasDependentTask() {
		return !super.dependentTasks.isEmpty();
	}

}
