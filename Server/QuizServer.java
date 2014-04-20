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
import java.text.SimpleDateFormat;

public class QuizServer extends UnicastRemoteObject implements Compute {
    public static ConcurrentMap<String, Player> players;
    public static List<Quiz> quizzes;
    private Quiz quizInUse;
    private Question questionInUse;
    private Player playerInUse;

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

    public String generateUniqueQuizID(String name){
        String upName = name.toUpperCase();
        String first4chars = "";
        int i = 0;

        for (char c : upName.toCharArray()){
            if (i < 4){
                if (c != ' '){
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
        return questionInUse.getOptions();
    }

    public List<String> printEntireQuiz(){
        List<String> result = new ArrayList<String>();
        List<Question> listOfQuestions = quizInUse.getQuestions();

        for (int i = 0; i < listOfQuestions.size(); i++){
            int qNum = i+1;
            Question q = listOfQuestions.get(i);
            String qString = qNum + ". " + q.getQuestion();
            result.add(qString);
            result.addAll(q.getOptions());
            result.add("Correct answer: " + q.getCorrect() + "\r\n");
        }

        return result;
    }

    public void saveQuiz(){
        quizzes.add(quizInUse);
        flush();
    }

    public List<String> getTop10(){
        List<Score> scoreList = quizInUse.getScores();
        int numberOfScores = scoreList.size(); 
        List<String> result = new ArrayList<String>();

        if (scoreList.isEmpty()){
            result.add("No scores available.");
        } else {
            for (int i = 0; i < 10 && i < numberOfScores; i++){
                Score s = scoreList.get(i);
                int points = s.getPoints();
                String playerNick = s.getNick();
                Calendar datePlayed = s.getDatePlayed();
                
                Player p = players.get(playerNick);
                String playerName = p.getName();
                String email = p.getEmail();

                String str = playerName + "(\"" + playerNick + "\")" + ": " + points + 
                    " points, played " + formatDate(datePlayed) + ", " + email; 
                result.add(str);
            }
        }
        return result;
    }

    public String getWinner(){
        List<Score> scoreList = quizInUse.getScores();
        String result = "";

        if (scoreList.isEmpty()){
            return "No scores available.";
        } else {
            Score s = scoreList.get(0);
            int points = s.getPoints();
            String playerNick = s.getNick();
            Calendar datePlayed = s.getDatePlayed();
            
            Player p = players.get(playerNick);
            String playerName = p.getName();
            String email = p.getEmail();

            result = playerName + "(\"" + playerNick + "\")" + ": " + points + 
                " points, played " + formatDate(datePlayed) + ", " + email; 
        }

        return result;
    }

    private String formatDate(Calendar date){
        Date timeAndDate = date.getTime();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return f.format(timeAndDate);
    }

    public boolean searchUser(String name){
        boolean result = players.containsKey(name); 
        
        if (result) {
            playerInUse = players.get(name);
        }

        return result;
    }

    public void enterPlayerData(List<String> list){
        String username = list.get(0);
        String name = list.get(1);
        String email = list.get(2);
        Player p = new Player(username, name, email);

        players.put(username, p);
        playerInUse = p;
        flush();
    }

    public List<String> getQuizNamesAndIDs(){
        List<String> result = new ArrayList<String>(quizzes.size());
        
        for (int i = 0; i < quizzes.size(); i++){
            Quiz q = quizzes.get(i);
            result.add(q.getQuizID() + ": " + q.getName());
        } 

        return result;
    }

    public boolean selectQuiz(String quizID){
       Iterator<Quiz> iterator = quizzes.iterator();
        
        while (iterator.hasNext()){
            Quiz q = iterator.next();
            String qID = q.getQuizID();
            if (quizID.equals(qID)){
                quizInUse = q;
                return true;
            }
        }
        return false;
    }

    public int getNumberOfQuestions(){
        List<Question> list = quizInUse.getQuestions();
        return list.size();
    }

    public String getQuestionString(int questionNumber){
        this.questionInUse = quizInUse.getQuestions().get(questionNumber);
        int numToPrint = questionNumber + 1;
        return numToPrint + ". " + this.questionInUse.getQuestion();
    }

    public int compareAnswers(char[] charArray){
        char[] correctArray = quizInUse.getCorrectArray();
        int score = 0;
        for (int i = 0; i < getNumberOfQuestions(); i++) {
            if (correctArray[i] == charArray[i]){
                score++;
            }
        }
        saveScore(score);
        return score;
    }

    private void saveScore(int num){
        String playerNick = playerInUse.getNick();
        String qid = quizInUse.getQuizID();
        Score newScore = new Score(num, playerNick, qid);
        quizInUse.addScore(newScore);
        flush();
    }

    private void flush() {
        final String QUIZFILE = "." + File.separator + "quizdata.xml";
        final String PLAYERFILE = "." + File.separator + "playerdata.xml";

        encode(QUIZFILE);
        encode(PLAYERFILE);
    }

    private synchronized void encode(String filename){
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
            List<Quiz> listToWrite = new ArrayList<Quiz>();
            listToWrite.addAll(quizzes);
            System.out.println("Writing quizzes");
            encode.writeObject(listToWrite);
        } else if (filename.equals("." + File.separator + "playerdata.xml")){
            ConcurrentMap<String, Player> mapToWrite = players;
            System.out.println("Writing players");
            encode.writeObject(mapToWrite);
        }
        System.out.println("DONE!");

        encode.close();
    }

    public static void main(String[] args) {
        try {
            QuizServer qs = new QuizServer();
            Quiz q = new Quiz("quizname", "YAYQ0");
            qs.quizzes.add(q);
            qs.flush();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }
}