public class Quiz {
	private final String quizName;
	private final String quizID;
	private SortedScoreList scoreList;
	private List<Question> questions;

	public Quiz(String name, String id){
		quizName = name;
		quizID = id;
	    scoreList = new SortedScoreList();
	}

	public String getName(){
		return quizName;
	}

	public String getQuizID(){
		return quizID;
	}
}

//inner class if not required to be serializable?
class Question {
	
}