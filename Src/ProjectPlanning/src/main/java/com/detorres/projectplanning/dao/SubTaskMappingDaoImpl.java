package com.detorres.projectplanning.dao;

import java.util.List;
import java.util.Map;

public class SubTaskMappingDaoImpl implements SubTaskMappingDao{

	private static Map<Integer, List<Integer>> mappingData;
	
	public Map<Integer, List<Integer>> getAllMapping() {
		return mappingData;
	}
	
	public List<Integer> getMappingById(int id) {
		return mappingData.get(id);
	}
	
	public void removeMappingById(int id) {
		mappingData.remove(id);
	}
	
	public void addMapping(int id, List<Integer> subTasks) {
		mappingData.put(id, subTasks);
	}
	
	public void removeDependentTasks(int id, int subTaskId) {
		mappingData.get(id).remove(subTaskId);
	}
}
