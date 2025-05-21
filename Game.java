import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private int WIDTH = 600, HEIGHT = 600;
    private PlayerObject player1, player2;
    private Rectangle[] platforms;

    public Game() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // this.setBackground(new Color(3, 251, 255));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setSize(WIDTH, HEIGHT);

        timer = new Timer(15, this);
        timer.start();


        platforms = new Rectangle[]{new Rectangle(100, 500, 400, 50)};

        startGame();
    }

    public void startGame() {
        player1 = new PlayerObject(Color.blue, 100, 100, platforms, new int[]{KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S});
        player2 = new PlayerObject(Color.red, 460, 100, platforms, new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN});
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (Rectangle p: platforms) {
            g.setColor(Color.gray);
            g.fillRect(p.x, p.y, p.width, p.height);
        }

        player1.draw(g);
        player2.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
