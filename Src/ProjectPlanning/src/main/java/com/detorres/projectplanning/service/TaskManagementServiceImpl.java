package com.detorres.projectplanning.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.detorres.projectplanning.constants.DefaultValConstants;
import com.detorres.projectplanning.constants.ValidatorConstants;
import com.detorres.projectplanning.dao.MappingDao;
import com.detorres.projectplanning.dao.ProjectDao;
import com.detorres.projectplanning.dao.ProjectDaoImpl;
import com.detorres.projectplanning.dao.ProjectMappingDao;
import com.detorres.projectplanning.dao.ProjectTaskMappingDao;
import com.detorres.projectplanning.dao.TaskDao;
import com.detorres.projectplanning.dao.TaskDaoImpl;
import com.detorres.projectplanning.entity.Project;
import com.detorres.projectplanning.entity.ServiceResponse;
import com.detorres.projectplanning.entity.Task;
import com.detorres.projectplanning.utility.DateUtility;

public class TaskManagementServiceImpl implements TaskManagementService {

	private ProjectDao projectDao;
	private MappingDao projectMappingDao;
	private ProjectTaskMappingDao projectTaskMappingDao;
	private TaskDao taskDao;

	public TaskManagementServiceImpl() {
		this.projectDao = new ProjectDaoImpl();
		this.projectMappingDao = new ProjectMappingDao();
		this.taskDao = new TaskDaoImpl();
		this.projectTaskMappingDao = new ProjectTaskMappingDao();
	}

	@Override
	public ServiceResponse<Void> completeTask(int id) {

		ServiceResponse<Void> response = new ServiceResponse<Void>();

		Task task = taskDao.getById(id);

		if (task.isComplete()) {
			response.addError(ValidatorConstants.INVALID_TASK_COMPLETE);
			return response;
		}

		if (task.hasParentTask()) {

			Task parentTask = taskDao.getById(task.getParentTaskId());

			int counter = 0;
			int totalSubTask = parentTask.getBranches().size();

			for (Task subTask : parentTask.getBranches().values()) {
				counter++;
				if (subTask.getId() == task.getId()) {
					break;
				}

				if (!subTask.isComplete()) {
					response.addError(ValidatorConstants.INVALID_TASK_INCOMPLETE);
					return response;
				}
			}

			taskDao.completeTask(task.getId());

			// Complete Parent task
			if (counter == totalSubTask) {
				taskDao.completeTask(parentTask.getId());
			}

		} else {
			List<Integer> projectTasks = this.projectMappingDao.getMappingById(task.getParentProjectId());

			for (int projectTask : projectTasks) {
				if (projectTask == task.getId()) {
					break;
				}

				if (!taskDao.getById(projectTask).isComplete()) {
					response.addError(ValidatorConstants.INVALID_TASK_INCOMPLETE);
					return response;
				}
			}

			taskDao.completeTask(task.getId());

		}

		return response;
	}

	@Override
	public void createProject(Project project) {
		this.projectDao.addProject(project);
	}

	@Override
	public void createTask(Task task, int projectId) {
		if (!projectMappingDao.containsMapping(projectId)) {
			task.setStatus(DefaultValConstants.TASK_STATUS_IN_PROGRESS);
		}

		this.taskDao.addTask(task);

		this.projectMappingDao.addMapping(projectId, task.getId());
		this.projectTaskMappingDao.addMapping(projectId, task.getId());
	}

	private void updateParentStatus(Task parentTask, Task subTask) {

		Task currentActive = this.getCurrentTaskOfProject(subTask.getParentProjectId());

		if (currentActive.getId() == subTask.getParentTaskId()) {
			subTask.setStatus(DefaultValConstants.TASK_STATUS_IN_PROGRESS);
		} else {
			subTask.setStatus(DefaultValConstants.TASK_STATUS_WAITING);
		}

	}

	@Override
	public void createTask(Task parentTask, Task subTask) {
		if (subTask.getStatus() == DefaultValConstants.TASK_STATUS_UNDEFINED) {
			this.updateParentStatus(parentTask, subTask);
		}
		this.taskDao.addTask(subTask);
		this.taskDao.addDependentTask(parentTask.getId(), subTask);
		this.projectTaskMappingDao.addMapping(subTask.getParentProjectId(), subTask.getId());

	}

	@Override
	public void editTask(Task task) {
		taskDao.editTask(task);
	}

	@Override
	public Task getCurrentTaskOfProject(int projectId) {
		int taskId = 0;

		List<Integer> projectTasks = projectMappingDao.getMappingById(projectId);

		for (Integer id : projectTasks) {
			Stack<Integer> stackId = this.getTaskStackTree(this.taskDao.getById(id));

			for (int x = 0; x < stackId.size(); x++) {
				Task task = this.taskDao.getById(stackId.get(x));
				if (!task.hasBranches() && task.inProgress()) {
					taskId = task.getId();
					break;
				}
			}
			if (taskId != 0) {
				break;
			}
		}

		return taskDao.getById(taskId);
	}

	@Override
	public Task getNextTaskOfProject(int projectId) {
		Task currentTask = this.getCurrentTaskOfProject(projectId);

		return nextLeaf(currentTask);
	}

	@Override
	public Task getPreviousTaskOfProject(int projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project getProjectByName(String name) {

		return null;
	}

	@Override
	public Date getProjectEndDate(int id) {

		Project project = this.projectDao.getProjectById(id);
		Date startDate = project.getStartDate();
		long duration = 0L;

		for (Integer taskId : this.projectTaskMappingDao.getMappingById(id)) {

			duration += this.taskDao.getById(taskId).getDuration();
		}

		return DateUtility.getInstance().computeEndDate(startDate, duration);
	}

	@Override
	public Task getTaskByName(String name) {

		return new Task();
	}

	@Override
	public Date getTaskEndDate(int id) {

		Task task = taskDao.getById(id);

		Project project = projectDao.getProjectById(task.getParentProjectId());

		Long duration = 0L;

		for (Task currentTask : this.getTaskTree(task)) {
			duration += currentTask.getDuration();
		}

		return DateUtility.getInstance().computeEndDate(project.getStartDate(), duration);
	}

	@Override
	public Project loadProject(int id) {
		return projectDao.getProjectById(id);
	}

	@Override
	public List<Project> loadProjects() {
		return projectDao.getAllProject();
	}

	@Override
	public Map<Integer, Task> loadProjectTasks(int id) {

		Map<Integer, Task> taskMap = new LinkedHashMap<Integer, Task>();
		for (Integer taskId : projectTaskMappingDao.getMappingById(id)) {
			Task task = this.loadTask(taskId);

			if (taskMap.containsKey(task.getParentTaskId())) {
				taskMap.get(task.getParentTaskId()).getBranches().put(task.getId(), task);
			}

			taskMap.put(task.getId(), task);
		}

		return taskMap;
	}

	@Override
	public Task loadTask(int id) {

		return taskDao.getById(id);
	}

	@Override
	public Map<Integer, Task> loadTasks(int taskId) {
		Map<Integer, Task> taskMap = new LinkedHashMap<Integer, Task>();

		for (Integer id : projectTaskMappingDao.getMappingById(this.taskDao.getById(taskId).getParentProjectId())) {
			Task task = this.loadTask(id);

			if (taskMap.containsKey(task.getParentTaskId())) {
				taskMap.get(task.getParentTaskId()).getBranches().put(task.getId(), task);
			}

			taskMap.put(task.getId(), task);
		}

		return taskMap;
	}

	@Override
	public ServiceResponse<Void> removeTask(int id) {
		ServiceResponse<Void> response = new ServiceResponse<Void>();
		Task task = this.taskDao.getById(id);

		if (task.isComplete()) {
			response.addError(ValidatorConstants.INVALID_REMOVE_TASK);

			return response;
		}

		if (task.hasBranches()) {
			projectTaskMappingDao.removeDependentTasks(task.getParentProjectId(), task.getId());

			Stack<Integer> stackBranches = new Stack<Integer>();
			this.getSubTaskIdTraverseDown(task, stackBranches);

			while (!stackBranches.isEmpty()) {
				int branchId = stackBranches.pop();

				taskDao.removeTask(branchId);
			}
		}

		taskDao.removeTask(id);

		return response;
	}

	private int getBottomRightOfTree(Task task, int bottomRight) {

		if (task.hasBranches()) {
			Integer lastKey = task.getBranches().lastKey();
			bottomRight = this.getBottomRightOfTree(task.getBranches().get(lastKey), lastKey);
		}

		return bottomRight;
	}

	private Long getDurationOfSubTask(Task task, Long duration) {
		duration += task.getDuration();

		if (task.hasBranches()) {
			for (Task subTask : task.getBranches().values()) {
				this.getDurationOfSubTask(subTask, duration);
			}
		}

		return duration;
	}

	private void getSubTaskIdTraverseDown(Task task, Stack<Integer> taskIdStack) {

		if (task.hasBranches()) {
			for (Task subTask : task.getBranches().values()) {
				this.getSubTaskIdTraverseDown(taskDao.getById(subTask.getId()), taskIdStack);
			}
		}

		taskIdStack.push(task.getId());

	}

	private void getSubTaskIdTraverseUp(Task task, Stack<Integer> taskIdStack, int mainTaskId, int previousParentTaskId) {
		if (task.hasBranches()) {
			for (Task subTask : task.getBranches().values()) {
				if (subTask.getId() == previousParentTaskId) {
					break;
				}

				subTask = taskDao.getById(subTask.getId());

				if (subTask.hasBranches()) {
					this.getSubTaskIdTraverseDown(subTask, taskIdStack);
				} else {
					taskIdStack.push(subTask.getId());
				}

				if (subTask.getId() == mainTaskId) {
					break;
				}
			}
		}

		if (task.hasParentTask()) {
			taskIdStack.push(task.getId());
			this.getSubTaskIdTraverseUp(taskDao.getById(task.getParentTaskId()), taskIdStack, mainTaskId, task.getId());
		} else if (task.getParentTaskId() == 0) {
			taskIdStack.push(task.getId());
		}

	}

	private Stack<Integer> getTaskStackTree(Task task) {

		Stack<Integer> taskIdStack = new Stack<Integer>();

		this.getSubTaskIdTraverseUp(taskDao.getById(task.getId()), taskIdStack, task.getId(), 0);

		List<Integer> parentTasks = this.projectMappingDao.getMappingById(task.getParentProjectId());

		if (parentTasks != null && !parentTasks.isEmpty()) {
			int lastParentStack = taskIdStack.lastElement();

			for (int taskId : parentTasks) {
				if (taskId == lastParentStack) {
					break;
				}

				getSubTaskIdTraverseUp(taskDao.getById(this.getBottomRightOfTree(taskDao.getById(taskId), 0)), taskIdStack, 0, 0);
			}
		}

		return taskIdStack;
	}

	private List<Task> getTaskTree(Task task) {
		List<Task> subTaskTree = new ArrayList<Task>();

		Stack<Integer> taskIdStack = this.getTaskStackTree(task);

		while (!taskIdStack.isEmpty()) {
			subTaskTree.add(taskDao.getById(taskIdStack.pop()));
		}

		return subTaskTree;
	}

	private Task nextLeaf(Task task) {

		Task nextTask = null;

		if (!task.hasParentTask()) {
			Stack<Integer> taskIdStack = new Stack<Integer>();
			this.getSubTaskIdTraverseDown(this.taskDao.getById(task.getId()), taskIdStack);

			for (int x = 0; x < taskIdStack.size(); x++) {
				Task stackTask = taskDao.getById(taskIdStack.get(x));
				if (!stackTask.hasBranches() && stackTask.isWaiting()) {
					nextTask = stackTask;
					break;
				}
			}
		} else {
			Task parentTask = this.taskDao.getById(task.getParentTaskId());
			if (task.getId() == parentTask.getBranches().lastKey()) {
				if (parentTask.hasParentTask()) {
					nextTask = nextLeaf(parentTask);
				} else {
					List<Integer> projectTasks = this.projectMappingDao.getMappingById(task.getParentProjectId());
					for (int val : projectTasks) {
						if (val > task.getParentTaskId()) {
							nextTask = this.nextLeaf(this.taskDao.getById(val));
							break;
						}
					}
				}
			} else {

				Stack<Integer> taskIdStack = new Stack<Integer>();
				this.getSubTaskIdTraverseDown(this.taskDao.getById(task.getParentTaskId()), taskIdStack);

				for (int x = 0; x < taskIdStack.size(); x++) {
					Task stackTask = taskDao.getById(taskIdStack.get(x));
					if (!stackTask.hasBranches() && stackTask.isWaiting()) {
						nextTask = stackTask;
						break;
					}
				}
			}
		}

		return nextTask;
	}

}
