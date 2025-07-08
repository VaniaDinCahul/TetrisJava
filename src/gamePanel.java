import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class gamePanel extends JPanel implements ActionListener {
    static final int X_GRID = 10;
    static final int Y_GRID = 20;
    static final int GRID_SIZE = 25;
    static final int WINDOW_HEIGHT = Y_GRID*GRID_SIZE;
    static final int WINDOW_WIDTH = X_GRID*GRID_SIZE;
    static int DELAY = 250;

    Timer timer;
    int[][] newPart;
    static int[][] map = new int[X_GRID][Y_GRID];

    boolean isRunning;
    boolean isCollided;
    static boolean moveTick;

    gamePanel(){
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter());
        isRunning = true;
        initGame();
        newPart();
    }

    public void initGame(){
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void newPart() {
        int[][] part = {
                {1, 0, 0},
                {1, 0, 0},
                {1, 0, 0}
        };
        for (int x = 0; x < part.length; x++) {
            for (int y = 0; y < part[x].length; y++) {
                if (part[x][y] == 1) {
                    map[x][y] = 2;
                }
            }
        }
//        newPart = part;
    }

    public void movePart() {
        for (int y = map[0].length; y >= 0; y--) {
            for (int x = 0; x < map.length; x++) {
                if (y < map[0].length){ //if it's in bounds
                    if ((map[x][y] == 2)&&(map[x][y+1] == 0)){

                        map[x][y+1] = map[x][y];
                        map[x][y] = 0;
                    } else if((map[x][y] == 2)&&(map[x][y+1] == 1)) {
                        map[x][y] = 1;
                        newPart();
                    }
                } else {
                    if (map[x][y-1] == 2){
                        map[x][y-1] = 1;
                        newPart();
                    }
                }
            }
        }
    }

    public static void turnPart(char d){
        if (d == 'r') {
            for (int y = map[0].length - 1; y >= 0; y--) {
                for (int x = map.length - 1; x >= 0; x--) {
                    int movableBlocks = 0;
                    if ((map[x][y] == 2)&&(x + 1 + movableBlocks < map.length)) {
                        System.out.println("on the right: "+map[x+1][y]);
                        if (map[x+1][y] == 0){
                            map[x+1][y] = 2;
                            map[x][y] = 0;
                        }
                    }
                    if (map[x][y] == 2){
                        movableBlocks += 1;
                    }
                }
            }
        } else {
            for (int y = map[0].length - 1; y >= 0; y--) {
                for (int x = 0; x < map.length; x++) {
                    int movableBlocks = 0;
                    if ((map[x][y] == 2)&&(x - 1 - movableBlocks >= 0)) {
                        // ON THE SUBJECT OF BUGS... IT DUPLICATES
                        if (map[x-1][y] == 0){
                            map[x-1][y] = 2;
                            map[x][y] = 0;
                        }
                    }
                    if (map[x][y] == 2){
                        movableBlocks += 1;
                    }
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        for (int x = 0; x < X_GRID; x++) {
        g.drawLine(x*GRID_SIZE, 0, x*GRID_SIZE, WINDOW_HEIGHT);
        }
        for (int y = 0; y < Y_GRID; y++) {
        g.drawLine(0, y*GRID_SIZE, WINDOW_WIDTH, y*GRID_SIZE);
        }

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y] == 1) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
                } else if (map[x][y] == 2) {
                    g.setColor(Color.RED);
                    g.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
                }
            }
        }
        if (newPart != null){
            for (int x = 0; x < newPart.length; x++) {
                for (int y = 0; y < newPart[x].length; y++) {
                    if (newPart[x][y] == 1) {
                        g.setColor(Color.RED);
                        g.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(isRunning) {
            movePart();
            moveTick = false;
        }
        repaint();
    }

    public static class KeyAdapter extends java.awt.event.KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if ((e.getKeyCode() == KeyEvent.VK_LEFT)&&(!moveTick)){
                System.out.println("moving to the left");
                turnPart('l');
                moveTick = true;
            }
            if ((e.getKeyCode() == KeyEvent.VK_RIGHT)&&(!moveTick)){
                System.out.println("moving to the right");
                turnPart('r');
                moveTick = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN){
                System.out.println("forcing down");
            }
            if (e.getKeyCode() == KeyEvent.VK_UP){
                System.out.println("rotating 90 degrees");
            }
        }
    }
}
