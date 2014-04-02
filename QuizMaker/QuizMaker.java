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

	private void isQuit(String str){
		str = str.toUpperCase();
		if (str.equals("Q") || str.equals("QUIT")){
			flush();
		}
	}

	private void flush(){
		//making sure all data gets saved 
	}

	public static void main(String[] args) {
		QuizMaker qm = new QuizMaker();
		qm.launch();
	}
}