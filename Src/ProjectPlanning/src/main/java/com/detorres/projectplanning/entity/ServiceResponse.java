package com.detorres.projectplanning.entity;

import java.util.ArrayList;
import java.util.List;

public class ServiceResponse<T> {

	private T data;

	private List<String> error = new ArrayList<String>();

	public void addError(String error) {
		this.error.add(error);
	}

	public T getData() {
		return data;
	}

	public List<String> getError() {
		return error;
	}

	public boolean hasError() {

		if (error.isEmpty()) {
			return false;
		}

		return true;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setError(List<String> error) {
		this.error = error;
	}

}
