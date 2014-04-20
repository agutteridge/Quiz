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

    /**
    * Quiz Player and Maker client programs.
    * 
    * Server searches for ID of quiz to be played provided by the client.
    * 
    * @return true if found, false if quiz with quizID does not exist.
    * @param String of quiz ID to search for
    */ 
    boolean selectQuiz(String quizID) throws RemoteException;

	void enterName(String name) throws RemoteException;

	String generateUniqueQuizID(String name) throws RemoteException;

    void addQuestion(String q) throws RemoteException;

    void addOption(String str) throws RemoteException;

    void setCorrect(char c) throws RemoteException;

    List<String> getOptions() throws RemoteException;

    void saveQuiz() throws RemoteException;

    List<String> printEntireQuiz() throws RemoteException;

    List<String> getTop10() throws RemoteException;

    String getWinner() throws RemoteException;
}