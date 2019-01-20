package com.detorres.projectplanning.entity;

import com.detorres.projectplanning.constants.DefaultValConstants;

/**
 * Does not consider multi use of application meaning this UserPreference wont work properly on ServerApplications and would cause bugs because values are being
 * shared.
 * 
 */
public class UserPreference {

	private static final UserPreference instance = new UserPreference();

	private int daysPerWeek = DefaultValConstants.DEFAULT_NUM_DAYS_PER_WEEK;

	private int hoursPerDay = DefaultValConstants.DEFAULT_NUM_OF_HRS_PER_DAY;

	private String dateFormat = DefaultValConstants.DEFAULT_DATE_FORMAT;

	private UserPreference() {

	}

	public static UserPreference getInstance() {
		return instance;
	}

	public int getDaysPerWeek() {
		return daysPerWeek;
	}

	public void setDaysPerWeek(int daysPerWeek) {
		this.daysPerWeek = daysPerWeek;
	}

	public int getHoursPerDay() {
		return hoursPerDay;
	}

	public void setHoursPerDay(int hoursPerDay) {
		this.hoursPerDay = hoursPerDay;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}
