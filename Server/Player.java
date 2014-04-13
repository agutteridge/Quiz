import java.io.Serializable;

/**
* Interface for players, a class where player data is described.
* 
* Nicknames are chosen by the player upon registration and are unique on the system.
*/
public class Player implements Serializable {
	private String nick;
	private String realName;
	private String email;

	public Player(){
		//no-argument constructor for serialisation		
	}

	public Player(String n, String name, String e){
		this.nick = n;
		this.realName = name;
		this.email = e;
	}

	public String getNick(){
		return this.nick;
	}

	public void setNick(String newString){
		this.nick = newString;
	}

	public String getName(){
		return this.realName;
	}

	public void setName(String newString){
		this.realName = newString;
	}

	public String getEmail(){
		return this.email;
	}

	public void setEmail(String newString){
		this.email = newString;
	}
}