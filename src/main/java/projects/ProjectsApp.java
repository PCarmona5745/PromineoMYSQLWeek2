package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DBException;
import projects.service.ProjectService;

public class ProjectsApp {

	ProjectService projectService = new ProjectService();

	// this is a list of operations a user can do (just the text to display to the
	// user)
	//@formatter:off
	private List<String> operations = List.of(
			"1) Add a project"
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
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
				
			}catch(Exception e){
				System.out.println("\nError: " + e + " Try again.");
			}
			
		}
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
	}

}
