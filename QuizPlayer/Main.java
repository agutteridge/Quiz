import java.rmi.Remote;
import java.io.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Main implements Serializable {
	public void launch(){
		System.out.println("Let's list all of the quizzes!");
		listQuizzes();
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