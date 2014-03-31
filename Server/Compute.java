import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Compute extends Remote {

	/**
	* Prints a list of quizzes that are available to play.
	* 
	* @return void, quizzes are printed (or make CL menu?)
	*/
	void listQuizzes();

	void startQuiz(UUID quizID);

	void sendAnswer(String answer); //enum?

	void sendPoints(int points);

	void listHighScores(String nickname); //list in batches of 10, enable scrolldown?

	void listHighScores(String nickname, UUID quizID); //as above

	void listHighScores(UUID quizID); //as above
}