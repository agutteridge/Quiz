import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

public class Quiz implements Serializable {
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
	    this.scoreList = new SortedScoreList();
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
}