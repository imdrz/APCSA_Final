import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class PlayerObject {
    private int x, y, dx = 0, dy = 0;
    private boolean onGround = false;
    private int velocityP = 0;
    private int WIDTH = 40, HEIGHT = 60;
    private Rectangle[] platforms;
    private Color c;
    private int[] keyBinds;
    
    public PlayerObject(Color c, int x, int y, Rectangle[] platforms, int[] keyBinds) {
        this.x = x;
        this.y = y;
        this.platforms = platforms;
        this.c = c;
        this.keyBinds = keyBinds;
    }

    public void draw(Graphics g) {
        g.setColor(c);
        g.fillRect(this.x, this.y, WIDTH, HEIGHT);
    }

    public void update() {
        dx = 5 * velocityP;
        dy++;

        if (dy < -10) {
            dy = -10;
        }

        x += dx;
        y += dy;


        checkCollision();

        // System.out.println(dx + ", " + dy + "(" + x + ", " + y + ")");
    }

    

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void checkCollision() {
        for (Rectangle p: platforms) {
            if (getBounds().intersects(p) && dy >= 0) {
                y = p.y - HEIGHT;
                onGround = true;
                dy = 0;
                break;
            } else {
                onGround = false;
            }
        }
    }

    public int[] getData() {
        // 0 - 1 - 2 - 3
        // x - y - w - h
        return new int[]{x, y, WIDTH, HEIGHT};
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == keyBinds[1]) {
            velocityP = 1;
        } else if (e.getKeyCode() == keyBinds[0]) {
            velocityP = -1;
        } else if (e.getKeyCode() == keyBinds[2] && onGround == true) {
            dy = -55;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == keyBinds[1]) {
            velocityP = 0;
        } else if (e.getKeyCode() == keyBinds[0]) {
            velocityP = 0;
        }
    }
}
