package com.detorres.projectplanning.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProjectTaskMappingDao extends AbstractMappingDaoImpl {

	private static Map<Integer, List<Integer>> projectTaskMapping = new LinkedHashMap<Integer, List<Integer>>();

	public ProjectTaskMappingDao() {
		super.setMappingData(projectTaskMapping);
	}

}
