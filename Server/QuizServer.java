import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.io.File;

public class QuizServer extends UnicastRemoteObject implements Compute {
    public static ConcurrentMap<String, Player> players;
    public static List<Quiz> quizzes;
    private Quiz quizInUse;
    private Quiz.Question questionInUse;
    // using member fields to store reference to Quiz (QuizMaker) and Player (QuizPlayer)? objects

    public QuizServer() throws RemoteException {
        players = new ConcurrentHashMap<String, Player>());
        quizzes = Collections.synchronizedList(new ArrayList<Quiz>());

        final String QUIZFILE = "." + File.separator + "quizdata.xml";
        final String PLAYERFILE = "." + File.separator + "playerdata.xml";        

        File f = new File(QUIZFILE);
        doesExist(f, QUIZFILE);
        f = new File(PLAYERFILE);
        doesExist(f, PLAYERFILE);
    }

    public Map<Player> getPlayers(){
        return this.players;
    }

    public void setPlayers(Map<Player> newMap){
        this.players = newMap;
    }

    public List<Quiz> getQuizzes(){
        return this.quizzes;
    }

    public void setQuizzes(List<Quiz> newList){
        this.quizzes = newList;
    }

    private void doesExist(File f, String filename){
        if (!f.exists()){ 
            try {
                f.createNewFile();
                System.out.println("Creating new file.");
            } catch (IOException e){
                System.out.println("Could not create " + f.getName());
                e.printStackTrace();
            }
        } else {
            System.out.println("Loading...");
            copyOver(filename);
            System.out.println(filename + " loaded.");
        } 
    }

    private void copyOver(String filename){
        Scanner sc = null;
        try {
            sc = new Scanner(
                    new BufferedInputStream(
                            new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sc.close();

        XMLDecoder d = null;
        try {
            d = new XMLDecoder(
                    new BufferedInputStream(
                            new FileInputStream(s)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (filename.equals(QUIZFILE)){
            quizzes = (List<Quiz>) d.readObject();
        } else if (filename.equals(PLAYERFILE)){
            players = (Map<Player>) d.readObject();
        } else {
            throw new FileNotFoundException();
        }

        d.close();        
    }

    public String listQuizzes(){
        String list = "";
        Iterator<Quiz> iterator = quizzes.iterator();
        for (Quiz q : quizzes){
            list += q.getName() + "\r\n";
        } 

        return list;
    }

    public String listAnswers(){
        return questionInUse.getOptions();
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
        quizInUse = q;
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

    public void addQuestion(String q){
        Quiz.Question newQuestion = quizInUse.addQuestion(q);
        questionInUse = newQuestion;
    }

    public void addOption(String str){
        questionInUse.addOption(str);
    }

    public void setCorrect(int num){
        questionInUse.setCorrect(num);
    }

    // private Player searchUser(String name){

    // }

    public void flush(){
        final String QUIZFILE = "." + File.separator + "quizdata.xml";
        final String PLAYERFILE = "." + File.separator + "playerdata.xml";

        encode(QUIZFILE);
        encode(PLAYERFILE);
    }

    private void encode(String filename){
        XMLEncoder encode = null;
        try {
            encode = new XMLEncoder(
                        new BufferedOutputStream(
                            new FileOutputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (filename.equals(QUIZFILE)){
            encode.writeObject(quizzes);
        } else if (filename.equals(PLAYERFILE)){
            encode.writeObject(players);
        } else {
            throw new FileNotFoundException();
        }

        encode.close();
    }

    //restrict quiz name to #chars, enable sorting by different params?

}