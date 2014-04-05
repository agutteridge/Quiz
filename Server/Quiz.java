import java.util.List;

public class Quiz extends Serializable {
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

	public boolean addQuestion(String q){
		if (questions == null){
			questions = new ArrayList<Question>();
		} else if (questions.size() > 9){
			return false;
		}

		Question newQuestion = new Question(q);


	}
}

//inner class if not required to be serializable?
class Question extends Serializable {
	private String question;
	private String[] options;
	private int correct answer;
	private boolean pass = false;

	public Question(String q){
		question = q;
	}

	public boolean addOption(String option){
		int 
		option[]
	}

	public void listOptions

}