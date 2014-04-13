import java.rmi.Remote;
import java.io.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.Scanner;
import java.util.List;
import java.util.Iterator;

public class QuizPlayer extends Thread {
	String name;

	public void launch(){
		boolean nameChosen = false;
		while (!nameChosen){
			name = enterName();
			if (name != null){
				nameChosen = true;
			}			
		}
	}

	private String enterName(){
        Scanner in = new Scanner(System.in);
        boolean alphanum = false;
        String str = "";
	        
        while (!alphanum) {
        	System.out.println("Please enter a username:");
            str = in.nextLine();
            char[] charArray = str.toCharArray();
        
            alphanum = true;
            if (charArray.length == 0) {
            	alphanum = false;
            } else {
	            for (char c : charArray) {
	                if (!Character.isLetterOrDigit(c)){
	                    alphanum = false;
	                }
	            }
            }

            if (!alphanum){
                System.out.println("Sorry, only alphanumeric characters can be used.");
            }
        }

        boolean existingUser = true;
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			existingUser = compute.searchUser(str);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}

		boolean ans = false;
		String result = null;
		if (existingUser){
			System.out.println("Someone has already registered this name, would you like to play as this user?");
			ans = yesNo();
			if (!ans){
				result = null;
			} else {
				result = str;
			}
		} else {
			System.out.println("This name has not been registered, would you like to register?");
			ans = yesNo();
			if (ans){
				result = register(str);
			} else {
				result = null;
				System.out.println("Quizzes cannot be played without a valid account.");
			}
		}

		return result;
	}

	private String register(String name){
		System.out.println("register");
		return name;
	}

	private void listQuizzes(){
		List<String> quizNameList;
		int quizTotal;

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			quizNameList = compute.getQuizNames();
			quizTotal = quizNameList.size();
			System.out.println(listToString(quizNameList));
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

    private String listToString(List<String> strings){
        String result = "";
        Iterator<String> iterator = strings.iterator();
        for (String str : strings){
           	result += str + "\r\n";
        } 

        return result;
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

	private void run(){
		System.out.println("Are you sure you want to quit? A score has not been submitted."); //data of last score
		boolean quit = yesNo();
		if (quit){
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		QuizPlayer qp = new QuizPlayer();

		try {
			Runtime.getRuntime().addShutdownHook(qp);
		} catch (Exception e) {
			e.printStackTrace();
		}	

		qp.launch();
	}
}