import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, ActionListener, JavaArcade {
    private Timer timer;
    private int WIDTH, HEIGHT;
    private PlayerObject player1, player2;
    private Rectangle[] platforms, m1, m2, m3;
    private JLabel player1HealthLabel, player2HealthLabel;
    public static ArrayList<Projectile> projectiles;
    public ArrayList<Cloud> clouds;
    private int[] p1C, p2C;
    private String state = "running";
    private int currentScore, highScore;

    public Game(int w, int h) {
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
        this.setBackground(new Color(162, 252, 249));
        // this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setSize(WIDTH, HEIGHT);

        m1 = new Rectangle[]{new Rectangle(200, 400, 200, 30), new Rectangle(90, 250, 60, 30), new Rectangle(450, 250, 60, 30), new Rectangle(100, 520, 400, 30)};
        m2 = new Rectangle[]{new Rectangle(50, 400, 60, 30), new Rectangle(490, 400, 60, 30), new Rectangle(100, 520, 400, 30)};
        m3 = new Rectangle[]{new Rectangle(100, 520, 400, 30)};

        int r = (int) (Math.random() * 3) + 1;

        switch (r) {
            case 1:
                platforms = m1;
                p1C = new int[]{100, 80};
                p2C = new int[]{460, 80};
                break;
            case 2:
                platforms = m2;
                p1C = new int[]{60, 350};
                p2C = new int[]{500, 350};
                break;
            case 3:
                platforms = m2;
                p1C = new int[]{60, 350};
                p2C = new int[]{500, 350};
                break;
            default:
                break;
        }

        resetGame();
        startGame();
        timer = new Timer(15, this);
        timer.start();
    }

    public void resetGame() {
        player1 = new PlayerObject("Imad1.png", p1C[0], p1C[1], platforms, new int[]{KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_R});
        player2 = new PlayerObject("Imad2.png", p2C[0], p2C[1], platforms, new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_COMMA});
        projectiles = new ArrayList<>();
        clouds = new ArrayList<>();
        clouds.add(new Cloud(this));
        clouds.add(new Cloud(this));
        clouds.add(new Cloud(this));
    }

    public void startGame() {
        if (state == "stopped") {
            resetGame();
            state = "running";
        } else {
            state = "running";
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
        for (Rectangle p: platforms) {
            g.drawImage(platformImage, p.x, p.y, p.width, p.height, this);
        }

        player1.draw(g);
        player2.draw(g);
    }

    
    public void actionPerformed(ActionEvent e) {
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
            
            for (Cloud c: toRemove) {
                clouds.remove(c);
                clouds.add(new Cloud(this));
            }

            player1HealthLabel.setText("Player 1 Health: " + player1.getHealth());
            player2HealthLabel.setText("Player 2 Health: " + player2.getHealth());

            if (player1.getHealth() <= 0) {
                JOptionPane.showMessageDialog(this, "Player 1 has been defeated!");
                timer.stop();
                timer.start();
                resetGame();

            }

            if (player2.getHealth() <= 0) {
                JOptionPane.showMessageDialog(this, "Player 2 has been defeated!");
                timer.stop();
                timer.start();
                resetGame();
            }
            repaint();
        }
    }

    
    public void keyTyped(KeyEvent e) {}

    
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

    
    public void setDisplay(GameStats d) {} 
}
