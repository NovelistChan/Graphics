import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.*;
public class MyRectangle extends MyItem implements Construct, Contain, Rotate {
    private Point2D[] points = new Point2D[4];
    MyRectangle(){
        itemIndex = 4;
    }

    @Override
    public void constructShape(){
        minx = x0 < x1 ? x0 : x1;
        miny = y0 < y1 ? y0 : y1;
        maxx = x0 > x1 ? x0 : x1;
        maxy = y0 > y1 ? y0 : y1;
        a0 = x0; b0 = y0; a1 = x1; b1 = y1;
        shape = new GeneralPath(Path2D.WIND_NON_ZERO);
        Point point1 = new Point(x0, y0);
        Point point2 = new Point(x0, y1);
        Point point3 = new Point(x1, y1);
        Point point4 = new Point(x1, y0);
        points = new Point2D[]{point1,point2,point3,point4};
        shape.moveTo(x0, y0);
        shape.lineTo(x0, y1);
        shape.moveTo(x0, y1);
        shape.lineTo(x1, y1);
        shape.moveTo(x1, y1);
        shape.lineTo(x1, y0);
        shape.moveTo(x1, y0);
        shape.lineTo(x0, y0);
        shape.closePath();
    }

    @Override
    public boolean contains(int x, int y) {
        boolean value = false;
        //      return shape.contains(new Point2D.Float(x, y));
        //      if(((x - xc) * (x - xc) * ry * ry + (y - yc) * (y - yc) * rx * rx) <= rx * rx * ry * ry) value = true;
        if((Math.abs(x - (a0 + a1)/2) < Math.abs((a0 - a1) / 2)) && ((Math.abs(y - (b0 + b1) / 2)) < Math.abs((b0 - b1) / 2))) value = true;
        return value;
    }

    @Override
    public void rotate(double angel){
        int midx = (x0 + x1) / 2;
        int midy = (y0 + y1) / 2;
        shape = new GeneralPath(Path2D.WIND_NON_ZERO);
        for(int i = 0; i < 4; i++){
            double newx = midx + (points[i].getX() - midx) * Math.cos(angel/180 * Math.PI) - (points[i].getY() - midy) * Math.sin(angel/180 * Math.PI);
            double newy = midy + (points[i].getX() - midx) * Math.sin(angel/180 * Math.PI) + (points[i].getY() - midy) * Math.cos(angel/180 * Math.PI);
            points[i] = new Point2D.Double(newx, newy);
        }
        for(int i = 0; i < 4; i++){
            shape.moveTo(points[i].getX(), points[i].getY());
            shape.lineTo(points[(i + 1) % 4].getX(), points[(i + 1) % 4].getY());
        }
    }
}
