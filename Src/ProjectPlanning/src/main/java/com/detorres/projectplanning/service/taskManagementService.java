package com.detorres.projectplanning.service;

import com.detorres.projectplanning.entity.Task;

public interface taskManagementService {

	public void calculateProject(int id);
	
	public void calculateTask(int id);
	
	public void createTask(Task task);
	
	public void completeTask(int id);
	
	public void removeTask(int id);
	
	public void editTask(Task task);
	
}
