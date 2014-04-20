import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Compute extends Remote {

	/**
	* Quiz Player and Quiz Maker client programs.
	*
	* Prints a list of quizzes that are available to play.
	* 
	* @return void, quizzes are printed (or make CL menu?)
	*/
    List<String> getQuizNamesAndIDs() throws RemoteException;

	String listAnswers() throws RemoteException;


	String generateUniqueQuizID(String name) throws RemoteException;

    void addQuestion(String q) throws RemoteException;

    void addOption(String str) throws RemoteException;

    void setCorrect(int num) throws RemoteException;

    void flush() throws RemoteException;

	/**
	* Quiz Player client program.
	* 
	* Searches for pre-existing player name.
	* 
	* @return true if user already exists, false if there is no such user.
	* @param the String of the name to be searched for.
	*/
	boolean searchUser(String name) throws RemoteException;

	/**
	* Quiz Player client program.
	* 
	* A list of Strings is returned from the client, containing the player data to be recorded.
	* 
	* @param the list of Strings:  list(0) username, list(1) name, list(2) email address.
	*/
    void enterPlayerData(List<String> list) throws RemoteException;

    List<String> printEntireQuiz() throws RemoteException;

    /**
    * Quiz Player client program.
    * 
    * Client program provides server with number of quiz to be played.
    boolean selectQuiz(String quizID) throws RemoteException;

    int getNumberOfQuestions() throws RemoteException;

}