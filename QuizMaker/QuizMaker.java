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

	private void create(){
		System.out.println("");
		System.out.println("*CREATE MODE*");
		String quizName = enterQuizName();
		generateUniqueQuizID(quizName);

		boolean anotherQuestion = true;
		int numberOfQuestions = 0;
		do {
			newQuestion();
			numberOfQuestions++;
			if (numberOfQuestions < 10){
				System.out.println("Add another question?");
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
			System.out.println("");
			System.out.println("Add another option?");
			isFinal = yesNo();
		} while (isFinal);

		setCorrect();
		System.out.println("");
		System.out.println("Question complete!");
	}

	private void newOption(int optionNum){
		Scanner in = new Scanner(System.in);
		boolean addToQuestion = false;
		String str = "";
		System.out.println("New option:");
		str = in.nextLine();

		char optionChar = numToChar(optionNum);
		if (optionChar == 'X'){
			System.out.println("Sorry, only 5 options allowed!");
		} else {
			str = optionChar + ": " + str;
			addToQuestion = true;			
		}
	
		if (addToQuestion){
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
	}

	private void setCorrect(){
		Scanner in = new Scanner(System.in);
		int optionNum = -1;
		char answer = 'X';
		int answerNum = -1;
		boolean isFinal = false;

		List<String> questionOptions;
		questionOptions = getOptions();
		optionNum = questionOptions.size();
		
		System.out.println(listToString(questionOptions));

		while (!isFinal) {
			System.out.println("");
			System.out.println("Answer:");
			String str = in.nextLine();
			answer = str.charAt(0);
			answerNum = charToNum(answer);
			if (answerNum <= optionNum){
				System.out.println("");
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

	private void monitor(){
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the quiz ID to see a list of scores.");
		String str = in.nextLine();

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

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

    private String listToString(List<String> strings){
        String result = "";
        Iterator<String> iterator = strings.iterator();
        for (String str : strings){
           	result += str + "\r\n";
        } 

        return result;
    }

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