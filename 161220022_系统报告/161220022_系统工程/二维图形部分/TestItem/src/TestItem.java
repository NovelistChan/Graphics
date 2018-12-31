import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestItem extends JPanel {
    private List<MyItem> Items = new ArrayList<MyItem>();
    private List<BoundaryFill> Fillpencils = new ArrayList<BoundaryFill>();
    private MyItem cutFrame;
    private MyItem inItem;
    private BoundaryFill fillpencil;
    private float range = 4f;
    JPopupMenu popUp;
    private MyItem selectedItem; // 选中的Item
    private JLabel imageLabel;
    private MyItem tmpSelectedItem; // 移动时临时图元
    //   Graphics2D g;
    //Color fillColor = Color.RED;
    //Color boundColor = Color.BLACK;
    //5种状态：0：无任何操作 1：选中图元 2：选中图元端点 3：开始画图 4: 填充区域
    int pressIndex = 0;
    //7种图元类(裁剪窗口继承自图元类)：1.直线 2.圆 3.椭圆 4.四边形 5.三角形 6.贝塞尔曲线(三次) 7.裁剪边框(四边形)
    int itemIndex = 0;
    BufferedImage panelImage = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);

    boolean fillState = false;

    class MyMouseListener implements MouseInputListener {
        Point oldPoint;
        boolean isDragge;

        private void constructXY0(MyItem Item, int x, int y) {
            if (x > getWidth())
                Item.x0 = getWidth();
            else if (x < 0)
                Item.x0 = 0;
            else
                Item.x0 = x;
            if (y > getHeight())
                Item.y0 = getHeight();
            else if (y < 0)
                Item.y0 = 0;
            else
                Item.y0 = y;
        }

        private void constructXY1(MyItem Item, int x, int y) {
            if (x > getWidth())
                Item.x1 = getWidth();
            else if (x < 0)
                Item.x1 = 0;
            else
                Item.x1 = x;
            if (y > getHeight())
                Item.y1 = getHeight();
            else if (y < 0)
                Item.y1 = 0;
            else
                Item.y1 = y;
        }

        private void constructXY2(MyItem Item, int x, int y){
            if (x > getWidth())
                Item.x2 = getWidth();
            else if (x < 0)
                Item.x2 = 0;
            else
                Item.x2 = x;
            if (y > getHeight())
                Item.y2 = getHeight();
            else if (y < 0)
                Item.y2 = 0;
            else
                Item.y2 = y;
        }

        private void constructXY3(MyItem Item, int x, int y){
            if (x > getWidth())
                Item.x3 = getWidth();
            else if (x < 0)
                Item.x3 = 0;
            else
                Item.x3 = x;
            if (y > getHeight())
                Item.y3 = getHeight();
            else if (y < 0)
                Item.y3 = 0;
            else
                Item.y3 = y;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //  saveImage();
            //  System.out.println("Catch!");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (pressIndex != 3 && pressIndex != 4 && pressIndex != 5)
                pressIndex = 0;
            // 先将状态重置
            for (MyItem Item : Items) {
                Item.isSelectP0 = false;
                Item.isSelectP1 = false;
                Item.isSelectP2 = false;
                Item.isSelectP3 = false;
                Item.isSelected = false;
            }

            // 判断是否是画图状态，若是则将光标改变
            if (pressIndex == 3) {
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                return;
            }

            //判断是否是填充状态
            if (pressIndex == 4) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return;
            }

            // 判断是否选中图元，若是则将光标改变
            for (MyItem Item : Items) {
                if (Item.contains(e.getX(), e.getY())) {
                    Item.isSelected = true;
                    pressIndex = 1;
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    break;
                }
            }
            // 如果选中了某个图元，再判断该图元的端控制点是否被选中
            if (selectedItem != null) {
                MyItem Item = selectedItem;
                Point p1 = new Point(Item.a0, Item.b0);
                Point p2 = new Point(Item.a1, Item.b1);
                Point p3 = new Point(Item.a2, Item.b2);
                Point p4 = new Point(Item.a3, Item.b3);
                Point p = new Point(e.getX(), e.getY());
                if (isRange(p1, p)) {
                    Item.isSelectP0 = true;
                    pressIndex = 2;
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
                if (isRange(p2, p)) {
                    Item.isSelectP1 = true;
                    pressIndex = 2;
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
                if(Item.itemIndex == 6){
                    if (isRange(p3, p)) {
                        Item.isSelectP2 = true;
                        pressIndex = 2;
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    }
                    if (isRange(p4, p)) {
                        Item.isSelectP3 = true;
                        pressIndex = 2;
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    }
                }

            }

        }

        @Override
        public void mousePressed(MouseEvent e) {
            //   System.out.print(getPixel(e.getX(),e.getY()));
            switch (pressIndex) {
                case 0:
                    selectedItem = null;
                    for (MyItem Item : Items) {
                        Item.isSelectP0 = false;
                        Item.isSelectP1 = false;
                        Item.isSelectP2 = false;
                        Item.isSelectP3 = false;
                        Item.isSelected = false;
                    }
                    break;
                case 1:
                    for (MyItem Item : Items) {
                        if (Item.isSelected) {
                            selectedItem = Item;
                            break;
                        }
                    }
                    switch (selectedItem.itemIndex) {
                        case 1:
                            tmpSelectedItem = new MyLine();
                            break;
                        case 2:
                            tmpSelectedItem = new MyCircle();
                            break;
                        case 3:
                            tmpSelectedItem = new MyOval();
                            break;
                        case 4:
                            tmpSelectedItem = new MyRectangle();
                            break;
                        case 5:
                            tmpSelectedItem = new MyTriangle();
                            break;
                        case 6:
                            tmpSelectedItem = new MyCurly();
                            break;
                        case 7:
                            tmpSelectedItem = new CutFrame();
                            break;
                        default:
                            break;
                    }
                    tmpSelectedItem.x0 = selectedItem.x0;
                    tmpSelectedItem.x1 = selectedItem.x1;
                    tmpSelectedItem.x2 = selectedItem.x2;
                    tmpSelectedItem.x3 = selectedItem.x3;
                    tmpSelectedItem.y0 = selectedItem.y0;
                    tmpSelectedItem.y1 = selectedItem.y1;
                    tmpSelectedItem.y2 = selectedItem.y2;
                    tmpSelectedItem.y3 = selectedItem.y3;
                    tmpSelectedItem.constructShape();
                    if(tmpSelectedItem.itemIndex == 7){
                        cutFrame = tmpSelectedItem;
                    }
                    oldPoint = new Point(e.getX(), e.getY());
                    break;
                case 2:

                    break;
                case 3:
                    switch (itemIndex) {
                        case 1:
                            inItem = new MyLine();
                            break;
                        case 2:
                            inItem = new MyCircle();
                            break;
                        case 3:
                            inItem = new MyOval();
                            break;
                        case 4:
                            inItem = new MyRectangle();
                            break;
                        case 5:
                            inItem = new MyTriangle();
                            break;
                        case 6:
                            inItem = new MyCurly();
                            break;
                        case 7:
                            inItem = new CutFrame();
                            break;
                        default:
                            break;
                    }
                    inItem.x0 = e.getX();
                    inItem.y0 = e.getY();
                    break;
                case 4:
                    fillpencil = new BoundaryFill(e.getX(), e.getY());
                    fillState = true;
                    Fillpencils.add(fillpencil);
                    fillpencil.constructShape();
                    pressIndex = 0;
                    //saveImage();
                    break;
                default:
                    break;

            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            switch (pressIndex) {
                case 0:
                    break;
                case 1:
                    Point point = new Point(e.getX(), e.getY());
                    int lenx = point.x - oldPoint.x;
                    int leny = point.y - oldPoint.y;
                    tmpSelectedItem.x0 = selectedItem.x0 + lenx;
                    tmpSelectedItem.x1 = selectedItem.x1 + lenx;
                    tmpSelectedItem.x2 = selectedItem.x2 + lenx;
                    tmpSelectedItem.x3 = selectedItem.x3 + lenx;
                    tmpSelectedItem.y0 = selectedItem.y0 + leny;
                    tmpSelectedItem.y1 = selectedItem.y1 + leny;
                    tmpSelectedItem.y2 = selectedItem.y2 + leny;
                    tmpSelectedItem.y3 = selectedItem.y3 + leny;
                    tmpSelectedItem.constructShape();
                    if(tmpSelectedItem.itemIndex == 7){
                        cutFrame = tmpSelectedItem;
                    }
                    isDragge = true;
                    break;
                case 2:
                    MyItem Item = selectedItem;// Items.get(selectPointInWhichItem);
                    if (Item.isSelectP0) {
                        constructXY0(Item, e.getX(), e.getY());
                    }
                    if (Item.isSelectP1) {
                        constructXY1(Item, e.getX(), e.getY());
                    }
                    if(Item.itemIndex == 6){
                        if (Item.isSelectP2) {
                            constructXY2(Item, e.getX(), e.getY());
                        }
                        if (Item.isSelectP3) {
                            constructXY3(Item, e.getX(), e.getY());
                        }
                    }
                    Item.constructShape();
                    if(Item.itemIndex == 7){
                        cutFrame = Item;
                    }
                    break;
                case 3:
                    constructXY1(inItem, e.getX(), e.getY());
                    if(inItem.itemIndex == 6){
                        constructXY2(inItem, (inItem.x0 * 2 + inItem.x1) / 3, (inItem.y0 * 2 + inItem.y1) / 3);
                        constructXY3(inItem, (inItem.x0 + inItem.x1 * 2) / 3, (inItem.y0 + inItem.y1 * 2) / 3);
                    }
                    inItem.constructShape();
                    break;
                default:
                    break;

            }
            imageLabel.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            switch (pressIndex) {
                case 0:
                    break;
                case 1:
                    Point point = new Point(e.getX(), e.getY());
                    int lenx = point.x - oldPoint.x;
                    int leny = point.y - oldPoint.y;
                    selectedItem.x0 += lenx;
                    selectedItem.x1 += lenx;
                    selectedItem.x2 += lenx;
                    selectedItem.x3 += lenx;
                    selectedItem.y0 += leny;
                    selectedItem.y1 += leny;
                    selectedItem.y2 += leny;
                    selectedItem.y3 += leny;
                    selectedItem.constructShape();
                    tmpSelectedItem = null;
                    if (!isDragge && e.isPopupTrigger()) {
                        popUp.show(e.getComponent(), e.getX(), e.getY());
                    }
                    isDragge = false;
                    break;
                case 2:
                    MyItem Item = selectedItem;
                    if (Item.isSelectP0) {
                        constructXY0(Item, e.getX(), e.getY());
                    }
                    if (Item.isSelectP1) {
                        constructXY1(Item, e.getX(), e.getY());
                    }
                    if(Item.itemIndex == 6){
                        if (Item.isSelectP2) {
                            constructXY2(Item, e.getX(), e.getY());
                        }
                        if (Item.isSelectP3) {
                            constructXY3(Item, e.getX(), e.getY());
                        }
                    }
                    Item.constructShape();
                    break;
                case 3:
                    constructXY1(inItem, e.getX(), e.getY());
                    if(inItem.itemIndex == 6){
                        constructXY2(inItem, (inItem.x0 * 2 + inItem.x1) / 3, (inItem.y0 * 2 + inItem.y1) / 3);
                        constructXY3(inItem, (inItem.x0 + inItem.x1 * 2) / 3, (inItem.y0 + inItem.y1 * 2) / 3);
                    }
                    if(inItem.itemIndex == 7) {
                        cutFrame = inItem;
                    }
                    inItem.constructShape();
                    Items.add(inItem);
                    //selectedItem = inItem;
                    //selectedItem.isSelected = true;
                    inItem = null;
                    pressIndex = 0;
                    break;
                case 4:
                    //saveImage();
                    break;

                default:
                    break;

            }
            // 统一在鼠标释放的时候重绘
            imageLabel.repaint();

        }

    }

    public static TestItem getTi(){
        return ti;
    }

    class PopupActionDelete extends AbstractAction {
        private static final long serialVersionUID = 1L;
        public PopupActionDelete(String name) {
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Items.remove(selectedItem)) {
                selectedItem = null;
                pressIndex = 0;
                JOptionPane.showMessageDialog(null, "已删除", "操作成功", JOptionPane.INFORMATION_MESSAGE);
            }
            imageLabel.repaint();
        }
    }

    class PopupActionRotate extends AbstractAction {
        private static final long serialVersionUID = 1L;
        public PopupActionRotate(String name) {
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog frame = new JDialog();
            frame.setTitle("旋转");
            frame.setLocation(600, 200);
            frame.setSize(400, 400);
            //GridLayout layout = new GridLayout(2, 2);
            JPanel panel = new JPanel(null);
            JLabel label = new JLabel("请输入旋转角度:");
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setBounds(50,100, 100, 50);
            panel.add(label);
            JTextField textField = new JTextField();
            textField.setBounds(200, 100, 100, 50);
            panel.add(textField);
            JButton yes = new JButton("确定");
            yes.setBounds(50, 200, 100, 50);
            yes.setHorizontalTextPosition(SwingConstants.CENTER);
            yes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedItem.rotate(Double.valueOf(textField.getText()));
                    pressIndex = 0;
                    imageLabel.repaint();
                    JOptionPane.showMessageDialog(null, "已完成旋转", "操作成功", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }
            });
            JButton nop = new JButton("取消");
            nop.setBounds(200, 200, 100, 50);
            nop.setHorizontalTextPosition(SwingConstants.CENTER);
            nop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });
            panel.add(yes);
            panel.add(nop);
            frame.getContentPane().add(panel);
            frame.setVisible(true);
           // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           // frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            imageLabel.repaint();
        }
    }

    class PopupActionZoom extends AbstractAction {
        private static final long serialVersionUID = 1L;
        public PopupActionZoom(String name) {
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog frame = new JDialog();
            frame.setTitle("缩放");
            frame.setLocation(600, 200);
            frame.setSize(400, 400);
            //GridLayout layout = new GridLayout(2, 2);
            JPanel panel = new JPanel(null);
            JLabel label = new JLabel("请输入缩放倍数:");
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setBounds(50,100, 100, 50);
            panel.add(label);
            JTextField textField = new JTextField();
            textField.setBounds(200, 100, 100, 50);
            panel.add(textField);
            JButton yes = new JButton("确定");
            yes.setBounds(50, 200, 100, 50);
            yes.setHorizontalTextPosition(SwingConstants.CENTER);
            yes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedItem.zoom(Double.valueOf(textField.getText()));
                    pressIndex = 0;
                    imageLabel.repaint();
                    JOptionPane.showMessageDialog(null, "已完成缩放", "操作成功", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }
            });
            JButton nop = new JButton("取消");
            nop.setBounds(200, 200, 100, 50);
            nop.setHorizontalTextPosition(SwingConstants.CENTER);
            nop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });
            panel.add(yes);
            panel.add(nop);
            frame.getContentPane().add(panel);
            frame.setVisible(true);
            // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            imageLabel.repaint();
        }
    }

    private static final long serialVersionUID = 1L;

    private static TestItem ti = new TestItem();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame("画图");
                frame.setSize(600, 600);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                ti.setLayout(null);
                frame.getContentPane().add(ti);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    // 初始化鼠标事件监听
    public TestItem() {
        Graphics2D g2 = panelImage.createGraphics();
        g2.setColor(new Color(245, 245, 220));
        g2.fillRect(0, 0, 600, 400);
        imageLabel = new JLabel(new ImageIcon(panelImage)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintInLabel(g);
            }
        };
        MyMouseListener ml = new MyMouseListener();
        imageLabel.addMouseListener(ml);
        imageLabel.addMouseMotionListener(ml);
        constructPopUp();
        imageLabel.setBounds(0, 0, 600,400);
        add(imageLabel);
        JButton addLine = new JButton("添加直线");
        addLine.setHorizontalTextPosition(SwingConstants.CENTER);
        addLine.setBounds(0, 500, 600 / 3, 50);
        JButton addCircle = new JButton("添加圆");
        addCircle.setHorizontalTextPosition(SwingConstants.CENTER);
        addCircle.setBounds(600 / 3, 500, 600 / 3, 50);
        JButton addOval = new JButton("添加椭圆");
        addOval.setHorizontalTextPosition(SwingConstants.CENTER);
        addOval.setBounds(1200 / 3, 500, 600 / 3, 50);
        JButton addRectangle = new JButton("添加长方形");
        addRectangle.setHorizontalTextPosition(SwingConstants.CENTER);
        addRectangle.setBounds(0, 450, 600 / 3, 50);
        JButton addTriangle = new JButton("添加三角形");
        addTriangle.setHorizontalTextPosition(SwingConstants.CENTER);
        addTriangle.setBounds(600 / 3, 450, 600 / 3, 50);
        JButton fillColor = new JButton("填充");
        fillColor.setHorizontalTextPosition(SwingConstants.CENTER);
        fillColor.setBounds(1200 / 3, 400, 600 / 3, 50);
        JButton saveToLocal = new JButton("保存当前画布");
        saveToLocal.setHorizontalTextPosition(SwingConstants.CENTER);
        saveToLocal.setBounds(0, 400, 600 / 3, 50);
        JButton cutAct = new JButton("裁剪");
        cutAct.setHorizontalTextPosition(SwingConstants.CENTER);
        cutAct.setBounds(600 / 3, 400, 600 / 3, 50);
        JButton addCurly = new JButton("添加曲线");
        addCurly.setHorizontalTextPosition(SwingConstants.CENTER);
        addCurly.setBounds(1200 / 3, 450, 600 / 3, 50);
        cutAct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ti.pressIndex = 3;
                ti.itemIndex = 7;
                MyItem DelItem = new MyItem();
                for(MyItem Item: Items){
                    if(Item.itemIndex == 7){
                        //Items.remove(Item);
                        DelItem = Item;
                    }
                }
                Items.remove(DelItem);
                ti.cutFrame = null;
                imageLabel.repaint();
            }
        });
        saveToLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
        addLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ti.pressIndex = 3;
                ti.itemIndex = 1;
            }
        });
        addCircle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ti.pressIndex = 3;
                ti.itemIndex = 2;
            }
        });
        addOval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ti.pressIndex = 3;
                ti.itemIndex = 3;
            }
        });
        addRectangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ti.pressIndex = 3;
                ti.itemIndex = 4;
            }
        });
        addTriangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ti.pressIndex = 3;
                ti.itemIndex = 5;
            }
        });
        addCurly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ti.pressIndex = 3;
                ti.itemIndex = 6;
            }
        });
        fillColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ti.pressIndex = 4;
            }
        });
        add(addLine);
        add(addCircle);
        add(addOval);
        add(addRectangle);
        add(addTriangle);
        add(fillColor);
        add(addCurly);
        add(cutAct);
        add(saveToLocal);
    }

    private void paintInLabel(Graphics g2) {
        g2.setColor(new Color(245, 245, 220));
        g2.fillRect(0, 0, 600, 400);
        Graphics2D g = (Graphics2D) g2;
        Paint oldPaint = g.getPaint();
        Stroke s = g.getStroke();

        for (BoundaryFill fill: Fillpencils){
            g.setStroke(new BasicStroke(1f));
            g.setPaint(Color.YELLOW);//黄色填充图层
            GeneralPath shape = fill.shape;
            g.draw(shape);
        }

        for (MyItem Item : Items) {
            g.setStroke(new BasicStroke(MyItem.lineWidth));
            g.setPaint(Color.BLACK);//黑色图元边界
            if(Item.itemIndex == 7){
                continue;
            }
            GeneralPath shape = Item.shape;
            g.draw(shape);
        }

        if (cutFrame != null){
            g.setStroke(new BasicStroke(MyItem.lineWidth));
            g.setPaint(Color.CYAN);//青色裁剪边框
            GeneralPath shape = cutFrame.shape;
            g.draw(shape);
        }

        // 如果被选择，那么直线的两端应该会有两个蓝色的点
        if (selectedItem != null) {
            MyItem Item = selectedItem;
            g.setPaint(Color.BLACK);
            int r = 6;
            g.drawOval(Item.a0 - r / 2, Item.b0 - r / 2, r, r);
            g.drawOval(Item.a1 - r / 2, Item.b1 - r / 2, r, r);
            if(Item.itemIndex == 6){
                g.drawOval(Item.a2 - r / 2, Item.b2 - r / 2, r, r);
                g.drawOval(Item.a3 - r / 2, Item.b3 - r / 2, r, r);
            }
            g.setPaint(Color.BLUE);
            g.fillOval(Item.a0 - r / 2, Item.b0 - r / 2, r, r);
            g.fillOval(Item.a1 - r / 2, Item.b1 - r / 2, r, r);
            if(Item.itemIndex == 6){
                g.fillOval(Item.a2 - r / 2, Item.b2 - r / 2, r, r);
                g.fillOval(Item.a3 - r / 2, Item.b3 - r / 2, r, r);
            }
            //这里使用了画椭圆的库函数，将两个焦半径设为相同，在端点处填充一个圆
        }

        //画移动时的图元
        if (tmpSelectedItem != null) {
            g.setStroke(new BasicStroke(MyItem.lineWidth));
            g.setPaint(Color.BLACK);
            if(tmpSelectedItem.itemIndex == 7){
                g.setPaint(Color.CYAN);//青色裁剪边框
            }
            GeneralPath shape = tmpSelectedItem.shape;
            g.draw(shape);
        }

        // 画正在产生的图元
        if (inItem != null) {
            g.setStroke(new BasicStroke(MyItem.lineWidth));
            g.setPaint(Color.BLACK);
            if(inItem.itemIndex == 7){
                g.setPaint(Color.CYAN);//青色裁剪边框
            }
            GeneralPath shape = inItem.shape;
            if (shape != null)
                g.draw(shape);
        }

        // 裁剪
        if (cutFrame != null) {
            g2.setColor(new Color(245, 245, 220));
            g2.fillRect(0, 0, 600, cutFrame.miny);
            g2.fillRect(0, cutFrame.miny, cutFrame.minx, cutFrame.maxy - cutFrame.miny);
            g2.fillRect(0, cutFrame.maxy, 600, 400 - cutFrame.maxy);
            g2.fillRect(cutFrame.maxx, cutFrame.miny, 600 - cutFrame.maxx, cutFrame.maxy - cutFrame.miny);
        }


        g.setPaint(oldPaint);
        g.setStroke(s);
        g2.dispose();
    }

    //保存图片至本地
    public void saveImage() {
        //BufferedImage bufImage = deepCopy(ti.panelImage);
        JComponent tmpImage = (JComponent)ti;
        tmpImage.printAll(ti.panelImage.getGraphics());
        //BufferedImage bufImage = deepCopy(ti.panelImage);
        // Color bufcolor = new Color(bufImage.getRGB(x - 100, y - 100));
        try {
            ImageIO.write(ti.panelImage, "png", new File("result.png"));
            System.out.println("Catch!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void constructPopUp() {
        popUp = new JPopupMenu();
        JMenuItem menuItemDel = new JMenuItem();
        menuItemDel.setAction(new PopupActionDelete("删除"));
        JMenuItem menuItemRot = new JMenuItem();
        menuItemRot.setAction(new PopupActionRotate("旋转"));
        JMenuItem menuItemZoom = new JMenuItem();
        menuItemZoom.setAction(new PopupActionZoom("缩放"));
        popUp.add(menuItemDel);
        popUp.add(menuItemRot);
        popUp.add(menuItemZoom);
    }

    //判断两点间距是否足够小，足够小则可以视为重合，用来确认是否指示端点
    private boolean isRange(Point a, Point b) {
        if (Math.hypot(a.x - b.x, a.y - b.y) < range)
            return true;
        return false;
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public Color getPixel(int x, int y) {//获得当前坐标颜色信息
        Robot rb = null;
        try {
            rb = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rb.getPixelColor(x, y);
    }

}
