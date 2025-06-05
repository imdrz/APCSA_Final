import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameStats extends JPanel {
  private JTextField gameNameText, currentHighScorer, currentHighScore;
  private int yourScore;
  private JLabel yourScoreText, hScore;
  private JavaArcade game;
  private int highScore;

  // Constructor
  public GameStats(JavaArcade t) {
    super(new GridLayout(2, 4, 10, 0));
    loadLongestWinStreakData();
    setBorder(new EmptyBorder(0, 0, 5, 0));
    Font gameNameFont = new Font("Monospaced", Font.BOLD, 24);
    JLabel gName = new JLabel(" " + t.getGameName());

    gName.setForeground(Color.red);
    gName.setFont(gameNameFont);
    add(gName);
    hScore = new JLabel(" High Score: " + highScore);

    add(hScore); // new JLabel(" Current High Score: " + t.getHighScore()));

    add(new JLabel(" "));
    yourScoreText = new JLabel(" Current Score: " + 0);

    add(yourScoreText);

    Font displayFont = new Font("Monospaced", Font.BOLD, 16);
    game = t;

  }

  public void update(int points) {
    yourScoreText.setText(" Current Score: " + points);
  }

  public void gameOver(int points) {
    yourScoreText.setForeground(Color.BLUE);
    String s = (String) JOptionPane.showInputDialog(this,
        "You are the new high scorer. Congratulations!\n Enter your name: ", "High Score", JOptionPane.PLAIN_MESSAGE,
        null, null, "name");
    hScore.setText(" Current High Score:   " + points);
  }

  public void loadLongestWinStreakData() {
    try (Scanner scanner = new Scanner(new File("scores.txt"))) {
      if (scanner.hasNextInt()) {
        highScore = scanner.nextInt();
      }
    } catch (IOException e) {
      System.out.println("Could not read longest streak data.");
    }
  }
}