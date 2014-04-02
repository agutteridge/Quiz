import java.rmi.Remote;
import java.io.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Main implements Serializable {
	public void launch(){
		System.out.println("Let's list all of the quizzes!");
		register();
		listQuizzes();
	}

	private void enterName(){
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter a username:");
        boolean alphanum = false;
        String str;
	        
        while (!alphanum) {
            str = in.nextLine();
            char[] charArray = str.toCharArray();
        
            alphanum = true;
            for (char c : charArray) {
                if (!Character.isLetterOrDigit(c)){
                    alphanum = false;
                }
            }

            if(!alphanum){
                System.out.println("Sorry, only alphanumeric characters can be used. Please enter a username:");
            }
        }

		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.enterName(str);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}		
	}

	private void listQuizzes(){
		try {
			Remote service = Naming.lookup("//127.0.0.1:1099/quiz");
			Compute compute = (Compute) service;
			compute.listQuizzes();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.launch();
	}
}