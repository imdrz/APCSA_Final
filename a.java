import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Game extends JPanel implements KeyListener, ActionListener, JavaArcade {
    private Timer timer;
    private int WIDTH, HEIGHT;
    private PlayerObject player1, player2;
    private Rectangle[] platforms, m1, m2, m3;
    private JLabel player1HealthLabel, player2HealthLabel;
    public static ArrayList<Projectile> projectiles = new ArrayList<>();;
    public ArrayList<Cloud> clouds;
    private int[] p1C, p2C;
    private String state = "running";
    private int currentScore, highScore = 10000;
    private int longestWinStreak = 0;
    private int lastWinner = 0;
    private int secondsElapsed = 0;
    private Timer secondTimer;
    private GameStats gameStats;

    public Game(int w, int h) {
        loadLongestWinStreakData();
        secondTimer = new Timer(1000, this);
        secondTimer.start();
        loadLongestWinStreakData();
        WIDTH = w;
        HEIGHT = h;
        this.setLayout(null);

        player1HealthLabel = new JLabel();
        player2HealthLabel = new JLabel();

        player1HealthLabel.setBounds(20, 20, 200, 30);
        player2HealthLabel.setBounds(20, 50, 200, 30);

        this.add(player1HealthLabel);
        this.add(player2HealthLabel);

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(130, 200, 229));
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setSize(WIDTH, HEIGHT);

        m1 = new Rectangle[] { new Rectangle(200, 300, 200, 30), new Rectangle(90, 150, 60, 30),
                new Rectangle(450, 150, 60, 30), new Rectangle(100, 420, 400, 30) };

        m2 = new Rectangle[] { new Rectangle(50, 300, 60, 30), new Rectangle(490, 300, 60, 30),
                new Rectangle(100, 420, 400, 30), new Rectangle(270, 150, 30, 75), new Rectangle(220, 175, 130, 30) };

        m3 = new Rectangle[] { new Rectangle(50, 420, 60, 30), new Rectangle(510, 420, 60, 30),
                new Rectangle(160, 350, 300, 30), new Rectangle(30, 220, 100, 30), new Rectangle(470, 220, 100, 30),
                new Rectangle(160, 100, 300, 30) };

        resetGame();
        startGame();
        timer = new Timer(15, this);
        timer.start();
    }

    public void startGame() {
        if (state == "stopped") {
            resetGame();
            state = "running";
        } else {
            state = "running";
        }
    }

    public void resetGame() {
        secondsElapsed = 0;
        randomizeMap();
        player1 = new PlayerObject(p1C[0], p1C[1], platforms,
                new int[] { KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_R });
        player2 = new PlayerObject(p2C[0], p2C[1], platforms,
                new int[] { KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_COMMA });
        clouds = new ArrayList<>();
        clouds.add(new Cloud(this));
        clouds.add(new Cloud(this));
        clouds.add(new Cloud(this));
        clouds.add(new Cloud(this));
    }

    public void randomizeMap() {
        int r = ((int) (Math.random() * 3)) + 1;

        switch (r) {
            case 1:
                platforms = m1;
                p1C = new int[] { 100, 0 };
                p2C = new int[] { 460, 0 };
                break;
            case 2:
                platforms = m2;
                p1C = new int[] { 60, 0 };
                p2C = new int[] { 500, 0 };
                break;
            case 3:
                platforms = m3;
                p1C = new int[] { 60, 320 };
                p2C = new int[] { 520, 320 };
                break;
            default:
                break;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Cloud c : clouds) {
            c.draw(g);
        }
        for (Projectile p : projectiles) {
            p.draw(g);
        }
        Image platformImage = new ImageIcon("grass.png").getImage();
        for (Rectangle p : platforms) {
            g.drawImage(platformImage, p.x, p.y, p.width, p.height, this);
            // g.setColor(Color.gray);
            // g.fillRect(p.x, p.y, p.width, p.height);
        }

        player1.draw(g);
        player2.draw(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Time: " + secondsElapsed + " sec", WIDTH - 150, 30);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == secondTimer) {
            secondsElapsed++;
        }

        if (state == "running") {
            player1.actionPerformed(e);
            player2.actionPerformed(e);
            player1.update();
            player2.update();
            for (Projectile p : projectiles) {
                p.update();
            }

            ArrayList<Cloud> toRemove = new ArrayList<>();
            for (Cloud c : clouds) {
                c.update();
                if (c.check()) {
                    toRemove.add(c);
                }
            }

            for (Cloud c : toRemove) {
                clouds.remove(c);
                clouds.add(new Cloud(this));
            }

            player1HealthLabel.setText("Player 1 Health: " + player1.getHealth());
            player2HealthLabel.setText("Player 2 Health: " + player2.getHealth());

            if (player1.getHealth() <= 0) {
                updateScores();
                JOptionPane.showMessageDialog(this, "Player 1 has been defeated!");
                timer.stop();
                timer.start();
                resetGame();
                gameStats.gameOver(highScore);

            }

            if (player2.getHealth() <= 0) {
                updateScores();
                JOptionPane.showMessageDialog(this, "Player 2 has been defeated!");
                timer.stop();
                timer.start();
                resetGame();
                gameStats.gameOver(highScore);
            }
            repaint();
        }
    }

    public void updateScores() {
        System.out.println(secondsElapsed);
        System.out.println(highScore);
        if (secondsElapsed < highScore) {
            saveLongestWinStreakData();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        player1.keyPressed(e);
        player2.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        player1.keyReleased(e);
        player2.keyReleased(e);
    }

    public boolean running() {
        return (state == "running");
    }

    public String getGameName() {
        return "2-Player Platformer";
    }

    public void pauseGame() {
        state = "paused";
    }

    public String getInstructions() {
        return "WASD and arrow keys to move, R/Comma to shoot.";
    }

    public String getCredits() {
        return "By Nav Garg and Imad Riaz";
    }

    public String getHighScore() {
        return String.valueOf(highScore);
    }

    public void stopGame() {
        currentScore = 0;
        state = "stopped";
    }

    public int getPoints() {
        return currentScore;
    }

    public void setDisplay(GameStats d) {
        gameStats = d;
    }

    public void loadLongestWinStreakData() {
        try (Scanner scanner = new Scanner(new File("/home/vncuser/runtime/scores.txt"))) {
            if (scanner.hasNextInt()) {
                highScore = scanner.nextInt();
            }
        } catch (IOException e) {
            System.out.println("Could not read longest streak data.");
        }
    }    

    public void saveLongestWinStreakData() {
        String filePath = "/home/vncuser/runtime/scores.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.write(Integer.toString(secondsElapsed));
            writer.newLine(); 
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
