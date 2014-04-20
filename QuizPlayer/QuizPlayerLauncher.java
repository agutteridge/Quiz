import java.util.Scanner;

public class QuizPlayerLauncher {

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
		QuizPlayerLauncher qpl = new QuizPlayerLauncher();
		QuizPlayer qp = new QuizPlayer();

		System.out.println("Welcome to Quiz Player!");
		System.out.println("Play to win great prizes!");
		qp.launch();

		System.out.println("Play another quiz?");
		boolean playAgain = qpl.yesNo();
		if (playAgain){
			qp.launch();
		} else {
			System.out.println("\r\n" + "Thank you for playing! We hope you'll come back soon!");
		}
	}
}