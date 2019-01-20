package com.detorres.projectplanning.dao;

import java.util.Date;

import org.apache.commons.collections4.map.LinkedMap;

import com.detorres.projectplanning.entity.Task;

public class TaskDaoImpl implements TaskDao {

	private static LinkedMap<Integer, Task> taskData = new LinkedMap<Integer, Task>();

	@Override
	public void addTask(Task task) {
		taskData.put(task.getId(), task);
	}

	@Override
	public void completeTask(int id) {
		taskData.get(Integer.valueOf(id)).setStatus(3);
	}

	@Override
	public void editTask(Task task) {
		Task currentTask = taskData.get(Integer.valueOf(task.getId()));

		currentTask.setName(task.getName());
		currentTask.setDuration(task.getDuration());
		currentTask.setStartDate(task.getStartDate());

	}

	@Override
	public Task getById(int id) {

		Task task = new Task();
		Task data = taskData.get(Integer.valueOf(id));

		copyTask(task, data);

		for (Task dependent : data.getBranches().values()) {
			Task dependentTask = new Task();

			copyTask(dependentTask, dependent);

			task.getBranches().put(dependentTask.getId(), dependentTask);

		}

		return task;
	}

	private void copyTask(Task t1, Task t2) {
		t1.setId(t2.getId());
		t1.setName(t2.getName());
		t1.setParentProjectId(t2.getParentProjectId());
		t1.setParentTaskId(t2.getParentTaskId());
		t1.setStatus(t2.getStatus());
		t1.setDuration(t2.getDuration());
	}

	@Override
	public void removeTask(int id) {
		taskData.remove(id);
	}

	@Override
	public void updateStartDate(int id, Date startDate) {
		taskData.get(Integer.valueOf(id)).setStartDate(startDate);
	}

	@Override
	public void addDependentTask(int id, Task task) {
		taskData.get(Integer.valueOf(id)).getBranches().put(task.getId(), task);

	}

	@Override
	public void updateStatus(int id, int status) {
		taskData.get(Integer.valueOf(id)).setStatus(status);
	}

}
