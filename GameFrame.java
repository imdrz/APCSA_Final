import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;

public class GameFrame extends JFrame implements KeyListener, ActionListener {
    private GamePanel contentPanel;
    private Timer timer;
    private int[] strokes;

    public GameFrame() {
        strokes = new int[8];

        // w
        // a
        // s
        // d
        // up_arrow
        // left_arrow
        // down_arrow
        // right_arrow

        timer = new Timer(16, this);
        timer.setInitialDelay(0);
        timer.start();

        contentPanel = new GamePanel();
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.cyan);

        contentPanel.addObject(0, 450, 500, 20, 0, 0, 0, 0);

        contentPanel.addObject(100, 350, 20, 30, 1, 0, 0, 0);
        contentPanel.addObject(400, 350, 20, 30, 1, 1, 0, 0);

        this.setContentPane(contentPanel);

        this.setSize(500, 500);
        this.setVisible(true);
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w' && contentPanel.aboveGround(0) == false) {
            strokes[0] = 1;
        }
        if (e.getKeyChar() == 'a') {
            strokes[1] = 1;
        }
        if (e.getKeyChar() == 's') {
            strokes[2] = 1;
        }
        if (e.getKeyChar() == 'd') {
            strokes[3] = 1;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            strokes[0] = 0;
        }
        if (e.getKeyChar() == 'a') {
            strokes[1] = 0;
        }
        if (e.getKeyChar() == 's') {
            strokes[2] = 0;
        }
        if (e.getKeyChar() == 'd') {
            strokes[3] = 0;
        }

        if (e.getKeyChar() == 'c') {
            contentPanel.attack(0);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (strokes[0] == 1) {
            contentPanel.updateVelocity(0, 9, 1, 0);
            strokes[0] = 0;
        }
        if (strokes[1] == 1) {
            contentPanel.updateVelocity(-7, 0, 1, 0);
        }
        if (strokes[2] == 1) {
            // contentPanel.updatePos(1, 0, 0, -10);
        }
        if (strokes[3] == 1) {
            contentPanel.updateVelocity(7, 0, 1, 0);
        }

        if (e.getSource() == timer) {
            contentPanel.check();
            repaint();
        }
    }
}
