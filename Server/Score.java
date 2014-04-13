import java.util.Calendar;
/**
* A Score object that contains data about the player, the score achieved and 
* the quiz played.
*/
public class Score implements Comparable {
	private String playerName;
	private int points;
	private String quizID;
	private Calendar datePlayed;

	public Score(){
		//no-argument constructor for serialisation		
	}

	public Score(int s, String p, String q){
		this.playerName = p;
		this.points = s;
		this.quizID = q;
		datePlayed = Calendar.getInstance();
	}

	public String getName(){
		return this.playerName;
	}

	public void setName(String newString){
		this.playerName = newString;
	}

	public int getPoints(){
		return this.points;
	}

	public void setPoints(int newInt){
		this.points = newInt;
	}

	public int getTotal(){
		return this.total;
	}

	public void setTotal(int newInt){
		this.total = newInt;
	}

	public String getQuizID(){
		return quizID;
	}

	public void setQuizID(String newString){
		this.quizID = newString;
	}

	public Calendar getDatePlayed(){
		return this.datePlayed;
	}

	public void setDatePlayed(Calendar newCal){
		this.datePlayed = newCal;
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

		double comparePercent = s.getPercentage();

		if (percentage == comparePercent){
			return 0;
		} else if (percentage > comparePercent){
			return 1;
		} else {
			return -1;
		}
	}
}