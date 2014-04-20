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

    /**
    * Constructor creates instances of data structures players (ConcurrentMap) and 
    * quizzes (List).
    */
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

    /**
    * Accessor for serialization.
    * 
    * @return The players (ConcurrentMap) variable.
    */
    public ConcurrentMap<String, Player> getPlayers(){
        return this.players;
    }

    /**
    * Mutator for serialization.
    *
    * @param newMap - A ConcurrentMap to replace the players variable.
    */
    public void setPlayers(ConcurrentMap<String, Player> newMap){
        this.players = newMap;
    }

    /**
    * Accessor for serialization.
    * 
    * @return The quizzes (List) variable.
    */
    public List<Quiz> getQuizzes(){
        return this.quizzes;
    }

    /**
    * Mutator for serialization.
    *
    * @param newList - A List to replace the quizzes variable.
    */
    public void setQuizzes(List<Quiz> newList){
        this.quizzes = newList;
    }

    /**
    * Checks whether files of the expected names are present in the 
    * directory. If they are not found, new empty new .xml files are created.
    *
    * @param f - The File reference.
    * @param filename - The filename.
    */
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

    /**
    * XMLDecoder is used to deserialize the .xml files and convert them into 
    * objects.
    *
    * @param filename - The filename of the .xml file to be read from.
    */ 
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

    /**
    * A quiz name is received from a client program, and a unique quiz ID is 
    * then generated from the first 4 (non-whitespace) characters. An integer is 
    * appended to these characters starting from 0, and this is incremented until 
    * a unique ID is made.
    *
    * @param name - The quiz name.
    * @return The unique quiz ID.
    */  
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
            nameInUse = selectQuiz(quizID);
            i++;
        } while (nameInUse);

        Quiz q = new Quiz(name, quizID);
        quizInUse = q;
        return quizID;
    }

    /**
    * Searches for a Quiz with an identical quiz ID.
    *
    * @param id - The quiz ID to search the quizzes List for.
    * @return True if a quiz already has this ID, false otherwise.
    */
    public boolean selectQuiz(String id){
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

    /**
    * Uses a String to create an instance of a Question object, which is 
    * then added to the quizInUse variable. The questionInUse variable is 
    * set to the newly created Question.
    *
    * @param q - Question as a String to be added a Quiz.
    */
    public void addQuestion(String q){
        Question newQuestion = new Question(q);
        quizInUse.addQuestion(newQuestion);
        questionInUse = newQuestion;
    }

    /**
    * Adds option to the questionInUse variable.
    *
    * @param str - The option as a String to be added to a Question.
    */
    public void addOption(String str){
        questionInUse.addOption(str);
    }

    /**
    * Changes the correct option variable to the user input from the 
    * client side.
    *
    * @param c - The character that the correct option will be changed to.
    */
    public void setCorrect(char c){
        questionInUse.setCorrect(c);
    }

    /**
    * Returns the list of options available from the questionInUse 
    * variable.
    *
    * @return List of options as Strings.
    */
    public List<String> getOptions(){
        return questionInUse.getOptions();
    }

    /** 
    * Returns a formatted list of the entire quizInUse. Each question is 
    * followed by its options, the correct option, and finally a 
    * line space.
    */
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

    /**
    * The quizInUse object is added to the List of quizzes and the flush() 
    * method is called.
    */
    public void saveQuiz(){
        quizzes.add(quizInUse);
        flush();
    }

    /**
    * The top 10 Scores from the quizInUse are returned and formatted to be 
    * printed client side.
    *
    * @return A List of Strings, one for each Score that contains player data:
    * the player's name, registered username, the points scored, the date the 
    * Score was created, and the player's email address.
    */
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

    /**
    * The top Score from the quizInUse is returned and formatted to be 
    * printed client side.
    *
    * @return A String that contains player data: the player's name, registered 
    * username, the points scored, the date the Score was created, and the player's 
    * email address.
    */
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

    /**
    * Formats Calendar objects date for user legibility.
    *
    * @param date - The Calendar object to be formatted.
    * @return The String in a readable format (dd/MM/yyyy HH:mm)
    */
    private String formatDate(Calendar date){
        Date timeAndDate = date.getTime();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return f.format(timeAndDate);
    }

    /**
    * The player username Map Key is used to see if the username is already 
    * in use.
    *
    * @param name - The player's username.
    * @return True if the username is already registered with a player, false 
    * otherwise.
    */
    public boolean searchUser(String name){
        boolean result = players.containsKey(name); 
        
        if (result) {
            playerInUse = players.get(name);
        }

        return result;
    }

    /**
    * Receives a List of Strings from the client, which can be parsed to obtain 
    * parameters to use for instantiation of a Player object. 
    * 
    * The newly-created Player object is then set as the playerInUse variable.
    * 
    * @param list - List of Strings for the username, player name and email, sequentially.
    */ 
    public void enterPlayerData(List<String> list){
        String username = list.get(0);
        String name = list.get(1);
        String email = list.get(2);
        Player p = new Player(username, name, email);

        players.put(username, p);
        playerInUse = p;
        flush();
    }

    /**
    * Formats and returns a List of Strings for reading the list of quizzes available 
    * on the server.
    *
    * @return A List of Strings, one String contains the quiz ID and quiz name.
    */
    public List<String> getQuizNamesAndIDs(){
        List<String> result = new ArrayList<String>(quizzes.size());
        
        for (int i = 0; i < quizzes.size(); i++){
            Quiz q = quizzes.get(i);
            result.add(q.getQuizID() + ": " + q.getName());
        } 

        return result;
    }

    /**
    * Returns the number of questions that the quizInUse Quiz has.
    *
    * @return The number of questions in the Quiz.
    */
    public int getNumberOfQuestions(){
        List<Question> list = quizInUse.getQuestions();
        return list.size();
    }

    /**
    * Sets the questionInUse object to the index of the list of questions in the 
    * quizInUse, and then returns the question variable as a String.
    *
    * @param questionNumber - The index or position of the question in the list of questions for 
    * the quizInUse.
    * @return The question variable of the questionInUse.
    */ 
    public String getQuestionString(int questionNumber){
        this.questionInUse = quizInUse.getQuestions().get(questionNumber);
        int numToPrint = questionNumber + 1;
        return numToPrint + ". " + this.questionInUse.getQuestion();
    }

    /**
    * An array of characters is compared to an array of the correct answers for all 
    * questions of the quizInUse. For every match, the score is incremented by one 
    * and then returned.
    *
    * @param charArray - Array of characters to be marked as correct or incorrect when compared 
    * to the correct variables in each Question object of the quizInUse.
    * @return The total number of matches between the two arrays.
    */ 
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

    /**
    * A new Score object is created, added to the appropriate Quiz object. The 
    * quizzes List is then saved to file.
    *
    * @param num - The points as an integer, to be added as a variable to a Score object.
    */
    private void saveScore(int num){
        String playerNick = playerInUse.getNick();
        String qid = quizInUse.getQuizID();
        Score newScore = new Score(num, playerNick, qid);
        quizInUse.addScore(newScore);
        flush();
    }

    /**
    * Prompts the server to save data to file using XML serialization.
    */
    private void flush() {
        final String QUIZFILE = "." + File.separator + "quizdata.xml";
        final String PLAYERFILE = "." + File.separator + "playerdata.xml";

        encode(QUIZFILE);
        encode(PLAYERFILE);
    }

    /**
    * XML Serialization of both players (ConcurrentMap) and quizzes (List) data 
    * structures. This method is synchronized so only one instance of the 
    * QuizServer class can write to file at one time.
    *
    * @param filename - The filename of the .xml file to write to.
    */
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
}