package com.detorres.projectplanning.dao;

import java.util.List;
import java.util.Map;

public interface MappingDao {

	public List<Integer> getMappingById(int id);

	public Map<Integer, List<Integer>> getAllMapping();

	public void removeMappingById(int id);

	public void addMapping(int parentId, int subId);

	public void removeDependentTasks(int parentId, int subId);

	public boolean containsMapping(int id);

	public void removeValueFromKey(int key, int value);

}
