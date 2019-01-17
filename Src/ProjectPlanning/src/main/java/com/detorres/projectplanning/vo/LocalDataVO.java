package com.detorres.projectplanning.vo;

import java.util.ArrayList;

import com.detorres.projectplanning.constants.DefaultValConstants;
import com.detorres.projectplanning.entity.Project;

public class LocalDataVO {
	
	private int daysPerWeek = DefaultValConstants.DEFAULT_NUM_DAYS_PER_WEEK;
	
	private int hoursPerDay = DefaultValConstants.DEFAULT_NUM_OF_HRS_PER_DAY;
	
	private ArrayList<Project> projects;
	
	
}
