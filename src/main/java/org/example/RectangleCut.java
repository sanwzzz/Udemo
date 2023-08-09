package org.example;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RectangleCut {

    public static void main(String[] args) {
        //外层的高 宽
        int width = 800;
        int height = 600;
        LinkedList<Line> lines;
        List<Shape> shapes;

        // 切割线列表 有序
        lines = new LinkedList<>();
        lines.add(new Line(550, 0, 550, 600));
        lines.add(new Line(0, 500, 550, 500));
        lines.add(new Line(0, 450, 550, 450));
        lines.add(new Line(450, 0, 450, 450));

        shapes = searchShape(800, 600, lines);
        for (Shape shape : shapes) {
            System.out.println(shape);
        }
        createHtml(shapes);

//        System.out.println("=========================");
//
//        width = 1200;
//        height = 1000;
//        // 切割线列表 有序
//        lines = new LinkedList<>();
//        lines.add(new Line(500, 0, 500, 1000));
//        lines.add(new Line(0, 500, 1200, 500));
//
//        shapes = searchShape(800, 600, lines);
//        for (Shape shape : shapes) {
//            System.out.println(shape);
//        }


    }

    public static List<Shape> searchShape(int width, int height, LinkedList<Line> lines){

        LinkedList<Shape> shapes = new LinkedList<>();

        Iterator<Line> iterator = lines.iterator();
        while (iterator.hasNext()){
            Line line = iterator.next();
            //模拟切割...

            //每次切割,如果是横切,那么上方的就是最终产品, 如果是竖切,右边的就是最终产品
            //存在十字切割,即横切后,右边的还需要竖切,竖切后,上方的还需要横切
            //检测是横切还是竖切
            if (line.getX1() == line.getX2()) {
                // 竖切
                //竖切后检测 右边的矩形是否需要继续切割
                //即还存在切割线 x 大于当前切割线的 x
                List<Line> collect = lines.stream().filter(l-> Objects.equals(l.getY1(), l.getY2())).filter(l -> l.getMaxX() > line.getMaxX()).collect(Collectors.toList());
                Shape shape = Shape.create(line.getX1(), line.getY1(), line.getX2(), line.getY2(), width, height, width - line.getMinY(), 0);
                if (CollUtil.isEmpty(collect)) {
                    // 如果不存在 则结束递归 添加右侧料板
                    shapes.add(shape);
                } else {
                    // 如果存在
                    shape.getHeight();
                    shape.getWidth();
                }
                width = line.getMaxX();
            }else if (line.getY1() == line.getY2()){
                // 横切
                // 横切后检测 上面的矩形是否需要继续切割
                // 即还存在切割线 y 大于当前切割线的 y
                List<Line> collect = lines.stream().filter(l-> Objects.equals(l.getX1(), l.getX2())).filter(l -> l.getMaxY() > line.getMaxY()).collect(Collectors.toList());
                if(CollUtil.isEmpty(collect)){
                    //如果不存在 则结束递归 添加上方料板
                    shapes.add(Shape.create(line.getX1(), line.getY1(), line.getX2(), line.getY2(), width, height, 0, height - line.getMinX()));
                }else {
                    //如果存在

                }
                height = line.getMaxY();
            }

            iterator.remove();
        }

        //添加最后一块剩余
        shapes.add(Shape.create(0,0, 0, height, width, height, 0, height));

        return shapes;
    }


    
    public static void createHtml(List<Shape> list){
        File file = FileUtil.file("cut.html");
        BufferedWriter writer = FileUtil.getWriter(file, "utf-8", false);
        for (Shape shape : list) {
            System.out.println(shape.toDiv());
            try {
                writer.write(shape.toDiv());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
