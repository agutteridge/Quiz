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
import java.util.concurrent.*;
import java.io.File;

public class QuizServer extends UnicastRemoteObject implements Compute {
    public static ConcurrentMap<String, Player> players;
    public static List<Quiz> quizzes;
    private Quiz quizInUse;
    private Question questionInUse;
    // using member fields to store reference to Quiz (QuizMaker) and Player (QuizPlayer)? objects

    public QuizServer() throws RemoteException {
        players = new ConcurrentHashMap<String, Player>();
        quizzes = Collections.synchronizedList(new ArrayList<Quiz>());

        final String QUIZFILE = "." + File.separator + "quizdata.xml";
        final String PLAYERFILE = "." + File.separator + "playerdata.xml";        

        File f = new File(QUIZFILE);
        doesExist(f, QUIZFILE);
        f = new File(PLAYERFILE);
        doesExist(f, PLAYERFILE);
    }

    public ConcurrentMap<String, Player> getPlayers(){
        return this.players;
    }

    public void setPlayers(ConcurrentMap<String, Player> newMap){
        this.players = newMap;
    }

    public List<Quiz> getQuizzes(){
        return this.quizzes;
    }

    public void setQuizzes(List<Quiz> newList){
        this.quizzes = newList;
    }

    public Quiz getQuizInUse(){
        return this.quizInUse;
    }

    public void setQuizInUse(Quiz newQuiz){
        this.quizInUse = newQuiz;
    }

    public Question getQuestionInUse(){
        return this.questionInUse;
    }

    public void setQuestionInUse(Question newQuestion){
        this.questionInUse = newQuestion;
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
            if (f.length() != 0){
                System.out.println("Loading...");
                copyOver(filename);
                System.out.println(filename + " loaded.");
            }
        } 
    }

    private void copyOver(String filename) {
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
                            new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (filename.equals("." + File.separator + "quizdata.xml")){
            quizzes = (List<Quiz>) d.readObject();
        } else if (filename.equals("." + File.separator + "playerdata.xml")){
            players = (ConcurrentMap<String, Player>) d.readObject();
        }

        d.close();        
    }

    public List<String> getQuizNames(){
        List<String> result = new ArrayList<String>(quizzes.size());
        
        Iterator<Quiz> iterator = quizzes.iterator();
        for (Quiz q : quizzes){
            result.add(q.getName());
        } 

        return result;
    }

    public String generateUniqueQuizID(String name){
        name = name.toUpperCase();
        String first4chars = "";
        int i = 0;

        for (char c : name.toCharArray()){
            if (i < 4){
                if (c != ' '){
                    System.out.println(c + " added to array position " + i);
                    i++;
                    first4chars += c;
                }
            }
        }

        i = 0;
        boolean nameInUse = true;
        String quizID = null;

        do {
            quizID = first4chars + i;
            nameInUse = sameName(quizID);
            i++;
        } while (nameInUse);

        Quiz q = new Quiz(name, quizID);
        quizInUse = q;
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
        Question newQuestion = new Question(q);
        quizInUse.addQuestion(newQuestion);
        questionInUse = newQuestion;
    }

    public void addOption(String str){
        questionInUse.addOption(str);
    }

    public void setCorrect(char c){
        questionInUse.setCorrect(c);
    }

    public List<String> getOptions(){
        List<String> result;
        result = questionInUse.getOptions();
        return result;
    }

    public List<String> printEntireQuiz(){
        List<String> result = new ArrayList<String>();
        List<Question> listOfQuestions = quizInUse.getQuestions();

        for (int i = 0; i < listOfQuestions.size(); i++){
            int qNum = i+1;
            Question q = listOfQuestions.get(i);
            String qString = qNum + ". " + q.getQuestion();
            result.add(qString);
            System.out.println("added " + qString);
            result.addAll(q.getOptions());
            result.add("Correct answer: " + q.getCorrect());
        }

        return result;
    }

    public boolean searchUser(String name){
        return players.containsKey(name);
    }    

    public void flush() {
        if (quizInUse != null){
            quizzes.add(quizInUse);
        }
        final String QUIZFILE = "." + File.separator + "quizdata.xml";
        final String PLAYERFILE = "." + File.separator + "playerdata.xml";

        encode(QUIZFILE);
        encode(PLAYERFILE);
    }

    private void encode(String filename){
        System.out.println("SERIALIZIN' " + filename);
        XMLEncoder encode = null;
        try {
            encode = new XMLEncoder(
                        new BufferedOutputStream(
                            new FileOutputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (filename.equals("." + File.separator + "quizdata.xml")){
            List<Quiz> listToWrite = new ArrayList<Quiz>(quizzes.size());
            for (Quiz q : quizzes) {
                listToWrite.add(q);
            }
            System.out.println("Writing quizzes");
            encode.writeObject(listToWrite);
        } else if (filename.equals("." + File.separator + "playerdata.xml")){
            System.out.println("Writing players");
            encode.writeObject(players);
        }
        System.out.println("DONE!");

        encode.close();
    }
    //restrict quiz name to #chars, enable sorting by different params?

}