package APCSA_Final;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private int WIDTH = 600, HEIGHT = 600;
    private PlayerObject player1, player2;
    private Rectangle[] platforms = new Rectangle[7];
    private JLabel player1HealthLabel, player2HealthLabel;
    public static ArrayList<Projectile> projectiles = new ArrayList<>();
    public Game() {

        this.setLayout(null);

        player1HealthLabel = new JLabel();
        player2HealthLabel = new JLabel();

        player1HealthLabel.setBounds(20, 20, 200, 30);
        player2HealthLabel.setBounds(20, 50, 200, 30);

        this.add(player1HealthLabel);
        this.add(player2HealthLabel);

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // this.setBackground(new Color(3, 251, 255));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setSize(WIDTH, HEIGHT);




        platforms[0] = new Rectangle(100, 500, 400, 50);


        for (int i = 1; i < 7; i++) {
            int width = 50;
            int height = 20;
            int x = (int)(Math.random() * (400)) + 100;
            int y = 200 + (int)(Math.random() * (300));
            platforms[i] = new Rectangle(x, y, width, height);
        }

        startGame();
        timer = new Timer(15, this);
        timer.start();
    }

    public void startGame() {
        player1 = new PlayerObject("Imad1.png", 100, 100, platforms, new int[]{KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S});
        player2 = new PlayerObject("Imad2.png", 460, 100, platforms, new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN});
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (Rectangle p: platforms) {
            g.setColor(Color.gray);
            g.fillRect(p.x, p.y, p.width, p.height);
        }
        for (Projectile p : projectiles) {
            p.draw(g);
        }

        player1.draw(g);
        player2.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player1.update();
        player2.update();
        for (Projectile p : projectiles) {
            p.update();
        }


        player1HealthLabel.setText("Player 1 Health: " + player1.getHealth());
        player2HealthLabel.setText("Player 2 Health: " + player2.getHealth());

        if (player1.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Player 1 has been defeated!");
            timer.stop();
            timer.start();
            for (int i = 1; i < 7; i++) {
                int width = 50;
                int height = 20;
                int x = (int)(Math.random() * (400)) + 100;
                int y = 200 + (int)(Math.random() * (300));
                platforms[i] = new Rectangle(x, y, width, height);
            }
            startGame();

        }

        if (player2.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Player 2 has been defeated!");
            timer.stop();
            timer.start();
            for (int i = 1; i < 7; i++) {
                int width = 50;
                int height = 20;
                int x = (int)(Math.random() * (WIDTH - width));
                int y = 200 + (int)(Math.random() * (300));
                platforms[i] = new Rectangle(x, y, width, height);
            }
            startGame();
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

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
        Game game = new Game();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(game);
        frame.pack();
        frame.setVisible(true);
    }


    
}
