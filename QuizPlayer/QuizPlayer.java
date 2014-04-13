import java.rmi.Remote;
import java.io.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.Scanner;
import java.util.List;
import java.util.Iterator;

public class QuizPlayer {
	public void launch(){
		enterName();
	}

	private void enterName(){
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
		if (existingUser){
			System.out.println("Someone has already registered this name, would you like to play as this user?");
			ans = yesNo();
		} else {
			System.out.println("This name has not been registered, would you like to register?");
			ans = yesNo();
		}
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
			isQuit(ans);

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
		QuizPlayer m = new QuizPlayer();
		m.launch();
	}
}