import java.awt.geom.*;

public class MyItem implements Construct, Contain, Rotate, Zoom{
    boolean isSelected;
    boolean isSelectP0;
    boolean isSelectP1;
    int x0, y0, x1, y1;
    int a0, b0, a1, b1;
    int itemIndex; // 判断图元种类：1.直线 2.圆 3.椭圆 4.四边形 5.三角形 6.贝塞尔曲线(三次) 7.裁剪边框(四边形)
    public static float lineWidth = 2f; // 线宽
    GeneralPath shape;
    int x2, y2, x3, y3;//MyItem初始化有4个控制点，实际上各图元用到的控制点个数是不同的
    int a2, b2, a3, b3;
    boolean isSelectP2;
    boolean isSelectP3;
    int minx, miny, maxx, maxy;
    MyItem(){
        x2 = 0; y2 = 0; x3 = 0; y3 = 0;
        a2 = 0; b2 = 0; a3 = 0; b3 = 0;
        minx = 0; miny = 0; maxx = 0; maxy = 0;
    }
    @Override public void constructShape(){
        //  itemIndex = 0;
    }

    @Override public boolean contains(int x, int y){
        return true;
    }

    @Override public void rotate(double angel){
        int midx = (x0 + x1) / 2;
        int midy = (y0 + y1) / 2;
        int newx0 = (int)(midx + (x0 - midx) * Math.cos(angel/180 * Math.PI) - (y0 - midy) * Math.sin(angel/180 * Math.PI));
        int newy0 = (int)(midy + (x0 - midx) * Math.sin(angel/180 * Math.PI) + (y0 - midy) * Math.cos(angel/180 * Math.PI));
        int newx1 = (int)(midx + (x1 - midx) * Math.cos(angel/180 * Math.PI) - (y1 - midy) * Math.sin(angel/180 * Math.PI));
        int newy1 = (int)(midy + (x1 - midx) * Math.sin(angel/180 * Math.PI) + (y1 - midy) * Math.cos(angel/180 * Math.PI));
        int newx2 = (int)(midx + (x2 - midx) * Math.cos(angel/180 * Math.PI) - (y2 - midy) * Math.sin(angel/180 * Math.PI));
        int newy2 = (int)(midy + (x2 - midx) * Math.sin(angel/180 * Math.PI) + (y2 - midy) * Math.cos(angel/180 * Math.PI));
        int newx3 = (int)(midx + (x3 - midx) * Math.cos(angel/180 * Math.PI) - (y3 - midy) * Math.sin(angel/180 * Math.PI));
        int newy3 = (int)(midy + (x3 - midx) * Math.sin(angel/180 * Math.PI) + (y3 - midy) * Math.cos(angel/180 * Math.PI));
        x0 = newx0; y0 = newy0;
        x1 = newx1; y1 = newy1;
        x2 = newx2; y2 = newy2;
        x3 = newx3; y3 = newy3;
        this.constructShape();
    }

    @Override public void zoom(double power){
        int midx = (x0 + x1) / 2;
        int midy = (y0 + y1) / 2;
        x0 = (int)(midx + power * (x0 - midx));
        y0 = (int)(midy + power * (y0 - midy));
        x1 = (int)(midx + power * (x1 - midx));
        y1 = (int)(midy + power * (y1 - midy));
        x2 = (int)(midx + power * (x2 - midx));
        y2 = (int)(midy + power * (y2 - midy));
        x3 = (int)(midx + power * (x3 - midx));
        y3 = (int)(midy + power * (y3 - midy));
        this.constructShape();
    }
}
