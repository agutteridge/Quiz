import java.util.UUID;
import java.util.Calendar;
import java.io.Serializable;

/**
* Interface for players, a class where player data is described.
* 
* Nicknames are chosen by the player upon registration and are unique on the system.
*/
public abstract class Player implements Serializable {
	private String nick;
	private Calendar dob;
	private String email;

	public Player(String n, String stringDate, String e){
		this.nick = n;
		this.email = e;
		parseDOB(stringDate);
	}

	public String getNick(){
		return this.nick;
	}

	public void setNick(String newString){
		this.nick = newString;
	}

	public Calendar getDOB(){
		return this.dob;
	}

	public void setDOB(Calendar newCalendar){
		this.dob = newCalendar;
	}

	public String getEmail(){
		return this.email;
	}

	public void setEmail(String newString){
		this.email = newString;
	}

	public void parseDOB(String str){
		Calendar result = Calendar.getInstance();
		int day = Integer.parseInt(str.substring(0,2));
		int month = Integer.parseInt(str.substring(3,5));
		int year = Integer.parseInt(str.substring(6,10));

		result.set(year, month, day);
		setDOB(result);
	}
}