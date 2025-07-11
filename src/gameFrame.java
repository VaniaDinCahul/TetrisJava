import javax.swing.*;

public class gameFrame extends JFrame{
    gameFrame(){

        gamePanelNew gamePanel =  new gamePanelNew();
        this.add(gamePanel);
        this.setTitle("Tetris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}