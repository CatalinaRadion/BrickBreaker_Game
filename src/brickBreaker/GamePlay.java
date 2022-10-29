package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    // KeyListener -> detects arrow keys for moving the slider
    // ActionListener -> detects the movement of the ball
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 600;

    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 1; //how fast the ball moves

    private int playerX = SCREEN_WIDTH/2; //slider
    private int ballX = (int)SCREEN_WIDTH/4;
    private int ballY = SCREEN_HEIGHT/2;
    private int ballDirectionX = -1;
    private int ballDirectionY = -2;

    private MapGenerator mapGenerator;

    public GamePlay() {
        mapGenerator = new MapGenerator(3,7);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        startGame();
    }

    public void startGame() {
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1,1, SCREEN_WIDTH, SCREEN_HEIGHT);
        //borders
        g.setColor(Color.YELLOW);
        g.fillRect(0,0,3,SCREEN_HEIGHT);
        g.fillRect(0,0,SCREEN_WIDTH,3);
        g.fillRect(SCREEN_WIDTH-3,0,3,SCREEN_HEIGHT);
        //the paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, SCREEN_HEIGHT-100, 100, 8);
        //the ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballX, ballY, 20, 20);
        //drawing bricks
        mapGenerator.draw((Graphics2D)g);
        //scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
//        g.drawString(""+score, 590, 30);
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + score,
                SCREEN_WIDTH - metrics.stringWidth("Score: " + score) - 10,
                g.getFont().getSize());
        //Game Over
        if(ballY>570) {
            play = false;
            timer.stop();
            ballDirectionY = 0;
            ballDirectionX = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            g.drawString("Game Over, Scores: "+score, 190, 300);

            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 230, 350);
        }
        //Game Win
        if(totalBricks <= 0) {
            play = false;
            timer.stop();
            ballDirectionY = 0;
            ballDirectionX = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            g.drawString("You Won, Scores: "+score, 190, 300);

            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Press Enter to Restart "+score, 230, 350);
        }

        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        moveBall();
        repaint();
    }

    public void moveBall() {
        if(play) {
            if(new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(playerX, SCREEN_HEIGHT-100, 100, 8))) {
                ballDirectionY = -ballDirectionY;
            }
            brickDestroyed();
            ballX += ballDirectionX;
            ballY += ballDirectionY;
            if(ballX < 0) {
                ballDirectionX = -ballDirectionX;
            }
            if(ballY < 0) {
                ballDirectionY = -ballDirectionY;
            }
            if(ballX > 670) {
                ballDirectionX = -ballDirectionX;
            }
        }
    }

    public void brickDestroyed() {
        A: for(int i = 0; i < mapGenerator.map.length; i++) {
            for(int j = 0; j<mapGenerator.map[0].length; j++) {
                if(mapGenerator.map[i][j] > 0) { //detect intersection
                    int brickX = j * mapGenerator.brickWidth + 80;
                    int brickY = i * mapGenerator.brickHeight + 50;
                    int brickWidth = mapGenerator.brickWidth;
                    int brickHeight = mapGenerator.brickHeight;

                    Rectangle rectangle = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                    Rectangle ballRectangle = new Rectangle(ballX, ballY, 20, 20);
                    Rectangle brickRectangle = rectangle;

                    if (ballRectangle.intersects(brickRectangle)) {
                        mapGenerator.setBrickValue(0, i, j); //destroy brick
                        totalBricks--;
                        score++;
                        //left and right intersection
                        if (ballX + 19 <= brickRectangle.x || ballX + 1 >= brickRectangle.x + brickRectangle.width) {
                            ballDirectionX = -ballDirectionX;
                        } else {
                            ballDirectionY = -ballDirectionY;
                        }
                        break A;
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;
                ballX = 120;
                ballY = 350;
                ballDirectionX = -1;
                ballDirectionY = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                mapGenerator = new MapGenerator(3,7);
                startGame();
                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 10;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
