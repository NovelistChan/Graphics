import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class MyCircle extends MyItem implements Construct, Contain {
    int xc, yc, r;
    MyCircle(){
        itemIndex = 2;
    }
    @Override public void constructShape() {
        a0 = x0; b0 = y0; a1 = x1; b1 = y1;
        if(a0 < a1){
            if(b0 < b1){
                int max = (a1 - a0) > (a1 - a0) ? (a1  - a0) : (b1 - b0);
                a1 = a0 + max; b1 = b0 + max;
            }else{
                int max = (a1 - a0) > (b0 - b1) ? (a1 - a0) : (b0 - b1);
                a1 = a0 + max; b1 = b0 - max;
            }
        }else{
            if(b0 < b1){
                int max = (a0 - a1) > (b1 - b0) ? (a0 - a1) : (b1 - b0);
                a1 = a0 - max; b1 = b0 + max;
                int tmpx = a1, tmpy = b1;
                a1 = a0; b1 = b0;
                a0 = tmpx; b0 = tmpy;
            }else{
                int max = (a0 - a1) > (b0 - b1) ? (a0 - a1) : (b0 - b1);
                a1 = a0 - max; b1 = b0 - max;
                int tmpx = a1, tmpy = b1;
                a1 = a0; b1 = b0;
                a0 = tmpx; b0 = tmpy;
            }
        }
        //      int max = (x1 - x0) > (y1 - y0) ? (x1  - x0) : (y1 - y0);
        //      x1 = x0 + max; y1 = y0 + max;
        xc = (a0 + a1) / 2;
        yc = (b0 + b1) / 2;//圆的中点
        r = Math.abs((a1 - a0) / 2);//圆半径
        int x = 0, y = r;
        int startx11 = 0, starty11 = r;
        int startx12 = 0, starty12 = r;
        int startx21 = r, starty21 = 0;
        int startx22 = r, starty22 = 0;
        int startx31 = 0, starty31 = -r;
        int startx32 = 0, starty32 = -r;
        int startx41 = -r, starty41 = 0;
        int startx42 = -r, starty42 = 0;//8个画圆起点
        int p = 1 - r; int px = 3, py = 5 - 2 * r;
        shape = new GeneralPath(Path2D.WIND_NON_ZERO);
        shape.moveTo(x + xc, y + yc);
        while(x <= y){
            if(p < 0){
                p += px;
                px += 2; py += 2;
                x++;
                shape.moveTo(startx11 + xc, starty11 + yc);
                startx11 = x; starty11 = y;
                shape.lineTo(startx11 + xc, starty11 + yc);//第一段
                shape.moveTo(startx12 + xc, starty12 + yc);
                startx12 = -x; starty12 = y;
                shape.lineTo(startx12 + xc, starty12 + yc);//第二段
                shape.moveTo(startx21 + xc, starty21 + yc);
                startx21 = y; starty21 = x;
                shape.lineTo(startx21 + xc, starty21 + yc);//第三段
                shape.moveTo(startx22 + xc, starty22 + yc);
                startx22 = y; starty22 = -x;
                shape.lineTo(startx22 + xc, starty22 + yc);//第四段
                shape.moveTo(startx31 + xc, starty31 + yc);
                startx31 = x; starty31 = -y;
                shape.lineTo(startx31 + xc, starty31 + yc);//第五段
                shape.moveTo(startx32 + xc, starty32 + yc);
                startx32 = -x; starty32 = -y;
                shape.lineTo(startx32 + xc, starty32 + yc);//第六段
                shape.moveTo(startx41 + xc, starty41 + yc);
                startx41 = -y; starty41 = x;
                shape.lineTo(startx41 + xc, starty41 + yc);//第七段
                shape.moveTo(startx42 + xc, starty42 + yc);
                startx42 = -y; starty42 = -x;
                shape.lineTo(startx42 + xc, starty42 + yc);//第八段
            }else if(p >= 0){
                p += py;
                px += 2; py += 4;
                x++; y--;
                shape.moveTo(startx11 + xc, starty11 + yc);
                startx11 = x; starty11 = y;
                shape.lineTo(startx11 + xc, starty11 + yc);//第一段
                shape.moveTo(startx12 + xc, starty12 + yc);
                startx12 = -x; starty12 = y;
                shape.lineTo(startx12 + xc, starty12 + yc);//第二段
                shape.moveTo(startx21 + xc, starty21 + yc);
                startx21 = y; starty21 = x;
                shape.lineTo(startx21 + xc, starty21 + yc);//第三段
                shape.moveTo(startx22 + xc, starty22 + yc);
                startx22 = y; starty22 = -x;
                shape.lineTo(startx22 + xc, starty22 + yc);//第四段
                shape.moveTo(startx31 + xc, starty31 + yc);
                startx31 = x; starty31 = -y;
                shape.lineTo(startx31 + xc, starty31 + yc);//第五段
                shape.moveTo(startx32 + xc, starty32 + yc);
                startx32 = -x; starty32 = -y;
                shape.lineTo(startx32 + xc, starty32 + yc);//第六段
                shape.moveTo(startx41 + xc, starty41 + yc);
                startx41 = -y; starty41 = x;
                shape.lineTo(startx41 + xc, starty41 + yc);//第七段
                shape.moveTo(startx42 + xc, starty42 + yc);
                startx42 = -y; starty42 = -x;
                shape.lineTo(startx42 + xc, starty42 + yc);//第八段
            }
        }
        shape.closePath();
    }

    @Override public boolean contains(int x, int y){
        boolean value = false;
        //    return shape.contains(new Point2D.Float(x, y));
        if((x - xc) * (x - xc) + (y - yc) * (y - yc) <= r * r) value = true;
        return value;
    }
}
