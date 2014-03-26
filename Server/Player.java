/**
* Interface for players, a class where player data is described.
* 
* Nicknames are chosen by the player upon registration and are unique on the system.
*/
public abstract class Player implements java.io.Serializable {
	private final String nick;
	private List<Score> scoreList; //all scores ever?
	private String password;

	public Player(String n, String p){
		nick = n;
		password = p;
		scoreList = new SortedList<Score>();
	}

	public String getNick(){
		return nick;
	}

	//create SortedList class
	//create Score class
	//create Quiz class (and maybe interface for different types of quiz..)
	//public SortedList<Score> getScore(UUID quizID){
		//server.getQuiz(quizID); //from server class?
		//return getPlayerScores(nick); //need printing method...;
	//}
}