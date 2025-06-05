import java.awt.*;

public class Cloud {
    private int x, y, dx, h, w;
    private int r;
    private Game game;
    public Cloud(Game g) {
        h = 30;
        w = (int) (Math.random() * 51) + 50;
        r = (((int) (Math.random() * 2)) + 1 == 2 ? 1 : -1);
        dx = ((int) (Math.random() * 3) + 5) * r;
        x = (r == 1 ? 0-w : g.getWidth()+w);
        y = (int) (Math.random() * 351) + 50;
        game = g;
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRoundRect(x, y, w, h, 20, 20);
    }

    public void update() {
        x+=dx;
    }

    public int getX() {
        return x;
    }

    public boolean check() {
        boolean done = false;
        switch (r) {
            case 1:
                if (x > game.getWidth()) {
                    done = true;
                }
                break;
            case -1:
                if (x + w < 0) {
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
    }
}
