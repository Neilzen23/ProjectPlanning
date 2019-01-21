package com.detorres.projectplanning.constants;

public class ValidatorConstants {

	public static final String DATA_EXISTS = "Data already exists";

	public static final String INVALID_PROJECT_START_DATE = "Project's start date of {0} overlaps with other project(s).";

	public static final String INVALID_TASK_INCOMPLETE = "This task can't be completed unless previous tasks are completed. \nPlease review completed tasks if any.";

	public static final String INVALID_TASK_COMPLETE = "This task is already complete.";

	public static final String INVALID_REMOVE_TASK = "Task is already completed and cant be removed";

	public static final String INVALID_TASK_EXIST = "Task Name Already Exist";

	public static final String INVALID_PROJECT_ADD = "Project can't be added because previous project(s) are not yet completed.";

}
