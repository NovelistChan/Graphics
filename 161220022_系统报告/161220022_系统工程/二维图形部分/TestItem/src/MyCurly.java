import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class MyCurly extends MyItem implements Construct, Contain{
    private Point2D[] points;//三次曲线的四个控制点
    MyCurly(){
        itemIndex = 6;
    }
    @Override
    public void constructShape(){//三次贝赛尔曲线
        a0 = x0; a1 = x1; a2 = x2; a3 = x3;
        b0 = y0; b1 = y1; b2 = y2; b3 = y3;
        shape = new GeneralPath(Path2D.WIND_NON_ZERO);
        Point2D A = new Point2D.Double(a0, b0);
        Point2D B = new Point2D.Double(a2, b2);
        Point2D C = new Point2D.Double(a3, b3);
        Point2D D = new Point2D.Double(a1, b1);
        points = new Point2D[]{A,B,C,D};
        //shape.moveTo(a0, b0);
        drawBezier(points);
    }

    //每次递进t值，计算插值点
    private Point2D cubicBezier(double t, Point2D[] p) {
        Point2D[] temp = new Point2D[p.length];
        for (int k = 0; k < p.length; k++) temp[k] = p[k];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4 - i - 1 ; j++) {
                double x = (1 - t) * temp[j].getX() + t * temp[j + 1].getX();
                double y = (1 - t) * temp[j].getY() + t * temp[j + 1].getY();
                temp[j] = new Point2D.Double(x,y);
            }
        }
        return temp[0];
    }

    private void drawBezier(Point2D[] p) {
        for (double t = 0; t < 1; t += 0.002) {
            Point2D p1= cubicBezier(t, p);
            Point2D p2 = cubicBezier(t + 0.001, p);
            shape.moveTo(p1.getX(), p1.getY());
            shape.lineTo(p2.getX(), p2.getY());
        }
    }

    @Override public boolean contains(int x, int y) {
        boolean value = false;
        //      return shape.contains(new Point2D.Float(x, y));
        //      if(((x - xc) * (x - xc) * ry * ry + (y - yc) * (y - yc) * rx * rx) <= rx * rx * ry * ry) value = true;
        if((Math.abs(x - (a0 + a1)/2) < Math.abs((a0 - a1) / 2)) && ((Math.abs(y - (b0 + b1) / 2)) < Math.abs((b0 - b1) / 2))) value = true;
        return value;
    }
}
