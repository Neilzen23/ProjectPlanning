package com.detorres.projectplanning.controller;

import java.util.ArrayList;
import java.util.List;

import com.detorres.projectplanning.entity.Project;
import com.detorres.projectplanning.entity.Task;
import com.detorres.projectplanning.entity.UserPreference;
import com.detorres.projectplanning.service.TaskManagementService;
import com.detorres.projectplanning.service.TaskManagementServiceImpl;
import com.detorres.projectplanning.utility.DateUtility;
import com.detorres.projectplanning.vo.ControllerResponse;
import com.detorres.projectplanning.vo.ProjectVO;
import com.detorres.projectplanning.vo.TaskVO;

public class AppControllerImpl implements AppController {

	private DateUtility dateUtility;
	private TaskManagementService taskManagementService;
	private UserPreference userPreference;

	public AppControllerImpl() {
		this.taskManagementService = new TaskManagementServiceImpl();
		this.dateUtility = DateUtility.getInstance();
		this.userPreference = UserPreference.getInstance();
	}

	@Override
	public ControllerResponse<Void> createProject(ProjectVO projectVO) {
		Project project = new Project();

		project.setProjectName(projectVO.getName());
		project.setStartDate(dateUtility.parseDate(projectVO.getStartDate()));

		ControllerResponse<Void> response = new ControllerResponse<Void>();

		return response;

	}

	@Override
	public ControllerResponse<Void> createTask(int projectId, int taskParentId, TaskVO taskVO) {
		// TODO Auto-generated method stub
		ControllerResponse<Void> response = new ControllerResponse<Void>();

		return response;
	}

	@Override
	public ControllerResponse<ProjectVO> loadProject(int id) {

		ControllerResponse<ProjectVO> response = new ControllerResponse<ProjectVO>();
		response.setData(this.copyProjectToVO(taskManagementService.loadProject(id)));

		return response;
	}

	@Override
	public ControllerResponse<List<ProjectVO>> loadProjects() {
		ControllerResponse<List<ProjectVO>> response = new ControllerResponse<List<ProjectVO>>();

		List<ProjectVO> projectVOs = new ArrayList<ProjectVO>();
		List<Project> projects = taskManagementService.loadProjects();

		for (Project project : projects) {
			projectVOs.add(this.copyProjectToVO(project));
		}

		response.setData(projectVOs);

		return response;
	}

	@Override
	public ControllerResponse<TaskVO> loadTask(int id) {

		ControllerResponse<TaskVO> response = new ControllerResponse<TaskVO>();

		response.setData(this.copyTasksToVO(taskManagementService.loadTask(id)));

		return response;
	}

	@Override
	public ControllerResponse<List<TaskVO>> loadTasks(int projectId) {
		ControllerResponse<List<TaskVO>> response = new ControllerResponse<List<TaskVO>>();
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();

		for (Task task : taskManagementService.loadProjectTasks(projectId).values()) {
			taskVOs.add(copyTasksToVO(task));
		}

		response.setData(taskVOs);

		return response;
	}

	private long calculateProjectDuration(Project project) {
		long duration = 0L;
		// Calculate hrs based on tasks attached to project
		for (Task task : project.getTasks().values()) {
			duration += task.getDuration();
		}

		return duration;
	}

	private ProjectVO copyProjectToVO(Project project) {
		ProjectVO projectVO = new ProjectVO();

		projectVO.setName(project.getProjectName());
		projectVO.setStartDate(dateUtility.formatDate(project.getStartDate()));
		projectVO.setDuration(this.calculateProjectDuration(project));
		projectVO.setEndDate(dateUtility.formatDate(dateUtility.computeEndDate(project.getStartDate(), project.getDuration())));

		return projectVO;
	}

	private TaskVO copyTasksToVO(Task task) {
		TaskVO taskVO = new TaskVO();
		taskVO.setDuration(task.getDuration());
		taskVO.setStartDate(dateUtility.formatDate(task.getStartDate()));
		taskVO.setEndDate(dateUtility.formatDate(task.getEndDate()));

		return taskVO;
	}

}
