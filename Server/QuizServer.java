import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class QuizServer extends UnicastRemoteObject implements Compute {
    public static Set<Player> players;
    public static List<Quiz> quizzes;
    // use member fields to store reference to Quiz (QuizMaker) and Player (QuizPlayer) objects?

    public void listQuizzes(){
        System.out.println("RMI is happenin'");
    }

    public void enterName(String name){
        // if (searchUser(name)) {

        // }
        System.out.println("Searching for player \'" + name + "\'");
    }

    public String generateUniqueQuizID(String name){
        String first4chars = name.substring(0,4);
        boolean nameInUse = true;
        int num = 0;
        String quizID = null;

        do {
            quizID = first4chars + num;
            nameInUse = sameName(quizID);
            num++;
        } while (nameInUse);

        Quiz q = new Quiz(name, quizID);
        quizzes.add(q);
        return quizID;
    }

    private boolean sameName(String id){
        Iterator<Quiz> iterator = quizzes.iterator();
        
        while (iterator.hasNext()){
            Quiz q = iterator.next();
            String qID = q.getQuizID();
            if (id.equals(qID)){
                return true;
            }
        }
        return false;
    }

    // private Player searchUser(String name){

    // }

    //restrict quiz name to #chars, enable sorting by different params?

    public QuizServer() throws RemoteException {
        players = new HashSet<Player>();
        quizzes = new ArrayList<Quiz>();
    }
}