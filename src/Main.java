import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String START_NEW_GAME = "Н";
    private static final String EXIT_GAME = "В";
    private static final StringBuilder CURRENT_WORD = new StringBuilder();
    private static final String NOT_OPENED_LETTER = "*";
    private static final int MAX_WRONG_COUNT = 6;
    private static final List<String> HIDDEN_WORDS = new ArrayList<>();
    private static final List<String> ENTERED_LETTERS = new ArrayList<>();

    private static String word;
    private static String letter;
    private static int mistakeCounter = 0;

    public static void main(String[] args) throws FileNotFoundException {
        startGameRound();
    }

    private static void startGameRound() throws FileNotFoundException {
        System.out.printf("[%s] - начать новую игру  [%s] - выйти из игры %n", START_NEW_GAME, EXIT_GAME);
        while (true) {
            String startOrExit = SCANNER.nextLine().toUpperCase();
            switch (startOrExit) {
                case START_NEW_GAME:
                    System.out.println("Игра началась");
                    startGameLoop();
                    return;
                case EXIT_GAME:
                    System.out.println("Игра окончена");
                    return;
                default:
                    System.out.printf("Некорректный ввод! Введите одну из букв %s и %s%n", START_NEW_GAME, EXIT_GAME);
            }
        }

    }

    private static void startGameLoop() throws FileNotFoundException {
        setRandomWord();
        makeTempWord();
        do {
            inputLetter();
        } while (!isGameFinished());
        restartGame();
    }

    private static void inputLetter() {
        System.out.println("Введите одиночную, русскую букву: ");
        while (true) {
            letter = SCANNER.nextLine().toLowerCase();
            if (ENTERED_LETTERS.contains(letter)) {
                System.out.println("Вы уже использовали эту букву! Введите другую: ");
            } else if (letter.matches("[а-яА-ЯёЁ]")) {
                break;
            } else {
                System.out.println("Некорректный ввод! Введите одиночную, русскую букву: ");
            }
        }

        if (word.contains(letter)) {
            System.out.println("Правильная буква!");
            updateCurrentWord();
        } else {
            System.out.println("Неправильная буква");
            increaseMistakeCounter();
        }
        System.out.println("Состояние виселицы: ");
        if(mistakeCounter == 0) System.out.println("Ошибок нет -> виселицы тоже)");
        else System.out.println(Gallows.hangmanStates[mistakeCounter - 1]);
        addLetterToEnteredLettersArray();
        System.out.println("Попыток осталось: " + (MAX_WRONG_COUNT - mistakeCounter));
        System.out.println(CURRENT_WORD);
    }

    private static void setRandomWord() throws FileNotFoundException {
        File file = new File("words.txt");
        Scanner scanner1 = new Scanner(file);

        while (scanner1.hasNextLine()){
            HIDDEN_WORDS.add(scanner1.nextLine());
        }
        word = HIDDEN_WORDS.get((int) (Math.random() * (HIDDEN_WORDS.toArray().length - 1)));

    }

    private static void makeTempWord() {
        CURRENT_WORD.append(NOT_OPENED_LETTER.repeat(word.length()));
    }

    private static void addLetterToEnteredLettersArray() {
        ENTERED_LETTERS.add(letter);
        System.out.println("Введенные буквы: " + ENTERED_LETTERS);
    }

    private static void updateCurrentWord() {
        for (int i = 0; i < word.length(); i++) {
            if (letter.equals(String.valueOf(word.charAt(i)))) {
                CURRENT_WORD.setCharAt(i, word.charAt(i));
            }
        }
    }

    private static void increaseMistakeCounter() {
        mistakeCounter++;
    }

    private static boolean isGameFinished() {
        if (CURRENT_WORD.toString().equals(word)) {
            System.out.println("Вы выиграли!");
            return true;
        } else if (mistakeCounter == MAX_WRONG_COUNT) {
            System.out.println("Вы проиграли");
            System.out.println("Правильное слово:" + word);
            return true;
        }
        return false;
    }

    private static void restartGame() throws FileNotFoundException {
        CURRENT_WORD.setLength(0);
        ENTERED_LETTERS.clear();
        HIDDEN_WORDS.clear();
        word = "";
        mistakeCounter = 0;
        startGameRound();
    }
}