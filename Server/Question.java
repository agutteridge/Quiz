import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

public class Question implements Serializable {
	private String question;
	private List<String> options;
	private int correct;
	/*evaluates whether question is finished (to be written) or not.*/
	private boolean pass; 

	public Question(){
		//no-argument constructor for serialisation		
	}

	public Question(String q){
		this.question = q;
		this.options = new ArrayList<String>(2);
		this.pass = false;
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

	public void setCorrect(int newInt){
		this.correct = newInt;	
	}

	public boolean hasPassed(){
		return pass;
	}

	public void setPassed(boolean newBool){
		this.pass = newBool;
	}

	public boolean addOption(String option){
		if (options.size() > 4){
			return false;
		} else {
			options.add(option);
			return true;
		}
	}

	public String getOptions(int num){
		return options.get(num);
	}
}
