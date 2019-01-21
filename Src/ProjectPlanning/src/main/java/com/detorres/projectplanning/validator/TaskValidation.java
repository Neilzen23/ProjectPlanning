package com.detorres.projectplanning.validator;

import java.util.ArrayList;
import java.util.List;

import com.detorres.projectplanning.constants.ValidatorConstants;
import com.detorres.projectplanning.dao.TaskDao;
import com.detorres.projectplanning.dao.TaskDaoImpl;
import com.detorres.projectplanning.entity.Task;
import com.detorres.projectplanning.service.TaskManagementService;
import com.detorres.projectplanning.service.TaskManagementServiceImpl;

public class TaskValidation<T> implements InterfaceValidation<T> {

	private TaskManagementService taskManagementService;
	private TaskDao taskDao;

	public TaskValidation() {
		this.taskDao = new TaskDaoImpl();
		this.taskManagementService = new TaskManagementServiceImpl();
	}

	@Override
	public List<String> checkDataExist(T data) {

		Task task = (Task) data;
		List<String> error = new ArrayList<String>();

		for (Task taskData : taskManagementService.loadProjectTasks(task.getParentProjectId()).values()) {
			if (taskData.getName().equalsIgnoreCase(task.getName())) {
				error.add(ValidatorConstants.INVALID_TASK_EXIST);
				return error;
			}
		}

		return error;
	}

	@Override
	public List<String> validInput(T data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> validEntry(T data) {
		// TODO Auto-generated method stub
		return null;
	}

}
