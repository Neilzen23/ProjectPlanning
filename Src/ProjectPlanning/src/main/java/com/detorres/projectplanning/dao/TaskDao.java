package com.detorres.projectplanning.dao;

import java.util.Date;

import com.detorres.projectplanning.entity.Task;

public interface TaskDao {

	public void addDependentTask(int id, Task task);

	public void addTask(Task task);

	public void completeTask(int id);

	public void editTask(Task task);

	public Task getById(int id);

	public void removeTask(int id);

	public void updateStartDate(int id, Date startDate);

	public void updateStatus(int id, int status);

	public void removeHours(int id);

}
