package com.detorres.projectplanning.dao;

import java.util.Map;

import com.detorres.projectplanning.entity.Task;

public class TaskDaoImpl implements TaskDao {

	private static Map<Integer, Task> taskData;

	public Task getTaskById(int id) {
		return taskData.get(id);
	}

	public void addTask(Task task) {
		taskData.put(task.getId(), task);
	}

}
