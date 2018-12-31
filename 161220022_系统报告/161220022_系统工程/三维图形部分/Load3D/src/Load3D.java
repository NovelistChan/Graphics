import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GraphicsConfiguration;
import java.io.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Load3D extends JPanel{
    private static Load3D load = new Load3D();
    private int[] faceVListA;
    private int[][] faceListA;
    private int nof;
    private int noe;
    private int nov;
    private double[][] verticeListA;
    Load3D(){
        //load.setLayout(new BorderLayout());
        JButton add3D = new JButton("加载一个三维模型");
        add3D.setHorizontalTextPosition(SwingConstants.CENTER);
        add3D.setBounds(0, 500, 600, 50);
        add(add3D);
        add3D.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fd = new JFileChooser();
                //fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fd.showOpenDialog(null);
                File file = fd.getSelectedFile();
                BufferedReader bufferedReader = null;
                try{
                    bufferedReader = new BufferedReader(new FileReader(file));
                    //System.out.println(bufferedReader.readLine());
                    if(bufferedReader.readLine().equals("OFF")){
                        System.out.println("Catch off file");
                        String line1 = bufferedReader.readLine();
                        StringBuffer line1bf = new StringBuffer();
                        char[] ch = line1.toCharArray();
                        int verticeNum = 0;
                        int faceNum = 0;
                        int edgeNum = 0;
                        int cnt = 0;
                        //System.out.println(ch);
                        //System.out.println(ch.length);
                        for(int i = 0; i < ch.length; i++){
                            char c = ch[i];
                            if(c != ' '){
                                line1bf.append(c);
                            } else if(i == (ch.length - 1)){
                                line1bf.append(c);
                            }
                            if(c == ' ' || i == (ch.length - 1)){
                                int r = Integer.parseInt(line1bf.toString());
                                //System.out.println(r);
                                if(cnt == 0){
                                    verticeNum = r;
                                } else if(cnt == 1){
                                    faceNum = r;
                                } else if(cnt == 2){
                                    edgeNum = r;
                                }
                                cnt++;
                                line1bf = new StringBuffer();
                            }
                        }
                        //System.out.println("ver: " + verticeNum + " face: " + faceNum + " edge: " + edgeNum);
                        double[][] verticeList = new double[verticeNum][3];
                        for(int i = 0; i < verticeNum; i++){
                            int cntv = 0;
                            String str = bufferedReader.readLine();
                            StringBuffer buffer = new StringBuffer();
                            char[] chars = str.toCharArray();
                            for(int j = 0; j < chars.length; j++){
                                char c = chars[j];
                                if(c != ' '){
                                    buffer.append(c);
                                } else if(j == (chars.length - 1)){
                                    buffer.append(c);
                                }
                                if(c == ' ' || j == (chars.length - 1)){
                                    //System.out.println(buffer.toString());
                                    double r = Double.parseDouble(buffer.toString());
                                    buffer = new StringBuffer();
                                    verticeList[i][cntv] = r;
                                    cntv++;
                                    /*if(cntv == 3){
                                        System.out.println(verticeList[i][0] + " " + verticeList[i][1] + " " + verticeList[i][2]);
                                    }*/
                                }
                            }
                        }
                        int[][] faceList = new int[faceNum][4];
                        int[] faceVList = new int[faceNum];
                        for(int i = 0; i < faceNum; i++){
                            int cntf = 0;
                            String str = bufferedReader.readLine();
                            StringBuffer buffer = new StringBuffer();
                            char[] chars = str.toCharArray();
                            int index = 0;
                            int vNum = 0;
                            for(;index < chars.length; index++){
                                char c = chars[index];
                                if(c != ' '){
                                    buffer.append(c);
                                } else{
                                    vNum = Integer.parseInt(buffer.toString());
                                    buffer = new StringBuffer();
                                    break;
                                }
                            }
                            faceVList[i] = vNum;
                            //System.out.print(vNum + " ");
                            for(int j = ++index; j < chars.length; j++){
                                char c = chars[j];
                                if(c != ' '){
                                    buffer.append(c);
                                }
                                if(c == ' ' || j == (chars.length - 1)){
                                    int r = Integer.parseInt(buffer.toString());
                                    buffer = new StringBuffer();
                                    faceList[i][cntf] = r;
                                    cntf++;
                                    /*if(cntf == vNum) {
                                        for(int k = 0; k < vNum; k++){
                                            System.out.print(faceList[i][k] + " ");
                                        }
                                        System.out.println();
                                    }*/
                                }
                            }
                        }
                        verticeListA = verticeList;
                        faceListA = faceList;
                        faceVListA = faceVList;
                        nof = faceNum;
                        noe = edgeNum;
                        nov = verticeNum;
                    }

                } catch (Exception e1){
                    e1.printStackTrace();
                } finally {
                    try{
                        bufferedReader.close();
                    } catch (IOException e2){
                        e2.printStackTrace();
                    }
                }
                // 创建一个虚拟空间
                // 创建一个用来包含对象的数据结构
                BranchGroup group = new BranchGroup();
                SimpleUniverse universe = new SimpleUniverse();
                for(int i = 0; i < nof; i++){
                    for(int j = 0; j < faceVListA[i]; j++){
                        LineArray lineX = new LineArray(2, LineArray.COORDINATES);
                        lineX.setCoordinate(0, new Point3d(verticeListA[faceListA[i][j]][0], verticeListA[faceListA[i][j]][1], verticeListA[faceListA[i][j]][2]));
                        lineX.setCoordinate(1, new Point3d(verticeListA[faceListA[i][(j + 1) % faceVListA[i]]][0], verticeListA[faceListA[i][(j + 1) % faceVListA[i]]][1], verticeListA[faceListA[i][(j + 1) % faceVListA[i]]][2]));
                        group.addChild(new Shape3D(lineX));
                    }
                }

                Color3f light1Color = new Color3f(1.8f, 0.1f, 0.1f);
                // 设置光线的颜色
                BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
                // 设置光线的作用范围
                Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
                // 设置光线的方向
                DirectionalLight light1= new DirectionalLight(light1Color, light1Direction);
                // 指定颜色和方向，产生单向光源
                light1.setInfluencingBounds(bounds);
                // 把光线的作用范围加入光源中
                group.addChild(light1);
                // 将光源加入group组,安放观察点
                universe.getViewingPlatform().setNominalViewingTransform();
                // 把group加入到虚拟空间中
                universe.addBranchGraph(group);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame("加载三维模型");
                frame.setSize(600, 600);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                load.setLayout(null);
                frame.getContentPane().add(load);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
