import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Arcade extends JPanel implements KeyListener, ActionListener, JavaArcade {
    private Timer timer;
    private int WIDTH = 600, HEIGHT = 600;
    private PlayerObject player1, player2;
    private Rectangle[] platforms = new Rectangle[7];
    private JLabel player1HealthLabel, player2HealthLabel;
    public static ArrayList<Projectile> projectiles = new ArrayList<>();
    private boolean isRunning = false;
    private int score = 0;
    private int highScore = 0;
    private GameStats display;
    private int[] cloudYLevels = {60, 180, 320, 460, 600, 740, 880, 1020};
    private int[] cloudXLevels = {100, 450, 250, 700, 400, 150, 600, 350};
    private Rectangle[] clouds = new Rectangle[cloudYLevels.length];
    private int cloudSpeed = 1;
    public Arcade() {
        this.setLayout(null);
        player1HealthLabel = new JLabel();
        player2HealthLabel = new JLabel();
        player1HealthLabel.setBounds(20, 20, 200, 30);
        player2HealthLabel.setBounds(20, 50, 200, 30);
        this.add(player1HealthLabel);
        this.add(player2HealthLabel);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(130,200,229));
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setSize(WIDTH, HEIGHT);
        platforms[0] = new Rectangle(100, 500, 400, 50);
        for (int i = 0; i < clouds.length; i++) {
            int x = cloudXLevels[i];
            int y = cloudYLevels[i];
            int width = 100;
            int height = 40;
            clouds[i] = new Rectangle(x, y, width, height);
        }

        startGame();
        timer = new Timer(15, this);
        timer.start();
    }

    public void startGame() {
        isRunning = true;
        int width = 50;
        int height = 20;

        for (int i = 1; i < 7; i++) {
            while (true) {
                int x = (int)(Math.random() * 320) + 100;
                int y = 200 + (int)(Math.random() * 300);
                Rectangle newPlatform = new Rectangle(x, y, width, height);

                boolean overlaps = false;
                for (int j = 0; j < i; j++) {
                    if (platforms[j] != null && newPlatform.intersects(platforms[j])) {
                        overlaps = true;
                        break;
                    }
                }

                if (!overlaps) {
                    platforms[i] = newPlatform;
                    break;
                }
            }
        }
        player1 = new PlayerObject("Imad1.png", 100, 100, platforms, new int[]{KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_T, KeyEvent.VK_R});
        player2 = new PlayerObject("Imad2.png", 460, 100, platforms, new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_PERIOD, KeyEvent.VK_COMMA});
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.setColor(Color.white);
        for (Rectangle cloud : clouds) {
            g.fillRoundRect(cloud.x, cloud.y, cloud.width, cloud.height, 20, 20);
        }
        Image platformImage = new ImageIcon("/Users/nav/IdeaProjects/APCDA_Final/src/APCSA_Final/grass.png").getImage();

        for (Rectangle p : platforms) {
            g.drawImage(platformImage, p.x, p.y, p.width, p.height, this);
        }
        for (Projectile p : projectiles) {
            p.draw(g);
        }

        player1.draw(g);
        player2.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isRunning) return;
        player1.update();
        player2.update();
        for (Projectile p : projectiles) {
            p.update();
        }
        player1HealthLabel.setText("Player 1 Health: " + player1.getHealth());
        player2HealthLabel.setText("Player 2 Health: " + player2.getHealth());
        for (Rectangle cloud : clouds) {
            cloud.x += cloudSpeed;
            if (cloud.x > WIDTH) {
                cloud.x = -cloud.width;
            }
        }
        score++;
        if (display != null) display.update(score);
        if (player1.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Player 1 has been defeated!");
            stopGame();
            startGame();
            timer.start();
        }
        if (player2.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Player 2 has been defeated!");
            stopGame();
            startGame();
            timer.start();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        player1.keyPressed(e);
        player2.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player1.keyReleased(e);
        player2.keyReleased(e);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("a");
        Arcade arcade = new Arcade();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(arcade);
        frame.pack();
        frame.setVisible(true);
    }

    public boolean running() {
        return isRunning;
    }

    public String getGameName() {
        return "2-Player Platformer";
    }

    public void pauseGame() {
        isRunning = false;
        timer.stop();
    }

    public String getInstructions() {
        return "WASD and arrow keys to move, R/Comma to shoot.";
    }

    public String getCredits() {
        return "By Nav Garg and Imad Diaz";
    }

    public String getHighScore() {
        return String.valueOf(highScore);
    }

    public void stopGame() {
        isRunning = false;
        if (score > highScore) highScore = score;
        score = 0;
        timer.stop();
    }

    public int getPoints() {
        return score;
    }

    public void setDisplay(GameStats d) {
        display = d;
    }
}