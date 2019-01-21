package com.detorres.projectplanning.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.detorres.projectplanning.entity.UserPreference;

public class DateUtility {

	private static final DateUtility instance = new DateUtility();

	public static DateUtility getInstance() {
		return instance;
	}

	private UserPreference userPreference = UserPreference.getInstance();

	private SimpleDateFormat format = new SimpleDateFormat(userPreference.getDateFormat());

	private DateUtility() {

	}

	public Date computeEndDate(Date startDate, long duration) {

		// Convert seconds into days
		double convertedDays = duration / 60D / 60D / userPreference.getHoursPerDay();
		int days = (int) convertedDays;

		if (convertedDays - days != 0) {
			days++;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Date tempDate = cal.getTime();
		int x = 0;
		while (x < convertedDays) {
			tempDate = DateUtils.addDays(tempDate, 1);
			if (this.isWorkingDay(tempDate)) {
				x++;
			}
		}
		return tempDate;
	}

	public boolean isWorkingDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return false;
		} else {
			return true;
		}

	}

	public Date computeStartDate(Date endDate, long duration) {
		// Convert seconds into days
		double convertedDays = duration / 60D / 60D / userPreference.getHoursPerDay();
		int days = (int) convertedDays;

		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		Date tempDate = cal.getTime();
		int x = 0;
		while (x < convertedDays) {
			tempDate = DateUtils.addDays(tempDate, -1);
			if (this.isWorkingDay(tempDate)) {
				x++;
			}
		}

		return tempDate;
	}

	public String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return format.format(date);
	}

	public boolean isValidDate(String date) {
		try {
			format.parse(date);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean isValidFormat(String date) {
		try {
			format.format(date);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	public void loadCustomDateFormat(String format) {
		this.format = new SimpleDateFormat(format);
	}

	public Date parseDate(String date) {

		try {
			return format.parse(date);
		} catch (ParseException e) {
			return null;
		}

	}

}
