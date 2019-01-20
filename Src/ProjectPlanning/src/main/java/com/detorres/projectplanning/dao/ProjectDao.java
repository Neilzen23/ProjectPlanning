package com.detorres.projectplanning.dao;

import java.util.List;

import com.detorres.projectplanning.entity.Project;

public interface ProjectDao {

	public Project getProjectById(int id);

	public void addProject(Project project);

	public List<Project> getAllProject();

}
