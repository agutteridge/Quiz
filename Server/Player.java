import java.util.UUID;

/**
* Interface for players, a class where player data is described.
* 
* Nicknames are chosen by the player upon registration and are unique on the system.
*/
public abstract class Player implements java.io.Serializable {
	private final String nick;

	public Player(String n){
		nick = n;
	}

	public String getNick(){
		return nick;
	}

	// Move class?
	public void addScore(Score newScore){
		UUID quiz = newScore.getQuizID();
		if (getScore(quiz) == null){
			scoreList.add(newScore);
		} else {

		}
	}

	//Move to server class
	// //create Quiz class (and maybe interface for different types of quiz..)
	// public Score getScore(UUID quizID){
	// 	Score result = null;

	// 	for (Score s : scoreList){
	// 		if (s.getQuizID.equals(quizID)){
	// 			s;
	// 		}
	// 	}
	// 	Quiz q = server.getQuiz(quizID); //from server class?
	// 	List<Score> = q.getPlayerScores(nick);
	// 	return ; //need printing method...;
	// }
}