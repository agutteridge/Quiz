import java.util.List;
import java.util.ArrayList;

public class Quiz {
	private String quizName;
	private String quizID;
	private List<Score> scoreList;
	private List<Question> questions;

	public Quiz(){
		//no-argument constructor for serialisation
	}

	public Quiz(String name, String id){
		this.quizName = name;
		this.quizID = id;
	    this.scoreList = new ArrayList<Score>();
	}

	public String getName(){
		return this.quizName;
	}

	public void setName(String name){
		this.quizName = name;
	}

	public String getQuizID(){
		return this.quizID;
	}

	public void setQuizID(String qID){
		this.quizID = qID;
	}

	public List<Score> getScores(){
		return this.scoreList;
	}

	public void setScores(List<Score> newList){
		this.scoreList = newList;
	}

	public List<Question> getQuestions(){
		return this.questions;
	}

	public void setQuestions(List<Question> newList){
		this.questions = newList;
	}

	public void addQuestion(Question q){
		if (questions == null){
			questions = new ArrayList<Question>();
		} 

		questions.add(q);
	}

	public char[] getCorrectArray(){
		int numberOfQuestions = this.questions.size();
		char[] result = new char[numberOfQuestions];

		for (int i = 0; i < numberOfQuestions; i++){
			Question q = this.questions.get(i);
			result[i] = q.getCorrect();
		}

		return result;
	}

	public void addScore(Score newScore){
		boolean inserted = false;

		if (scoreList.size() == 0){
			scoreList.add(newScore);
			inserted = true;
		} else {
			for (int i = 0; i < scoreList.size(); i++) {
				Score nextScore = scoreList.get(i);
				int comparison = newScore.compareTo(nextScore);
				if (comparison > 0){
					scoreList.add(i, newScore);
					i = scoreList.size() + 1;
					inserted = true;
				} 
			}

			if (!inserted){
				scoreList.add(newScore);
				inserted = true;
			}
		}
	}
}