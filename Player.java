/**
* Interface for players, a class where player data is described.
* 
* Nicknames are chosen by the player upon registration and are unique on the system.
*/
public abstract class Player implements java.io.Serializable {
	private final String nick;

	public Player(String inputNick){
		nick = inputNick;
	}

	public String getNick(){
		return nick;
	}

	//create SortedList class
	//create Score class
	//create Quiz class (and maybe interface for different types of quiz..)
	//public SortedList<Score> getScore(GUID quizID){
		//server.getQuiz(quizID); //from server class?
		//return getPlayerScores(nick); //need printing method...;
	//}
}