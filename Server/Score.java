import java.util.UUID;
/**
* A Score object that contains data about the player, the score achieved and 
* the quiz played.
*/
public class Score implements Comparable {
	// private final String playerName;
	private final int points;
	private final int total;
	private final double percentage;
	// private final UUID quizID;

	public Score(int s, int t){
		// playerName = p;
		// quizID = q;

		points = s;
		total = t;
		percentage = percentCalc();

	}

	private double percentCalc(){
		double decimal = (double) points / (double) total;
		double result = decimal * 100;
		return result;
	}

	public int getPoints(){
		return points;
	}

	public double getPercent(){
		return percentage;
	}

	// public UUID getQuizID(){
	// 	return quizID;
	// }

	// public UUID getPlayerID(){
	// 	return playerID;
	// }

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

		double comparePercent = s.getPercent();

		if (percentage == comparePercent){
			return 0;
		} else if (percentage > comparePercent){
			return 1;
		} else {
			return -1;
		}
	}

	public static void main(String[] args) {
		Score a = new Score (5, 20);
		System.out.println(Math.round(a.getPercent()) + "%");
		a = new Score (33, 34);
		System.out.println(Math.round(a.getPercent()) + "%");
		a = new Score (7, 9);
		System.out.println(Math.round(a.getPercent()) + "%");
		a = new Score (1, 1);
		System.out.println(Math.round(a.getPercent()) + "%");
	}
}