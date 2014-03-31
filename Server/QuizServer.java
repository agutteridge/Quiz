import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class QuizServer extends UnicastRemoteObject implements Compute{
    public static SortedScoreList<Score> scoreList;
    public static HashMap<Player> players; //hashmap?
    public static HashMap<Quiz> quizzes; //hashmap?

    public void listQuizzes(){
        Iterator
    }



    //restrict quiz name to #chars, enable sorting by different params?

    public QuizServer() throws RemoteException {
        scoreList = new SortedScoreList<Score>();
        players = new 
    }


}