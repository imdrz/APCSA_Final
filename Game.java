import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private int WIDTH = 600, HEIGHT = 600;
    private PlayerObject player1, player2;
    private Rectangle[] platforms;
    private int xReach = 85, yReach = 85;

    public Game() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // this.setBackground(new Color(162, 252, 249));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setSize(WIDTH, HEIGHT);

        Rectangle bottomBound = new Rectangle(-200, 800, 1200, 30);
        platforms = new Rectangle[]{new Rectangle(200, 400, 200, 30), new Rectangle(90, 220, 60, 30), new Rectangle(450, 220, 60, 30), new Rectangle(100, 520, 400, 30), bottomBound};

        startGame();

        timer = new Timer(15, this);
        timer.start();
    }

    public void startGame() {
        player1 = new PlayerObject(Color.blue, 100, 100, platforms, new int[]{KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S});
        player2 = new PlayerObject(Color.red, 460, 100, platforms, new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN});
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.gray);
        for (Rectangle p: platforms) {
            g.fillRect(p.x, p.y, p.width, p.height);
        }

        player1.draw(g);
        player2.draw(g);
    }

    public void attack(KeyEvent e) {
        int[] p1D = player1.getData();
        int[] p2D = player2.getData();
        int[] hitCoords;
        int xD, yD;
        if (e.getKeyCode() == KeyEvent.VK_S) {
            hitCoords = player1.getHitCoords();
            xD = p2D[0] - p1D[0];
            yD = p2D[1] - p1D[1];
            if (hitCoords[0] > 0) {
                if ((xD < xReach * hitCoords[0] && (xD > -15)) && yD <= yReach * hitCoords[1]) {
                    player2.takeHit(1);
                }
            } else {
                if ((xD > xReach * hitCoords[0] && (xD < 15)) && yD <= yReach * hitCoords[1]) {
                    player2.takeHit(-1);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            hitCoords = player2.getHitCoords();
            xD = p1D[0] - p2D[0];
            yD = p1D[1] - p2D[1];
            if (hitCoords[0] > 0) {
                if ((xD < xReach * hitCoords[0] && (xD > -15)) && yD <= yReach * hitCoords[1]) {
                    player1.takeHit(1);
                }
            } else {
                if ((xD > xReach * hitCoords[0] && (xD < 15)) && yD <= yReach * hitCoords[1]) {
                    player1.takeHit(-1);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player1.actionPerformed(e);
        player2.actionPerformed(e);
        player1.update();
        player2.update();
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
        attack(e);
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
