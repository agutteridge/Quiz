import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Compute extends Remote {

	/**
	* Quiz Player and Quiz Maker client programs.
	*
	* Prints a list of quizzes that are available to play in the format of
	* quiz ID: quiz name
	* 
	* @return A List of Strings, one String for each Quiz available to play.
	*/
    List<String> getQuizNamesAndIDs() throws RemoteException;

	String generateUniqueQuizID(String name) throws RemoteException;

    void addQuestion(String q) throws RemoteException;

    void addOption(String str) throws RemoteException;

    void setCorrect(char c) throws RemoteException;

    void saveQuiz() throws RemoteException;

    List<String> getTop10() throws RemoteException;

    String getWinner() throws RemoteException;

    List<String> printEntireQuiz() throws RemoteException;

	/**
	* Quiz Player client program.
	* 
	* Searches for pre-existing player name.
	* 
	* @return True if user already exists, false if there is no such user.
	* @param The String of the name to be searched for.
	*/
	boolean searchUser(String name) throws RemoteException;

	/**
	* Quiz Player client program.
	* 
	* A list of Strings is returned from the client, containing the player data to be recorded.
	* 
	* @param The list of Strings:  list(0) username, list(1) name, list(2) email address.
	*/
    void enterPlayerData(List<String> list) throws RemoteException;

    /**
    * Quiz Player client program.
    * 
    * Server searches for ID of quiz to be played provided by the client.
    * 
    * @return true if found, false if quiz with quizID does not exist.
    * @param String of quiz ID to search for
    */ 
    boolean selectQuiz(String quizID) throws RemoteException;

    /**
    * Quiz Player client program.
    * 
    * Server returns the number of questions available for the selected quiz.
    *
    * @return An int for the number of questions.
    */
    int getNumberOfQuestions() throws RemoteException;

    /**
    * Quiz Player client program.
	*
	* Server returns 'question' part of question in use.
	* @param The question number (0 - 9)
	* @return The question as a String.
	*/
    String getQuestionString(int questionNumber) throws RemoteException;

    /**
    * Quiz Player client program.
    * 
    * Server returns a list of Strings containing the question options.
    *
    * @return A List of Strings, in order from option A-E.
    */
    List<String> getOptions() throws RemoteException;

    /**
    * Quiz Player client program.
    *
    * Server receives an array of chars and then calculates a score by comparing to 
    * an array of correct answer chars.
    * 
    * @param An array of characters for the player's answers, in order from the first 
    * to last questions.
    * @return The number of correct answers given.
    */
    int compareAnswers(char[] charArray) throws RemoteException;
}