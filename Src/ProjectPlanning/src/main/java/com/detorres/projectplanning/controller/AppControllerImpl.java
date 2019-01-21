package com.detorres.projectplanning.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.map.LinkedMap;

import com.detorres.projectplanning.constants.DefaultValConstants;
import com.detorres.projectplanning.entity.Project;
import com.detorres.projectplanning.entity.Task;
import com.detorres.projectplanning.entity.UserPreference;
import com.detorres.projectplanning.service.TaskManagementService;
import com.detorres.projectplanning.service.TaskManagementServiceImpl;
import com.detorres.projectplanning.utility.DateUtility;
import com.detorres.projectplanning.validator.ProjectValidation;
import com.detorres.projectplanning.validator.TaskValidation;
import com.detorres.projectplanning.vo.ControllerResponse;
import com.detorres.projectplanning.vo.ProjectVO;
import com.detorres.projectplanning.vo.TaskVO;

public class AppControllerImpl implements AppController {

	private DateUtility dateUtility;
	private TaskManagementService taskManagementService;

	public AppControllerImpl() {
		this.taskManagementService = new TaskManagementServiceImpl();
		this.dateUtility = DateUtility.getInstance();
	}

	@Override
	public ControllerResponse<Void> completeTask(int taskId) {

		ControllerResponse<Void> response = new ControllerResponse<Void>();

		response.setError(this.taskManagementService.completeTask(taskId).getError());

		return response;
	}

	@Override
	public ControllerResponse<ProjectVO> createProject(ProjectVO projectVO) {
		Project project = new Project();

		project.setProjectName(projectVO.getName());
		project.setStartDate(dateUtility.parseDate(projectVO.getStartDate()));

		ControllerResponse<ProjectVO> response = new ControllerResponse<ProjectVO>();

		ProjectValidation<Project> validation = new ProjectValidation<Project>();

		List<String> errors = validation.checkDataExist(project);

		if (!errors.isEmpty()) {
			response.setError(errors);
			return response;
		}

		errors = validation.validEntry(project);

		if (!errors.isEmpty()) {
			response.setError(errors);
			return response;
		}

		taskManagementService.createProject(project);
		projectVO.setId(project.getId());
		response.setData(projectVO);

		return response;

	}

	@Override
	public ControllerResponse<TaskVO> createTask(int projectId, int taskParentId, TaskVO taskVO) {
		ControllerResponse<TaskVO> response = new ControllerResponse<TaskVO>();

		Task parentTask = new Task();
		parentTask.setId(taskParentId);
		parentTask.setParentProjectId(projectId);

		Task subTask = this.copyVoToTask(taskVO);
		subTask.setStatus(DefaultValConstants.STATUS_UNDEFINED);

		TaskValidation<Task> validation = new TaskValidation<Task>();
		List<String> error = validation.checkDataExist(subTask);

		if (!error.isEmpty()) {
			response.setError(error);
			return response;
		}

		this.taskManagementService.createTask(parentTask, subTask);

		return response;
	}

	@Override
	public ControllerResponse<TaskVO> createTask(int projectId, TaskVO taskVO) {
		ControllerResponse<TaskVO> response = new ControllerResponse<TaskVO>();

		Task task = this.copyVoToTask(taskVO);
		task.setName(taskVO.getName());
		task.setDuration(this.convertDurationToUserPreference(taskVO.getDuration()));
		task.setParentProjectId(projectId);
		task.setStatus(DefaultValConstants.STATUS_UNDEFINED);

		TaskValidation<Task> validation = new TaskValidation<Task>();
		List<String> error = validation.checkDataExist(task);

		if (!error.isEmpty()) {
			response.setError(error);
			return response;
		}

		taskManagementService.createTask(task, projectId);

		return response;
	}

	@Override
	public ProjectVO loadProject(int id) {

		return this.copyProjectToVO(taskManagementService.loadProject(id));
	}

	@Override
	public List<ProjectVO> loadProjects() {
		List<ProjectVO> projectVOs = new ArrayList<ProjectVO>();
		List<Project> projects = taskManagementService.loadProjects();

		for (Project project : projects) {
			projectVOs.add(this.copyProjectToVO(project));
		}

		return projectVOs;
	}

	@Override
	public ControllerResponse<TaskVO> loadTask(int id) {

		ControllerResponse<TaskVO> response = new ControllerResponse<TaskVO>();

		Task task = taskManagementService.loadTask(id);

		TaskVO taskVO = this.copyTasksToVO(task);

		LinkedMap<Integer, TaskVO> branches = new LinkedMap<Integer, TaskVO>();
		for (Task branch : task.getBranches().values()) {
			branches.put(branch.getId(), this.copyTasksToVO(branch));
		}

		taskVO.setBranches(branches);

		response.setData(taskVO);

		return response;
	}

	@Override
	public List<TaskVO> loadTasks(int projectId) {

		List<TaskVO> taskVOs = new ArrayList<TaskVO>();

		for (Task task : taskManagementService.loadProjectTasks(projectId).values()) {
			taskVOs.add(copyTasksToVO(task));
		}

		return taskVOs;
	}

	private long calculateProjectDuration(Project project) {
		long duration = 0L;
		// Calculate hrs based on tasks attached to project
		for (Task task : project.getBranches().values()) {
			duration += task.getDuration();
		}

		return duration;
	}

	private String convertDurationToUserPreference(long duration) {
		int format = UserPreference.getInstance().getDefaultDurationFormat();
		if (DefaultValConstants.DEFAULT_DURATION_HRS == format) {
			return Float.valueOf(duration) / 60F / 60F + " hrs";
		} else {
			return Float.valueOf(duration) / 60 + " min";
		}
	}

	private long convertDurationToUserPreference(String duration) {
		int format = UserPreference.getInstance().getDefaultDurationFormat();
		if (DefaultValConstants.DEFAULT_DURATION_HRS == format) {
			return (long) (Float.valueOf(duration) * 60F * 60F);
		} else {
			return (long) (Float.valueOf(duration) * 60);
		}
	}

	private ProjectVO copyProjectToVO(Project project) {
		ProjectVO projectVO = new ProjectVO();
		projectVO.setId(project.getId());
		projectVO.setName(project.getProjectName());
		projectVO.setStartDate(dateUtility.formatDate(project.getStartDate()));
		projectVO.setDuration(this.convertDurationToUserPreference(this.calculateProjectDuration(project)));
		projectVO.setEndDate(dateUtility.formatDate(project.getEndDate()));

		if (project.hasBranches()) {
			LinkedMap<Integer, TaskVO> branches = new LinkedMap<Integer, TaskVO>();

			for (Task task : project.getBranches().values()) {
				branches.put(task.getId(), copyTasksToVO(task));
			}

			projectVO.setBranches(branches);

		}

		if (project.isComplete()) {
			projectVO.setStatus(DefaultValConstants.STATUS_STRING_COMPLETE);
		} else if (project.inProgress()) {
			projectVO.setStatus(DefaultValConstants.STATUS_STRING_IN_PROGRESS);
		} else {
			projectVO.setStatus(DefaultValConstants.STATUS_STRING_WAITING);
		}

		return projectVO;
	}

	private TaskVO copyTasksToVO(Task task) {
		TaskVO taskVO = new TaskVO();
		taskVO.setId(task.getId());
		taskVO.setName(task.getName());
		taskVO.setDuration(this.convertDurationToUserPreference(task.getDuration()));
		taskVO.setStartDate(dateUtility.formatDate(task.getStartDate()));
		taskVO.setEndDate(dateUtility.formatDate(task.getEndDate()));
		taskVO.setParentTaskId(task.getParentTaskId());
		taskVO.setProjectId(task.getParentProjectId());
		if (task.isComplete()) {
			taskVO.setStatus(DefaultValConstants.STATUS_STRING_COMPLETE);
		} else if (task.inProgress()) {
			taskVO.setStatus(DefaultValConstants.STATUS_STRING_IN_PROGRESS);
		} else {
			taskVO.setStatus(DefaultValConstants.STATUS_STRING_WAITING);
		}

		return taskVO;
	}

	private Task copyVoToTask(TaskVO taskVO) {
		Task task = new Task();
		task.setName(taskVO.getName());
		task.setDuration(this.convertDurationToUserPreference(taskVO.getDuration()));
		task.setParentProjectId(taskVO.getProjectId());
		task.setParentTaskId(taskVO.getParentTaskId());
		return task;
	}

}
