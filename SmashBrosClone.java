import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SmashBrosClone extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800, HEIGHT = 600;
    private Timer timer;
    private Player player1, player2;
    private Rectangle platform;
    private enum GameState { MENU, PLAYING, GAME_OVER }
    private GameState gameState = GameState.MENU;
    private String winner = "";

    public SmashBrosClone() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        platform = new Rectangle(100, 500, 600, 20);
        timer = new Timer(15, this);
        timer.start();
    }

    public void startGame() {
        player1 = new Player(100, 400, Color.RED, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S);
        player2 = new Player(600, 400, Color.BLUE, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        gameState = GameState.PLAYING;
        winner = "";
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameState == GameState.MENU) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Mini Smash Bros", WIDTH / 2 - 150, HEIGHT / 2 - 50);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press ENTER to Start", WIDTH / 2 - 120, HEIGHT / 2);
        } else if (gameState == GameState.GAME_OVER) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString(winner + " Wins!", WIDTH / 2 - 100, HEIGHT / 2 - 50);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press ENTER to Return to Menu", WIDTH / 2 - 160, HEIGHT / 2);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(platform.x, platform.y, platform.width, platform.height);

            drawKnockbackBar(g, player1, 50, 30);
            drawKnockbackBar(g, player2, WIDTH - 250, 30);

            drawLives(g, player1, 50, 50);
            drawLives(g, player2, WIDTH - 250, 50);

            player1.draw(g);
            player2.draw(g);
        }
    }

    private void drawKnockbackBar(Graphics g, Player player, int x, int y) {
        g.setColor(Color.WHITE);
        g.drawString("Knockback: " + player.knockback, x, y - 10);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, 200, 10);
        g.setColor(player.color);
        int fillWidth = Math.min(200, player.knockback * 2);
        g.fillRect(x, y, fillWidth, 10);
    }

    private void drawLives(Graphics g, Player player, int x, int y) {
        g.setColor(Color.WHITE);
        g.drawString("Lives: " + player.lives, x, y);
    }

    public void actionPerformed(ActionEvent e) {
        if (gameState != GameState.PLAYING) return;

        player1.update(platform);
        player2.update(platform);
        checkAttack();
        checkDeath(player1);
        checkDeath(player2);
        repaint();
    }

    private void checkAttack() {
        if (player1.isAttacking && player1.getBounds().intersects(player2.getBounds())) {
            player2.takeHit(player1.facingRight);
        }
        if (player2.isAttacking && player2.getBounds().intersects(player1.getBounds())) {
            player1.takeHit(player2.facingRight);
        }
    }

    private void checkDeath(Player player) {
        if (player.y > HEIGHT) {
            player.lives--;
            player.respawn();
            if (player.lives <= 0) {
                gameState = GameState.GAME_OVER;
                winner = player == player1 ? "Player 2" : "Player 1";
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (gameState == GameState.MENU && e.getKeyCode() == KeyEvent.VK_ENTER) {
            startGame();
        } else if (gameState == GameState.GAME_OVER && e.getKeyCode() == KeyEvent.VK_ENTER) {
            gameState = GameState.MENU;
        }

        if (gameState == GameState.PLAYING) {
            player1.keyPressed(e);
            player2.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (gameState == GameState.PLAYING) {
            player1.keyReleased(e);
            player2.keyReleased(e);
        }
    }

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mini Smash Bros");
        SmashBrosClone game = new SmashBrosClone();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(game);
        frame.pack();
        frame.setVisible(true);
    }
}