import java.util.UUID;
/**
* A Score object that contains data about the player, the score achieved and 
* the quiz played.
*/
public class Score implements Comparable {
	// private final UUID player;
	private final int points;
	// private final UUID quizID;

	public Score(int s){
		// player = p;
		points = s;
		// quizID = q;
	}

	public int getPoints(){
		return points;
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

		Score s;
		if (o instanceof Score){
			s = (Score) o; 
		} else {
			throw new ClassCastException();
		}

		int comparePoints = s.getPoints();

		if (points == comparePoints){
			return 0;
		} else if (points > comparePoints){
			return 1;
		} else {
			return -1;
		}
	}
}