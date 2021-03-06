import java.util.Scanner;
import java.util.Iterator;
import java.util.List;
import java.rmi.Remote;
import java.io.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class QuizMaker {
	private String quizID; 

	public QuizMaker(){
		this.quizID = "";
	}

	/**
	* Launch method called from Launcher class.
	* 
	* User input determines which mode is chosen, i.e. which methods are called.
	*/
	public void launch(){
		Scanner in = new Scanner(System.in);
		System.out.println("Would you like to enter create (C) or monitor (M) mode?");
		
		boolean modeSelected = false;

		do {
			String str = in.nextLine();
			if (str.toUpperCase().equals("C")){
				create();
				modeSelected = true;
			} else if (str.toUpperCase().equals("M")) {
				monitor();
				modeSelected = true;				
			} else {
				System.out.println("Sorry, your entry wasn't recognised.");
				System.out.println("");
				modeSelected = false;
			}
		} while (!modeSelected);
	}

	/**
	* Starts 'Create Mode', from which a new quiz can be made. A quiz can have a 
	* maximum of 10 questions. The user is then prompted to save or discard their
	* new quiz - saving causes the QuizServer to write the quiz to file.
	*/
	private void create(){
		System.out.println("\r\n" + "*CREATE MODE*");
		String quizName = enterQuizName();
		generateUniqueQuizID(quizName);

		boolean anotherQuestion = true;
		int numberOfQuestions = 0;
		do {
			newQuestion();
			numberOfQuestions++;
			if (numberOfQuestions < 10){
				System.out.println("Add another question (Y/N)?");
				anotherQuestion = yesNo();
			}
		} while (anotherQuestion);

		System.out.println("Quiz complete!");
		printEntireQuiz(quizName);
		boolean saved = saveOrDiscard();
		if (saved){
			System.out.println("Thank you, your quiz (ID: " + this.quizID + ") has been created!");
		}
	}

	/**
	* User input defines the quiz name. Quiz names must be between 4 and 30 characters long.
	*
	* @return An acceptable quiz name.
	*/
	private String enterQuizName(){
		Scanner in = new Scanner(System.in);
		boolean isFinal = false;
		String quizName = "";

		do {
			System.out.println("Please enter a name for your quiz (4 chars min, 30 chars max.):");
			String str = in.nextLine();
			System.out.println("");

			if (str.length() < 4){
				isFinal = false;
				System.out.println("Sorry, the quiz name must be at least 4 characters long.");
			} else {
				if (str.length() > 30) {
					System.out.println("Name will be truncated to: ");
					str = str.substring(0, 31);
					System.out.println(str);
					System.out.println("");
				}
				System.out.println("Are you happy with \'" + str + "\'? (Y/N)");
				isFinal = yesNo();
				quizName = str;
			}
		} while (!isFinal);

		return quizName;
	}

	/**
	* The quiz name is used to generate a quiz ID, QuizServer side.
	* 
	* @param name - the name of the quiz.
	*/
	private void generateUniqueQuizID(String name){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			this.quizID = compute.generateUniqueQuizID(name);
			System.out.println("The ID of your quiz \"" + name + "\" is " + this.quizID);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	* Adding a new question with between 2 and 5 options inclusive.
	*
	* The question (a String) is sent to the QuizServer to store until the quiz is saved.
	*/
	private void newQuestion(){
		Scanner in = new Scanner(System.in);		
		boolean isFinal = false;
		String str = "";
		System.out.println("New question:");
		str = in.nextLine();
		System.out.println("");

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.addQuestion(str);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}

		newOption(0);
		int i = 1;

		do {
			newOption(i);
			i++;
			if (i < 5){
				System.out.println("");
				System.out.println("Add another option (Y/N)?");
				isFinal = yesNo();
			} else {
				System.out.println("\r\n" + "Maximum of 5 options allowed!");
				isFinal = false;
			}
		} while (isFinal);

		setCorrect();
		System.out.println("\r\n" + "Question complete!");
	}

	/**
	* Adding a new option.
	*
	* The option (a String) is sent to the QuizServer to store until the quiz is saved.
	*
	* @param optionNum - The numerical position of the option, as determined by the 
	* newQuestion() method.
	*/
	private void newOption(int optionNum){
		Scanner in = new Scanner(System.in);
		String str = "";

		char optionChar = numToChar(optionNum);
		System.out.println("New option:");
		str = in.nextLine();
		str = "\t" + optionChar + ": " + str;

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.addOption(str);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	* The correct option for the question is set. The character must be within the 
	* appropriate range.
	* 
	* The option (a character) is sent to the QuizServer to store until the quiz is saved.
	*/
	private void setCorrect(){
		Scanner in = new Scanner(System.in);
		int optionNum = -1;
		char answer = 'X';
		int answerNum = -1;
		boolean isFinal = false;

		List<String> questionOptions;
		questionOptions = getOptions();
		optionNum = questionOptions.size();
		
		System.out.println("Which of these options is correct?");
		System.out.println(listToString(questionOptions));

		while (!isFinal) {
			System.out.println("Answer:");
			String str = in.nextLine();
			answer = str.charAt(0);
			answerNum = charToNum(answer);

			if (answerNum < optionNum){
				System.out.println("");
				answer = Character.toUpperCase(answer);
				System.out.println("Is the correct answer " + answer + "?");
				isFinal = yesNo();
			} else {
				System.out.println("Invalid option.");
			}
		}

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.setCorrect(answer);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	/** 
	* Returns a list of options from the QuizServer.
	*
	* @return The List of Strings that represent the options in sequential order.
	*/
	private List<String> getOptions(){
		List<String> result = null;

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			result = compute.getOptions();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/** 
	* Prints a formatted list of the entire quiz from the QuizServer. Each
	* question is followed by its options, the correct option, and finally a 
	* line space.
	*/
	private void printEntireQuiz(String name){
        System.out.println("Displaying " + name + "...");
        System.out.println("");

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			List<String> quizToPrint = compute.printEntireQuiz();
			System.out.println(listToString(quizToPrint));
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	* Asks the user whether the quiz should be saved or discarded. Saving will result in 
	* file persistence on the QuizServer.
	*
	* @return True if the file has been saved, false otherwise.
	*/
	private boolean saveOrDiscard(){
		boolean firstAnswer = false;
		boolean secondAnswer = false;
		do {
			System.out.println("Would you like to save this quiz?");
			firstAnswer = yesNo();
			if (firstAnswer){
				System.out.println("Are you sure? Quizzes cannot be changed or removed!");
				secondAnswer = yesNo();
			} else {
				System.out.println("Are you sure? ");
				secondAnswer = yesNo();		
			}
		} while (!secondAnswer);

		if (firstAnswer){
			try {
				Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
				Compute compute = (Compute) service;
				compute.saveQuiz();
				return true;
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (RemoteException ex) {
				ex.printStackTrace();
			} catch (NotBoundException ex) {
				ex.printStackTrace();
			}			
		}

		return false;
	}

	/**
	* 'Monitor mode' allows the user to input a quiz ID and see either the top score or top
	* ten scores for the quiz.
	*/
	private void monitor(){
		System.out.println("\r\n" + "*MONITOR MODE*");
		Scanner in = new Scanner(System.in);
		boolean quizSelected = false;
		while (!quizSelected){
			quizSelected = selectQuiz();		
		}

		System.out.println("Would you like the top score ('A') or the top 10 scores ('T')?");
		boolean modeSelected = false;
		do {
			String str = in.nextLine();
			if (str.toUpperCase().equals("A")){
				getWinner();
				modeSelected = true;
			} else if (str.toUpperCase().equals("T")) {
				getTop10();
				modeSelected = true;				
			} else {
				System.out.println("Sorry, your entry wasn't recognised.");
				System.out.println("");
				modeSelected = false;
			}
		} while (!modeSelected);
	}

	/**
	* The user inputs a quiz ID, which is then used as a reference to lookup a Quiz
	* on the server. If no such quiz exists, the user can enter input again.
	*
	* @return True if the quiz is on the server, false otherwise.
	*/
	private boolean selectQuiz(){
		Scanner in = new Scanner(System.in);
		boolean chosen = false;
		String chosenQuiz = "";

		while (!chosen){
			System.out.println("Please enter a Quiz ID (4 letters + number).");
			chosenQuiz = in.nextLine();
			chosenQuiz = chosenQuiz.toUpperCase();
			System.out.println("Look up " + chosenQuiz + "?");
			chosen = yesNo();
		}
		
		boolean success = false;
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			success = compute.selectQuiz(chosenQuiz);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}

		if (!success) {
			System.out.println("Sorry, the quiz ID was not recognised.");
		}
		return success;
	}

	/**
	* The top 10 Scores and the user details are formatted and retrieved from 
	* the QuizServer, and then printed client side.
	*/
	private void getTop10(){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			System.out.println(listToString(compute.getTop10()));
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}		
	}

	/**
	* The top Score and the user details are formatted and retrieved from 
	* the QuizServer, and then printed client side.
	*/
	private void getWinner(){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			System.out.println(compute.getWinner());
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}		
	}

	/**
	* Uses user input to answer questions with a Yes/No outcome.
	*
	* @return 'Y' keyboard input returns true, 'N' returns false.
	*/ 
	private boolean yesNo(){
		Scanner in = new Scanner(System.in);
		boolean result = false;
		boolean correctInput = false;

		do {
			String ans = in.nextLine();

			ans = ans.toUpperCase();
			if (ans.equals("Y")){
				result = true;
				correctInput = true;
			} else if (ans.equals("N")){
				result = false;
				correctInput = true;
			} else {
				System.out.println("Sorry, your entry wasn't recognised.");
			}
		} while (!correctInput);

		return result;
	}

	/**
	* Concatenates a List of Strings into one String.
	*
	* @return String, can be over multiple lines.
	*/
    private String listToString(List<String> strings){
        String result = "";
        Iterator<String> iterator = strings.iterator();
        for (String str : strings){
           	result += str + "\r\n";
        } 

        return result;
    }

    /**
    * Converts a character into a numerical equivalent.
    *
    * @param c - Character to be converted.
    * @return Corresponding number, or 6 if the character is out of range.
    */
    private int charToNum(char c){
    	c = Character.toUpperCase(c);

    	switch(c){
    		case 'A':	return 0;
    		case 'B':	return 1;
    		case 'C':	return 2;
       		case 'D':	return 3;
    		case 'E':	return 4;
    		default:	return 6;
    	}
    }

    /**
    * Converts a number into a character.
    *
    * @param i - Number (int) to be converted.
    * @return Corresponding character, or 'X' if the number is out of range.
    */
    private char numToChar(int i){
    	switch(i){
    		case 0:	return 'A';
    		case 1:	return 'B';
    		case 2:	return 'C';
       		case 3:	return 'D';
    		case 4:	return 'E';
    		default:	return 'X';
    	}
    }
}