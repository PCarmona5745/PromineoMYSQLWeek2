package projects;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DBException;
import projects.service.ProjectService;

public class ProjectsApp {

	ProjectService projectService = new ProjectService();
	Project curProject;

	// this is a list of operations a user can do (just the text to display to the
	// user)
	//@formatter:off
	private List<String> operations = List.of(
			"1) Add a project.",
			"2) List projects.",
			"3) Select a project.",
			"4) Update project details.",
			"5) Delete a project."
			);
	//formatter:on
	
	//this is the scanner object that will take in user input form the console.
	private Scanner s = new Scanner(System.in);
	
	//MAIN METHOD
	public static void main(String[] args) {
		//new projects app object. calls process user selections. once that method is done it exits the application.
		ProjectsApp pa = new ProjectsApp();
		pa.processUserSelections();
	}


	private void processUserSelections() {
		boolean done = false;
		
		while(!done) {
			//try catch block ensures the program still runs when an unexpected input is entered.
			try {
				int selection = getUserSelection();
				
				switch(selection) {
				case -1:
					done = exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				case 4:
					updateProjectDetails();
					break;
				case 5:
					deleteProject();
					break;
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
				
			}catch(Exception e){
				System.out.println("\nError: " + e + " Try again.");
			}
			
		}
	}


	private void deleteProject() {
		listProjects();
		
		Integer projectId = getIntInput("Enter project ID you wish to delete.");
		
		projectService.deleteProject(projectId);
		
		System.out.println("Selected Project " + projectId + " deleted.");
		
		if(!Objects.isNull(curProject)) {
		if(curProject.getProjectId().equals(projectId)) {
			curProject = null;
			
		}}
		
		
	}


	private void updateProjectDetails() {
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project.");
			return;
		}
		
		
		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the difficulty [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the notes [" + curProject.getNotes() + "]");
		
		Project project = new Project();
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		

		
		
		
		project.setProjectId(curProject.getProjectId());
		
		projectService.modifyProjectDetails(project);
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());
	}


	private void selectProject() {
		listProjects();
		
		Integer projectId = getIntInput("Enter a project ID to select a project");
		
		curProject = null;
		
		curProject = projectService.fetchProjectById(projectId);
		
		if(Objects.isNull(curProject)) {
			System.out.println("Warning: " + projectId + " is not a valid project ID.");
		}
	}


	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects: ");
		
		projects.forEach(project -> System.out.println("    " + project.getProjectId() + ": " + project.getProjectName()));
	}


	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the difficulty");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		
		System.out.println("You have successfully added project: " + dbProject);
	}


	private BigDecimal getDecimalInput(String prompt) {
		//gets a string input from the user.
		String input = getStringInput(prompt);
		
		//tests if the input was null and just returns null.
		if(Objects.isNull(input)) {
			return null;
		}
		
		
		//tries to convert string to BigDecimal, catches if it throws a numberformatexception
		try {
			return new BigDecimal(input).setScale(2);
		}catch(NumberFormatException e) {
			throw new DBException(input + " is not a valid decimal number.");
		}
	}


	private boolean exitMenu() {
		System.out.println("\nNow exiting the menu.");
		return true;
	}


	private int getUserSelection() {
		//prints out the list of operations
		printOperations();
		
		//gets input from the user, displaying a prompt. input should be an int and can be null so Integer is used.
		Integer input = getIntInput("Enter a menu selection.");
		
		//if input is null, then return -1, otherwise return input.
		return Objects.isNull(input) ? -1 : input;
	}


	private Integer getIntInput(String prompt) {
		
		//gets a string input from the user.
		String input = getStringInput(prompt);
		
		//tests if the input was null and just returns null.
		if(Objects.isNull(input)) {
			return null;
		}
		
		
		//tries to convert string to integer, catches if it throws a numberformatexception
		try {
			return Integer.valueOf(input);
		}catch(NumberFormatException e) {
			throw new DBException(input + " is not a valid number.");
		}
	}


	private String getStringInput(String prompt) {
		
		//prints out the prompt
		System.out.print(prompt + ":  ");
		
		//gets next line of user input as a string
		String input = s.nextLine();
		
		//checks if input was blank, if it was, returns null. otherwise returns the input but with no whitespace before or after.
		return input.isBlank() ? null : input.trim();
	}


	private void printOperations() {
		System.out.println("\nThese are the available selections. Enter a number or just press enter to quit:");
		
		//operations is an object of type list and has a lambda expression that runs through each item in the list and prints each line out.
		operations.forEach(line -> System.out.println("    " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not currently working with a project");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}

}
