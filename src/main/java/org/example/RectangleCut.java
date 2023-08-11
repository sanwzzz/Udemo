package org.example;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RectangleCut {

    public static void main(String[] args) throws URISyntaxException {

        // 获取同级目录的source.txt文件路径
        String filePath = new File(RectangleCut.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent() + "/source.txt";
        List<String> sources = FileUtil.readLines(filePath, "utf-8");


        //外层的高 宽
        int width = 0, height = 0;
        LinkedList<Line> lines = new LinkedList<>();


        for (String source : sources) {
            if (width == 0) {
                String[] split = source.split(",");
                width = Integer.parseInt(split[0]);
                height = Integer.parseInt(split[1]);
                continue;
            }
            String[] split = source.split(",");
            //         切割线列表 从右到左 从上到下 有序排列
            lines.add(new Line(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])));
        }



        List<Shape> shapes = new LinkedList<>();

        shapes = searchShape(new Integer(width), new Integer(height), lines);
        for (Shape shape : shapes) {
            System.out.println(shape);
        }
        createHtml(shapes);
    }

    public static List<Shape> searchShape(int width, int height, List<Line> lines){

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
                List<Line> collect = lines.stream()
                        .filter(l-> Objects.equals(l.getY1(), l.getY2()))
                        .filter(l -> l.getMaxX() > line.getMaxX())
                        .collect(Collectors.toList());
                Shape shape = Shape.create(line.getX1(), line.getY1(), line.getX2(), line.getY2(), width, height, width - line.getMinY(), 0);
                if (CollUtil.isEmpty(collect)) {
                    // 如果不存在 则结束递归 添加右侧料板
                    shapes.add(shape);
                } else {
                    //右侧线要减去当前切割线的x
                    int tmp;
                    //重置切线X
                    Line rightCutLine;
                    for (int i = 0; i < collect.size(); i++) {
                        Line originCutLine = collect.get(i);
                        collect.remove(i);
                        rightCutLine = new Line(originCutLine);
                        collect.add(i, rightCutLine);
                        //右侧切线的x要减去当前切割线的x
                        if ((tmp = rightCutLine.getX1() - line.getX1()) > 0){
                            rightCutLine.setX1(tmp);
                            originCutLine.setX1(line.getX1());
                        }
                        if ((tmp = rightCutLine.getX2() - line.getX1()) > 0){
                            rightCutLine.setX2(tmp);
                            originCutLine.setX2(line.getX1());
                        }
                    }

                    // 如果存在
                    List<Shape> rightShapes = searchShape(shape.getWidth(), shape.getHeight(), collect);
                    //shapes1中为右侧料板的切割结果 其中所有的x都要加上当前切割线的x
                    for (Shape rShape : rightShapes) {
                        for (Line rSLine : rShape.lines) {
                            rSLine.setX1(rSLine.getX1() + line.getMaxX());
                            rSLine.setX2(rSLine.getX2() + line.getMaxX());
                        }
                    }
                    shapes.addAll(rightShapes);

                }
                width = line.getMaxX();
            }else if (line.getY1() == line.getY2()){
                // 横切
                // 横切后检测 上面的矩形是否需要继续切割
                // 即还存在切割线 y 大于当前切割线的 y
                List<Line> collect = lines.stream()
                        .filter(l-> Objects.equals(l.getX1(), l.getX2()))
                        .filter(l -> l.getMaxY() > line.getMaxY())
                        .collect(Collectors.toList());
                Shape shape = Shape.create(line.getX1(), line.getY1(), line.getX2(), line.getY2(), width, height, 0, height - line.getMinX());
                if(CollUtil.isEmpty(collect)){
                    //如果不存在 则结束递归 添加上方料板
                    shapes.add(shape);
                }else {
                    int tmp;
                    Line rightCutLine;
                    for (int i = 0; i < collect.size(); i++) {
                        Line originCutLine = collect.get(i);
                        rightCutLine = new Line(originCutLine);
                        if ((tmp = rightCutLine.getY1() - line.getY1()) > 0){
                            rightCutLine.setY1(tmp);
                            originCutLine.setY1(line.getY1());
                        }
                        if ((tmp = rightCutLine.getY2() - line.getY1()) > 0){
                            rightCutLine.setY2(tmp);
                            originCutLine.setY2(line.getY1());
                        }
                    }

                    //如果存在 递归调用
                    List<Shape> topShapes = searchShape(shape.getWidth(), shape.getHeight(), collect);
                    //shapes1中为右侧料板的切割结果 其中所有的Y都要加上当前切割线的Y
                    for (Shape tShape : topShapes) {
                        for (Line tSLine : tShape.lines) {
                            tSLine.setY1(tSLine.getY1() + line.getMaxY());
                            tSLine.setY2(tSLine.getY2() + line.getMaxY());
                        }
                    }
                    shapes.addAll(topShapes);

                }
                height = line.getMaxY();
            }

            iterator.remove();
        }

        //添加最后一块剩余
        if (width > 0 && height > 0){
            shapes.add(Shape.create(0,0, 0, height, width, height, width, 0));
        }

        return shapes;
    }

    public static volatile int countShape = 0;
    
    public static void createHtml(List<Shape> list){
        File file = FileUtil.file("cut.html");
        BufferedWriter writer = FileUtil.getWriter(file, "utf-8", false);
        for (Shape shape : list) {
            String strDiv = shape.toDiv();
            System.out.println(strDiv);
            try {
                writer.write(strDiv);
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
