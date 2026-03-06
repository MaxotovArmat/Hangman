import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final Scanner scanner = new Scanner(System.in);

    static final String START_NEW_GAME = "Н";
    static final String EXIT_GAME = "В";
    static final String NOT_OPENED_LETTERS = "*";
    static final int MAX_WRONG_COUNT = 6;
    static final int HIDDEN_WORDS_COUNT = 100;

    static String HIDDEN_WORD;
    static String startOrExit;
    static String tempWord = "";
    static String letter;
    static List<String> enteredLetters = new ArrayList<>();
    static int wrongCount = 0;


    public static void main(String[] args) throws FileNotFoundException{
        startGameRound();
    }

    public static void startGameRound() throws FileNotFoundException{
        System.out.println("[Н] - начать новую игру  [В] - выйти из игры");
        OUTER:
        while (true){
            startOrExit = scanner.nextLine();
            switch (startOrExit.toUpperCase()){
                case START_NEW_GAME:
                    System.out.println("Игра началась");
                    startGameLoop();
                    break OUTER;
                case EXIT_GAME:
                    System.out.println("Игра окончена");
                    break OUTER;
                default:
                    System.out.println("Некорректный ввод! Введите одну из букв Н и В");
            }
        }

    }

    public static void startGameLoop() throws FileNotFoundException{
        chooseRandomWord();
        makeTempWord();
        do {
            inputLetter();
        } while (!checkGameStatus()); // checkGameStatus returns false if game is already finished
        restartGame();
    }

    public static void restartGame() throws FileNotFoundException{
        tempWord = "";
        enteredLetters.clear();
        wrongCount = 0;
        startGameRound();
    }

    public static boolean checkGameStatus(){
        if(tempWord.equals(HIDDEN_WORD)){
            System.out.println("Вы выиграли!");
            return true;
        } else if(wrongCount == MAX_WRONG_COUNT){
            System.out.println("Вы проиграли");
            System.out.println("Правильное слово:" + HIDDEN_WORD);
            return true;
        }
        return false;
    }

    public static void chooseRandomWord() throws FileNotFoundException {
        File file = new File("words.txt");

        Scanner scanner1 = new Scanner(file);

        int randomLine = (int) (Math.random() * HIDDEN_WORDS_COUNT);
        for(int lineIndex = 0; lineIndex < HIDDEN_WORDS_COUNT; lineIndex++){
            String tempHiddenWord = scanner1.nextLine();
            if(lineIndex == randomLine){
                HIDDEN_WORD = tempHiddenWord;
                scanner1.close();
                break;
            }
        }
    }

    public static void makeTempWord(){
        for(int i = 0; i < HIDDEN_WORD.length(); i ++){
            tempWord += NOT_OPENED_LETTERS;
        }
    }

    public static void inputLetter(){
        System.out.println("Введите одиночную, русскую букву: ");
        while (true){
            letter = scanner.nextLine();
            if(enteredLetters.contains(letter)){
                System.out.println("Вы уже использовали эту букву! Введите другую: ");
            } else if (letter.matches("[а-яА-ЯёЁ]")){
                break;
            } else {
                System.out.println("Некорректный ввод! Введите одиночную, русскую букву: ");
            }
        }

        if(HIDDEN_WORD.contains(letter)){
            System.out.println("Правильная буква!");
            gotRightLetter();
        } else {
            System.out.println("Неправильная буква");
            gotWrongLetter();
        }
        addLetterToArray();
        System.out.println("Попыток осталось: " + (MAX_WRONG_COUNT - wrongCount));
    }

    public static void addLetterToArray(){
        enteredLetters.add(letter);
        System.out.println("Введенные буквы: " + enteredLetters);
    }

    public static void gotRightLetter(){
        for(int i = 0; i < HIDDEN_WORD.length(); i ++){
            if(letter.equals(String.valueOf(HIDDEN_WORD.charAt(i)))){
                tempWord = tempWord.substring(0, i) + HIDDEN_WORD.charAt(i) + tempWord.substring(i + 1);
            }
        }
        System.out.println(tempWord);

    }

    public static void gotWrongLetter(){
        wrongCount ++;
        System.out.println("Состояние виселицы: \n");
        System.out.println(Gallows.Hangman_status[wrongCount - 1]);
    }
}