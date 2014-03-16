import java.security.MessageDigest;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;

public class Register {
	public void launch(){
		Scanner in = new Scanner(System.in);
		System.out.println("Please type in a password to haaaaash!");
		String pw = in.nextLine();

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(pw.getBytes());
			String encryptedString = new String(messageDigest.digest());
			System.out.println(encryptedString);
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Register r = new Register();
		r.launch();
	}
}