import java.util.Scanner;

public class QuizMakerLauncher {

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
		QuizMakerLauncher qml = new QuizMakerLauncher(); 
		QuizMaker qm = new QuizMaker();
		Scanner in = new Scanner(System.in);

		System.out.println("Welcome to QuizMaker!");
		qm.launch();

		System.out.println("Return to main menu?");
		boolean backToMenu = qml.yesNo();
		if (backToMenu){
			qm.launch();
		}
	}
}