

public class CutFrame extends MyRectangle{
    CutFrame(){
        itemIndex = 7;
    }

    @Override
    public boolean contains(int x, int y) {
        boolean value = false;
        if((Math.abs(x - (a0 + a1)/2) < Math.abs((a0 - a1) / 2)) && ((Math.abs(y - (b0 + b1) / 2)) < Math.abs((b0 - b1) / 2))) value = true;
        if((Math.abs(x - (a0 + a1)/2) < Math.abs((a0 - a1) / 10 * 4)) && ((Math.abs(y - (b0 + b1) / 2)) < Math.abs((b0 - b1) / 10 * 4))) value = false;
        return value;
    }
}
