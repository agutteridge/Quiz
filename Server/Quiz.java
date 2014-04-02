import java.util.UUID;

public class Quiz {
	private final String quizName;
	private final UUID quizID;
	private SortedScoreList scoreList;


	public Quiz(String name){
		quizName = name;
	    quizID = UUID.randomUUID();
	    scoreList = new SortedScoreList();
	}
}

//inner class if not required to be serializable?
class Question {
	
}