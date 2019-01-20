package com.detorres.projectplanning.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProjectMappingDao extends AbstractMappingDaoImpl {

	private static Map<Integer, List<Integer>> projectMappingData = new LinkedHashMap<Integer, List<Integer>>();

	public ProjectMappingDao() {
		super.setMappingData(projectMappingData);
	}

}
