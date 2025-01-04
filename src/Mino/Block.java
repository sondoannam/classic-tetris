package Mino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block extends Rectangle {
    public int x, y;
    public static final int SIZE = 30; // 30x30 block
    public Color c;

    public Block(Color c) {
        this.c = c;
    }

    public void draw(Graphics2D g2) {
        // Main block fill
        g2.setColor(c);
        g2.fillRect(x, y, SIZE, SIZE);

        // Define colors for highlights and shadows
        Color fillColor = c;
        Color topColor = fillColor.brighter().brighter(); // Much lighter for the top
        Color sideColor = fillColor.darker();            // Slightly darker for left and right
        Color bottomColor = fillColor.darker().darker().darker(); // Much darker for the bottom

        // Draw top highlight
        g2.setPaint(topColor);
        g2.fillRect(x, y, SIZE, 1);
        g2.fillRect(x + 1, y + 1, SIZE - 2, 1);
        g2.fillRect(x + 2, y + 2, SIZE - 4, 1);
        g2.fillRect(x + 3, y + 3, SIZE - 6, 1);

        // Draw right shadow
        g2.setPaint(sideColor);
        g2.fillRect(x + SIZE - 1, y, 1, SIZE);
        g2.fillRect(x + SIZE - 2, y + 1, 1, SIZE - 2);
        g2.fillRect(x + SIZE - 3, y + 2, 1, SIZE - 4);
        g2.fillRect(x + SIZE - 4, y + 3, 1, SIZE - 6);
        g2.fillRect(x + SIZE - 5, y + 4, 1, SIZE - 8);

        // Draw left highlight
        g2.setPaint(sideColor);
        g2.fillRect(x, y, 1, SIZE);
        g2.fillRect(x + 1, y + 1, 1, SIZE - 2);
        g2.fillRect(x + 2, y + 2, 1, SIZE - 4);
        g2.fillRect(x + 3, y + 3, 1, SIZE - 6);
        g2.fillRect(x + 4, y + 4, 1, SIZE - 8);

        // Draw bottom shadow
        g2.setPaint(bottomColor);
        g2.fillRect(x, y + SIZE, SIZE, 1);
        g2.fillRect(x + 1, y + SIZE - 1, SIZE - 2, 1);
        g2.fillRect(x + 2, y + SIZE - 2, SIZE - 4, 1);
        g2.fillRect(x + 3, y + SIZE - 3, SIZE - 6, 1);
        g2.fillRect(x + 4, y + SIZE - 4, SIZE - 8, 1);

    }
} 
