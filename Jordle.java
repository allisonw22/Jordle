import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import javafx.scene.input.KeyEvent;
import java.util.Random;

/**
 * The Jordle program implementation uses JavaFX to create a parody of the New York Times Wordle game
 * in which a user has 6 tries to guess a 5-letter Java term. If a letter is in the correct position, the
 * box will turn green, if the letter is correct but in the wrong position, the box will turn yellow, and
 * if the letter is incorrect, the box with turn grey.
 *
 * @author Allison Wong
 * @version 1.0
 */
public class Jordle extends Application {
    private int loc = 0;
    private int locRow = 0;
    private String word;
    private Label message;
    private Label[][] labelArray = new Label[6][5];
    private ArrayList<String> input = new ArrayList<>();
    private ArrayList<ArrayList<Rectangle>> rectangles = new ArrayList<ArrayList<Rectangle>>();

    private Stage primaryStage;
    private Scene scene1;

    /**
     * The start method initiates the beginning of the Jordle applicaiton.
     * @param primaryStage the Stage of which the Jordle is displayed in
     */
    public void start(Stage primaryStage) {
        Random rand = new Random();
        word = Words.list.get(rand.nextInt(Words.list.size()));
        // System.out.println(word);

        primaryStage.setTitle("HW08: Jordle");
        Text title = new Text("JORDLE");
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 28));

        BorderPane border = new BorderPane();
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        GridPane gridPane = new GridPane();
        VBox bottomBox = new VBox(15);
        border.setTop(topBox);
        border.setCenter(gridPane);
        border.setBottom(bottomBox);
        border.setPadding(new Insets(15, 15, 10, 15));

        message = new Label("Try guessing a word!");
        message.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 18));
        Button restart = new Button("Restart");
        Button info = new Button("?");
        HBox infoBox = new HBox(10);
        infoBox.getChildren().addAll(restart, info);
        infoBox.setAlignment(Pos.CENTER_RIGHT);
        topBox.getChildren().add(title);
        bottomBox.getChildren().addAll(message, infoBox);
        bottomBox.setAlignment(Pos.CENTER);

        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);

        addRectangles(gridPane);

        scene1 = new Scene(border, 450, 500);
        primaryStage.setScene(scene1);
        primaryStage.show();
        gridPane.requestFocus();

        info.setOnAction(e -> {
            scene2();
            gridPane.requestFocus();
        });
        restart.setOnAction(e -> restart(gridPane));

        gridPane.requestFocus();
        restart(gridPane);

        gridPane.requestFocus();
        keyPress(labelArray[locRow][loc], gridPane);
    }

    /**
     * Adds Rectangle and Label Objects to the GridPane in a 6 x 5 rectangular format.
     * @param gridPane the GridPane of which the rectangles and labels are added to
     */
    public void addRectangles(GridPane gridPane) {
        for (int i = 0; i < 6; i++) {
            ArrayList<Rectangle> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                Rectangle square = new Rectangle(50, 50, Color.WHITE);
                square.setStroke(Color.BLACK);
                row.add(square);
            }
            rectangles.add(row);
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                gridPane.add(rectangles.get(i).get(j), j, i);
                labelArray[i][j] = new Label();
                labelArray[i][j].setFont(Font.font("Verdana", FontWeight.BOLD, 28));
                labelArray[i][j].setAlignment(Pos.CENTER);

                gridPane.add(labelArray[i][j], j, i);
                GridPane.setHalignment(labelArray[i][j], HPos.CENTER);
            }
        }
    }

    /**
     * Restarts the Jordle game by reseting all of the GridPane nodes.
     * @param gridPane the central GridPane that is being reset
     */
    public void restart(GridPane gridPane) {
        Random rand = new Random();
        word = Words.list.get(rand.nextInt(Words.list.size()));
        System.out.println(word);
        message.setText("Try guessing a word!");

        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 5; column++) {
                rectangles.get(row).get(column).setFill(Color.WHITE);
                labelArray[row][column].setText("");
                labelArray[row][column].setTextFill(Color.BLACK);
            }
        }
        loc = 0;
        locRow = 0;
        input.clear();
        gridPane.requestFocus();
    }

    /**
     * Determines if the word inputted is made up of solely letters.
     * @param keyInput the word that is checked whether if it is made of letters
     * @return whether the word entered only contains letters
     */
    public boolean isAlpha(String keyInput) {
        char[] chars = keyInput.toCharArray();
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether a character from one array is present in another array.
     * @param answer an array of characters
     * @param guess an array of characters
     * @param index the index of the character that is to be checked
     * @return whether the character of a certain index is present in the other array
     */
    public boolean checkArray(char[] answer, char[] guess, int index) {
        for (char c : answer) {
            if (c == guess[index]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a scene that contains instructions to the Jordle game.
     */
    public void scene2() {
        Stage intructionsStage = new Stage();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.setPadding(new Insets(25));

        Label heading = new Label("How To Play:");
        heading.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 28));
        Label instructions = new Label("1. You have 6 tries to guess a 5-letter word (no repeats in letters!)\n"
            + "2. If a letter is correct and in the right position, the box will turn green\n3. If a letter is"
            + "correct but in the wrong location, the box will turn yellow\n4. If a letter is incorrect, the box"
            + "will turn gray\n5. Good luck and have fun!!");
        instructions.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        instructions.setWrapText(true);
        Button back = new Button("Go Back");

        vBox.getChildren().addAll(heading, instructions, back);
        Scene scene2 = new Scene(vBox, 500, 300);

        intructionsStage.setScene(scene2);
        intructionsStage.show();

        back.setOnAction(e -> intructionsStage.close());
    }

    /**
     * Creates a scene that contains an Error message if a player types in a word that is less than 5 characters.
     */
    public void scene3() {
        Stage errorStage = new Stage();

        HBox goBack = new HBox(10);
        Button exitError = new Button("OK");
        goBack.getChildren().add(exitError);
        goBack.setAlignment(Pos.CENTER_RIGHT);

        VBox errorLayout = new VBox(10);
        Label error = new Label("ERROR!!");
        error.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 36));
        error.setAlignment(Pos.CENTER_LEFT);
        Label errorMessage = new Label("Please enter a 5 letter word");
        errorMessage.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        errorMessage.setAlignment(Pos.CENTER_LEFT);
        errorLayout.getChildren().addAll(error, errorMessage, goBack);
        errorLayout.setPadding(new Insets(20, 20, 20, 20));

        Scene scene3 = new Scene(errorLayout, 250, 150);

        errorStage.setScene(scene3);
        errorStage.show();

        exitError.setOnAction(e -> errorStage.close());
    }

    /**
     * Creates a scene that contains a Congratulations message if the player correctly guesses the word.
     * @param gridPane the gridPane of the Jordle, used to reset the board when the Jordle is completed
     */
    public void scene4(GridPane gridPane) {
        Stage congratsStage = new Stage();

        HBox playAgain = new HBox(10);
        Button again = new Button("Play Again");
        playAgain.getChildren().add(again);
        playAgain.setAlignment(Pos.CENTER_RIGHT);

        VBox congratsLayout = new VBox(10);
        Label congrats = new Label("CONGRATULATIONS!!");
        congrats.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 36));
        congrats.setAlignment(Pos.CENTER_LEFT);
        Label congratsMessage = new Label("You completed the Jordle!!\nPress the button below to play again :)");
        congratsMessage.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        congratsMessage.setAlignment(Pos.CENTER_LEFT);
        congratsLayout.getChildren().addAll(congrats, congratsMessage, playAgain);
        congratsLayout.setPadding(new Insets(20, 20, 20, 20));

        Scene scene4 = new Scene(congratsLayout, 500, 175);

        congratsStage.setScene(scene4);
        congratsStage.show();

        again.setOnAction(e -> {
            congratsStage.close();
            restart(gridPane);
        });
    }

    /**
     * launches the Jordle application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Takes the input of a KeyPress and manipulates the data as needed, for instance checking which letters
     * entered are completely correct, in the wrong spot, or wrong, and turning their boxes green, yellow, and grey,
     * and dealing with deleting characters and entering a 5-letter word.
     * @param text the Label object that is being edited through Key Presses
     * @param gridPane the GridPane in which the Labels reside in
     */
    public void keyPress(Label text, GridPane gridPane) {
        gridPane.setOnKeyPressed((final KeyEvent e) -> {
            // System.out.println("Key Pressed: " + e.getCode());
            switch (e.getCode()) {
            case SHIFT:
                break;
            case CAPS:
                break;
            case COMMAND:
                break;
            case CONTROL:
                break;
            case ALT:
                break;
            case UP:
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            case BACK_SPACE:
                if (loc == 0) {
                    break;
                } else {
                    input.remove(input.size() - 1);
                    labelArray[locRow][loc - 1].setText("");
                    loc--;
                    break;
                }
            case ENTER:
                if (input.size() == 5) {
                    if (correctWord(input, word)) {
                        for (int i = 0; i < 5; i++) {
                            rectangles.get(locRow).get(i).setFill(Color.GREEN);
                        }
                        message.setText("Correct!");
                        scene4(gridPane);
                        e.consume();
                    } else {
                        String str = input.toString().replaceAll(",", "");
                        str = str.toLowerCase();
                        char[] wordGuessed = str.substring(1, str.length() - 1).replaceAll(" ", "").toCharArray();
                        char[] answer = word.toCharArray();
                        // System.out.println(wordGuessed);
                        // System.out.println(answer);
                        for (int i = 0; i < 5; i++) {
                            if (wordGuessed[i] == answer[i]) {
                                rectangles.get(locRow).get(i).setFill(Color.GREEN);
                            } else if (checkArray(answer, wordGuessed, i)) {
                                rectangles.get(locRow).get(i).setFill(Color.GOLD);
                            } else {
                                rectangles.get(locRow).get(i).setFill(Color.GREY);
                            }
                        }
                    }
                    for (int i = 0; i < 5; i++) {
                        Label wordEntered = labelArray[locRow][i];
                        wordEntered.setTextFill(Color.WHITE);
                    }
                    locRow++;
                    loc = 0;
                    if (locRow == 6) {
                        if (correctWord(input, word)) {
                            for (int i = 0; i < 5; i++) {
                                rectangles.get(locRow - 1).get(i).setFill(Color.GREEN);
                            }
                            message.setText("Correct!");
                            scene4(gridPane);
                        } else {
                            message.setText("GAME OVER :( the word was " + word);
                        }
                    }
                    input.clear();
                } else {
                    scene3();
                }
                break;
            default:
                if (isAlpha(e.getText()) && !(e.getText().equals("SHIFT")) && input.size() < 5) {
                    String cap = (e.getText().toUpperCase());
                    input.add(cap);
                    labelArray[locRow][loc].setText(input.get(loc));
                    loc++;
                }
                break;
            }
        });
        text.requestFocus();
    }

    /**
     * Determines whether a user's input is equal to the word needed to be guessed.
     * @param input the user's input
     * @param word the word needed to be guessed
     * @return whether the user's input is the correct word
     */
    private static boolean correctWord(ArrayList<String> input, String word) {
        if ((input.size() == 5)) {
            for (int i = 0; i < 5; i++) {
                if (!(input.get(i).toLowerCase().equals(Character.toString(word.charAt(i))))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}