import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

public class Quiz implements Serializable {
	private String quizName;
	private String quizID;
	private List<Score> scoreList;
	private List<Question> questions;
	/*evaluates whether question is finished (to be written) or not.*/
	private boolean pass;

	public Quiz(){
		//no-argument constructor for serialisation
	}

	public Quiz(String name, String id){
		this.quizName = name;
		this.quizID = id;
	    this.scoreList = new SortedScoreList();
	    this.pass = false;
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

	public boolean hasPassed(){
		return this.pass;
	}

	public void setPass(boolean newBool){
		this.pass = newBool;
	}

	public Question addQuestion(String q){
		if (questions == null){
			questions = new ArrayList<Question>();
		} else if (questions.size() > 9){
			return null;
		}

		Question newQuestion = new Question(q);
		questions.add(newQuestion);
		return newQuestion;
	}
}