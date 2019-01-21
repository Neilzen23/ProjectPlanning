package com.detorres.projectplanning.vo;

import java.util.ArrayList;
import java.util.List;

public class ControllerResponse<T> {

	private T data;

	private List<String> error = new ArrayList<String>();

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<String> getError() {
		return error;
	}

	public void setError(List<String> error) {
		this.error = error;
	}

	public boolean hasError() {

		return !error.isEmpty();
	}

}
