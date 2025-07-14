import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class gamePanelNew extends JPanel implements ActionListener {
    static int X_GRID = 10; // squares
    static int Y_GRID = 20; // squares
    static int GRID_SIZE = 35; // px
    static int GAME_MENU_SIZE = GRID_SIZE*3; // px
    static int BORDER_MARGIN = 2;
    static Font MENU_FONT = new Font("Arial", Font.BOLD, 28);
    static Font SCORE_FONT = new Font("Arial", Font.BOLD, 12);


    int SCREEN_HEIGHT = Y_GRID*GRID_SIZE;
    int SCREEN_WIDTH = X_GRID*GRID_SIZE+GAME_MENU_SIZE;
    int GAME_DELAY = 250; // ms
    int DROPDOWN_GAME_DELAY = GAME_DELAY/10;
    boolean moveTick;


    blockFactory blockFactory;
    colorFactory colorFactory;
    int GAME_SCORE;
    static Timer timer;
    gameState GAME_STATE;
    Cell[][] map;

    // Constructor for this
    gamePanelNew(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        gameInit();
    }


    // Initializes game
    public void gameInit() {
        GAME_STATE = gameState.MAIN_MENU;
        map = new Cell[X_GRID][Y_GRID];
        blockFactory = new blockFactory();
        colorFactory = new colorFactory();

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                map[x][y] = new Cell();
            }
        }

        timer = new Timer(GAME_DELAY, this);
        timer.start();

        GAME_SCORE = 0;
        createBlock();
    }


    //Creates a new Block
    public void createBlock(){
        Cell[][] newBlock = blockFactory.generateBlock();
        int MIDDLE_GRID = X_GRID/2-1;

        for (int x = 0; x < newBlock.length; x++) {
            for (int y = 0; y < newBlock[x].length; y++) {
                if (newBlock[x][y].v == 2){
                    map[x+MIDDLE_GRID-1][y].v = newBlock[x][y].v;
                    map[x+MIDDLE_GRID-1][y].c = newBlock[x][y].c;
                }
            }
        }
        timer.setDelay(GAME_DELAY);
    }


    //Speeds up the game;
    public void forceBlockDown(){
        timer.setDelay(DROPDOWN_GAME_DELAY);
    }

    //Moves the block left or right
    public void horizontalMove(char d){
        int additionalBlocks = 0;
        if (!moveTick) return;
        if (d == 'r'){
            for (int x = map.length-1; x >= 0; x--) {
                for (int y = map[0].length-1; y >= 0; y--) {
                    if ((x+1+additionalBlocks < map.length)&&(map[x][y].v == 2)){
                        map[x+1][y].c = map[x][y].c;
                        map[x+1][y].v = 2;
                        map[x][y].v = 0;
                    }
                    if (map[x][y].v == 2){
                        additionalBlocks += 1;
                    }
                }
            }
        } else {
            for (int x = 0; x < map.length; x++) {
                for (int y = map[0].length-1; y >= 0; y--) {
                    if ((x-1-additionalBlocks >= 0)&&(map[x][y].v == 2)){
                        map[x-1][y].c = map[x][y].c;
                        map[x-1][y].v = 2;
                        map[x][y].v = 0;
                    }
                    if (map[x][y].v == 2){
                        additionalBlocks += 1;
                    }
                }
            }
        }
        moveTick = false;
    }


    //Ticks all the blocks down
    public void VerticalMove() {
        for (int y = map[0].length-1; y >= 0; y--){
            for (int x = map.length-1; x >= 0; x--) {
                if (map[x][y].v == 2){
                    if (y+1 <= map[0].length-1){
                        map[x][y+1].c = map[x][y].c;
                        map[x][y+1].v = 2;
                        map[x][y].v = 0;
                    }
                }
            }
        }
    }


    // Anchors all movable blocks
    public void anchorAllBlocks() {
        for (Cell[] cells : map) {
            for (int y = 0; y < map[0].length; y++) {
                if (cells[y].v == 2) {
                    cells[y].v = 1;
                }
            }
        }
        createBlock();
    }


    // clears a row
    public void clearRow(int y){
        for (int x = 0; x < map.length; x++) {
            map[x][y].v = 0;
        }
    }


    //Shifts down all rows from this point
    public void shiftRowDown(int y){
        for (int iy = y-1; iy >= 0 ; iy--) {
            System.out.println(iy);
            for (int x = 0; x < map.length; x++) {
                if (iy+1 < map[0].length) {
                    map[x][iy+1].v = map[x][iy].v;
                    map[x][iy+1].c = map[x][iy].c;
                    map[x][iy].v = 0;
                }
            }
        }
    }


    //Checks all rows and removes any if needed
    public void pointCheck(){
        for (int y = map[0].length-1; y >= 0; y--) {
            int filledBlocks = 0;
            for (Cell[] Cells : map) {
                if (Cells[y].v == 1) filledBlocks++;
            }

            if (filledBlocks == X_GRID) {
                GAME_SCORE += 500;
                clearRow(y);
                shiftRowDown(y);
                y++;
            }
        }
    }


    // Checks for collisions
    public void collisionCheck() {
        for (int y = map[0].length-1; y >= 0; y--){
            for (int x = map.length-1; x >= 0; x--) {
                if (map[x][y].v == 2){
                    if ((y+1 < map[0].length)&&(map[x][y+1].v==1)){
                        anchorAllBlocks();
                        System.out.println("Moving block hit a anchored Block");
                    }
                    if (y+1 > map[0].length-1){
                        anchorAllBlocks();
                        System.out.println("Moving block reached the end");
                    }
                }
            }
        }
    }


    // Super element to draw graphics
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }


    // Instructions for the graphics
    public void draw(Graphics g){
        FontMetrics fm;
        if (GAME_STATE == gameState.MAIN_MENU) {
            Dimension SCREEN_CENTER = new Dimension(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
            setBackground(Color.BLACK);

            fm = g.getFontMetrics(MENU_FONT);
            g.setColor(Color.BLUE);
            g.setFont(MENU_FONT);
            g.drawString("TETRIS",SCREEN_CENTER.width-(fm.stringWidth("TETRIS")/2),SCREEN_CENTER.height/2);

            fm = g.getFontMetrics(SCORE_FONT);
            g.setColor(Color.GRAY);
            g.setFont(SCORE_FONT);
            g.drawString(
                    "Press",
                    SCREEN_CENTER.width-(fm.stringWidth(" SPACE ")/2)-(fm.stringWidth("Press")),
                    SCREEN_CENTER.height);
            g.setColor(Color.RED);
            g.drawString(
                    " SPACE ",
                    SCREEN_CENTER.width-(fm.stringWidth(" SPACE ")/2),
                    SCREEN_CENTER.height);
            g.setColor(Color.GRAY);
            g.drawString(
                    "to Play",
                    SCREEN_CENTER.width+(fm.stringWidth(" SPACE ")/2),
                    SCREEN_CENTER.height);
        }

        // If the game is running
        fm = g.getFontMetrics(SCORE_FONT);
        if (GAME_STATE == gameState.RUNNING || GAME_STATE == gameState.PAUSED){

            // Generates the game blocks
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[x].length; y++) {
                    if (map[x][y].v != 0){
                        g.setColor(colorFactory.colorPallet[map[x][y].c][0]);
                        g.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
                        g.setColor(colorFactory.colorPallet[map[x][y].c][1]);
                        g.fillRect(x*GRID_SIZE+BORDER_MARGIN, y*GRID_SIZE+BORDER_MARGIN, GRID_SIZE-BORDER_MARGIN*2, GRID_SIZE-BORDER_MARGIN*2);

                        Polygon topTriangle = new Polygon();
                        Polygon bottomTriangle = new Polygon();

                        bottomTriangle.addPoint(x*GRID_SIZE+BORDER_MARGIN, ((y+1)*GRID_SIZE)-BORDER_MARGIN);
                        bottomTriangle.addPoint(((x+1)*GRID_SIZE)-BORDER_MARGIN,(y+1)*GRID_SIZE-BORDER_MARGIN);
                        bottomTriangle.addPoint(x*GRID_SIZE+GRID_SIZE/2,y*GRID_SIZE+GRID_SIZE/2);

                        topTriangle.addPoint(x*GRID_SIZE+BORDER_MARGIN, y*GRID_SIZE+BORDER_MARGIN);
                        topTriangle.addPoint((x+1)*GRID_SIZE-BORDER_MARGIN*2,y*GRID_SIZE+BORDER_MARGIN) ;
                        topTriangle.addPoint(x*GRID_SIZE+GRID_SIZE/2,y*GRID_SIZE+GRID_SIZE/2);

                        g.setColor(colorFactory.colorPallet[map[x][y].c][2]);
                        g.fillPolygon(topTriangle);

                        g.setColor(colorFactory.colorPallet[map[x][y].c][3]);
                        g.fillPolygon(bottomTriangle);
                    } else {
                        g.setColor(new Color(27, 27, 27));
                        g.fillRect(x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);

                        //Order is : 1. left, right, top
                        Polygon topTriangle = new Polygon();
                        Polygon bottomTriangle = new Polygon();

                        bottomTriangle.addPoint((x*GRID_SIZE)+(GRID_SIZE/2), ((y+1)*GRID_SIZE));
                        bottomTriangle.addPoint(((x+1)*GRID_SIZE),(y+1)*GRID_SIZE);
                        bottomTriangle.addPoint(((x+1)*GRID_SIZE),y*GRID_SIZE+GRID_SIZE/2);

                        topTriangle.addPoint(x*GRID_SIZE, y*GRID_SIZE);
                        topTriangle.addPoint(x*GRID_SIZE+GRID_SIZE/2,y*GRID_SIZE) ;
                        topTriangle.addPoint(x*GRID_SIZE, y*GRID_SIZE+GRID_SIZE/2);

                        g.setColor(new Color(35, 35, 35));
                        g.fillPolygon(topTriangle);

                        g.setColor(new Color(21, 21, 21));
                        g.fillPolygon(bottomTriangle);
                    }
                }
            }

            //Generates the Vertical Lines
            for (int x = 0; x < map.length; x++) {
                if (x%2!=0){
                    g.setColor(new Color(54, 54, 54));
                } else {
                    g.setColor(new Color(14, 14, 14));
                }

                g.drawLine(x*GRID_SIZE, 0,x*GRID_SIZE,SCREEN_HEIGHT);
            }

            // Generates the Horizontal Lines
            for (int y = 0; y < map[0].length; y++) {
                if (y %2!=0){
                    g.setColor(new Color(54, 54, 54));
                    if (y==3) {
                        g.setColor(new Color(154, 24, 24));
                    }
                } else {
                    g.setColor(new Color(14, 14, 14));
                }
                g.drawLine(0, y*GRID_SIZE, SCREEN_WIDTH-GAME_MENU_SIZE, y*GRID_SIZE);
            }

            // Generates the Right-hand Menu
            for (int x = 0; x < GAME_MENU_SIZE/GRID_SIZE; x++) {
                for (int y = 0; y < SCREEN_HEIGHT; y++) {
                    g.setColor(new Color(89, 89, 89));
                    g.fillRect((SCREEN_WIDTH-GAME_MENU_SIZE)+x*GRID_SIZE, y*GRID_SIZE, GRID_SIZE, GRID_SIZE);

                    //Order is : 1. left, right, top
                    Polygon topTriangle = new Polygon();
                    Polygon bottomTriangle = new Polygon();

                    bottomTriangle.addPoint(((SCREEN_WIDTH-GAME_MENU_SIZE)+x*GRID_SIZE)+(GRID_SIZE/2), ((y+1)*GRID_SIZE));
                    bottomTriangle.addPoint(((SCREEN_WIDTH-GAME_MENU_SIZE)+(x+1)*GRID_SIZE),(y+1)*GRID_SIZE);
                    bottomTriangle.addPoint(((SCREEN_WIDTH-GAME_MENU_SIZE)+(x+1)*GRID_SIZE),y*GRID_SIZE+GRID_SIZE/2);

                    topTriangle.addPoint((SCREEN_WIDTH-GAME_MENU_SIZE)+x*GRID_SIZE, y*GRID_SIZE);
                    topTriangle.addPoint((SCREEN_WIDTH-GAME_MENU_SIZE)+x*GRID_SIZE+GRID_SIZE/2,y*GRID_SIZE) ;
                    topTriangle.addPoint((SCREEN_WIDTH-GAME_MENU_SIZE)+x*GRID_SIZE, y*GRID_SIZE+GRID_SIZE/2);

                    g.setColor(new Color(99, 99, 99));
                    g.fillPolygon(topTriangle);

                    g.setColor(new Color(79, 79, 79));
                    g.fillPolygon(bottomTriangle);
                }
            }

            // Generates the text
            g.setColor(Color.WHITE);
            g.setFont(SCORE_FONT);
            g.drawString(
                    "SCORE:",
                    SCREEN_WIDTH-(GAME_MENU_SIZE/2)-(fm.stringWidth("SCORE:")/2),
                    50);
            g.drawString(
                    Integer.toString(GAME_SCORE),
                    SCREEN_WIDTH-(GAME_MENU_SIZE/2)-(fm.stringWidth(Integer.toString(GAME_SCORE))/2),
                    50+fm.getHeight()+5
            );

            // Generates the pause menu if any

            if (GAME_STATE == gameState.PAUSED){
                g.setColor(new Color(28, 28, 28, 128));
                g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

                fm = g.getFontMetrics(MENU_FONT);
                g.setColor(Color.WHITE);
                g.setFont(MENU_FONT);
                g.drawString("PAUSE", SCREEN_WIDTH/2-fm.stringWidth("PAUSE"), SCREEN_HEIGHT/2-fm.getAscent());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (GAME_STATE == gameState.RUNNING){
            VerticalMove();
            collisionCheck();
            pointCheck();
//            failCheck();
            moveTick = true;
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter  {
        @Override
        public void keyPressed(KeyEvent e) {

            if (GAME_STATE==gameState.MAIN_MENU){
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    GAME_STATE = gameState.RUNNING;
                }
            }


            if (GAME_STATE == gameState.RUNNING){
                if (e.getKeyCode() == KeyEvent.VK_LEFT){
                    horizontalMove('l');
                }

                if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                    horizontalMove('r');
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN){
                    forceBlockDown();
                }
            }


            if (GAME_STATE == gameState.PAUSED || GAME_STATE == gameState.RUNNING){
                if (e.getKeyCode() == KeyEvent.VK_P){
                    if (GAME_STATE == gameState.RUNNING) GAME_STATE = gameState.PAUSED;
                    else GAME_STATE = gameState.RUNNING;
                }
            }
        }
    }
}