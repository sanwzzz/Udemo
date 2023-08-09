package org.example;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Shape {

    public List<Line> lines = new LinkedList<>();
    public void add(Line line){
        lines.add(line);
    }


    /**
     * 四点创建一个形状
     * @param a1
     * @param a2
     * @param b1
     * @param b2
     * @param c1
     * @param c2
     * @param d1
     * @param d2
     * @return
     */
    public static Shape create(int a1, int a2, int b1, int b2, int c1, int c2, int d1, int d2){
        Shape shape = new Shape();
        shape.add(new Line(a1, a2, b1, b2));
        shape.add(new Line(b1, b2, c1, c2));
        shape.add(new Line(c1, c2, d1, d2));
        shape.add(new Line(d1, d2, a1, a2));
        return shape;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "lines=" + lines +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape)) return false;
        Shape shape = (Shape) o;
        return Objects.equals(lines, shape.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }

    /**
     * 返回图形的长
     * @return
     */
    public int getHeight(){
        int minY = lines.stream().mapToInt(Line::getMinY).min().getAsInt();
        int maxY = lines.stream().mapToInt(Line::getMaxY).max().getAsInt();
        return maxY - minY;
    }

    /**
     * 返回图形的宽
     * @return
     */
    public int getWidth() {
        int minX = lines.stream().mapToInt(Line::getMinX).min().getAsInt();
        int maxX = lines.stream().mapToInt(Line::getMaxX).max().getAsInt();
        return maxX - minX;
    }

    /**
     * 创建Div用来描述图形
     */
    public String toDiv() {
        //求出四个边的极限
        int minX = lines.stream().mapToInt(Line::getMinX).min().getAsInt();
        int minY = lines.stream().mapToInt(Line::getMinY).min().getAsInt();
        int maxX = lines.stream().mapToInt(Line::getMaxX).max().getAsInt();
        int maxY = lines.stream().mapToInt(Line::getMaxY).max().getAsInt();

        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"position: absolute; left: ").append(minX).append("px; top: ").append(minY).append("px; width: ").append(maxX-minX).append("px; height: ").append(maxY-minY)
                .append("px; border: 1px solid #").append(Integer.toHexString((int) (Math.random() * 0xFFFFFF))).append(";\">");
        sb.append("</div>");

        return sb.toString();
    }
}
