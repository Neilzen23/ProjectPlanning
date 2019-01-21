package com.detorres.projectplanning.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractMappingDaoImpl implements MappingDao {

	private Map<Integer, List<Integer>> mappingData;

	public AbstractMappingDaoImpl() {
		mappingData = new HashMap<Integer, List<Integer>>();
	}

	@Override
	public Map<Integer, List<Integer>> getAllMapping() {
		return mappingData;
	}

	@Override
	public List<Integer> getMappingById(int id) {
		return mappingData.get(id);
	}

	@Override
	public void removeMappingById(int id) {
		mappingData.remove(id);
	}

	@Override
	public void addMapping(int parentId, int subId) {
		if (mappingData.containsKey(parentId)) {
			mappingData.get(parentId).add(subId);
		} else {
			List<Integer> subTaskMapping = new ArrayList<Integer>();
			subTaskMapping.add(subId);

			mappingData.put(parentId, subTaskMapping);
		}
	}

	@Override
	public void removeDependentTasks(int parentId, int subId) {
		mappingData.get(parentId).remove(subId);
	}

	void setMappingData(Map<Integer, List<Integer>> mappingData) {
		this.mappingData = mappingData;
	}

	@Override
	public boolean containsMapping(int id) {
		return mappingData.containsKey(id);
	}

	@Override
	public void removeValueFromKey(int key, int value) {
		mappingData.get(key).remove(mappingData.get(key).indexOf(value));
	}
}
