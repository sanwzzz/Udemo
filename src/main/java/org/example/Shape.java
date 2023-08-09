package org.example;

import java.util.LinkedList;
import java.util.List;

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
}
