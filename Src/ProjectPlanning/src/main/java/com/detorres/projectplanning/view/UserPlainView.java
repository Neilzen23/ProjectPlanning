package com.detorres.projectplanning.view;

import com.detorres.projectplanning.constants.ViewConstants;
import com.detorres.projectplanning.controller.ProjectController;
import com.detorres.projectplanning.controller.ProjectControllerImpl;
import com.detorres.projectplanning.controller.TaskController;
import com.detorres.projectplanning.controller.TaskControllorImpl;

public class UserPlainView extends AbstractView {

	private ProjectController projectController;

	private TaskController taskController;

	public UserPlainView() {
		this.projectController = new ProjectControllerImpl();
		this.taskController = new TaskControllorImpl();
	}

	@Override
	public void start() {
		this.loadInitialDisplay();
		this.userActions();
	}

	private void loadInitialDisplay() {
		this.displayOutput(ViewConstants.INITIAL_DISPLAY);
	}

	@Override
	protected void userActions() {
		String input = super.captureUserInput().toLowerCase();
		while (true) {
			if (input.equals(ViewConstants.OPTION_1)) {

			} else if (input.equals(ViewConstants.OPTION_2)) {

			} else if (input.equals(ViewConstants.OPTION_END)) {
				displayOutput(ViewConstants.END_DISPLAY);
				break;
			}
		}
	}

	@Override
	protected void displayOutput(StringBuilder output) {
		System.out.println(output.toString());
	}

	@Override
	protected void displayOutput(String output) {
		System.out.println(output);
	}

	@Override
	protected void displayOptions(String[] options) {
		for (String option : options) {
			this.displayOutput(option);
		}

		this.displayOutput(ViewConstants.SELECT_OPTION);
	}

}
