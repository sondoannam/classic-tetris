package Mino;

import java.awt.*;
import java.util.logging.Handler;

import Main.GamePanel;
import Main.KeyHandler;
import Main.PlayManager;

public class Mino {
    public Block[] b = new Block[4];
    public Block[] tempB = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivateCounter = 0;


    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {
    }

    public void updateXY(int direction) {

        checkRotationCollision();

        if (!leftCollision && !rightCollision && !bottomCollision) {
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }

    }

    public void getDirection1() {
    }

    public void getDirection2() {
    }

    public void getDirection3() {
    }

    public void getDirection4() {
    }

    private void checkStaticBlockCollision() {
        // check left
        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) {
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            // check down
            for (int ii = 0; ii < b.length; ii++) {
                if (b[ii].y + Block.SIZE == targetY && b[ii].x == targetX) {
                    bottomCollision = true;
                }
            }
            // check left
            for (int ii = 0; ii < b.length; ii++) {
                if (b[ii].x - Block.SIZE == targetX && b[ii].y == targetY) {
                    leftCollision = true;
                }
            }
			// check right
            for (int ii = 0; ii < b.length; ii++) {
                if (b[ii].x + Block.SIZE == targetX && b[ii].y == targetY) {
                    rightCollision = true;
                }
            }
        }
    }

    public void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // Check frame collision
        // Left wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x == PlayManager.left_x) {
                leftCollision = true;
            }
        }

        //right wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x + Block.SIZE == PlayManager.right_x) {
                rightCollision = true;
            }
        }

        // Bottom floor
        for (int i = 0; i < b.length; i++) {
            if (b[i].y + Block.SIZE == PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    public void checkRotationCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // Check frame collision
        // Left wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < PlayManager.left_x) {
                leftCollision = true;
            }
        }

        //right wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x + Block.SIZE > PlayManager.right_x) {
                rightCollision = true;
            }
        }

        // Bottom floor
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    public void update() {

        if (deactivating) {
            deactivating();
        }

        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }
            KeyHandler.upPressed = false;
            GamePanel.soundEffect.play(3, false);
        }

        checkMovementCollision();

        if (KeyHandler.downPressed) {
            if (!bottomCollision) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                // When moved down, reset the autoDropCounter
                autoDropCounter = 0;
            }
            KeyHandler.downPressed = false;
        }

        if (KeyHandler.leftPressed) {
            if (!leftCollision) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }
            KeyHandler.leftPressed = false;
        }

        if (KeyHandler.rightPressed) {
            if (!rightCollision) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }
            KeyHandler.rightPressed = false;
        }

        if (bottomCollision) {
            if (!deactivating) GamePanel.soundEffect.play(4, false);
            deactivating = true;
        } else {
            autoDropCounter++;
            if (autoDropCounter == PlayManager.dropInterval) {
                // the mino goes down
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }

    }

    private void deactivating() {

        deactivateCounter++;

        // Wait 45 frames until diactivate
        if (deactivateCounter == 45) {

            deactivateCounter = 0;
            checkMovementCollision(); // check if the bottom is still hitting
            // if the bottom is still hitting after 45 frames, deactivate the mino
            if (bottomCollision) {
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < b.length; i++) {
            int x = b[i].x;
            int y = b[i].y;
            int size = Block.SIZE;

            // Main block fill
            g2.setColor(b[i].c);
            g2.fillRect(x, y, size, size);

            // Define colors for highlights and shadows
            Color fillColor = b[i].c;
            Color topColor = fillColor.brighter().brighter(); // Much lighter for the top
            Color sideColor = fillColor.darker();            // Slightly darker for left and right
            Color bottomColor = fillColor.darker().darker().darker(); // Much darker for the bottom

            // Draw top highlight
            g2.setPaint(topColor);
            g2.fillRect(x, y, size, 1);
            g2.fillRect(x + 1, y + 1, size - 2, 1);
            g2.fillRect(x + 2, y + 2, size - 4, 1);
            g2.fillRect(x + 3, y + 3, size - 6, 1);

            // Draw right shadow
            g2.setPaint(sideColor);
            g2.fillRect(x + size - 1, y, 1, size);
            g2.fillRect(x + size - 2, y + 1, 1, size - 2);
            g2.fillRect(x + size - 3, y + 2, 1, size - 4);
            g2.fillRect(x + size - 4, y + 3, 1, size - 6);
            g2.fillRect(x + size - 5, y + 4, 1, size - 8);

            // Draw left highlight
            g2.setPaint(sideColor);
            g2.fillRect(x, y, 1, size);
            g2.fillRect(x + 1, y + 1, 1, size - 2);
            g2.fillRect(x + 2, y + 2, 1, size - 4);
            g2.fillRect(x + 3, y + 3, 1, size - 6);
            g2.fillRect(x + 4, y + 4, 1, size - 8);

            // Draw bottom shadow
            g2.setPaint(bottomColor);
            g2.fillRect(x, y + size, size, 1);
            g2.fillRect(x + 1, y + size - 1, size - 2, 1);
            g2.fillRect(x + 2, y + size - 2, size - 4, 1);
            g2.fillRect(x + 3, y + size - 3, size - 6, 1);
            g2.fillRect(x + 4, y + size - 4, size - 8, 1);
        }
    }
}
