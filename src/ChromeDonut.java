import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

class ChromeDonut extends JPanel {

    private boolean gameOver = false;
    private int score = 0;
    private int highScore = 0;
    private int increase = 0;
    private boolean gameStarted = false;
    private boolean isJumping = false;
    private boolean running = false;
    private Mob donut;
    private ArrayList<Mob> obstacles;

    Random random = new Random();

    public ChromeDonut() {
        setBackground(Color.lightGray);
        setFocusable(true);

        donut = new Mob(Constants.donutX, Constants.donutY, Constants.donutWidth,
                Constants.donutHeight, Images.runningDonut);

        obstacles = new ArrayList<>();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((!gameStarted || gameOver) && (e.getKeyCode() == KeyEvent.VK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_UP)) {
                    restartGame();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
                    jump();
                }
            }
        });
    }

    public void jump() {
        if (!isJumping && !gameOver && gameStarted) {
            isJumping = true;
            Constants.velocityY = Constants.jumpStrength;
            donut.setIcon(Images.staticDonut);
        }
    }

    public void restartGame() {
        gameStarted = true;
        gameOver = false;
        running = true;
        score = 0;
        Constants.velocityY = 0;
        donut.setY(Constants.donutY);
        donut.setIcon(Images.runningDonut);
        obstacles.clear();
        gameLoop();
        obstaclePlacement();
        requestFocus();
    }

    public void gameLoop(){
        new Thread(() -> {
            while (!gameOver && gameStarted) {
                move();
                repaint();
                try {
                    Thread.sleep(1000 / 55);
                } catch (InterruptedException e) {}
            }
            if (gameOver) {
                stopThread();
            }
        }).start();
    }

    public void obstaclePlacement(){
        new Thread(() ->{
            while (running && !gameOver && gameStarted) {
                placeObstacle();
                if (increase <= 500){
                    increase = (score / 300) * 100;
                }
                int generate = random.nextInt(1300 - increase, 1701 - increase);
                try {
                    Thread.sleep(generate);
                } catch (InterruptedException e) {}
            }
        }).start();
    }

    public void stopThread() {
        running = false;
    }

    public void placeObstacle() {
        if (!(gameOver || !gameStarted)) {
            int chance = random.nextInt(1, 101);
            Mob obstacle;
            if (chance >= 75) {
                obstacle = new Mob(Constants.obstacleX, Constants.obstacleY, Constants.obstacle3Width,
                        Constants.obstacleHeight, Images.obstacle3Img);
            } else if (chance > 40) {
                obstacle = new Mob(Constants.obstacleX, Constants.obstacleY, Constants.obstacle2Width,
                        Constants.obstacleHeight, Images.obstacle2Img);
            } else {
                obstacle = new Mob(Constants.obstacleX, Constants.obstacleY, Constants.obstacle1Width,
                        Constants.obstacleHeight, Images.obstacle1Img);
            }
            obstacles.add(obstacle);

            if (obstacles.size() > 10) {
                obstacles.remove(0);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(donut.getIcon(), donut.getX(), donut.getY(), donut.getWidth(), donut.getHeight(), null);
        try {
            for (Mob obstacle : obstacles) {
                g.drawImage(obstacle.getIcon(), obstacle.getX(), obstacle.getY(), obstacle.getWidth(),
                        obstacle.getHeight(), null);
            }
        } catch (Exception e) {}
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 30));
        if (gameOver) {
            g.drawString("Game Over: " + score, Constants.SCORE_X_POSITION,
                    Constants.SCORE_Y_POSITION);
            g.drawString("HI " + highScore, Constants.SCORE_X_POSITION,
                    Constants.HIGH_SCORE_Y_POSITION);
            g.drawString("Press SPACE or UP to play again", Constants.INSTRUCTIONS_X_POSITION,
                    Constants.INSTRUCTIONS_Y_POSITION);
        } else {
            g.drawString(String.valueOf(score), Constants.SCORE_X_POSITION,
                    Constants.SCORE_Y_POSITION);
            g.drawString("HI " + highScore, Constants.SCORE_X_POSITION,
                    Constants.HIGH_SCORE_Y_POSITION);
            if (!gameStarted) {
                g.drawString("Press SPACE or UP to play", Constants.INSTRUCTIONS_X_POSITION,
                        Constants.INSTRUCTIONS_Y_POSITION);
            }
        }
    }

    public void move() {
        Constants.velocityY += Constants.gravity;
        donut.setY(donut.getY() + Constants.velocityY);

        if (donut.getY() > Constants.donutY) {
            donut.setY(Constants.donutY);
            Constants.velocityY = 0;
            isJumping = false;
            donut.setIcon(Images.runningDonut);
        }
        score++;

        for (Mob obstacle : obstacles) {
            obstacle.setX(obstacle.getX() + Constants.velocityX);
            if (collision(donut, obstacle)) {
                gameOver = true;
                highScore = Math.max(score, highScore);
                donut.setIcon(Images.bittenDonut);
                break;
            }
        }
    }

    public boolean collision(Mob a, Mob b) {
        return getAsRect(a).intersects(getAsRect(b));
    }

    public Rectangle getAsRect(Mob mob) {
        return new Rectangle(mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight());
    }
}