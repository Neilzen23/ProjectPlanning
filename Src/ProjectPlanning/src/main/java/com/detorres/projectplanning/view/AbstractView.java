package com.detorres.projectplanning.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AbstractView {

	protected BufferedReader br;

	public AbstractView() {
		this.br = new BufferedReader(new InputStreamReader(System.in));
	}

	public abstract void start();

	protected abstract void userActions();

	protected abstract void displayOutput(String output);

	protected String captureUserInput() {
		try {
			return br.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	protected abstract void displayOptions(String[] options);

}
