import java.util.List;

public class Quiz extends Serializable {
	private final String quizName;
	private final String quizID;
	private SortedScoreList scoreList;
	private List<Question> questions;
	private boolean pass = false; //evaluates whether quiz is //finished (to be written) or not.

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
	private List<String> options;
	private int correct;
	private boolean pass = false; //evaluates whether question is //finished (to be written) or not.

	public Question(String q){
		question = q;
		options = new ArrayList<String>(2);
	}

	public boolean addOption(String option){
		if (options.size() > 4){
			return false;
		} else {
			options.add(option);
			return true;
		}
	}

	public boolean setCorrect(int answer){
		if (answer < options.size()){
			correct = answer;
			if (options.size() > 1){
				pass = true;
			}
		} 
	}

	public boolean hasPassed(){
		return pass;
	}

	public String getOption(int num){
		return options.get(num);
	}

}