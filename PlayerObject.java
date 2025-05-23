package APCSA_Final;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PlayerObject {
    private int x, y, dx = 0, dy = 0;
    private boolean onGround = false;
    private int velocityP = 0;
    private int WIDTH = 40, HEIGHT = 60;
    private Rectangle[] platforms;
    private Color c;
    private int[] keyBinds;
    private int spawnY;
    private int spawnX;
    private int health = 3;
    private ArrayList<Integer> velocityPArr= new ArrayList<Integer>();
    private Image playerImage;
    
    public PlayerObject(String imagePath, int x, int y, Rectangle[] platforms, int[] keyBinds) {
        spawnY = y;
        spawnX = x;
        this.x = x;
        this.y = y;
        this.platforms = platforms;
        playerImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        this.keyBinds = keyBinds;
    }

    public void draw(Graphics g) {
        g.drawImage(playerImage, this.x, this.y, WIDTH, HEIGHT, null);
    }

    public void update() {
        dx = 5 * velocityP;
        dy++;

        if (dy < -20) {
            dy = -10;
        }

        x += dx;
        y += dy;

        if(y > 800){
            health--;
            reset();
        }


        projectileCollision();
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
            velocityPArr.add(velocityP);
        } else if (e.getKeyCode() == keyBinds[0]) {
            velocityP = -1;
            velocityPArr.add(velocityP);
        } else if (e.getKeyCode() == keyBinds[2] && onGround == true) {
            dy = -20;
        } else if(e.getKeyCode() == keyBinds[3]){
            if(velocityP == 0){
                Game.projectiles.add(new Projectile(x, y, velocityPArr.getLast()*5, this));
            }
            else {
                Game.projectiles.add(new Projectile(x, y, velocityP * 5, this));
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == keyBinds[1]) {
            velocityP = 0;
        } else if (e.getKeyCode() == keyBinds[0]) {
            velocityP = 0;
        }
    }

    public void projectileCollision(){
        ArrayList<Projectile> toRemove = new ArrayList<>();
        for (Projectile p : Game.projectiles) {
            if (p.getOwner() != this && p.getBounds().intersects(this.getBounds())) {
                if (p.getX() < this.x) {
                    dx = 50;
                    dy = -5;
                    x += dx;
                    y += dy;
                } else {
                    dx = -50;
                    dy = -5;
                    x += dx;
                    y += dy;
                }
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
    private void reset() {
        x = spawnX;
        y = spawnY;
        dx = 0;
        dy = 0;
        velocityP = 0;
    }
    public int getHealth(){
        return health;
    }
}
