import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class MyOval extends MyItem implements Construct, Contain {
    int xc, yc, rx, ry;
    MyOval(){
        itemIndex = 3;
    }
    @Override public void constructShape(){
        a0 = x0; b0 = y0; a1 = x1; b1 = y1;
        xc = (x0 + x1) / 2;
        yc = (y0 + y1) / 2;//椭圆中点
        rx = Math.abs((x1 - x0) / 2);//x轴截距
        ry = Math.abs((y1 - y0) / 2);//y轴截距
        int x = 0, y = ry;//绘制的笔所在点
        int startx1 = 0, starty1 = ry;
        int startx2 = 0, starty2 = ry;
        int startx3 = 0, starty3 = -ry;
        int startx4 = 0, starty4 = -ry;//4个起点
        int p1 = ry * ry - rx * rx * ry + rx * rx / 4;
        shape = new GeneralPath(Path2D.WIND_NON_ZERO);
        while(ry * ry * x < rx * rx * y){
            if(p1 < 0){
                p1 = p1 + 2 * ry * ry * x + 3 * ry * ry;
                x++;
                shape.moveTo(startx1 + xc, starty1 + yc);
                startx1 = x; starty1 = y;
                shape.lineTo(startx1 + xc, starty1 + yc);//第一段
                shape.moveTo(startx2 + xc, starty2 + yc);
                startx2 = -x; starty2 = y;
                shape.lineTo(startx2 + xc, starty2 + yc);//第二段
                shape.moveTo(startx3 + xc, starty3 + yc);
                startx3 = x; starty3 = -y;
                shape.lineTo(startx3 + xc, starty3 + yc);//第三段
                shape.moveTo(startx4 + xc, starty4 + yc);
                startx4 = -x; starty4 = -y;
                shape.lineTo(startx4 + xc, starty4 + yc);//第四段
            }else{
                p1 = p1 + 2 * ry * ry * x - 2 * rx * rx * y + 3 * ry * ry + 2 * rx * rx;
                x++; y--;
                shape.moveTo(startx1 + xc, starty1 + yc);
                startx1 = x; starty1 = y;
                shape.lineTo(startx1 + xc, starty1 + yc);//第一段
                shape.moveTo(startx2 + xc, starty2 + yc);
                startx2 = -x; starty2 = y;
                shape.lineTo(startx2 + xc, starty2 + yc);//第二段
                shape.moveTo(startx3 + xc, starty3 + yc);
                startx3 = x; starty3 = -y;
                shape.lineTo(startx3 + xc, starty3 + yc);//第三段
                shape.moveTo(startx4 + xc, starty4 + yc);
                startx4 = -x; starty4 = -y;
                shape.lineTo(startx4 + xc, starty4 + yc);//第四段
            }
        }
        int p2 = ry * ry * (x + 1/2) * (x + 1/2) + rx * rx * (y - 1) * (y - 1) - rx * rx * ry * ry;
        while(x <= rx && y >= 0){
            if(p2 > 0){
                p2 = p2 - 2 * rx * rx * y + 3 * rx * rx;
                y--;
                shape.moveTo(startx1 + xc, starty1 + yc);
                startx1 = x; starty1 = y;
                shape.lineTo(startx1 + xc, starty1 + yc);//第一段
                shape.moveTo(startx2 + xc, starty2 + yc);
                startx2 = -x; starty2 = y;
                shape.lineTo(startx2 + xc, starty2 + yc);//第二段
                shape.moveTo(startx3 + xc, starty3 + yc);
                startx3 = x; starty3 = -y;
                shape.lineTo(startx3 + xc, starty3 + yc);//第三段
                shape.moveTo(startx4 + xc, starty4 + yc);
                startx4 = -x; starty4 = -y;
                shape.lineTo(startx4 + xc, starty4 + yc);//第四段
            }else{
                p2 = p2 + 2 * ry * ry * (x + 1) - 2 * rx * rx * (y - 1) + rx * rx;
                x++; y--;
                shape.moveTo(startx1 + xc, starty1 + yc);
                startx1 = x; starty1 = y;
                shape.lineTo(startx1 + xc, starty1 + yc);//第一段
                shape.moveTo(startx2 + xc, starty2 + yc);
                startx2 = -x; starty2 = y;
                shape.lineTo(startx2 + xc, starty2 + yc);//第二段
                shape.moveTo(startx3 + xc, starty3 + yc);
                startx3 = x; starty3 = -y;
                shape.lineTo(startx3 + xc, starty3 + yc);//第三段
                shape.moveTo(startx4 + xc, starty4 + yc);
                startx4 = -x; starty4 = -y;
                shape.lineTo(startx4 + xc, starty4 + yc);//第四段
            }
        }
        shape.closePath();
    }

    @Override public boolean contains(int x, int y){
          boolean value = false;
    //      return shape.contains(new Point2D.Float(x, y));
    //      if(((x - xc) * (x - xc) * ry * ry + (y - yc) * (y - yc) * rx * rx) <= rx * rx * ry * ry) value = true;
          if(Math.abs(x - xc) < rx && Math.abs(y - yc) < ry) value = true;
          return value;
    }
}
