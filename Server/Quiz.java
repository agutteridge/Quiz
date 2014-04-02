public class Quiz {
	private final String quizName;
	private final String quizID;
	private SortedScoreList scoreList;

	public Quiz(String name, String id){
		quizName = name;
		quizID = id;
	    scoreList = new SortedScoreList();
	}
}

//inner class if not required to be serializable?
class Question {
	
}