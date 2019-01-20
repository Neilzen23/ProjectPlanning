package com.detorres.projectplanning.controller;

import java.util.List;

import com.detorres.projectplanning.vo.ControllerResponse;
import com.detorres.projectplanning.vo.ProjectVO;
import com.detorres.projectplanning.vo.TaskVO;

public interface AppController {

	public ControllerResponse<List<ProjectVO>> loadProjects();

	public ControllerResponse<ProjectVO> loadProject(int id);

	public ControllerResponse<List<TaskVO>> loadTasks(int id);

	public ControllerResponse<TaskVO> loadTask(int id);

	public ControllerResponse<Void> createProject(ProjectVO projectVO);

	public ControllerResponse<Void> createTask(int projectId, int taskParentId, TaskVO taskVO);

}
