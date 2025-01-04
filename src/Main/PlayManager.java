package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import Mino.Block;
import Mino.Mino;
import Mino.Mino_Bar;
import Mino.Mino_L1;
import Mino.Mino_L2;
import Mino.Mino_Square;
import Mino.Mino_T;
import Mino.Mino_Z1;
import Mino.Mino_Z2;

public class PlayManager {
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();
//    boolean start = false;
    boolean gameOver;

    public static int dropInterval = 60; //drop in every 60 frames

    // Effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();
    ArrayList<Color> effectColors = new ArrayList<>();
    final int EFFECT_DURATION = 30; // 30 frames

    //level n score
    int level = 1;
    int lines;
    int score;

//    public void startGame() {
//        start = true;
//    }
//
//    public boolean isStart() {
//        return start;
//    }

    public PlayManager() {
        // Main Play Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); // 1280/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 180;
        NEXTMINO_Y = top_y + 500;

        // Set the starting Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    private Mino pickMino() {

        // Pick a random mino
        Mino mino = null;
        int i = new Random().nextInt(7);

        mino = switch (i) {
            case 0 -> new Mino_L1();
            case 1 -> new Mino_L2();
            case 2 -> new Mino_Square();
            case 3 -> new Mino_Bar();
            case 4 -> new Mino_T();
            case 5 -> new Mino_Z1();
            case 6 -> new Mino_Z2();
            default -> mino;
        };
        return mino;
    }

    public void update() {
        // Check if the currentMino is active
        if (!currentMino.active) {
            // if the mino is not active, put it into the staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // check the game is over
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.soundEffect.play(2, false);
            }

            currentMino.deactivating = false;

            // replace the currentMino with the nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            checkDelete();
        } else {
            currentMino.update();
        }
    }

    private void checkDelete() {

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {

            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    // increase the count if there is a static block
                    blockCount++;
                }
            }

            x += Block.SIZE;

            if (x == right_x) {

                if (blockCount == 12) {
                    effectCounterOn = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        // remove all the blocks in the current y line
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;
                    // Drop Speed
                    // if the line score hits a certain number, increase the drop speed
                    // 1 is the fastest
                    if (lines % 10 == 0 && dropInterval > 1) {
                        level++;
                        if (dropInterval > 10) {
                            dropInterval -= 10;
                        } else {
                            dropInterval -= 1;
                        }
                    }

                    // a line has been deleted so need to slide down blocks that are above it
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        // if a block is above the current y, move it down by the block size
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }

        // Add Score
        if (lineCount > 0) {
            GamePanel.soundEffect.play(1, false);
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }

    public void draw(Graphics2D g2) {

        //Draw area frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        //Draw next mino frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);


        // Draw Score Frame
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + lines, x, y);
        y += 70;
        g2.drawString("LINES: " + lines, x, y);
        y += 70;
        g2.drawString("SCORE: " + score, x, y);

        //Draw current Mino
        if (currentMino != null) {
            currentMino.draw(g2);
        }
        //draw the next mino
        nextMino.draw(g2);
        //draw static block
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }
        //draw effect
        if (effectCounterOn) {
            effectCounter++;

            g2.setColor(Color.red);
            for (int i = 0; i < effectY.size(); i++) {
                g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);

            }

            if (effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }
//        if (effectCounterOn) {
//            effectCounter++;
//            for (int i = 0; i < effectY.size(); i++) {
//                int lineY = effectY.get(i);
//                Color color = effectColors.get(i);
//
//                int alpha = Math.max(0, 255 - (effectCounter * 255 / EFFECT_DURATION));
//                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
//                g2.fillRect(left_x, lineY, WIDTH, Block.SIZE);
//            }
//
//            if (effectCounter == EFFECT_DURATION) {
//                effectCounterOn = false;
//                effectCounter = 0;
//                effectY.clear();
//                effectColors.clear();
//            }
//        }

        //draw start & paused
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));

        if (!GamePanel.start) {
            x = left_x + 60;
            y = top_y + 320;
            g2.drawString("New game", x, y);
            x = left_x + 90;
            y = top_y + 370;
            g2.setFont(g2.getFont().deriveFont(32f));
            g2.drawString("Enter to start", x, y);
        } else {
            if (gameOver) {
                GamePanel.history.saveScore(score);
                x = left_x + 25;
                y = top_y + 320;
                g2.drawString("GAME OVER", x, y);
            } else if (KeyHandler.pausePressed) {
                x = left_x + 70;
                y = top_y + 320;
                g2.drawString("PAUSED", x, y);
            }
        }

        // Draw the Game Highest Score:
        x = 35;
        y = top_y + 120;
        g2.setColor(Color.white);
        g2.setFont(new Font("Verdana", Font.ITALIC, 32));
        g2.drawString("Highest record: " + GamePanel.history.getHighestScore(), x + 20, y);

        // Draw the Game Title
        x = 35;
        y = top_y + 320;
        g2.setFont(new Font("Verdana", Font.BOLD, 60));
        g2.drawString("Tetris", x + 20, y);

    }

}
