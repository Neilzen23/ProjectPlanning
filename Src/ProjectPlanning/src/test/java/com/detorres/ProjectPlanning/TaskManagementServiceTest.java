package com.detorres.ProjectPlanning;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.time.DateUtils;

import com.detorres.projectplanning.constants.DefaultValConstants;
import com.detorres.projectplanning.constants.ValidatorConstants;
import com.detorres.projectplanning.entity.Project;
import com.detorres.projectplanning.entity.Task;
import com.detorres.projectplanning.service.TaskManagementService;
import com.detorres.projectplanning.service.TaskManagementServiceImpl;
import com.detorres.projectplanning.utility.DateUtility;

import junit.framework.TestCase;

public class TaskManagementServiceTest extends TestCase {

	private Project project;
	private TaskManagementService taskManagementService;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		generateTestData();
		this.taskManagementService = new TaskManagementServiceImpl();
	}

	public void testAddTask() {

		Project testProject = new Project();
		this.copyProject(this.project, testProject);
		taskManagementService.createProject(testProject);

		Task testTask = new Task();
		testTask.setName("task1");
		testTask.setDuration(this.convertHoursToSec(8));
		testTask.setParentProjectId(testProject.getId());
		taskManagementService.createTask(testTask, testProject.getId());

		Task createdTask = taskManagementService.loadTask(testTask.getId());
		assertTrue(compareTask(testTask, createdTask));

	}

	public void testCompleteFirstTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		for (Task task : this.project.getBranches().values()) {
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				task.setStatus(DefaultValConstants.STATUS_UNDEFINED);
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue(!taskManagementService.completeTask(task.getId()).hasError());

	}

	public void testCompleteMiddleTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		for (Task task : this.project.getBranches().values()) {
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				task.setStatus(DefaultValConstants.STATUS_UNDEFINED);
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue(!taskManagementService.completeTask(task.getId()).hasError());

	}

	public void testCompleteWithoutParent() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		for (Task task : this.project.getBranches().values()) {
			if (!task.hasParentTask()) {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());
			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue(!taskManagementService.completeTask(task.getId()).hasError());
	}

	public void testCompleteNextTaskWithoutParent() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (!task.hasParentTask()) {
				task.setParentProjectId(testProject.getId());
				if (index == 0) {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				}

				if (index == 1) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				}

				taskManagementService.createTask(task, testProject.getId());
			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue(!taskManagementService.completeTask(task.getId()).hasError());
	}

	public void testCompleteWithoutParentWithError() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		int taskId = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (!task.hasParentTask()) {
				task.setParentProjectId(testProject.getId());
				if (index == 0) {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				}

				if (index == 1) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				}

				if (index == 3) {
					taskId = task.getId();
				}
				taskManagementService.createTask(task, testProject.getId());
			}
		}

		List<String> errors = taskManagementService.completeTask(taskId).getError();

		assertTrue(ValidatorConstants.INVALID_TASK_INCOMPLETE.equals(errors.get(0)));
	}

	public void testCompleteToNextTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		int id = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				if (index == 1 || index == 2 || index == 5 || index == 14 || index == 7 || index == 9 || index == 10 || index == 13) {
					Task parentTask = new Task();
					parentTask.setId(task.getParentTaskId());
					task.setParentProjectId(testProject.getId());
					if (index == 14) {
						task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
					} else if (index == 16 || index == 6 || index == 12) {
						task.setStatus(DefaultValConstants.STATUS_WAITING);
					} else {
						task.setStatus(DefaultValConstants.STATUS_COMPLETE);
					}

					taskManagementService.createTask(parentTask, task);
				}
			} else {

				if (index == 2) {
					id = task.getId();
				}
				task.setParentProjectId(testProject.getId());

				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());
		taskManagementService.completeTask(task.getId());
		assertTrue(taskManagementService.loadTask(id).getStatus() == DefaultValConstants.STATUS_IN_PROGRESS);
	}

	public void testCompleteNextTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 19) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				} else if (index == 16 || index == 6 || index == 12) {
					task.setStatus(DefaultValConstants.STATUS_WAITING);
				} else {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				}

				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue(!taskManagementService.completeTask(task.getId()).hasError());
	}

	public void testCompleteLastTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 12) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				} else {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				}

				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue(!taskManagementService.completeTask(task.getId()).hasError());
		assertTrue(testProject.getStatus() == DefaultValConstants.STATUS_COMPLETE);
	}

	public void testCreateProject() {

		Project testProject = new Project();
		this.copyProject(this.project, testProject);
		taskManagementService.createProject(testProject);
		Project createdProject = taskManagementService.loadProject(testProject.getId());

		assertEquals(testProject, createdProject);
	}

	public void testGetFirstNextTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 10) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				}
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getNextTaskOfProject(testProject.getId());

		assertTrue("task13".equals(task.getName()));
	}

	public void testGetFirstTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 10) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				}
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue("task10".equals(task.getName()));
	}

	public void testGetFirstTaskEndDate() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int counter = 0;
		int taskId = 0;
		for (Task task : this.project.getBranches().values()) {
			if (++counter == 10) {
				taskId = task.getId();
			}

			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Date taskEndDate = taskManagementService.getTaskEndDate(taskId);

		assertTrue(DateUtility.getInstance().parseDate("2019-01-17").compareTo(taskEndDate) == 0);

	}

	public void testGetLastTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 12) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				} else {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				}
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue("task12".equals(task.getName()));
	}

	public void testGetLastTaskEndDate() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int counter = 0;
		int taskId = 0;
		for (Task task : this.project.getBranches().values()) {
			if (++counter == 12) {
				taskId = task.getId();
			}
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());
			}
		}

		Date taskEndDate = taskManagementService.getTaskEndDate(taskId);

		assertTrue(DateUtility.getInstance().parseDate("2019-02-15").compareTo(taskEndDate) == 0);
	}

	public void testGetMiddleNextTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 19) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				} else if (index == 16 || index == 6 || index == 12 || index == 20) {

					task.setStatus(DefaultValConstants.STATUS_WAITING);
				} else {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				}
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getNextTaskOfProject(testProject.getId());

		assertTrue("task20".equals(task.getName()));
	}

	public void testGetMiddleTask() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index < 19) {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				} else if (index == 19) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				}
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getCurrentTaskOfProject(testProject.getId());

		assertTrue("task19".equals(task.getName()));
	}

	public void testGetMiddleTaskEndDate() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int counter = 0;
		int taskId = 0;
		for (Task task : this.project.getBranches().values()) {
			if (++counter == 15) {
				taskId = task.getId();
			}
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Date taskEndDate = taskManagementService.getTaskEndDate(taskId);

		assertTrue(DateUtility.getInstance().parseDate("2019-02-11").compareTo(taskEndDate) == 0);
	}

	public void testGetMiddleParentEndDate() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int counter = 0;
		int taskId = 0;
		for (Task task : this.project.getBranches().values()) {
			if (++counter == 2) {
				taskId = task.getId();
			}
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Date taskEndDate = taskManagementService.getTaskEndDate(taskId);

		assertTrue(DateUtility.getInstance().parseDate("2019-02-13").compareTo(taskEndDate) == 0);
	}

	public void testGetNextLowerStack() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 10 || index == 13 || index == 9 || index == 14) {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				}
				if (index == 4) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				}
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getNextTaskOfProject(testProject.getId());

		assertTrue("task11".equals(task.getName()));
	}

	public void testGetNextTreeStack() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 10 || index == 13 || index == 9) {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				} else if (index == 14) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				} else {
					task.setStatus(DefaultValConstants.STATUS_WAITING);
				}
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getNextTaskOfProject(testProject.getId());

		assertTrue("task4".equals(task.getName()));
	}

	public void testGetNextUpperStack() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		int index = 0;
		for (Task task : this.project.getBranches().values()) {
			index++;
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				if (index == 20) {
					task.setStatus(DefaultValConstants.STATUS_IN_PROGRESS);
				} else if (index == 16 || index == 6 || index == 12) {

					task.setStatus(DefaultValConstants.STATUS_WAITING);
				} else {
					task.setStatus(DefaultValConstants.STATUS_COMPLETE);
				}
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Task task = taskManagementService.getNextTaskOfProject(testProject.getId());

		assertTrue("task16".equals(task.getName()));
	}

	public void testProjectEndDate() {
		Project testProject = new Project();
		this.copyProject(this.project, testProject);

		taskManagementService.createProject(testProject);

		for (Task task : this.project.getBranches().values()) {
			if (task.hasParentTask()) {
				Task parentTask = new Task();
				parentTask.setId(task.getParentTaskId());
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(parentTask, task);
			} else {
				task.setParentProjectId(testProject.getId());
				taskManagementService.createTask(task, testProject.getId());

			}
		}

		Date resultEndDate = taskManagementService.getProjectEndDate(testProject.getId());

		assertTrue(DateUtility.getInstance().parseDate("2019-02-15").compareTo(resultEndDate) == 0);
	}

	private boolean compareTask(Task t1, Task t2) {
		if (!t1.getName().equals(t2.getName())) {
			return false;
		} else if (t1.getDuration() != t2.getDuration()) {
			return false;
		} else if (t1.getParentProjectId() != t2.getParentProjectId()) {
			return false;
		}

		return true;
	}

	private long convertHoursToSec(long hours) {
		return hours * 60L * 60L;
	}

	private void copyProject(Project source, Project object) {
		object.setStartDate(source.getStartDate());
		object.setProjectName(source.getProjectName());
	}

	private void generateTasks(Project project) {
		LinkedMap<Integer, Task> tasks = new LinkedMap<Integer, Task>();

		project.setBranches(tasks);

		Task task1 = new Task();
		task1.setName("task1");
		task1.setParentProjectId(project.getId());
		tasks.put(task1.getId(), task1);

		Task task2 = new Task();
		task2.setName("task2");
		task2.setParentProjectId(project.getId());
		tasks.put(task2.getId(), task2);

		Task task3 = new Task();
		task3.setName("task3");
		task3.setParentProjectId(project.getId());
		tasks.put(task3.getId(), task3);

		Task task4 = new Task();
		task4.setName("task4");
		task4.setDuration(this.convertHoursToSec(8));
		task4.setParentProjectId(project.getId());
		task4.setParentTaskId(task2.getId());
		tasks.put(task4.getId(), task4);

		Task task5 = new Task();
		task5.setName("task5");
		task5.setParentProjectId(project.getId());
		task5.setParentTaskId(task1.getId());
		tasks.put(task5.getId(), task5);

		Task task6 = new Task();
		task6.setName("task6");
		task6.setDuration(this.convertHoursToSec(10));
		task6.setParentProjectId(project.getId());
		task6.setParentTaskId(task3.getId());
		tasks.put(task6.getId(), task6);

		Task task7 = new Task();
		task7.setName("task7");
		task7.setParentProjectId(project.getId());
		task7.setParentTaskId(task5.getId());
		tasks.put(task7.getId(), task7);

		Task task8 = new Task();
		task8.setName("task8");
		task8.setParentProjectId(project.getId());
		task8.setParentTaskId(task2.getId());
		tasks.put(task8.getId(), task8);

		Task task9 = new Task();
		task9.setName("task9");
		task9.setParentProjectId(project.getId());
		task9.setParentTaskId(task5.getId());
		task9.setDuration(this.convertHoursToSec(18));
		tasks.put(task9.getId(), task9);

		Task task10 = new Task();
		task10.setName("task10");
		task10.setParentProjectId(project.getId());
		task10.setParentTaskId(task7.getId());
		task10.setDuration(this.convertHoursToSec(15));
		tasks.put(task10.getId(), task10);

		Task task11 = new Task();
		task11.setName("task11");
		task11.setParentProjectId(project.getId());
		task11.setParentTaskId(task8.getId());
		task11.setDuration(this.convertHoursToSec(10));
		tasks.put(task11.getId(), task11);

		Task task12 = new Task();
		task12.setName("task12");
		task12.setParentProjectId(project.getId());
		task12.setParentTaskId(task3.getId());
		task12.setDuration(this.convertHoursToSec(4));
		tasks.put(task12.getId(), task12);

		Task task13 = new Task();
		task13.setName("task13");
		task13.setParentProjectId(project.getId());
		task13.setDuration(this.convertHoursToSec(20));
		task13.setParentTaskId(task7.getId());
		tasks.put(task13.getId(), task13);

		Task task14 = new Task();
		task14.setName("task14");
		task14.setParentProjectId(project.getId());
		task14.setParentTaskId(task1.getId());
		task14.setDuration(this.convertHoursToSec(40));
		tasks.put(task14.getId(), task14);

		Task task15 = new Task();
		task15.setName("task15");
		task15.setParentProjectId(project.getId());
		task15.setParentTaskId(task8.getId());
		tasks.put(task15.getId(), task15);

		Task task16 = new Task();
		task16.setName("task16");
		task16.setParentProjectId(project.getId());
		task16.setParentTaskId(task8.getId());
		task16.setDuration(this.convertHoursToSec(15));
		tasks.put(task16.getId(), task16);

		Task task17 = new Task();
		task17.setName("task17");
		task17.setParentProjectId(project.getId());
		task17.setParentTaskId(task15.getId());
		task17.setDuration(this.convertHoursToSec(12));
		tasks.put(task17.getId(), task17);

		Task task18 = new Task();
		task18.setName("task18");
		task18.setParentProjectId(project.getId());
		task18.setParentTaskId(task15.getId());
		task18.setDuration(this.convertHoursToSec(5));
		tasks.put(task18.getId(), task18);

		Task task19 = new Task();
		task19.setName("task19");
		task19.setParentProjectId(project.getId());
		task19.setParentTaskId(task15.getId());
		task19.setDuration(this.convertHoursToSec(8));
		tasks.put(task19.getId(), task19);

		Task task20 = new Task();
		task20.setName("task20");
		task20.setParentProjectId(project.getId());
		task20.setParentTaskId(task15.getId());
		task20.setDuration(this.convertHoursToSec(12));
		tasks.put(task20.getId(), task20);
	}

	private void generateTestData() {
		this.project = new Project();
		this.project.setProjectName("TestProject");
		try {
			this.project.setStartDate(DateUtils.parseDate("2019-01-15", DefaultValConstants.DEFAULT_DATE_FORMAT));
		} catch (Exception e) {

		}

		this.project.setDuration(this.convertHoursToSec(162));
		this.generateTasks(project);

	}

}
