package com.detorres.projectplanning.dao;

import java.util.Map;

import com.detorres.projectplanning.entity.Project;

public class ProjectDaoImpl implements ProjectDao {

	private static Map<Integer, Project> projectData;

	public Project getProjectById(int id) {
		return projectData.get(id);
	}

	public void addProject(Project project) {
		projectData.put(project.getId(), project);
	}

}
