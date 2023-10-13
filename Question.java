import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Question {
    private String category;
    private String question_text;
    private String firstOption;
    private String secondOption;
    private String thirdOption;
    private String fourthOption;
    private char correctAnswer;
    private String difficulty;
    private String randomWord;
    private boolean isAppeared;

    public String getCategory() {
        return category;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }

    public String getFirstOption() {
        return firstOption;
    }

    public String getSecondOption() {
        return secondOption;
    }

    public String getThirdOption() {
        return thirdOption;
    }

    public String getFourthOption() {
        return fourthOption;
    }

    public void setAppearedTrue() {
        isAppeared = true;
    }

    public Question(String category, String question_text, String firstOption, String secondOption, String thirdOption, String fourthOption, char correctAnswer, String difficulty, String randomWord) {
        this.question_text = question_text;
        this.difficulty = difficulty;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        this.thirdOption = thirdOption;
        this.fourthOption = fourthOption;
        this.correctAnswer = correctAnswer;
        this.category = category;
        this.randomWord = randomWord;
        isAppeared = false;

    }

    public boolean isAppeared() {
        return isAppeared;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestionCategory() {
        return category;
    }

    public String getRandomWord() {
        return randomWord;
    }

    public static String[] fiftyPercent(Question aQuestion) {
        Random random = new Random();
        String correctResult = null;
        String[] optionArray = new String[3];
        int i = 0;
        if (aQuestion.correctAnswer == 'A') {
            correctResult = aQuestion.firstOption;
        }
        else if (aQuestion.correctAnswer == 'B')
            correctResult = aQuestion.secondOption;
        else if (aQuestion.correctAnswer == 'C')
            correctResult = aQuestion.thirdOption;
        else if (aQuestion.correctAnswer == 'D')
            correctResult = aQuestion.fourthOption;

        if(correctResult != aQuestion.firstOption)
            optionArray[i++] = aQuestion.firstOption;
        if(correctResult != aQuestion.secondOption)
            optionArray[i++] = aQuestion.secondOption;
        if(correctResult != aQuestion.thirdOption)
            optionArray[i++] = aQuestion.thirdOption;
        if(correctResult != aQuestion.fourthOption)
            optionArray[i++] = aQuestion.fourthOption;
        int index = random.nextInt(3);
        optionArray[index] = null;
//        if (Arrays.asList(optionArray).contains(aQuestion.firstOption))
//            System.out.println("\n");
//        else
//            System.out.println(aQuestion.firstOption);
//        if (Arrays.asList(optionArray).contains(aQuestion.secondOption))
//            System.out.println("\n");
//        else
//            System.out.println(aQuestion.secondOption);
//        if (Arrays.asList(optionArray).contains(aQuestion.thirdOption))
//            System.out.println("");
//        else
//            System.out.println(aQuestion.thirdOption);
//        if (Arrays.asList(optionArray).contains(aQuestion.fourthOption))
//            System.out.println("");
//        else
//            System.out.println(aQuestion.fourthOption);

     return optionArray;
    }


}

