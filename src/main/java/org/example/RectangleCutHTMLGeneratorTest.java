package org.example;

import cn.hutool.core.collection.CollUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RectangleCutHTMLGeneratorTest {

    public static void main(String[] args) {
        //外层的高 宽
        int width = 800;
        int height = 600;

        // 切割线列表 有序
        LinkedList<Line> lines = new LinkedList<>();
        lines.add(new Line(550, 0, 550, 600));
        lines.add(new Line(0, 500, 550, 500));
        lines.add(new Line(0, 450, 550, 450));
        lines.add(new Line(450, 0, 450, 450));

        List<Shape> shapes = searchShape(800, 600, lines);
        for (Shape shape : shapes) {
            System.out.println(shape);
        }

    }

    public static List<Shape> searchShape(int width, int height, List<Line> lines){

        LinkedList<Shape> shapes = new LinkedList<>();
        for (Line line : lines) {
            //模拟切割

            //检测是横切还是竖切
            if (line.getX1() == line.getX2()) {
                // 竖切
                //竖切后检测 右边的矩形是否需要继续切割
                //即还存在切割线 x 大于当前切割线的 x
                List<Line> collect = lines.stream().filter(l -> l.getY1() > line.getMaxY()).collect(Collectors.toList());
                if (CollUtil.isEmpty(collect)) {
                    // 如果不存在 则结束递归 添加右侧料板
                    shapes.add(Shape.create(line.getX1(), line.getY1(), line.getX2(), line.getY2(), width, height, width - line.getMinY(), 0));

                } else {
                    // 如果存在 则递归

                }
                width = width - line.getMaxX();
            }else if (line.getY1() == line.getY2()){
                // 横切
                // 横切后检测 上面的矩形是否需要继续切割
                // 即还存在切割线 y 大于当前切割线的 y
                List<Line> collect = lines.stream().filter(l -> l.getX1() > line.getMaxX()).collect(Collectors.toList());
                if(CollUtil.isEmpty(collect)){
                    //如果不存在 则结束递归 添加上方料板
                    shapes.add(Shape.create(line.getX1(), line.getY1(), line.getX2(), line.getY2(), width, height, 0, height - line.getMinX()));
                }else {
                    //如果存在 则递归

                }
                height = height - line.getMaxY();
            }
        }

        return shapes;
    }




}
