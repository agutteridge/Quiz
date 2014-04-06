import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface Compute extends Remote {

	/**
	* Prints a list of quizzes that are available to play.
	* 
	* @return void, quizzes are printed (or make CL menu?)
	*/
	String listQuizzes() throws RemoteException;

	String listAnswers() throws RemoteException;

	void enterName(String name) throws RemoteException;

	String generateUniqueQuizID(String name) throws RemoteException;

    void addQuestion(String q) throws RemoteException;

    void addOption(String str) throws RemoteException;

    void setCorrect(int num) throws RemoteException;

    void flush() throws RemoteException;

	// void startQuiz(UUID quizID);

	// void sendPoints(int points);

	// void listHighScores(String nickname); //list in batches of 10, enable scrolldown?

	// void listHighScores(String nickname, UUID quizID); //as above

	// void listHighScores(UUID quizID); //as above
}