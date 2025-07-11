import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class gamePanelNew extends JPanel implements ActionListener {

    static int X_GRID = 10; // squares
    static int Y_GRID = 20; // squares
    static int GRID_SIZE = 35; // px
    static int GAME_MENU_SIZE = 80; // px
    static Font MENU_FONT = new Font("Arial", Font.BOLD, 28);
    static Font SCORE_FONT = new Font("Arial", Font.BOLD, 12);

    int SCREEN_HEIGHT = Y_GRID*GRID_SIZE;
    int SCREEN_WIDTH = X_GRID*GRID_SIZE+GAME_MENU_SIZE;
    int GAME_DELAY = 500; // ms
    int DROPDOWN_GAME_DELAY = GAME_DELAY/10;


    static Timer timer;
    gameState GAME_STATE;
    int[][] map;
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
        map = new int[X_GRID][Y_GRID];

        timer = new Timer(GAME_DELAY, this);
        timer.start();
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
            g.setColor(Color.DARK_GRAY);
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
        if (GAME_STATE == gameState.RUNNING){
            // Render the Grid and Menu
            for (int x = 0; x < map.length; x++) {
                g.drawLine(x*GRID_SIZE, 0,SCREEN_WIDTH-GAME_MENU_SIZE,0);
            }
            for (int y = 0; y < map[0].length; y++) {

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (GAME_STATE == gameState.RUNNING){

        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter  {
        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                if (GAME_STATE==gameState.MAIN_MENU){
                    GAME_STATE = gameState.RUNNING;
                }
            }
        }
    }
}
