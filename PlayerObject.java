import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PlayerObject {
    private int x, y, dx = 0, dy = 0, SPAWN_X, SPAWN_Y;
    private boolean onGround = false, hasDoubleJump = true;
    private int WIDTH = 40, HEIGHT = 60, velocityP = 0, lives = 3;
    private Rectangle[] platforms;
    private Color c;
    private Image playerImage;
    private int[] keyBinds, strokes;
    private String facing;
    private double knockback = 1;
    
    public PlayerObject(String imagePath, int x, int y, Rectangle[] platforms, int[] keyBinds) {
        this.SPAWN_X = y;
        this.SPAWN_Y = x;
        this.x = x;
        this.y = y;
        this.platforms = platforms;
        playerImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        this.keyBinds = keyBinds;
        this.strokes = new int[]{0, 0, 0};
    }

    public void draw(Graphics g) {
        g.drawImage(playerImage, this.x, this.y, WIDTH, HEIGHT, null);
    }

    public void update() {
        dx = 6 * velocityP;
        dy++;

        if (dy < -10) {
            dy = -10;
        }

        x += dx;
        y += dy;

        if (y > 800) {
            die();
        }


        projectileCollision();
        checkCollision();
    }

    

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void checkCollision() {
        for (Rectangle p: platforms) {
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
    
    public int[] getData() {
        return new int[]{x, y, WIDTH, HEIGHT};
    }

    public int[] getHitCoords() {
        return new int[]{(facing == "right" ? 1 : -1), (dy < 0 ? 1 : 0)};
    }

    public void die() {
        x = SPAWN_X;
        y = SPAWN_Y;
        dx = 0;
        dy = 0;
        lives--;
        knockback = 0.75;
    }

    public void takeHit(int dir) {
        x+= ((int) 50 * knockback * dir);
        y-= ((int) -5 * knockback);
        knockback += 0.45;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == keyBinds[1]) {
            strokes[1] = 1;
            velocityP = 1;
        } else if (e.getKeyCode() == keyBinds[0]) {
            velocityP = -1;
            strokes[0] = 1;
        } else if (e.getKeyCode() == keyBinds[2]) {
            strokes[2] = 1;
        }

        if (e.getKeyCode() == keyBinds[3]) {
            Game.projectiles.add(new Projectile(x, y, getHitCoords()[0] * 10, this));
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

        if (strokes[2] == 1 && (onGround == true || (onGround == false && hasDoubleJump == true))) {
            if (onGround == false) {
                hasDoubleJump = false;
            }
            dy = -35;
            strokes[2] = 0;
            onGround = false;
        }
    }

    public void projectileCollision(){
        ArrayList<Projectile> toRemove = new ArrayList<>();
        for (Projectile p : Game.projectiles) {
            for (Rectangle platform: platforms) {
                if (platform.intersects(p.getBounds())) {
                    toRemove.add(p);
                }
            }
            if (p.getOwner() != this && p.getBounds().intersects(this.getBounds())) {
                if (p.getX() < this.x) {
                    dx = (int) (50 * knockback);
                    dy = -5;
                    x += dx;
                    y += dy;
                } else {
                    dx = (int) (-50 * knockback);
                    dy = -5;
                    x += dx;
                    y += dy;
                }
                knockback += 0.35;
                toRemove.add(p);
            }
        }
        
        Game.projectiles.removeAll(toRemove);
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getHealth(){
        return lives;
    }
}
