package com.detorres.projectplanning.view;

import org.apache.commons.lang3.StringUtils;

import com.detorres.projectplanning.constants.ViewConstants;
import com.detorres.projectplanning.controller.AppController;
import com.detorres.projectplanning.controller.AppControllerImpl;
import com.detorres.projectplanning.entity.UserPreference;
import com.detorres.projectplanning.utility.DateUtility;
import com.detorres.projectplanning.vo.ProjectVO;

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

	private void loadInitialDisplay() {
		this.displayOutput(ViewConstants.INITIAL_DISPLAY);
	}

	@Override
	protected void userActions() {

		while (true) {
			this.displayOptions(ViewConstants.OPTIONS_PROJECT);
			String input = super.captureUserInput().toLowerCase();

			if (input.equals(ViewConstants.OPTION_PROJECT_VIEW_ALL)) {

			} else if (input.equals(ViewConstants.OPTION_PROJECT_CREATE)) {
				this.displayOutput("\n\n");
				this.createProject();
				this.displayOutput("\n\n");
			} else if (input.equals(ViewConstants.OPTION_END)) {
				this.displayOutput(ViewConstants.END_DISPLAY);
				break;
			} else {
				this.displayOutput(ViewConstants.INVALID_OPTION);
			}

		}
	}

	private void createProject() {

		this.displayOutput(ViewConstants.HEAD_CREATE_PROJECT);

		ProjectVO projectVO = new ProjectVO();
		while (true) {
			this.displayOutput("\n\n");
			this.displayOutput(ViewConstants.FIELD_PROJECT_NAME);
			String input = super.captureUserInput();
			if (StringUtils.isEmpty(input)) {
				this.displayOutput(ViewConstants.INVALID_NAME_LENGTH);
			} else if (input.length() < 3 || 15 < input.length()) {
				this.displayOutput(ViewConstants.INVALID_NAME_LENGTH);
			} else if (StringUtils.isAlphanumericSpace(input)) {
				projectVO.setName(input);
				break;
			} else {
				this.displayOutput(ViewConstants.INVALID_ALPHA_NUMBERIC);
			}
		}

		while (true) {
			this.displayOutput("\n\n");
			this.displayOutput(ViewConstants.FIELD_PROJECT_START_DATE + " " + UserPreference.getInstance().getDateFormat());
			String input = super.captureUserInput();

			if (StringUtils.isEmpty(input)) {
				this.displayOutput(ViewConstants.INVALID_EMPTY);
			} else if (DateUtility.getInstance().isValidDate(input)) {
				projectVO.setStartDate(input);
				break;
			} else {
				this.displayOutput(ViewConstants.INVALID_DATE_INPUT);
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
		for (int x = 0; x < options.length; x++) {
			this.displayOutput("[" + (x + 1) + "] " + options[x]);
		}

		this.displayOutput(ViewConstants.SELECT_OPTION);
	}

}
