import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;


class PointNode{
    int nodex;
    int nodey;
    //boolean in;
    PointNode(int x, int y){
        nodex = x;
        nodey = y;
    }
}

public class BoundaryFill implements Construct {
    int paintSeedx;
    int paintSeedy;
    BufferedImage bufImage = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
    Color fillColor = Color.YELLOW;
    Color boundColor = Color.BLACK;
    Color cutColor = Color.CYAN;
    GeneralPath shape;
    int cnt = 0;
    int nodeNum;
    PointNode[] pointStack;
    Stack<PointNode> pStack = new Stack<>();
    BoundaryFill(int x, int y){
        paintSeedx = x; paintSeedy = y;
        nodeNum = 0;
        pointStack = new PointNode[1000000];
        for(int i = 0; i < 1000000; i++){
            pointStack[i] = new PointNode(-1, -1);
        }
        TestItem.getTi().paintAll(bufImage.getGraphics());
        /*try {
            ImageIO.write(bufImage, "png", new File("E:/","1.png"));
            System.out.println("Catch!");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    @Override
    public void constructShape(){
        shape = new GeneralPath(Path2D.WIND_NON_ZERO);
        boundaryFill4();
        shape.closePath();
    }

    boolean inPointStack(int x, int y){
        for(int i = 0; i < nodeNum; i++){
            if(x == pointStack[i].nodex && y == pointStack[i].nodey) return true;
        }
        return false;
    }

    void setPixel(int nextx, int nexty){//在当前坐标画一个红点

        shape.lineTo(nextx, nexty);
        shape.moveTo(nextx, nexty);
        //    g.dispose();
    }

    public Color getPixel(int x, int y){//获得当前坐标颜色信息
        //if(cnt == 0){
        //    System.out.println("x: " + bufImage.getMinX() + " y: " + bufImage.getMinY() + " height: " + bufImage.getHeight() + " width: " + bufImage.getWidth());
        //    System.out.println("posx: " + x + " posy: " + y);
        //}
        int rgb = bufImage.getRGB(x, y);
        Color color = new Color(rgb, true);
        return color;
    }

    public void boundaryFill4() {
        pStack.push(new PointNode(paintSeedx, paintSeedy));
        shape.moveTo(paintSeedx, paintSeedy);
        while(!pStack.empty()){
            PointNode node = pStack.pop();
            pointStack[nodeNum].nodex = node.nodex; pointStack[nodeNum].nodey = node.nodey;
            nodeNum++;
            if((node.nodex > 1000) || (node.nodex < 0) || (node.nodey > 850) || (node.nodey < 0)){
                continue;
            }
            Color interiorColor = getPixel(node.nodex, node.nodey);
            if((interiorColor.equals(boundColor)) || (interiorColor.equals(fillColor)) || (interiorColor.equals(cutColor))){
                continue;
                //setPixel(node.nodex, node.nodey);
            }
            setPixel(node.nodex, node.nodey);
            PointNode[] pNode = {new PointNode(node.nodex - 1, node.nodey), new PointNode(node.nodex + 1, node.nodey), new PointNode(node.nodex, node.nodey + 1), new PointNode(node.nodex, node.nodey - 1)};
            for(int i = 0; i < 4; i++){
                if((pNode[i].nodex > 1000) || (pNode[i].nodex < 0) || (pNode[i].nodey > 850) || (pNode[i].nodey < 0)){
                    continue;
                }
                Color thisColor = getPixel(pNode[i].nodex, pNode[i].nodey);
                if((thisColor.equals(boundColor)) || (thisColor.equals(fillColor)) || (thisColor.equals(cutColor))){
                    continue;
                }
                if(inPointStack(pNode[i].nodex, pNode[i].nodey)){
                    continue;
                }
                pStack.push(pNode[i]);
             //   System.out.println("painting x: " + pNode[i].nodex + " y: " + pNode[i].nodey);
            }
        }
    }
}

