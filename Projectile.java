import java.awt.*;

public class Projectile {
    private int x, y, dx, dy, width = 10, height = 5;
    private Color color;
    private PlayerObject owner;

    public Projectile(int x, int y, int dx, PlayerObject owner) {
        this.x = x;
        this.y = y+ 21;
        this.dx = dx;
        this.dy = 0;
        this.color = Color.BLACK;
        this.owner = owner;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public PlayerObject getOwner() {
        return owner;
    }

    public int getDy() {
        return dy;
    }

    public int getDx() {
        return dx;
    }

    public int getX() {
        return x;
    }
}