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
				isFinal = yesNo(str);
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
	}

	private boolean yesNo(String str){
		Scanner in = new Scanner(System.in);
		isQuit(str);

		boolean result = false;
		boolean correctInput = false;

		do {
			System.out.println("Is the name " + str + " acceptable?");
			String ans = in.nextLine();
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
				System.out.println("");
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
		//making sure all data gets saved 
		System.exit(0);
	}

	public static void main(String[] args) {
		QuizMaker qm = new QuizMaker();
		qm.launch();
	}
}