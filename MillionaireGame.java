import enigma.core.Enigma;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class


public class MillionaireGame {
    public static enigma.console.Console cn = Enigma.getConsole("Millionaire Game");


    // ------ Standard variables for mouse and keyboard ------
    public static String[] dictionaryArray = new String[400000];
    public static String[] stopWordsArray = new String[1000];
    public KeyListener klis;
    public static int keypr;
    public static int rkey;
    static String[][] categoriesWithAnsweredCorrectlyBadly = new String[20][3];
    static String[][] participantCitySort = new String[35][2];
    static String[] mostSuccessfulContestant = new String[2];
    static int categoriesWithAnsweredCorrectlyBadlyCounter = 0;
    static int participantCitySortCounter = 0;
    static int SumOfunderAge30AverageCorrectAnswers = 0;
    static int SumOfbetweenAge30and50AverageCorrectAnswers = 0;
    static int SumOfaboveAge50AverageCorrectAnswers = 0;
    static int underAge30AverageCorrectAnswersCounter = 0;
    static int betweenAge30and50AverageCorrectAnswersCounter = 0;
    static int aboveAge50AverageCorrectAnswersCounter = 0;
    static int averageUnder30 = 0;
    static int avBetweenAge30And50 = 0;
    static int avAbove50 = 0;





    MillionaireGame() throws Exception {   // --- Contructor
//        createTheDictionary("dictionary.txt");

        // ------ Standard code for mouse and keyboard ------ Do not change
        klis=new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if(keypr==0) {
                    keypr=1;
                    rkey=e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {}
        };
        cn.getTextWindow().addKeyListener(klis);


        // ----------------------------------------------------
        int px = 5, py = 5;
        cn.getTextWindow().setCursorPosition(6,6);
        Scanner scanner = new Scanner(System.in);
        Question[] questions = new Question[50];
        Participant[] participants = new Participant[50];
        while(true) {
            cn.getTextWindow().output("***** Menu *****.\n");
            cn.getTextWindow().output("1.Load questions\n");
            cn.getTextWindow().output(("2.Load participants\n"));
            cn.getTextWindow().output("3.Start competition\n");
            cn.getTextWindow().output("4.Show statistics\n");
            String nextText = scanner.next();
            if (nextText.equals("E"))
                break;
            else if (nextText.equals("1")) {
                System.out.println("Enter the name of file to load : ");
                String fileName = scanner.next();
                    questions = loadQuestions("src/"+fileName);
                    printingLoadingQuestionCategories(questions);
                    printingLoadingQuestionDifficulties(questions);
                }
            else if (nextText.equals("2")){
                System.out.println("Enter the name of file to load : ");
                String fileName = scanner.next();
                participants = loadParticipants("src/"+fileName);
                System.out.println("The file " + fileName + " is loaded.");

            }

            else if (nextText.equals("3")){
                contest(questions,participants);
            }
            else if (nextText.equals("4")){
                showStatistics();
            }

            }
        }





    public static void createTheDictionary(String fileName) throws FileNotFoundException {
        File dictionary = new File(fileName);
        Scanner scanner = new Scanner(dictionary);
        int i = 0;
        while (scanner.hasNextLine()){
            String word = scanner.nextLine();
            dictionaryArray[i++] = word;
        }
    }
    public static void createTheStopWordArray(String fileName) throws FileNotFoundException {
        File stopWords = new File(fileName);
        Scanner scanner = new Scanner(stopWords);
        int i = 0;
        while (scanner.hasNextLine()){
            String word = scanner.nextLine();
            stopWordsArray[i++] = word;
        }
    }

    public String spellCheck(String question) {
        int falseChecker;
        String[] questionWords = question.split(" ");
        boolean done = false;
        for (int j = 0; j < questionWords.length; j++) {
            for (String aWord : dictionaryArray) {
                if (aWord == null)
                    continue;
                falseChecker = 0;
                if (questionWords[j].length() == aWord.length()) {
                    for (int i = 0; i < questionWords[j].length(); i++) {
                        if (questionWords[j].charAt(i) != aWord.charAt(i))
                            falseChecker += 1;
                    }
                    if (falseChecker == 1 && aWord.length()>=6) {
                        questionWords[j] = aWord;
                        break;
                    }
                    else if (falseChecker == 2 && aWord.length()>=6 && areAnagram(questionWords[j],aWord)){
                        questionWords[j] = aWord;
                        break;
                    }
                }
            }
        }
        String lastQuestion = String.join(" ",questionWords);
        return lastQuestion;
    }

    static boolean areAnagram(String str1, String str2)
    {
        // Get lengths of both strings
        int n1 = str1.length();
        int n2 = str2.length();

        // If length of both strings is not same,
        // then they cannot be anagram
        if (n1 != n2)
            return false;
        char[] str1toCharArray= str1.toCharArray();
        char[] str2toCharArray= str2.toCharArray();
        Arrays.sort(str1toCharArray);
        Arrays.sort(str2toCharArray);

        // Compare sorted strings
        for (int i = 0; i < n1; i++)
            if (str1toCharArray[i] != str2toCharArray[i])
                return false;

        return true;
    }

    public Question[] loadQuestions(String fileName) throws FileNotFoundException {
        createTheStopWordArray("src/stop_words.txt");
        Question[] questions = new Question[200];
        File questionFile = new File(fileName);
        Scanner scanner = new Scanner(questionFile);
        Random rand = new Random();
        int i = 0;
        while (scanner.hasNextLine()){
            String questionLine = scanner.nextLine();
            String[] questionSplit = questionLine.strip().split("#");
            String[] possibleWordsForCloud = new String[5];
            int possibleWordsForCloudCounter = 0;
            String wordForCloud = null;
            String questionTextRaw = questionSplit[1].replaceAll("\\p{Punct}", "");
            String[] questionText = questionTextRaw.strip().split(" ");

            for(int index = 0; index < questionText.length;index++){
                    if (!Arrays.asList(stopWordsArray).contains(questionText[index].toLowerCase()) && questionText[index].matches("[a-zA-Z]+") &&
                            possibleWordsForCloudCounter < 5){
                        possibleWordsForCloud[possibleWordsForCloudCounter] = questionText[index];
                        possibleWordsForCloudCounter++;
                    }

            }
            if (possibleWordsForCloudCounter == 0){
                wordForCloud = questionSplit[0].toLowerCase();
            }
            else {
                int randomWordCloudIndex = rand.nextInt(possibleWordsForCloudCounter);
                wordForCloud = possibleWordsForCloud[randomWordCloudIndex];
            }
            char correctAnswer = questionSplit[6].charAt(0);
            Question aQuestion = new Question(questionSplit[0],questionSplit[1],questionSplit[2],questionSplit[3],
                    questionSplit[4],questionSplit[5],correctAnswer,questionSplit[7],wordForCloud.toLowerCase());
            questions[i++] = aQuestion;

        }
        return questions;
    }


    public Participant[] loadParticipants(String fileName) throws FileNotFoundException {
        Participant[] participants = new Participant[35];
        File participantFile = new File(fileName);
        Scanner scanner = new Scanner(participantFile);
        int i = 0;
        while (scanner.hasNextLine()){
            String participantLine = scanner.nextLine();
            String[] participantSplit = participantLine.strip().split("#");
            String[] addressSplit = participantSplit[3].strip().split(";");
            String[] birthDateSplit = participantSplit[1].split("\\.");
            Date participantBirthDate = new Date(Integer.parseInt(birthDateSplit[0]),Integer.parseInt(birthDateSplit[1]),Integer.parseInt(birthDateSplit[2]));
            Adress participantAdress = new Adress(addressSplit[0],addressSplit[1],addressSplit[2],addressSplit[3],addressSplit[4]);
            Participant participant = new Participant(participantSplit[0],participantBirthDate.calculateAge(),participantSplit[2],
                    participantAdress);
            participants[i++] = participant;
        }
        return participants;

    }

    public static void printingLoadingQuestionCategories(Question[] questions){
        String[][] questionCategoryCount = new String[questions.length][2];
        boolean done = false;
        int numberOfEntriesInArray = 0;
        for (Question aQuestion : questions){
            if (aQuestion == null)
                continue;
            done = false;
            String questionCategory = aQuestion.getQuestionCategory();
            for (int i = 0;i < questions.length;i++){
                if (questionCategoryCount[i][0] != null && questionCategoryCount[i][1] != null){
                    if(questionCategoryCount[i][0].equals(questionCategory)){
                        int frequency = Integer.parseInt(questionCategoryCount[i][1]);
                        questionCategoryCount[i][1] = String.valueOf(frequency+1);
                        done = true;
                        break;
                    }


                }

            }
            if (done == false){
                questionCategoryCount[numberOfEntriesInArray][0] = questionCategory;
                questionCategoryCount[numberOfEntriesInArray][1] = String.valueOf(1);
                numberOfEntriesInArray++;
            }
        }
        System.out.println("Category\t\t\tThe Number of Questions");
        System.out.println("________\t\t\t_______________________");
        for(int i = 0;i < numberOfEntriesInArray+1;i++){
            if(questionCategoryCount[i][0] != null && questionCategoryCount[i][1] != null)
            System.out.println(questionCategoryCount[i][0] + "  \t\t\t\t\t  " + questionCategoryCount[i][1]);
        }
    }

    public static void printingLoadingQuestionDifficulties(Question[] questions){
        String[][] questionDifficultyCount = new String[questions.length][2];
        boolean done = false;
        int numberOfEntriesInArray = 0;
        for (Question aQuestion : questions){
            if (aQuestion == null)
                continue;
            done = false;
            String questionDifficulty = aQuestion.getDifficulty();
            for (int i = 0;i < questions.length;i++){
                if (questionDifficultyCount[i][0] != null && questionDifficultyCount[i][1] != null){
                    if(questionDifficultyCount[i][0].equals(questionDifficulty)){
                        int frequency = Integer.parseInt(questionDifficultyCount[i][1]);
                        questionDifficultyCount[i][1] = String.valueOf(frequency+1);
                        done = true;
                        break;
                    }


                }

            }
            if (done == false){
                questionDifficultyCount[numberOfEntriesInArray][0] = questionDifficulty;
                questionDifficultyCount[numberOfEntriesInArray][1] = String.valueOf(1);
                numberOfEntriesInArray++;
            }
        }
        System.out.println("Difficulty Level\tThe Number of Questions");
        System.out.println("________________\t_______________________");
        for(int i = 0;i < numberOfEntriesInArray+1;i++){
            if(questionDifficultyCount[i][0] != null && questionDifficultyCount[i][1] != null)
                System.out.println(questionDifficultyCount[i][0] + "\t\t\t\t\t\t" + questionDifficultyCount[i][1]);
        }
    }

    public static void contest(Question[] questions, Participant[] participants) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        boolean nextContestant = true;
        int questionsRemaining = 5;
        int currentTier = 1;
        int gameFlag = 0; // 1 : game over by wrong answer 2 : retreat
        boolean fiftyPercent, doubleDip, extraTime;
        File storingFile = new File("src/stored_answers.txt");
        if (storingFile.createNewFile()) {
            System.out.println("File created: " + storingFile.getName());
        } else {
            System.out.println("File already exists.");
        }
        FileWriter storingFileWriter = new FileWriter("src/stored_answers.txt");
        Question[] wordCloudQuestions = new Question[20];





        int currentDifficulty = 1;
        while (nextContestant == true) {
            boolean doubleDipActivated = false;
            int doubleDipCounter = 0;
            questionsRemaining = 5;
            fiftyPercent = true;
            doubleDip = true;
            extraTime = true;
            gameFlag = 0;
            currentTier = 1;
            currentDifficulty = 1;
            Participant[] randomContestantList = new Participant[35];
            int questionsAnsweredCorrectly=0;
            int randomContestantListCounter = 0;
            for (int c = 0; c < participants.length; c++) {
                if (participants[c] != null && !participants[c].isContested()) {
                    randomContestantList[randomContestantListCounter] = participants[c];
                    randomContestantListCounter++;
                }
            }
            int randomContestantElector = random.nextInt(randomContestantListCounter);
            Participant currentContestant = randomContestantList[randomContestantElector];
            System.out.println("\nNext Contestant : " + currentContestant.getName());
            for(int parIndex = 0;parIndex<participantCitySort.length;parIndex++){
                if(participantCitySort[parIndex][0] != null){
                    if(currentContestant.getCity().equals(participantCitySort[parIndex][0])){
                        int UpgradedFrequency = Integer.parseInt(participantCitySort[parIndex][1]) + 1;
                        participantCitySort[parIndex][1] = String.valueOf(UpgradedFrequency);
                        break;
                    }
                }
                else{
                    participantCitySort[participantCitySortCounter][0] = currentContestant.getCity();
                    participantCitySort[participantCitySortCounter++][1] = "1";
                    break;
                }
            }


            while (gameFlag == 0) {
                String[] remainingOptions = new String[3];
                boolean questionAnswered = false;
                int i = 0;
                for (int index = 0; index < questions.length; index++) {
                    if (questions[index] != null && Integer.parseInt(questions[index].getDifficulty()) == currentDifficulty && !questions[index].isAppeared() &&
                            i < 20) {
                        wordCloudQuestions[i] = questions[index];
                        i++;

                    }
                }

                for (int j = 0; j < wordCloudQuestions.length; j++) {
                    if (wordCloudQuestions[j] != null) {
                        System.out.println(wordCloudQuestions[j].getRandomWord());
                    }
                }
                System.out.println("Please enter the word to display the question.");
                String inputWordCloud = scanner.next();
                Question currentQuestion = null;
                for (int z = 0; z < wordCloudQuestions.length; z++) {
                    if (wordCloudQuestions[z].getRandomWord().equals(inputWordCloud)) {
                        currentQuestion = wordCloudQuestions[z];
                    }
                }

                System.out.println(currentQuestion.getQuestion_text());
                System.out.println("A) " + currentQuestion.getFirstOption());
                System.out.println("B) " + currentQuestion.getSecondOption());
                System.out.println("C) " + currentQuestion.getThirdOption());
                System.out.println("D) " + currentQuestion.getFourthOption());
                int timeCounter  = 0;
                cn.getTextWindow().setCursorPosition(40, 40);
                System.out.println("Money : " + "$" + currentContestant.getGainedMoney());
                cn.getTextWindow().setCursorPosition(40, 41);
                if (fiftyPercent) {
                    System.out.println("%50");
                } else
                    System.out.println("-");
                cn.getTextWindow().setCursorPosition(40, 42);
                if (doubleDip) {
                    System.out.println("Double Dip");
                } else
                    System.out.println("-");
                cn.getTextWindow().setCursorPosition(40, 43);
                if (extraTime) {
                    System.out.println("Extra Time");
                } else
                    System.out.println("-");
                cn.getTextWindow().setCursorPosition(40, 44);
                System.out.print("Your initial time : " + (20 - timeCounter) + " seconds");
                cn.getTextWindow().setCursorPosition(0, 50);
                System.out.println("Enter your answer.");
                keypr = 0;
                while (questionAnswered == false) {
                    if (keypr == 1) {
                        char rckey = (char)rkey;

                        if (rckey == currentQuestion.getCorrectAnswer()) {

                        questionAnswered = true;
                        questionsRemaining -= 1;
                        System.out.println("\nWell done! Correct answer.");
                        currentDifficulty += 1;
                        if (questionsRemaining == 4)
                            currentContestant.setGainedMoney(20000);
                        if (questionsRemaining == 3) {
                            currentTier = 2;
                            currentContestant.setGainedMoney(100000);
                        }
                        if (questionsRemaining == 2)
                            currentContestant.setGainedMoney(250000);
                        if (questionsRemaining == 1) {
                            currentContestant.setGainedMoney(500000);
                            currentTier = 3;
                        }
                        if (questionsRemaining == 0){
                            currentContestant.setGainedMoney(1000000);
                            System.out.println("Congratulations " + currentContestant.getName() + "from " + currentContestant.getCity() +  ", you have won the biggest prize. You are now a " +
                                    "millionaire" );
                            gameFlag = 1;
                            questionAnswered = true;
                        }
                            questionsAnsweredCorrectly += 1;
                            storingFileWriter.write(currentQuestion.getQuestion_text() +"," + currentContestant.getName() + "," + "true\n");
                            for(int categoryIndex = 0;categoryIndex<categoriesWithAnsweredCorrectlyBadly.length;categoryIndex++){
                                if(categoriesWithAnsweredCorrectlyBadly[categoryIndex][0] != null){
                                    if(currentQuestion.getCategory().equals(categoriesWithAnsweredCorrectlyBadly[categoryIndex][0])){
                                        if (categoriesWithAnsweredCorrectlyBadly[categoryIndex][1] != null){
                                            int UpgradedFrequency = Integer.parseInt(categoriesWithAnsweredCorrectlyBadly[categoryIndex][1]) + 1;
                                            categoriesWithAnsweredCorrectlyBadly[categoryIndex][1] = String.valueOf(UpgradedFrequency);
                                            break;
                                        }
                                        else{
                                            categoriesWithAnsweredCorrectlyBadly[categoryIndex][1] = "1";
                                            break;
                                        }

                                    }
                                }
                                else{
                                    categoriesWithAnsweredCorrectlyBadly[categoriesWithAnsweredCorrectlyBadlyCounter][0] = currentQuestion.getCategory();
                                    categoriesWithAnsweredCorrectlyBadly[categoriesWithAnsweredCorrectlyBadlyCounter++][1] = "1";
                                    break;
                                }
                            }

                    } else if (rckey == '1' && fiftyPercent) {
                        fiftyPercent = false;
                        System.out.println("\n" + currentQuestion.getQuestion_text());
                        remainingOptions = Question.fiftyPercent(currentQuestion);
                        if (Arrays.asList(remainingOptions).contains(currentQuestion.getFirstOption()))
                            System.out.println("");
                        else
                            System.out.println("A) " + currentQuestion.getFirstOption());
                        if (Arrays.asList(remainingOptions).contains(currentQuestion.getSecondOption()))
                            System.out.println("");
                        else
                            System.out.println("B) " + currentQuestion.getSecondOption());
                        if (Arrays.asList(remainingOptions).contains(currentQuestion.getThirdOption()))
                            System.out.println("");
                        else
                            System.out.println("C) " + currentQuestion.getThirdOption());
                        if (Arrays.asList(remainingOptions).contains(currentQuestion.getFourthOption()))
                            System.out.println("");
                        else
                            System.out.println("D) " + currentQuestion.getFourthOption());
                        cn.getTextWindow().setCursorPosition(40, 40);
                        System.out.println("Money : " + "$" + currentContestant.getGainedMoney());
                        cn.getTextWindow().setCursorPosition(0, 50);
                        cn.getTextWindow().setCursorPosition(40, 41);
                        if (fiftyPercent) {
                            System.out.println("%50");
                        } else
                            System.out.println("-");
                        cn.getTextWindow().setCursorPosition(40, 42);
                        if (doubleDip) {
                            System.out.println("Double Dip");
                        } else
                            System.out.println("-");
                        cn.getTextWindow().setCursorPosition(40, 42);
                            if (extraTime) {
                                System.out.println("Extra Time");
                            } else
                                System.out.println("-");
                            cn.getTextWindow().setCursorPosition(0, 50);



                    } else if (rckey == '2' && doubleDip) {
                        doubleDip = false;
                        doubleDipActivated = true;
                        }
                        else if (rckey == '3' && extraTime){
                            extraTime = false;
                            timeCounter -= 20;
                        }
                        else if (rckey == 'R'){
                            System.out.println("\nContestant " + currentContestant.getName() + " has chosen to retreat with gained " + currentContestant.getGainedMoney());
                            gameFlag = 1;
                            questionAnswered = true;

                        }
                        else if (rckey != currentQuestion.getCorrectAnswer() && doubleDipActivated &&  doubleDipCounter < 2){
                            doubleDipCounter += 1;
                            System.out.println("\nYour first try of double dip was wrong.");
                        }


                        else if (rckey != currentQuestion.getCorrectAnswer()) {
                        System.out.println("\nThe answer is wrong. The correct answer was option " + currentQuestion.getCorrectAnswer());
                        gameFlag = 1;
                        questionAnswered = true;
                            storingFileWriter.write(currentQuestion.getQuestion_text() +"," + currentContestant.getName() + "," + "false\n");
                        if (currentTier == 1){
                            currentContestant.setGainedMoney(0);
                        }
                        else if (currentTier == 2)
                            currentContestant.setGainedMoney(100000);
                        else if (currentTier == 3)
                            currentContestant.setGainedMoney(500000);
                            System.out.println("You have won " + currentContestant.getGainedMoney()+"$");
                            for(int categoryIndex = 0;categoryIndex<categoriesWithAnsweredCorrectlyBadly.length;categoryIndex++){
                                if(categoriesWithAnsweredCorrectlyBadly[categoryIndex][0] != null){
                                    if(currentQuestion.getCategory().equals(categoriesWithAnsweredCorrectlyBadly[categoryIndex][0])){
                                        if (categoriesWithAnsweredCorrectlyBadly[categoryIndex][2] != null){
                                            int UpgradedFrequency = Integer.parseInt(categoriesWithAnsweredCorrectlyBadly[categoryIndex][2]) + 1;
                                            categoriesWithAnsweredCorrectlyBadly[categoryIndex][2] = String.valueOf(UpgradedFrequency);
                                            break;
                                        }
                                        else{
                                            categoriesWithAnsweredCorrectlyBadly[categoryIndex][2] = "1";
                                            break;
                                        }

                                    }
                                }
                                else{
                                    categoriesWithAnsweredCorrectlyBadly[categoriesWithAnsweredCorrectlyBadlyCounter][0] = currentQuestion.getCategory();
                                    categoriesWithAnsweredCorrectlyBadly[categoriesWithAnsweredCorrectlyBadlyCounter++][2] = "1";
                                    break;
                                }
                            }
                    }


                        keypr=0;
                }
                    else{
                        timeCounter += 1;
                        if (timeCounter == 20){
                            gameFlag = 1;
                            questionAnswered = true;
                            System.out.println("\nTime is up. You lost. The correct answer was option " + currentQuestion.getCorrectAnswer());
                            storingFileWriter.write(currentQuestion.getQuestion_text() +"," + currentContestant.getName() + "," + "false\n");
                        }
                        cn.getTextWindow().setCursorPosition(40, 43);
                        System.out.print("Remaining time : " + (20 - timeCounter) + " seconds");
                    }
                    if (questionAnswered) {
                        currentQuestion.setAppearedTrue();
                        for (int k = 0; k < wordCloudQuestions.length; k++) {
                            wordCloudQuestions[k] = null;
                        }
                    }


                    if (gameFlag == 1) {
                        break;
                    }

                    Thread.sleep(1000);


                }
            }

            System.out.println("\nNext Contestant ? (Y/N)");
            String nextContestantChecker = scanner.next();
            if (nextContestantChecker.equalsIgnoreCase("N")){
                storingFileWriter.close();
                break;
            }
            if (mostSuccessfulContestant[0] != null){
                if (currentContestant.getGainedMoney() >= Integer.parseInt(mostSuccessfulContestant[1])){
                    mostSuccessfulContestant[0] = currentContestant.getName();
                    mostSuccessfulContestant[1] = String.valueOf(currentContestant.getGainedMoney());
                }
            }
            else{
                mostSuccessfulContestant[0] = currentContestant.getName();
                mostSuccessfulContestant[1] = String.valueOf(currentContestant.getGainedMoney());
            }
            if (currentContestant.getAge() <= 30){
                underAge30AverageCorrectAnswersCounter++;
                SumOfunderAge30AverageCorrectAnswers += questionsAnsweredCorrectly;
            }
            else if (currentContestant.getAge() > 30 && currentContestant.getAge() <= 50){
                betweenAge30and50AverageCorrectAnswersCounter++;
                SumOfbetweenAge30and50AverageCorrectAnswers += questionsAnsweredCorrectly;
            }
            else if (currentContestant.getAge() > 50){
                aboveAge50AverageCorrectAnswersCounter++;
                SumOfaboveAge50AverageCorrectAnswers += questionsAnsweredCorrectly;
            }
        }
        if (underAge30AverageCorrectAnswersCounter == 0){
            averageUnder30 = 0;
        }
        else{
            averageUnder30 = SumOfunderAge30AverageCorrectAnswers/underAge30AverageCorrectAnswersCounter;
        }
        if (betweenAge30and50AverageCorrectAnswersCounter == 0){
           avBetweenAge30And50 = 0;
        }
        else{
            avBetweenAge30And50 = SumOfbetweenAge30and50AverageCorrectAnswers/betweenAge30and50AverageCorrectAnswersCounter;
        }
        if (aboveAge50AverageCorrectAnswersCounter == 0){
            avAbove50 = 0;
        }
        else{
            avAbove50 = SumOfaboveAge50AverageCorrectAnswers/aboveAge50AverageCorrectAnswersCounter;
        }




    }


    public static void showStatistics(){
        System.out.println("The most successful contestant: " + mostSuccessfulContestant[0] +  " with prize of " + mostSuccessfulContestant[1]+"$");
        System.out.println("The categories with the correct and bad answers:");
        System.out.println("CATEGORY\tCORRECT ANSWERS\tBAD ANSWERS");
        for(int i = 0;i<categoriesWithAnsweredCorrectlyBadly.length;i++){
            if (categoriesWithAnsweredCorrectlyBadly[i][0] != null){
                if(categoriesWithAnsweredCorrectlyBadly[i][1] == null)
                    categoriesWithAnsweredCorrectlyBadly[i][1] = "0";
                if(categoriesWithAnsweredCorrectlyBadly[i][2] == null)
                    categoriesWithAnsweredCorrectlyBadly[i][2] = "0";


                System.out.println(categoriesWithAnsweredCorrectlyBadly[i][0]+"\t\t\t"+categoriesWithAnsweredCorrectlyBadly[i][1]+"\t"+categoriesWithAnsweredCorrectlyBadly[i][2]);
            }
        }
        System.out.println("Cities with the participant numbers:");
        for(int i = 0;i <participantCitySort.length;i++){
            if(participantCitySort[i][0] != null){
                System.out.println(participantCitySort[i][0]+"\t\t\t"+participantCitySort[i][1]);
            }
        }
        System.out.println("On average, how many questions did contestants in each age group answer correctly?");
        System.out.println("Average above 50 : " + avAbove50);
        System.out.println("Average between 30 and 50 : " + avBetweenAge30And50);
        System.out.println("Average under 30 : " + averageUnder30);
    }
}



