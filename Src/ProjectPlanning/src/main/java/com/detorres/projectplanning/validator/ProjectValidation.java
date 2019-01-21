package com.detorres.projectplanning.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.detorres.projectplanning.constants.DefaultValConstants;
import com.detorres.projectplanning.constants.ValidatorConstants;
import com.detorres.projectplanning.dao.ProjectDao;
import com.detorres.projectplanning.dao.ProjectDaoImpl;
import com.detorres.projectplanning.entity.Project;
import com.detorres.projectplanning.service.TaskManagementService;
import com.detorres.projectplanning.service.TaskManagementServiceImpl;
import com.detorres.projectplanning.utility.DateUtility;

public class ProjectValidation<T> implements InterfaceValidation<T> {

	private ProjectDao projectDao;
	private TaskManagementService taskManagementService;

	private List<String> errors = new ArrayList<String>();

	public ProjectValidation() {
		this.projectDao = new ProjectDaoImpl();
		this.taskManagementService = new TaskManagementServiceImpl();
	}

	@Override
	public List<String> checkDataExist(T data) {

		Project dataProject = (Project) data;
		List<Project> projects = this.projectDao.getAllProject();

		for (Project project : projects) {
			if (dataProject.getProjectName().equals(project.getProjectName())) {
				errors.add(ValidatorConstants.DATA_EXISTS);
				return errors;
			}
		}

		return errors;
	}

	@Override
	public List<String> validInput(T data) {

		return null;
	}

	@Override
	public List<String> validEntry(T data) {

		Project dataProject = (Project) data;

		List<Project> projects = projectDao.getAllProject();

		if (!projects.isEmpty()) {
			Project lastProject = projects.get(projects.size() - 1);

			if (lastProject.getStatus() != DefaultValConstants.STATUS_COMPLETE) {
				errors.add(ValidatorConstants.INVALID_PROJECT_ADD);
				return errors;
			}

			if (lastProject.hasBranches()) {
				Date lastEndDate = taskManagementService.getProjectEndDate(projects.get(projects.size() - 1).getId());

				if (lastEndDate.compareTo(dataProject.getStartDate()) > 0) {

					errors.add(this.convertArgumentedConstant(ValidatorConstants.INVALID_PROJECT_START_DATE,
							Arrays.asList(DateUtility.getInstance().formatDate(dataProject.getStartDate()))));

				}
			}

		}

		return errors;
	}

	private String convertArgumentedConstant(String error, List<String> val) {

		for (int x = 0; x < val.size(); x++) {
			error.replace("{" + x + "}", "{" + val.get(x) + "}");
		}

		return error;

	}

}
