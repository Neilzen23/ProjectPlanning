package com.detorres.projectplanning.controller;

import java.util.List;

import com.detorres.projectplanning.vo.ControllerResponse;
import com.detorres.projectplanning.vo.ProjectVO;
import com.detorres.projectplanning.vo.TaskVO;

public interface AppController {

	public List<ProjectVO> loadProjects();

	public ProjectVO loadProject(int id);

	public List<TaskVO> loadTasks(int id);

	public ControllerResponse<TaskVO> loadTask(int id);

	public ControllerResponse<ProjectVO> createProject(ProjectVO projectVO);

	public ControllerResponse<TaskVO> createTask(int projectId, int taskParentId, TaskVO taskVO);

	public ControllerResponse<TaskVO> createTask(int projectId, TaskVO taskVO);

	public ControllerResponse<Void> completeTask(int taskId);

}
