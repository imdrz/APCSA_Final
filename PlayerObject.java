import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class PlayerObject {
    private int x, y, dx = 0, dy = 0, SPAWN_X, SPAWN_Y;
    private boolean onGround = false, hasDoubleJump = true;
    private int WIDTH = 40, HEIGHT = 60, velocityP = 0, lives = 3;
    private Rectangle[] platforms;
    private Color c;
    private int[] keyBinds, strokes;
    private String facing;
    private double knockback = 0.75;
    
    public PlayerObject(Color c, int x, int y, Rectangle[] platforms, int[] keyBinds) {
        this.x = x;
        this.y = y;
        this.platforms = platforms;
        this.c = c;
        this.keyBinds = keyBinds;
        this.strokes = new int[]{0, 0, 0};
        this.SPAWN_X = x;
        this.SPAWN_Y = y;
    }

    public void draw(Graphics g) {
        g.setColor(c);
        g.fillRect(this.x, this.y, WIDTH, HEIGHT);
    }

    public void update() {
        dx = 6 * velocityP;
        dy++;

        if (dy > 17) {
            dy = 17;
        }

        x += dx;
        y += dy;

        checkCollision();
    }

    

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void checkCollision() {
        for (Rectangle p: platforms) {
            if (p.y == platforms[platforms.length-1].y) {
                if (getBounds().intersects(p)) {
                    die();
                }
            } else {
                if (new Rectangle(x, y+HEIGHT-1, WIDTH, 1).intersects(p)) {
                    // bottom bound
                    y = p.y - HEIGHT;
                    onGround = true;
                    hasDoubleJump = true;
                    dy = 0;
                } else if (new Rectangle(x, y-1, WIDTH, 1).intersects(p)) {
                    // top bound
                    y = p.y+p.height;
                    dy = 0;
                } else if (new Rectangle(x-1, y, 1, HEIGHT).intersects(p)) {
                    // left bound
                    x = p.x+p.width;
                    dx = 0;
                } else if (new Rectangle(x+WIDTH-1, y, 1, HEIGHT).intersects(p)) {
                    // right bound
                    x = p.x-WIDTH;
                    dx = 0;
                }
            }
        }
    }
    
    public int[] getData() {
        return new int[]{x, y, WIDTH, HEIGHT};
    }

    public int[] getHitCoords() {
        return new int[]{(facing == "right" ? 1 : -1), (dy < 0 ? 1 : 0)};
    }

    public void die() {
        x = SPAWN_X;
        y = SPAWN_Y;
        lives--;
        knockback = 0.75;
    }

    public void takeHit(int dir) {
        x+= ((int) 45 * knockback * dir);
        y-= ((int) 20 * knockback);
        knockback += 0.45;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == keyBinds[1]) {
            strokes[1] = 1;
        } else if (e.getKeyCode() == keyBinds[0]) {
            strokes[0] = 1;
        } else if (e.getKeyCode() == keyBinds[2] && onGround == true) {
            strokes[2] = 1;
        } else if (e.getKeyCode() == keyBinds[2] && onGround == false && hasDoubleJump == true) {
            strokes[2] = 1;
            hasDoubleJump = false;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == keyBinds[1]) {
            strokes[1] = 0;
        } else if (e.getKeyCode() == keyBinds[0]) {
            strokes[0] = 0;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (strokes[1] == 1) {
            velocityP = 1;
            facing = "right";
        } else if (strokes[0] == 1) {
            velocityP = -1;
            facing = "left";
        } else {
            velocityP = 0;
        }

        if (strokes[2] == 1 && (onGround == true || hasDoubleJump == false)) {
            dy = -15;
            strokes[2] = 0;
            onGround = false;
        }
    }
}
