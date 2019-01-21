package com.detorres.projectplanning.view;

import java.util.List;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;

import com.detorres.projectplanning.constants.ViewConstants;
import com.detorres.projectplanning.controller.AppController;
import com.detorres.projectplanning.controller.AppControllerImpl;
import com.detorres.projectplanning.entity.UserPreference;
import com.detorres.projectplanning.utility.DateUtility;
import com.detorres.projectplanning.vo.ControllerResponse;
import com.detorres.projectplanning.vo.ProjectVO;
import com.detorres.projectplanning.vo.TaskVO;

public class UserPlainView extends AbstractView {

	private AppController appController;

	public UserPlainView() {
		this.appController = new AppControllerImpl();
	}

	@Override
	public void start() {
		this.loadInitialDisplay();
		this.userActions();
	}

	private ProjectVO createProject() {

		ProjectVO projectVO = new ProjectVO();
		this.displayOutput(ViewConstants.HEAD_CREATE_PROJECT);

		projectVO.setName(this.fieldNameInput(ViewConstants.FIELD_PROJECT_NAME));
		projectVO.setStartDate(this.fieldDateInput(ViewConstants.FIELD_PROJECT_START_DATE + " " + UserPreference.getInstance().getDateFormat()));

		ControllerResponse<ProjectVO> response = appController.createProject(projectVO);
		projectVO = response.getData();
		if (response.hasError()) {
			this.displayOutput("");
			for (String error : response.getError()) {
				this.displayOutput(error);
			}
			return null;
		}

		projectVO = appController.loadProject(projectVO.getId());

		return projectVO;

	}

	private void createTask(int projectId) {
		while (true) {
			TaskVO taskVO = new TaskVO();
			taskVO.setName(this.fieldNameInput(ViewConstants.FIELD_TASK_NAME));
			taskVO.setDuration(this.fieldNumberInput(ViewConstants.FIELD_TASK_DURATION));

			ControllerResponse<TaskVO> response = appController.createTask(projectId, taskVO);

			if (response.hasError()) {
				for (String error : response.getError()) {
					this.displayOutput(error);
				}
			} else {
				break;
			}
		}

	}

	private void createSubTask(int projectId, int taskParentId) {
		while (true) {

			this.displayOutput(ViewConstants.CREATE_SUB_TASK);

			TaskVO taskVO = new TaskVO();
			taskVO.setProjectId(projectId);
			taskVO.setParentTaskId(taskParentId);
			taskVO.setName(this.fieldNameInput(ViewConstants.FIELD_TASK_NAME));
			taskVO.setDuration(this.fieldNumberInput(ViewConstants.FIELD_TASK_DURATION));

			ControllerResponse<TaskVO> response = appController.createTask(projectId, taskParentId, taskVO);

			if (response.hasError()) {
				for (String error : response.getError()) {
					this.displayOutput(error);
				}
			} else {
				break;
			}
		}

	}

	private String fieldDateInput(String label) {
		String input = "";
		while (true) {
			this.displayOutput("");
			this.displayOutput(ViewConstants.FIELD_PROJECT_START_DATE + " " + UserPreference.getInstance().getDateFormat());
			input = super.captureUserInput();

			if (StringUtils.isEmpty(input)) {
				this.displayOutput(ViewConstants.INVALID_EMPTY);
			} else if (DateUtility.getInstance().isValidDate(input)) {
				if (!DateUtility.getInstance().isWorkingDay(DateUtility.getInstance().parseDate(input))) {
					this.displayOutput(ViewConstants.INVALID_DATE_WEEKEND);
				} else {
					break;
				}
			} else {
				this.displayOutput(ViewConstants.INVALID_DATE_INPUT);
			}
			this.displayOutput("");
		}

		return input;
	}

	private String fieldNameInput(String label) {
		String input = "";
		while (true) {
			this.displayOutput("");
			this.displayOutput(label);
			input = super.captureUserInput();
			if (StringUtils.isEmpty(input)) {
				this.displayOutput(ViewConstants.INVALID_NAME_LENGTH);
			} else if (input.length() < 3 || 15 < input.length()) {
				this.displayOutput(ViewConstants.INVALID_NAME_LENGTH);
			} else if (StringUtils.isAlphanumericSpace(input)) {
				break;
			} else {
				this.displayOutput(ViewConstants.INVALID_ALPHA_NUMBERIC);
			}
			this.displayOutput("");
		}

		return input;
	}

	private String fieldNumberInput(String label) {
		String input = "";
		while (true) {
			this.displayOutput("");
			this.displayOutput(label);
			input = super.captureUserInput();
			if (StringUtils.isEmpty(input)) {
				this.displayOutput(ViewConstants.INVALID_EMPTY);
			} else {
				try {
					float val = Float.valueOf(input);

					if (val < 0) {
						this.displayOutput(ViewConstants.INVALID_NUMBER_BELOW_0);
					}

					break;
				} catch (NumberFormatException e) {
					this.displayOutput(ViewConstants.INVALID_NUMBER);
				}
			}

		}

		return input;
	}

	private void loadInitialDisplay() {
		this.displayOutput(ViewConstants.INITIAL_DISPLAY);
	}

	private void controlTask(TaskVO taskVO, int projectId) {

		while (true) {
			this.displayOutput("");
			this.displayOutput("Current Task: " + taskVO.toString());

			this.displaySubTasks(taskVO.getBranches());

			this.displayOutput("");
			this.displayOptions(ViewConstants.OPTIONS_TASK);

			String input = super.captureUserInput().toLowerCase();
			if (input.equals(ViewConstants.OPTIONS_TASK_ADD_SUB)) {

				this.createSubTask(projectId, taskVO.getId());

				taskVO = this.appController.loadTask(taskVO.getId()).getData();

			} else if (input.equals(ViewConstants.OPTIONS_TASK_COMPLETE)) {

				this.displayOutput("");

				Integer index = this.listOptionInput(taskVO.getBranches().size(), ViewConstants.OPTIONS_TASK_SUB_COMPLETE);

				if (index != 0) {
					ControllerResponse<Void> response = this.appController.completeTask(taskVO.getBranches().asList().get(index - 1));

					if (response.hasError()) {
						for (String error : response.getError()) {
							this.displayOutput(error);
						}
					}
				}

				taskVO = this.appController.loadTask(taskVO.getId()).getData();

			} else if (input.equals(ViewConstants.OPTIONS_TASK_VIEW_SUB)) {
				this.displayOutput("");
				Integer index = this.listOptionInput(taskVO.getBranches().size(), ViewConstants.OPTIONS_TASK_SUB_TO_VIEW);

				if (index != 0) {
					taskVO = this.appController.loadTask(taskVO.getBranches().asList().get(index - 1)).getData();
				}

				taskVO = this.appController.loadTask(taskVO.getId()).getData();
			} else if (input.equals(ViewConstants.OPTION_END)) {
				break;
			} else {
				this.displayOutput(ViewConstants.INVALID_OPTION);
			}

		}

	}

	private int selectFromProjects() {
		int projectId = 0;

		List<ProjectVO> projectList = appController.loadProjects();

		if (projectList.isEmpty()) {
			this.displayOutput(ViewConstants.EMPTY_PROJECT);
		} else {
			this.displayOutput("");
			this.displayOutput("List of projects");
			int counter = 1;
			while (true) {
				for (ProjectVO projectVO : projectList) {
					this.displayOutput("[" + counter + "]" + projectVO.getName() + " - " + projectVO.getDuration() + " [" + projectVO.getStartDate() + " -> "
							+ projectVO.getEndDate() + "] " + projectVO.getStatus());
					counter++;
				}

				projectId = this.listOptionInput(projectList.size(), ViewConstants.OPTION_PROJECTS);

				if (projectId > 0) {

					projectId = projectList.get(projectId - 1).getId();

					break;
				}

			}
		}

		this.displayOutput("");

		return projectId;

	}

	private void displayBranches(LinkedMap<Integer, TaskVO> branches) {

		this.displayOutput("");
		this.displayOutput("Lists of tasks: ");

		int counter = 1;

		for (TaskVO taskVO : branches.values()) {
			this.displayOutput("[" + counter + "] " + taskVO.toString());
			counter++;
		}

		this.displayOutput("");
	}

	private void displaySubTasks(LinkedMap<Integer, TaskVO> branches) {
		this.displayOutput("");
		this.displayOutput("Lists of Branches: ");
		int counter = 1;

		for (TaskVO taskVO : branches.values()) {
			this.displayOutput("[" + counter + "] " + taskVO.toString());
			counter++;
		}

		this.displayOutput("");

	}

	private int listOptionInput(int size, String label) {
		int selectedBranch = 0;
		String input = "";
		while (true) {
			this.displayOutput(label);

			input = super.captureUserInput();

			if (input == null || input.isEmpty()) {
				this.displayOutput(ViewConstants.INVALID_EMPTY);
			} else if (input.equals(ViewConstants.OPTION_END)) {
				return 0;
			} else {
				try {
					selectedBranch = Integer.valueOf(input);
					if (selectedBranch > size) {
						this.displayOutput(ViewConstants.INVALID_OPTION);
						continue;
					}

					break;
				} catch (NumberFormatException e) {
					this.displayOutput(ViewConstants.INVALID_NUMBER);
				}
			}

		}

		return selectedBranch;
	}

	@Override
	protected void displayOptions(String[] options) {
		for (int x = 0; x < options.length; x++) {
			this.displayOutput("[" + (x + 1) + "] " + options[x]);
		}

		this.displayOutput(ViewConstants.SELECT_OPTION);
	}

	@Override
	protected void displayOutput(String output) {
		System.out.println(output);
	}

	private void displayProjectTasks(ProjectVO projectVO) {
		while (true) {
			this.displayOutput("");
			this.displayOutput("Current Project: " + projectVO.toString());
			this.displayOutput("");
			this.displayBranches(projectVO.getBranches());
			this.displayOptions(ViewConstants.OPTIONS_PROJECT_TASK);

			String input = super.captureUserInput();

			if (input.equals(ViewConstants.OPTION_PROJECT_TASK_ADD)) {
				this.createTask(projectVO.getId());

				projectVO = appController.loadProject(projectVO.getId());

			} else if (input.equals(ViewConstants.OPTION_PROJECT_TASK_COMPLETE)) {
				this.displayOutput("");

				Integer index = this.listOptionInput(projectVO.getBranches().size(), ViewConstants.OPTIONS_TASK_SUB_COMPLETE);

				if (index != 0) {
					ControllerResponse<Void> response = this.appController.completeTask(projectVO.getBranches().asList().get(index - 1));

					if (response.hasError()) {
						for (String error : response.getError()) {
							this.displayOutput(error);
						}
					}
				}

				projectVO = appController.loadProject(projectVO.getId());
			} else if (input.equals(ViewConstants.OPTION_PROJECT_TASK_VIEW)) {
				int index = this.listOptionInput(projectVO.getBranches().size(), ViewConstants.OPTION_TASKS);

				if (index > 0) {

					TaskVO taskVO = appController.loadTask(projectVO.getBranches().asList().get(index - 1)).getData();

					this.controlTask(taskVO, projectVO.getId());
				}

				projectVO = appController.loadProject(projectVO.getId());
			} else if (input.equals(ViewConstants.OPTION_END)) {
				break;
			} else {
				this.displayOutput(ViewConstants.INVALID_OPTION);
				projectVO = appController.loadProject(projectVO.getId());
			}

		}
	}

	@Override
	protected void userActions() {

		while (true) {
			this.displayOptions(ViewConstants.OPTIONS_PROJECT);
			String input = super.captureUserInput().toLowerCase();

			if (input.equals(ViewConstants.OPTION_PROJECT_VIEW_ALL)) {
				int projectId = this.selectFromProjects();

				if (projectId != 0) {
					this.displayProjectTasks(appController.loadProject(projectId));
				}

			} else if (input.equals(ViewConstants.OPTION_PROJECT_CREATE)) {
				this.displayOutput("\n");
				ProjectVO projectVO = this.createProject();
				this.displayOutput("\n");
				if (projectVO == null) {
					continue;
				}

			} else if (input.equals(ViewConstants.OPTION_END)) {
				this.displayOutput(ViewConstants.END_DISPLAY);
				break;
			} else {
				this.displayOutput(ViewConstants.INVALID_OPTION);
			}

		}
	}

}
