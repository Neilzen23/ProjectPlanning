package com.detorres.projectplanning.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.detorres.projectplanning.constants.DefaultValConstants;
import com.detorres.projectplanning.entity.Project;

public class ProjectDaoImpl implements ProjectDao {

	private static Map<Integer, Project> projectData = new HashMap<Integer, Project>();

	@Override
	public Project getProjectById(int id) {
		return projectData.get(id);
	}

	@Override
	public void addProject(Project project) {
		projectData.put(project.getId(), project);
	}

	@Override
	public List<Project> getAllProject() {
		SortedSet<Integer> keys = new TreeSet<>(projectData.keySet());
		List<Project> projects = new ArrayList<Project>();
		for (Integer key : keys) {
			projects.add(projectData.get(key));
		}

		return projects;
	}

	@Override
	public void updateStatus(int id) {
		projectData.get(id).setStatus(DefaultValConstants.STATUS_COMPLETE);
	}

}
