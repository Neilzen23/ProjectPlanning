package com.detorres.projectplanning.validator;

import java.util.List;

public interface InterfaceValidation<T> {

	public List<String> checkDataExist(T data);

	public List<String> validInput(T data);

	public List<String> validEntry(T data);

}
