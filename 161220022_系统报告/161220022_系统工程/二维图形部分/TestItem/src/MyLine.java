import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class MyLine extends MyItem implements Construct, Contain{
    static float len = 1.0f;  //线宽的改变主要靠该参数
    MyLine(){
        itemIndex = 1;
    }
    @Override public void constructShape(){
        int m = Math.round(len / 2);
        a0 = x0 + m;
        b0 = y0 + m;
        a1 = x1 + m;
        b1 = y1 + m;

        int a, b, det1, det2, d, x, y;
        int startx, starty, endx, endy;
        if (x0 < x1) {
            startx = x0;
            endx = x1;
            starty = y0;
            endy = y1;
        } else {
            startx = x1;
            endx = x0;
            starty = y1;
            endy = y0;
        }
        if (Math.abs(x1 - x0) < 1e-4) {//斜率为无穷
            shape = new GeneralPath(Path2D.WIND_NON_ZERO);
            shape.moveTo(x0, y0);
            shape.lineTo(x1, y1);
        } else if (Math.abs(y1 - y0) < 1e-4) {//斜率为0
            shape = new GeneralPath(Path2D.WIND_NON_ZERO);
            shape.moveTo(x0, y0);
            shape.lineTo(x1, y1);
        } else {
            float k = (float) (y1 - y0) / (x1 - x0);
            a = starty - endy;
            b = endx - startx;
            x = startx;
            y = starty;
            shape = new GeneralPath(Path2D.WIND_NON_ZERO);
            shape.moveTo(startx, starty);
            if (k > 0 && k <= 1) {
                d = 2 * a + b;
                det1 = 2 * a;
                det2 = 2 * (a + b);
                while (x < endx) {
                    if (d < 0) {
                        x++;
                        y++;
                        d += det2;
                    } else {
                        x++;
                        d += det1;
                    }
                    shape.lineTo(x, y);
                    shape.moveTo(x, y);
                }
            } else if (k > 1) {
                d = a + 2 * b;
                det1 = 2 * b;
                det2 = 2 * (a + b);
                while (y < endy) {
                    if (d < 0) {
                        y++;
                        d += det1;
                    } else {
                        x++;
                        y++;
                        d += det2;
                    }
                    shape.lineTo(x, y);
                    shape.moveTo(x, y);
                }
            } else if (k >= -1 && k <= 0) {
                d = 2 * a - b;
                det1 = 2 * a;
                det2 = 2 * (a - b);
                while (x < endx) {
                    if (d < 0) {
                        x++;
                        d += det1;
                    } else {
                        x++;
                        y--;
                        d += det2;
                    }
                    shape.lineTo(x, y);
                    shape.moveTo(x, y);
                }
            } else if (k < -1) {
                d = a - 2 * b;
                det1 = (-b) * 2;
                det2 = 2 * (a - b);
                while (y > endy) {
                    if (d < 0) {
                        x++;
                        y--;
                        d += det2;
                    } else {
                        y--;
                        d += det1;
                    }
                    shape.lineTo(x, y);
                    shape.moveTo(x, y);
                }
            }
            shape.lineTo(endx, endy);
            shape.lineTo(endx - 1, endy - 1);
            shape.lineTo(startx - 1, starty - 1);
            shape.lineTo(startx, starty);
            shape.closePath();
        }
    }

    @Override public boolean contains(int x, int y){
        return Path2D.contains(shape.getPathIterator(null), new Point2D.Float(x, y));
    }
}
