import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private int WIDTH = 600, HEIGHT = 600;
    private PlayerObject player1, player2;
    private Rectangle[] platforms, m1, m2, m3;
    private JLabel player1HealthLabel, player2HealthLabel;
    public static ArrayList<Projectile> projectiles;
    private int xReach = 85, yReach = 85;
    private int[] p1C, p2C;

    public Game() {
        this.setLayout(null);

        player1HealthLabel = new JLabel();
        player2HealthLabel = new JLabel();

        player1HealthLabel.setBounds(20, 20, 200, 30);
        player2HealthLabel.setBounds(20, 50, 200, 30);

        this.add(player1HealthLabel);
        this.add(player2HealthLabel);

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // this.setBackground(new Color(162, 252, 249));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setSize(WIDTH, HEIGHT);

        m1 = new Rectangle[]{new Rectangle(200, 400, 200, 30), new Rectangle(90, 220, 60, 30), new Rectangle(450, 220, 60, 30), new Rectangle(100, 520, 400, 30)};
        m2 = new Rectangle[]{new Rectangle(50, 400, 60, 30), new Rectangle(490, 400, 60, 30), new Rectangle(100, 520, 400, 30)};
        m3 = new Rectangle[]{new Rectangle(100, 520, 400, 30)};

        int r = (int) (Math.random() * 3) + 1;
        // System.out.println((int) (Math.random() * 3) + 1);


        switch (r) {
            case 1:
                platforms = m1;
                p1C = new int[]{100, 100};
                p2C = new int[]{460, 100};
                break;
            case 2:
                platforms = m2;
                p1C = new int[]{60, 350};
                p2C = new int[]{500, 200};
                break;
            case 3:
                platforms = m3;
                break;
            default:
                break;
        }


        // platforms[0] = new Rectangle(100, 500, 400, 50);

        // for (int i = 1; i < 7; i++) {
        //     int width = 50;
        //     int height = 20;
        //     int x = (int)(Math.random() * (400)) + 100;
        //     int y = 200 + (int)(Math.random() * (300));
        //     platforms[i] = new Rectangle(x, y, width, height);
        // }

        startGame();
        timer = new Timer(15, this);
        timer.start();
    }

    public void startGame() {
        player1 = new PlayerObject("Imad1.png", p1C[0], p1C[1], platforms, new int[]{KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S});
        player2 = new PlayerObject("Imad2.png", p2C[0], p2C[0], platforms, new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN});
        projectiles = new ArrayList<>();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.gray);
        for (Rectangle p: platforms) {
            g.fillRect(p.x, p.y, p.width, p.height);
        }
        for (Projectile p : projectiles) {
            p.draw(g);
        }

        player1.draw(g);
        player2.draw(g);
    }

    // public void attack(KeyEvent e) {
    //     int[] p1D = player1.getData();
    //     int[] p2D = player2.getData();
    //     int[] hitCoords;
    //     int xD, yD;
    //     if (e.getKeyCode() == KeyEvent.VK_S) {
    //         hitCoords = player1.getHitCoords();
    //         xD = p2D[0] - p1D[0];
    //         yD = p2D[1] - p1D[1];
    //         if (hitCoords[0] > 0) {
    //             if ((xD < xReach * hitCoords[0] && (xD > -15)) && yD <= yReach * hitCoords[1]) {
    //                 player2.takeHit(1);
    //             }
    //         } else {
    //             if ((xD > xReach * hitCoords[0] && (xD < 15)) && yD <= yReach * hitCoords[1]) {
    //                 player2.takeHit(-1);
    //             }
    //         }
    //     } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
    //         hitCoords = player2.getHitCoords();
    //         xD = p1D[0] - p2D[0];
    //         yD = p1D[1] - p2D[1];
    //         if (hitCoords[0] > 0) {
    //             if ((xD < xReach * hitCoords[0] && (xD > -15)) && yD <= yReach * hitCoords[1]) {
    //                 player1.takeHit(1);
    //             }
    //         } else {
    //             if ((xD > xReach * hitCoords[0] && (xD < 15)) && yD <= yReach * hitCoords[1]) {
    //                 player1.takeHit(-1);
    //             }
    //         }
    //     }
    // }

    @Override
    public void actionPerformed(ActionEvent e) {
        player1.actionPerformed(e);
        player2.actionPerformed(e);
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
            startGame();

        }

        if (player2.getHealth() <= 0) {
            JOptionPane.showMessageDialog(this, "Player 2 has been defeated!");
            timer.stop();
            timer.start();
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
        // attack(e);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GameFrame");
        Game game = new Game();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(game);
        frame.pack();
        frame.setVisible(true);
    } 
}
