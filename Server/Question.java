import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

public class Question implements Serializable {
	private String question;
	private List<String> options;
	private char correct;

	public Question(){
		//no-argument constructor for serialisation		
	}

	public Question(String q){
		this.question = q;
		this.options = new ArrayList<String>(2);
	}

	public String getQuestion(){
		return this.question;
	}

	public void setQuestion(String newString){
		this.question = newString;
	}

	public List<String> getOptions(){
		return this.options;
	}

	public void setOptions(List<String> newList){
		this.options = newList;
	}

	public int getCorrect(){
		return this.correct;
	}

	public void setCorrect(char newChar){
		this.correct = newChar;
	}

	public void addOption(String option){
		options.add(option);
	}
}
