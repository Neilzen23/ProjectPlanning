package com.detorres.projectplanning.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.detorres.projectplanning.entity.Project;
import com.detorres.projectplanning.entity.ServiceResponse;
import com.detorres.projectplanning.entity.Task;

public interface TaskManagementService {

	public ServiceResponse<Void> completeTask(int id);

	public void createProject(Project project);

	public void createTask(Task task, int projectId);

	public void createTask(Task parentTask, Task subTask);

	public void editTask(Task task);

	public Date getProjectEndDate(int id);

	public Date getTaskEndDate(int id);

	public Project loadProject(int id);

	public List<Project> loadProjects();

	public Map<Integer, Task> loadProjectTasks(int id);

	public Task loadTask(int id);

	public Map<Integer, Task> loadTasks(int taskId);

	public Task getTaskByName(String name);

	public Project getProjectByName(String name);

	public Task getCurrentTaskOfProject(int projectId);

	public Task getNextTaskOfProject(int projectId);

	public Task getPreviousTaskOfProject(int projectId);

}
