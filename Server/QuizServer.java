import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class QuizServer extends UnicastRemoteObject implements Compute {
    // public static HashMap<Player> players; //hashmap?
    // public static HashMap<Quiz> quizzes; //hashmap?

    public void listQuizzes(){
        System.out.println("RMI is happenin'");
    }

    public void enterName(String name){

    }

    private boolean searchUser(String name){

    }

    //restrict quiz name to #chars, enable sorting by different params?

    public QuizServer() throws RemoteException {
        scoreList = new SortedScoreList();
    }
}