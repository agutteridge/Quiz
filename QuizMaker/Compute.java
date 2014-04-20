import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Compute extends Remote {
    /**
    * Quiz Player and Maker client programs.
    * 
    * The QuizServer returns a list of Strings containing the question options.
    *
    * @return A List of Strings, in order from option A-E.
    */
    List<String> getOptions() throws RemoteException;

    /**
    * Quiz Player and Maker client programs.
    * 
    * Server searches for ID of quiz to be played provided by the client.
    * 
    * @return true if found, false if quiz with quizID does not exist.
    * @param quizID - String of quiz ID to search for
    */ 
    boolean selectQuiz(String quizID) throws RemoteException;

    /**
    * Quiz Player and Quiz Maker client programs.
    *
    * Prints a list of quizzes that are available to play in the format of
    * quiz ID: quiz name
    * 
    * @return A List of Strings, one String for each Quiz available to play.
    */
    List<String> getQuizNamesAndIDs() throws RemoteException;

    /**
    * Quiz Maker client program.
    * 
    * Sends quiz name String to the QuizServer for creation of a unique ID.
    * 
    * @param name - String to use as a base for ID generation.
    * @return Unique ID as a String.
    */
    String generateUniqueQuizID(String name) throws RemoteException;

    /**
    * Quiz Maker client program.
    *
    * Sends a question as a String to the QuizServer to create an instance of a 
    * Question object.
    * 
    * @param q - String that is passed as a parameter when creating an instance of a 
    * Question object.
    */
    void addQuestion(String q) throws RemoteException;

    /**
    * Quiz Maker client program.
    *
    * Sends option to the QuizServer as a String to the server to add to options 
    * List in the Question object.
    * 
    * @param str - String to add as a Question option.
    */    
    void addOption(String str) throws RemoteException;

    /**
    * Quiz Maker client program.
    * 
    * Sends character to the QuizServer to set as the correct option.
    *
    * @param c - The character that represents the correct option for that Question.
    */
    void setCorrect(char c) throws RemoteException;

    /**
    * Quiz Maker client program.
    *
    * Each question followed by its options and the correct option are returned
    * as Strings in List format.
    *
    * @return A List of Strings that can be printed sequentially.
    */ 
    List<String> printEntireQuiz() throws RemoteException;
    
    /**
    * Quiz Maker client program.
    * 
    * Prompts the QuizServer to add the quiz to the list and save to the XML file.
    */
    void saveQuiz() throws RemoteException;

    /**
    * Quiz Maker client program.
    * 
    * A list of the top 10 high scores is returned from the QuizServer. Each String is 
    * formatted to include the player name, email address and date played. 
    *
    * @return A List of maximum 10 Strings, one for each high score.
    */
    List<String> getTop10() throws RemoteException;

    /**
    * Quiz Maker client program.
    *
    * The top score is returned from the QuizServer. The String includes the player name, 
    * email address and date played.
    */
    String getWinner() throws RemoteException;

    /**
    * Quiz Player client program.
    * 
    * Searches for pre-existing player name.
    * 
    * @return True if user already exists, false if there is no such user.
    * @param name - The String of the name to be searched for.
    */
    boolean searchUser(String name) throws RemoteException;

    /**
    * Quiz Player client program.
    * 
    * A list of Strings is returned from the client, containing the player data to be recorded.
    * 
    * @param list - The list of Strings:  list(0) username, list(1) name, list(2) email address.
    */
    void enterPlayerData(List<String> list) throws RemoteException;

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
    * @param questionNumber - The question number (0 - 9)
    * @return The question as a String.
    */
    String getQuestionString(int questionNumber) throws RemoteException;

    /**
    * Quiz Player client program.
    *
    * Server receives an array of chars and then calculates a score by comparing to 
    * an array of correct answer chars.
    * 
    * @param charArray - An array of characters for the player's answers, in order from the first 
    * to last questions.
    * @return The number of correct answers given.
    */
    int compareAnswers(char[] charArray) throws RemoteException;
}