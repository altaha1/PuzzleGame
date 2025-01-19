package BlockPuzzle;

import java.util.ArrayList;
import java.util.List;

record Cell(int x, int y){

}

enum RegionType {
    ROW, COL, SUBSQUARE, PIECE
}

public class Shape extends ArrayList<Cell> {
    RegionType type;
    private List<Cell> cells;

    Shape(RegionType type, List<Cell> cells){
        super(cells);
        this.type = type;
    }

    public RegionType getType(){
        return type;
    }
//
}
