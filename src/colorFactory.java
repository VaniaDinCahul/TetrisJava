import java.awt.*;
import java.util.Random;
import java.util.random.*;

public class colorFactory {

    public Color[][] colorPallet = new Color[][]{
            { // RED color id 0
                    new Color(108, 21, 56),
                    new Color(177, 34, 59),
                    new Color(218, 64, 68),
                    new Color(131, 30, 62)
            },
            { // Yellow color id 1
                    new Color(108, 88, 21),
                    new Color(177, 144, 34),
                    new Color(218, 174, 64),
                    new Color(131, 107, 30)
            },
            { // Green color id 2
                    new Color(46, 108, 21),
                    new Color(70, 177, 34),
                    new Color(108, 218, 64),
                    new Color(75, 131, 30)
            },
            { // Pink color id 3
                    new Color(89, 21, 108),
                    new Color(158, 34, 177),
                    new Color(190, 64, 218),
                    new Color(118, 30, 131)
            },
            { // Blue color id 4
                    new Color(21, 35, 108),
                    new Color(34, 58, 177),
                    new Color(64, 90, 218),
                    new Color(30, 43, 131)
            },
            { // Cyan color id 5
                    new Color(21, 108, 101),
                    new Color(34, 177, 160),
                    new Color(64, 218, 200),
                    new Color(30, 131, 119)
            },
            { // purple color id 6
                    new Color(46, 21, 108),
                    new Color(82, 34, 177),
                    new Color(115, 64, 218),
                    new Color(70, 30, 131)
            }
    };

    public int generateColor(){
        Random rand = new Random();

        return rand.nextInt(colorPallet.length);
    }
}
