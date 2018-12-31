import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.math.*;
public class MyTriangle extends MyItem implements Construct, Contain, Rotate{
    private Point2D[] points = new Point2D[3];
    MyTriangle(){
        itemIndex = 5;
    }

    @Override
    public void constructShape(){
        a0 = x0; b0 = y0; a1 = x1; b1 = y1;
        shape = new GeneralPath(Path2D.WIND_NON_ZERO);
        shape.moveTo(x0, y1);
        shape.lineTo(x1, y1);
        shape.moveTo(x1, y1);
        shape.lineTo((x0 + x1) / 2, y0);
        shape.moveTo((x0 + x1) / 2, y0);
        shape.lineTo(x0, y1);
        Point2D p1 = new Point2D.Double(x0, y1);
        Point2D p2 = new Point2D.Double(x1, y1);
        Point2D p3 = new Point2D.Double((x0 + x1) / 2, y0);
        points = new Point2D[]{p1, p2, p3};
        shape.closePath();
    }

    @Override
    public void rotate(double angel){
        int midx = (x0 + x1) / 2;
        int midy = (y0 + y1) / 2;
        shape = new GeneralPath(Path2D.WIND_NON_ZERO);
        for(int i = 0; i < 3; i++){
            double newx = midx + (points[i].getX() - midx) * Math.cos(angel/180 * Math.PI) - (points[i].getY() - midy) * Math.sin(angel/180 * Math.PI);
            double newy = midy + (points[i].getX() - midx) * Math.sin(angel/180 * Math.PI) + (points[i].getY() - midy) * Math.cos(angel/180 * Math.PI);
            points[i] = new Point2D.Double(newx, newy);
        }
        for(int i = 0; i < 3; i++){
            shape.moveTo(points[i].getX(), points[i].getY());
            shape.lineTo(points[(i + 1) % 3].getX(), points[(i + 1) % 3].getY());
        }
    }

    @Override
    public boolean contains(int x, int y){
        boolean value = false;
        if((Math.abs(x - (a0 + a1)/2) < Math.abs((a0 - a1) / 2)) && ((Math.abs(y - (b0 + b1) / 2)) < Math.abs((b0 - b1) / 2))) value = true;
        return value;
    }
}
