import java.awt.Rectangle;

public class test {
    public static void main(String[] args) {
        getside(new Rectangle(0, 129, 30, 30), new Rectangle(100, 100, 100, 30));
    }

    public static void getside(Rectangle p1, Rectangle p) {
        System.out.println(p1.intersects(p));
        
        // if (p1.y) {

        // }

        System.out.println(p1.intersects(p));
    }
}
