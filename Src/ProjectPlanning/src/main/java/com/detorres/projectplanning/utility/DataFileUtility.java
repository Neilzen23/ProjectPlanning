package com.detorres.projectplanning.utility;

import java.io.File;

public class DataFileUtility {

	private String fileName;

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean dataFileExists() {
		return new File(this.fileName).exists();
	}

}
