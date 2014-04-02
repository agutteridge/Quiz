import java.util.Scanner;
import java.rmi.Remote;
import java.io.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class QuizMaker {

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
				System.out.println("Would you like to enter create (C) or edit (E) mode?");
			}
		} while (modeSelected = false);
	}


	private void create(){
		System.out.println("*CREATE MODE*");

		Scanner in = new Scanner(System.in);
		boolean isFinal = false;
		String quizName = "";
		String first4chars = "";

		do {
			System.out.println("Please enter a name for your quiz (4 chars min, 30 chars max.):");
			String str = in.nextLine();

			if (str.length() < 4){
				isFinal = false;
				System.out.println("Sorry, the quiz name must be at least 4 characters long.");
			} else {
				isFinal = yesNo(str);
				first4chars = str.substring(0,4);
				quizName = str;
			}
		} while (!isFinal);

		String quizID = "";
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			quizID = compute.generateUniqueQuizID(first4chars);
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
		isQuit(str);
		boolean correctInput = false;
		str = str.substring(0, 31);

		do {
			System.out.println("Is the name " + str + " acceptable?");
			String ans = in.nextLine();
			ans = str.toUpperCase();
			if (ans.equals("Y")){
				return true;
			} else if (ans.equals("N")){
				return false;
			} else {
				System.out.println("Sorry, your entry wasn't recognised.");
			}
		} while (!correctInput);
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