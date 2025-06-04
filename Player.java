import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Player {
        int x, y, width = 40, height = 60;
        int dx = 0, dy = 0;
        boolean left, right, jump, isAttacking = false;
        boolean onGround = false;
        boolean facingRight = true;
        Color color;
        int leftKey, rightKey, jumpKey, attackKey;
        int attackTimer = 0;
        int knockback = 0;
        int lives = 3;

        public Player(int x, int y, Color color, int leftKey, int rightKey, int jumpKey, int attackKey) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.leftKey = leftKey;
            this.rightKey = rightKey;
            this.jumpKey = jumpKey;
            this.attackKey = attackKey;
        }

        public void update(Rectangle platform) {
            if (left) dx = -5;
            else if (right) dx = 5;
            else dx = 0;

            dy += 1;
            x += dx;
            y += dy;

            Rectangle playerBounds = getBounds();
            if (playerBounds.intersects(platform) && dy >= 0) {
                y = platform.y - height;
                dy = 0;
                onGround = true;
            } else {
                onGround = false;
            }

            if (attackTimer > 0) attackTimer--;
            else isAttacking = false;
        }

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillRect(x, y, width, height);

            if (isAttacking) {
                g.setColor(Color.YELLOW);
                int ax = facingRight ? x + width : x - 10;
                g.fillRect(ax, y + 20, 10, 20);
            }
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }

        public void takeHit(boolean fromRight) {
            knockback += 10;
            int force = 10 + knockback / 5;
            x += fromRight ? force : -force;
            dy = -10;
        }

        public void respawn() {
            x = color == Color.RED ? 100 : 600;
            y = 400;
            dx = dy = 0;
            knockback = 0;
        }

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == leftKey) left = true;
            if (key == rightKey) right = true;
            if (key == jumpKey && onGround) {
                dy = -15;
            }
            if (key == attackKey && !isAttacking) {
                isAttacking = true;
                attackTimer = 10;
                facingRight = right || (!left && facingRight);
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == leftKey) left = false;
            if (key == rightKey) right = false;
        }
    }
