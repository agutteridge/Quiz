import java.util.UUID;
/**
* A Score object that contains data about the player, the score achieved and 
* the quiz played.
*/
public class Score extends Comparable {
	private final Player player;
	private final int scoreNum;
	private final UUID quizID;

	public Score(Player p, int s, UUID q){
		player = p;
		scoreNum = s;
		quizID = q;
	}

	public int getScore(){
		return scoreNum;
	}

	/**
	* Compares this Score with the specified object for order. Only the scoreNum 
	* variable of the score objects are compared. Returns a negative integer, 
	* zero, or a positive integer if this object is less than, equal to, or 
	* greater than the specified object. Scores are only comparable to other 
	* Score objects.  
	* @param o - the score to be compared.
	* @throws ClassCastException if o's type is not Score.
	* @return a negative integer, zero, or a positive integer as this object 
	* is less than, equal to, or greater than the specified object.
	*/ 
	@Override
	public int compareTo(Object o){
		if (o == null){
			throw new NullPointerException();
		} 

		if (o instanceof Score){
			Score s = (Score) o; 
		} else {
			throw new ClassCastException();
		}

		int compareScore = s.getScore();

		if (scoreNum == compareScore){
			return 0;
		} else if (scoreNum > compareScore){
			return 1;
		} else {
			return -1;
		}
	}
}