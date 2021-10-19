// import required packages
import javax.swing.JFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Random;

// main class - runs the class which creates frame for game
class snakeGame{
    public static void main(String[] args){
        // calling class which creates frame for game
        new GameFrame();
    }
}

// class which creates frame for game
class GameFrame extends JFrame{
    String title = "Snack Game";
    
    // constructor
    GameFrame(){
        // creating object for class which creates panel in frame
        GamePanel panel = new GamePanel();

        this.add(panel);
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

// class which creates panel for frame
class GamePanel extends JPanel implements ActionListener{
    static final int WIDTH = 1000;
    static final int HEIGHT = 600;

    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    int DELAY = 100;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 2;
    int applesEaten;
    int appleX;
    int appleY;
    
    char direction = 'R';
    
    boolean running = false;
    
    Timer timer;
    
    Random random;

    // initializing method to start game
    GamePanel(){
        random = new Random();

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        StartGame();
    }

    // method for starting the game
    public void StartGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g){
        if(running){
            // drawing apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // drawing snake
            for (int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // drawing score board
            g.setColor(Color.green);
            g.setFont( new Font("Ink Free",Font.BOLD,40));
            
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        }

        else{
            gameOver(g);
        }
    }

    // method for creating new apple
    public void newApple(){
        appleX = random.nextInt((int)(WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    // method for moving the snake
    public void move(){
        // updating snake size
        for (int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break; 
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break; 
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break; 
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    // method to check if the apple is eaten by snake or not
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            newApple();
            bodyParts++;
            applesEaten++;
        }
    }

    // method to check the collision of snake
    public void checkCollision(){
        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;  
            }
        }

        //check if head touches left border
        if(x[0] < 0){
            running= false;
        }

        //check if head touches right border
        if(x[0] > WIDTH){
            running= false;
        }

        //check if head touches TOP border
        if(y[0] < 0){
            running= false;
        }

        //check if head touches bottom border
        if(y[0] > HEIGHT){
            running= false;
        }

        if(!running){
            timer.stop();
        }
    }

    // method for gameover
    public void gameOver(Graphics g){
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (WIDTH - metrics.stringWidth("GAME OVER"))/2, HEIGHT/2);

        g.setColor(Color.green);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
    }

    // method for changing speed of the snake based on the score
    public void changeSpeed(){
        if (applesEaten >= 5 && applesEaten < 10){
            DELAY = 80;
        }

        else if (applesEaten  >= 10 && applesEaten < 15){
            DELAY = 70;
        }

        else if (applesEaten  >= 15 && applesEaten < 20){
            DELAY = 60;
        }

        else if (applesEaten  >= 20 && applesEaten < 25){
            DELAY = 50;
        }

        else if (applesEaten  >= 25 && applesEaten < 30){
            DELAY = 40;
        }

        else if (applesEaten  >= 40 && applesEaten < 45){
            DELAY = 30;
        }

        else if (applesEaten  >= 45 && applesEaten < 50){
            DELAY = 20;
        }

        else{
            DELAY = 100;
        }

        timer.stop();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e){ 
        if(running){
            move();
            checkApple();
            checkCollision();
            changeSpeed();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                if(direction != 'R'){
                    direction = 'L';
                }
                break;

            case KeyEvent.VK_RIGHT:
                if(direction != 'L'){
                    direction = 'R';
                }
                break;

            case KeyEvent.VK_UP:
                if(direction != 'D'){
                    direction = 'U';
                }
                break;

            case KeyEvent.VK_DOWN:
                if(direction != 'U'){
                    direction = 'D';
                }
                break;

            case KeyEvent.VK_ENTER:
                if(!running){
                    new GameFrame();
                }
            }
        }
    }
}
