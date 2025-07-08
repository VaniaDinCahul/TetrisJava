import javax.swing.*;

public class gameFrame extends JFrame{
    gameFrame(){

        gamePanel gamePanel =  new gamePanel();
        this.add(gamePanel);
        this.setTitle("Tetris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}