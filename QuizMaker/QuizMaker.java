import java.util.Scanner;

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
		System.out.println("*EDIT MODE*");

		Scanner in = new Scanner(System.in);
		boolean finalName = false;

		do {
			System.out.println("Please enter a name for your quiz:");
			String str = in.nextLine();
			System.out.println("Is the name " + str + " acceptable?");
			String ans = in.nextLine();
			finalName = yesNo(ans);
		} while (finalName = false);

		String quizName = str.substring(0,4);
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.checkQuizName();
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
		str = str.toUpperCase();
		if (str.equals("Y")){
			return true;
		} else if (str.equals("N")){
			return false
		} else {
			System.out.println("Sorry, your entry wasn't recognised.");
			return false;	
		}
	}

	private void isQuit(String str){
		str = str.toUpperCase();
		if (str.equals("Q") || str.equals("QUIT")){
			flush();
		}
	}

	private void flush(){
		//making sure all data gets saved 
		System.exit();
	}

	public static void main(String[] args) {
		QuizMaker qm = new QuizMaker();
		qm.launch();
	}
}