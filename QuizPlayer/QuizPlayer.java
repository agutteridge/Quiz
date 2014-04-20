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
	private String name;
	private int upperIntBound;
	private char[] answers;

	public QuizPlayer(){
		this.name = null;
		this.upperIntBound = 0;
	}

	public void launch(){
		while (this.name == null){
			this.name = enterName();
		}

		listQuizzes();
		selectQuiz();		
		play();
		int finalScore = getScore();
		System.out.println("Thank you for playing! Your score is: " + finalScore + "\r\n");
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

		System.out.println("Great! You've been registered!" + "\r\n");
		return name;
	}

	private void listQuizzes(){
		Scanner in = new Scanner(System.in);
		List<String> quizList = new ArrayList<String>();

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			quizList = compute.getQuizNamesAndIDs();
			this.upperIntBound = quizList.size();
			System.out.println(listToString(quizList));
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	private boolean selectQuiz(){
		boolean chosen = false;
		String chosenQuizName = "";

		while (!chosen){
			System.out.println("\r\n" + "Please enter an ID (4 letters + number) to play the corresponding quiz:");
			searchQuizName();
			System.out.println("Play " + chosenQuizName + "?");
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

		return success;
	}

	private void play(){
		int numberOfQuestions = 0;

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			numberOfQuestions = compute.getNumberOfQuestions();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}		

		this.answers = char[numberOfQuestions];

		for (int i = 0; i < numberOfQuestions; i++) {
			playQuestion(i);
		}
	}

	private void playQuestion(int questionNumber){
		String question = "";
		List<String> options = new ArrayList<String>();

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			question = compute.getQuestion(questionNumber);
			options = compute.getOptions();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}		

		this.upperIntBound = options.size();
		System.out.println("\r\n" + question);
		System.out.println(listToString(options) + "\r\n");
		char answer = enterAnswer();
		this.answers[questionNumber] = answer;
	}

	private char enterAnswer(){
		Scanner in = new Scanner(System.in);
		int answerNum = 9;
		char answer = 'X';

		while (answerNum > upperIntBound){
			System.outprint("Your answer: ");
			answer = in.nextLine().charAt(0);
			answerNum = charToNum(c);
			if (answerNum == 6){
				System.out.println("Sorry, that answer was invalid." + "\r\n");
			}
		}

		return answer;
	}

	private int getScore(){
		int score = 0;

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			score = compute.compareAnswers(answers);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}

		return score;
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
}