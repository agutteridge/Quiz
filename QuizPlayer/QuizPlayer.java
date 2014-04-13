import java.rmi.Remote;
import java.io.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class QuizPlayer {

	public void launch(){
		System.out.println("*QUIZZES*");
		System.out.println("Play to win great prizes!");
		String username = null;
		while (username == null){
			username = enterName();
		}

		System.out.println("");
		System.out.println("Please enter a number to play the corresponding quiz:");
		int highestOption = listQuizzes();
		selectQuiz(highestOption);
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
			System.out.println("Someone has already registered this username, would you like to play as this user?");
			ans = yesNo();
			if (!ans){
				result = null;
			} else {
				result = str;
			}
		} else {
			System.out.println("This username has not been registered, would you like to register?");
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
		Scanner in = new Scanner(System.in);
		List<String> list = new ArrayList<String>();
		list.add(name);

		System.out.println("Please enter the following details:");
		System.out.print("Your first name and surname: ");
		String str = in.nextLine();
		list.add(str);

		System.out.print("Your email address: ");
		str = in.nextLine();
		list.add(str);

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.enterPlayerData(list);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}

		System.out.println("Great! You've been registered!");
		System.out.println("");
		return name;
	}

	private int listQuizzes(){
		List<String> quizNameList;
		int quizTotal = 0;

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

		return quizTotal;
	}

	private void selectQuiz(int max){
		System.out.println("CHOOSE WISELY.");
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

	public static void main(String[] args) {
		QuizPlayer qp = new QuizPlayer();
		qp.launch();
	}
}