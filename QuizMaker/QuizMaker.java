import java.util.Scanner;
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

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			quizID = compute.generateUniqueQuizID(quizName);
			if (quizID == null){
				throw new NullPointerException();
			}
			System.out.println("The ID of your quiz \"" + quizName + "\" is " + quizID);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}

		do {
			newQuestion();
			System.out.println("Add another question?");
			isFinal = yesNo();
		} while (isFinal);

		System.out.println("Quiz complete!");
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
		Scanner in = new Scanner(System.in);
		boolean isFinal = false;

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			System.out.println(compute.listAnswers());
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}		

		System.out.println("Answer number:");
		String str = in.nextLine();
		int ans = Integer.parseInt(str);

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

	private void edit(){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			System.out.println(compute.listQuizzes());
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
			flush();
		}
	}

	private void flush(){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.flush();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}		
		System.exit(0);
	}

	public static void main(String[] args) {
		QuizMaker qm = new QuizMaker();
		qm.launch();
		qm.flush();
	}
}