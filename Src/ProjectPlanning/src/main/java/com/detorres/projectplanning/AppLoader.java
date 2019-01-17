package com.detorres.projectplanning;

import com.detorres.projectplanning.view.UserPlainView;

public class AppLoader {

	protected void startApp() {
		this.loadConfig();
		this.loadView();
	}

	private void loadConfig() {

	}

	private void loadView() {
		UserPlainView view = new UserPlainView();
		view.start();
	}

}
