import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Compute extends Remote {

	/**
	* Prints a list of quizzes that are available to play.
	* 
	* @return void, quizzes are printed (or make CL menu?)
	*/
    List<String> getQuizNames() throws RemoteException;

	boolean searchUser(String name) throws RemoteException;

	String generateUniqueQuizID(String name) throws RemoteException;

    void addQuestion(String q) throws RemoteException;

    void addOption(String str) throws RemoteException;

    void setCorrect(char c) throws RemoteException;

    List<String> getOptions() throws RemoteException;

    void flush() throws RemoteException;

    void enterPlayerData(List<String> list) throws RemoteException;

    List<String> printEntireQuiz() throws RemoteException;

    void selectQuiz(int num) throws RemoteException;

    int getNumberOfQuestions() throws RemoteException;

	// void sendPoints(int points);

	// void listHighScores(String nickname); //list in batches of 10, enable scrolldown?

	// void listHighScores(String nickname, UUID quizID); //as above

	// void listHighScores(UUID quizID); //as above
}