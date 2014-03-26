import java.security.MessageDigest;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;

public class Register implements Task, Serializable {
	public void execute(){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/date");
			TaskReceiver rs = (DateService) service;
			String date = ds.date();
			System.out.println(date);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	

		Scanner in = new Scanner(System.in);
		do {
			System.out.print("Please enter a nickname to register: ");
			String str = in.nextLine();
			System.out.print("Is " + str + " okay? (Y/N)");
			String ans = in.nextChar();			
			boolean ansBool = false;
			if (ans == 'Y'){

			}
		} while (!ansBool);

		System.out.println("Please type in a password to haaaaash!");
		str = in.nextLine();

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(pw.getBytes());
			String encryptedString = new String(messageDigest.digest());
			System.out.println(encryptedString);
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}

		Player p = new Player()
	}

	public static void main(String[] args) {
		Register r = new Register();
		r.execute();
	}
}