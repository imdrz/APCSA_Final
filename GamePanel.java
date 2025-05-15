import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private ArrayList<int[]> objects = new ArrayList<>();


    /*
     * type variable
     * 0 - platform
     * 1 - player
     */

    public void addObject(int x, int y, int w, int h, int type, int id, int xV, int yV) {
        objects.add(new int[]{x, y, xV, yV, w, h, type, id, 100});
    }


    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, 500, 500);
        for (int[] a: objects) {
            int x = a[6];
            if (x == 0) {
                g.setColor(Color.GREEN);
                g.fillRect(a[0], a[1], a[4], a[5]/3);
                g.setColor(new Color(158, 102, 41));
                g.fillRect(a[0], a[1]+a[5]/3, a[4], a[5]*2/3);
            } else if (x == 1) {
                g.setColor(Color.black);
                g.fillRect(a[0], a[1], a[4], a[5]);
            }
        }
    }

    public void updateVelocity(int xC, int yC, int type, int id) {
        for (int[] a: objects) {
            if (a[6] == type && a[7] == id) {
                a[2] = xC;
                a[3] += yC;
                if (a[3] < -10) {
                    a[3] = -10;
                } else if (a[3] > 10) {
                    a[3] = 10;
                }
                
            }
        }
        updatePos(type, id);
    }

    

    public void updatePos(int type, int id) {
        for (int[] a: objects) {
            if (a[6] == type && a[7] == id) {
                a[0] += a[2];
                a[1] -= a[3];
            }
        }
    }

    public void check() {
        for (int[] a: objects) {
            if (aboveGround(a[7]) == true && a[6] == 1) {
                updateVelocity(0, -1, 1, a[7]);
            } else if (aboveGround(0) == false && 450-a[1] < a[5] && a[6] == 1) {
                a[3] = 0;
                a[1] = 450-a[5];
            }
        }
    }

    public boolean aboveGround(int id) {
        for (int[] a: objects) {
            if (a[6] == 1 && a[7] == id) {
                return (a[1] < 450-a[5]);
            }
        }
        return false;
    }

    public void attack(int id) {
        String direction = "";
        int[] arr = getSpecificData(id, 1);
        if (arr[7] == id && arr[6] == 1) {
            if (arr[3] > 0) {
                direction += "N";
            }

            if (arr[2] > 0) {
                direction += "E";
            } else if (arr[2] < 0) {
                direction += "W";
            }
                System.out.println(Arrays.toString(arr));
        }

        for (int[] a: objects) {
            if (a[6] == 1 && a[7] != id) {
                boolean hit = false;
                int xBuffer = 50;
                int yBuffer = 50;
                System.out.println(arr[0] + "-" + a[0] + "=" + (a[0]-arr[0]) + "." + xBuffer);
                int xDiff = a[0]-arr[0];
                int yDiff = a[1]-arr[1];
                if (direction.equals("E") && xDiff < xBuffer && xDiff > 10) {
                    hit = true;
                    triggerHit(a[7]);
                } else if (direction.equals("W") && xDiff > xBuffer*-1 && xDiff < -10) {
                    hit = true;
                    triggerHit(a[7]);
                }
                System.out.println(hit);
            }
        }
        System.out.println(direction);
    }

    public void triggerHit(int id) {
        System.out.println("1");
        int[] a = getSpecificData(id, 1);
        a[8] -= 7;
        System.out.println(a[8]);
    }

    public ArrayList<int[]> getData() {
        return objects;
    }

    public int[] getSpecificData(int id, int type) {
        int[] out = new int[8];
        for (int[] a: objects) {
            if (a[6] == type && a[7] == id) {
                out = a;
                break;
            }
        }
        return out;
    }
        
}
