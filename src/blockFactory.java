import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class blockFactory {

    Random rand = new Random();
    colorFactory colorFactory = new colorFactory();

    final String[] SHAPES = {
            "o",
            "l",
            "i",
            "j",
            "l",
            "s",
            "z",
            "t"
    };

    List<String> shapeBag = new ArrayList<>(Arrays.asList(SHAPES));

    private void refillBag(){
        shapeBag.addAll(Arrays.asList(SHAPES));
        Collections.shuffle(shapeBag);
    }

    public Cell[][] generateBlock(){
        Collections.shuffle(shapeBag);
        Cell[][] newBlock = new Cell[3][3];

        int colorId = colorFactory.generateColor();

        for (int x = 0; x < newBlock.length; x++) {
            for (int y = 0; y < newBlock[0].length; y++) {
                newBlock[x][y] = new Cell();
                newBlock[x][y].v = 0;
                newBlock[x][y].c = colorId;
            }
        }

        String shape;
        Collections.shuffle(shapeBag);

        if (!shapeBag.isEmpty()){
            int value = rand.nextInt(shapeBag.size());
            shape = shapeBag.get(value);
            shapeBag.remove(shapeBag.get(value));
        } else {
            refillBag();
            int value = rand.nextInt(shapeBag.size());
            shape = shapeBag.get(value);
            shapeBag.remove(shapeBag.get(value));
        }
        System.out.println(shapeBag.size());

        if (shape.equals("o")){
            newBlock[0][0].v = 2;
            newBlock[0][1].v = 2;
            newBlock[1][0].v = 2;
            newBlock[1][1].v = 2;
        }

        if (shape.equals("l")){
            newBlock[0][0].v = 2;
            newBlock[0][1].v = 2;
            newBlock[1][1].v = 2;
            newBlock[2][1].v = 2;
        }

        if (shape.equals("i")){
            newBlock[1][0].v = 2;
            newBlock[1][1].v = 2;
            newBlock[1][2].v = 2;
        }

        if (shape.equals("j")){
            newBlock[2][0].v = 2;
            newBlock[0][1].v = 2;
            newBlock[1][1].v = 2;
            newBlock[2][1].v = 2;
        }

        if (shape.equals("s")){
            newBlock[0][1].v = 2;
            newBlock[1][1].v = 2;
            newBlock[1][0].v = 2;
            newBlock[2][0].v = 2;
        }

        if (shape.equals("z")){
            newBlock[0][0].v = 2;
            newBlock[1][0].v = 2;
            newBlock[1][1].v = 2;
            newBlock[2][1].v = 2;
        }

        if (shape.equals("t")){
            newBlock[0][0].v = 2;
            newBlock[1][0].v = 2;
            newBlock[2][0].v = 2;
            newBlock[1][1].v = 2;
        }

        return newBlock;
    }
}