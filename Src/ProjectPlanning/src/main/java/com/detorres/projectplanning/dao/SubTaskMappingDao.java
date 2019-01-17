package com.detorres.projectplanning.dao;

import java.util.List;
import java.util.Map;

public interface SubTaskMappingDao {

	public List<Integer> getMappingById(int id);

	public Map<Integer, List<Integer>> getAllMapping();

	public void removeMappingById(int id);

	public void addMapping(int id, List<Integer> subTasks);

	public void removeDependentTasks(int id, int subTaskId);

}
