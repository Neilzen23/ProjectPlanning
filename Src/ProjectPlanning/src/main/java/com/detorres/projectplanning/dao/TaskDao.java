package com.detorres.projectplanning.dao;

import com.detorres.projectplanning.entity.Task;

public interface TaskDao {

	public Task getTaskById(int id);

	public void addTask(Task task);

}
