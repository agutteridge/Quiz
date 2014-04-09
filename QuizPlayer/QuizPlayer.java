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
        System.out.println("Please enter a username:");
        boolean alphanum = false;
        String str = "";
	        
        while (!alphanum) {
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
                System.out.println("Please enter a username:");
            }
        }

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.enterName(str);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
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
			chooseQuiz(quizTotal);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
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
		QuizPlayer m = new QuizPlayer();
		m.launch();
	}
}