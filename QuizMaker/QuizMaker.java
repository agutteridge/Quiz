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

	public void launch(){
		Scanner in = new Scanner(System.in);

		System.out.println("Welcome to QuizMaker!");
		System.out.println("Press (Q) to quit at any point.");
		System.out.println("Would you like to enter create (C) or edit (E) mode?");
		
		boolean modeSelected = false;
		do {
			String str = in.nextLine();
			if (str.toUpperCase().equals("C")){
				create();
				modeSelected = true;
			} else if (str.toUpperCase().equals("E")) {
				edit();
				modeSelected = true;				
			} else {
				isQuit(str);
				System.out.println("Sorry, your entry wasn't recognised.");
				System.out.println("");
				System.out.println("Would you like to enter create (C) or edit (E) mode?");
				modeSelected = false;
			}
		} while (!modeSelected);
	}

	private void create(){
		System.out.println("");
		System.out.println("*CREATE MODE*");
		String quizName = enterQuizName();
		String quizID = generateUniqueQuizID(quizName);

		boolean anotherQuestion = true;
		do {
			newQuestion();
			System.out.println("Add another question?");
			anotherQuestion = yesNo();
		} while (anotherQuestion);

		System.out.println("Quiz complete!");
		printEntireQuiz();
		boolean saved = saveOrDiscard();
		if (saved){
			System.out.println("Thank you, your quiz (ID: " + quizID + ") has been created!");
		}

		System.out.println("Return to main menu?");
	}

	private String enterName(){
		Scanner in = new Scanner(System.in);
		boolean isFinal = false;
		String quizName = "";

		do {
			System.out.println("Please enter a name for your quiz (4 chars min, 30 chars max.):");
			String str = in.nextLine();
			isQuit(str);
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

	}

	private String generateUniqueQuizID(String name){
		String quizID = "";

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			quizID = compute.generateUniqueQuizID(quizName);
			System.out.println("The ID of your quiz \"" + quizName + "\" is " + quizID);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}

		return quizID;
	}
	
	private void newQuestion(){
		Scanner in = new Scanner(System.in);		
		boolean isFinal = false;
		String str = "";
		System.out.println("New question:");
		str = in.nextLine();
		isQuit(str);
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

		do {
			newOption();
			System.out.println("Add another option?");
			isFinal = yesNo();
		} while (isFinal);

		setCorrect();
		System.out.println("");
		System.out.println("Question complete!");
	}

	private void newOption(){
		Scanner in = new Scanner(System.in);
		boolean isFinal = false;
		String str = "";
		System.out.println("New option:");
		str = in.nextLine();
		isQuit(str);

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

	private void setCorrect(){
		List<String> questionOptions;
		int optionNum = -1;
		questionOptions = getOptions();
		optionNum = questionOptions.size();
		System.out.println("highest option number: " + optionNum);
		System.out.println(listToString(questionOptions));

		Scanner in = new Scanner(System.in);
		boolean isFinal = false;
		int ans = -1;

		System.out.println("Answer number:");
		do {
			String str = in.nextLine();
			try {
				ans = Integer.parseInt(str);
				if (ans < optionNum && ans >= 0){
					isFinal = true;
				} else {
					int highest = optionNum - 1;
					System.out.println("");
					System.out.println("Please choose a value between 0 and " + highest + ".");
				}
			} catch (NumberFormatException e){
				System.out.println("");
				System.out.println("Please enter a numerical value.");
			}
		} while (!isFinal);

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.setCorrect(ans);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	private void printEntireQuiz(){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.printEntireQuiz();
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
				compute.save();
				compute.flush();
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

	private void edit(){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			//lookup quizID
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
			isQuit(ans);
			System.out.println("");

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

	private void isQuit(String str){
		str = str.toUpperCase();
		if (str.equals("Q") || str.equals("QUIT")){
			System.out.println("Are you sure you want to quit? Data has not been saved.");
			boolean quit = yesNo();
			if (quit){
				System.exit(0);
			}
		}
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

    private String listToString(List<String> options){
        String result = "";
        Iterator<String> iterator = options.iterator();
        int i = 0;
        for (String str : options){
            result += i + ": " + str + "\r\n";
            i++;
        } 

        return result;
    }

	public static void main(String[] args) {
		QuizMaker qm = new QuizMaker();
		qm.launch();
		qm.flush();
	}
}