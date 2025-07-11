import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class gamePanel extends JPanel implements ActionListener {
    static final int X_GRID = 9;
    static final int Y_GRID = 20;
    static final int GRID_SIZE = 25;
    static final int WINDOW_HEIGHT = Y_GRID*GRID_SIZE;
    static final int WINDOW_WIDTH = X_GRID*GRID_SIZE;
    int DELAY = 250;
    static int FORCE_DOWN_DELAY = 50;

    static Timer timer;
    int[][] newPart;
    static int[][] map = new int[X_GRID][Y_GRID];

    boolean isRunning;
    static boolean spawnTick = true;
    static boolean cosmeticPoint;
    static boolean moveTick;


    // Constructor
    gamePanel(){
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter());
        isRunning = true;
        initGame();
        newPart();
    }


    // Initializes the timer that controls the game
    public void initGame(){
        timer = new Timer(DELAY, this);
        timer.start();
    }


    //Makes a new block and places it
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
        timer.setDelay(DELAY);
    }


    //Turn all the current movable parts into unmovable
    public void turnMovingPart() {
        for (int x = 0; x < map.length; x++){
            for (int y = 0; y< map[0].length; y++){
                if (map[x][y] == 2&&spawnTick){
                    map[x][y] = 1;
                    newPart();
                    spawnTick = true;
                    System.out.println("3");
                }
            }
        }
    }


    // Checks if there is anything under each part
    public void collisionCheck() {
        for (int y = map[0].length; y >= 0; y--){
            for (int[] ints : map) {
                if (y + 1 < map[0].length) {
                    if (ints[y] == 2) {
                        if (ints[y + 1] == 1) {
                            turnMovingPart();
                            break;
                        }
                    }
                }
            }
        }
    }


    // Moves the block down if possible, otherwise creates a new one
    public void movePart() {
        for (int y = map[0].length; y >= 0; y--) {
            for (int x = 0; x < map.length; x++) {
                if (y < map[0].length){ //if it's in bounds
                    if ((map[x][y] == 2)&&(map[x][y+1] == 0)){

                        map[x][y+1] = map[x][y];
                        map[x][y] = 0;
                    } else if((map[x][y] == 2)&&(map[x][y+1] == 1)&&(spawnTick)) {
                        map[x][y] = 1;
                        newPart();
                        spawnTick = true;
                        System.out.println("2");
                    }
                } else {
                    if (map[x][y-1] == 2&&spawnTick){
                        map[x][y-1] = 1;
                        newPart();
                        spawnTick = true;
                        System.out.println("1");
                    }
                }
            }
        }
    }


    // Moves each piece either left or right
    public static void moveHorizontal(char d){
        if (d == 'r') {
            for (int y = map[0].length - 1; y >= 0; y--) {
                for (int x = map.length - 1; x >= 0; x--) {
                    int movableBlocks = 0;
                    if ((map[x][y] == 2)&&(x + 1 + movableBlocks < map.length)) {
//                        System.out.println("on the right: "+map[x+1][y]);
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


    // forces down all blocks to the highest point
    static public void forceDown(){
        timer.setDelay(FORCE_DOWN_DELAY);
    }

    static public void cosmeticPointRow(int y){
        for (int iy = map[0].length-1; iy >= 0; iy--){
            for(int x = map.length-1; x >= 0; x--){
                if (iy == y){
                    map[x][iy] = 4;
                }
            }
        }
    }

    static public void clearRow(int y){
        for (int iy = map[0].length-1; iy >= 0; iy--){
            for(int x = map.length-1; x >= 0; x--){
                if (iy == y){
                    map[x][iy] = 0;
                }
                if (iy < y){
                    map[x][iy + 1] = map[x][iy];
                }
            }
        }
    }


    static public void pointCheck(){

        for (int y = 0; y < map[0].length; y++){
            int yup = 0;
            for (int x = 0; x < map.length; x++){
                if (map[x][y] == 1){
                    yup += 1;
                }
            }
            if (yup == X_GRID){
                cosmeticPoint = true;
                cosmeticPointRow(y);
                clearRow(y);
                cosmeticPoint = false;
            }
        }
    }


    // handles rendering
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    // drawing assigment
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
                } else if (map[x][y] == 4){
                    g.setColor(Color.WHITE);
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

    //Game logic, overrides the default actionPerformed
    @Override
    public void actionPerformed(ActionEvent e){
        if(isRunning&&!cosmeticPoint) {

            movePart();
            collisionCheck();
            pointCheck();
            moveTick = false;
        }
        repaint();
    }


    // Adds the key listener
    public static class KeyAdapter extends java.awt.event.KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if ((e.getKeyCode() == KeyEvent.VK_LEFT)&&(!moveTick)){
                System.out.println("moving to the left");
                moveHorizontal('l');
                moveTick = true;
            }
            if ((e.getKeyCode() == KeyEvent.VK_RIGHT)&&(!moveTick)){
                System.out.println("moving to the right");
                moveHorizontal('r');
                moveTick = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN){
                System.out.println("forcing down");
                forceDown();
            }
            if (e.getKeyCode() == KeyEvent.VK_UP){
                System.out.println("rotating 90 degrees");
            }
        }
    }
}
