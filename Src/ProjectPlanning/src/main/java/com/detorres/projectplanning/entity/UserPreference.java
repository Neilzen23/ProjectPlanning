package com.detorres.projectplanning.entity;

import com.detorres.projectplanning.constants.DefaultValConstants;

/**
 * Does not consider multi use of application meaning this UserPreference wont work properly on ServerApplications and would cause bugs because values are being
 * shared.
 * 
 */
public class UserPreference {

	private static final UserPreference instance = new UserPreference();

	public static UserPreference getInstance() {
		return instance;
	}

	private String dateFormat = DefaultValConstants.DEFAULT_DATE_FORMAT;

	private int defaultDurationFormat = DefaultValConstants.DEFAULT_DURATION_HRS;

	private int hoursPerDay = DefaultValConstants.DEFAULT_NUM_OF_HRS_PER_DAY;

	private UserPreference() {

	}

	public String getDateFormat() {
		return dateFormat;
	}

	public int getDefaultDurationFormat() {
		return defaultDurationFormat;
	}

	public int getHoursPerDay() {
		return hoursPerDay;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setDefaultDurationFormat(int defaultDurationFormat) {
		this.defaultDurationFormat = defaultDurationFormat;
	}

	public void setHoursPerDay(int hoursPerDay) {
		this.hoursPerDay = hoursPerDay;
	}

}
