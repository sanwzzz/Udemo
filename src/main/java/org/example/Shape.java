package org.example;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Data
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

        //求出最长的边 且x靠近原点的值 以及短边的夹角
        Line maxLineOr = null;
        Line minLineOr = null;
        for (Line line : lines) {
            //首次跳过
            if (Objects.isNull(maxLineOr)) {
                maxLineOr = line;
                minLineOr = line;
                continue;
            }
            // >1次数 求出靠近远点的长边
            //Y1 Y2的差值的绝对值比对
            int newLength = line.length();//本次
            int maxLLength = maxLineOr.length();//最大
            int minLLength = minLineOr.length();//最小

            if (newLength > maxLLength) {
                //如果新边大于旧 替换最长边
                maxLineOr = line;
            }else if (newLength == maxLLength){
                //如果相等 则取出x最小的那个
                int newXLength = Math.min(line.getX1(), line.getX2());
                int oldXLength = Math.min(maxLineOr.getX1(), maxLineOr.getX2());
                if (newXLength < oldXLength){
                    maxLineOr = line;
                }
            }

            // 求出靠近远点的短边
            if (newLength < minLLength) {
                //如果新边大于旧 替换最长边
                minLineOr = line;
            }else if (newLength == minLLength){
                //如果相等 则取出x最小的那个
                int newMinYLength = Math.min(line.getY1(), line.getY2());
                int oldMinYLength = Math.min(minLineOr.getY1(), minLineOr.getY2());
                if (newMinYLength < oldMinYLength){
                    minLineOr = line;
                }
            }
        }
        sb.append("</div>");

        //长边是否是水平线
        boolean maxIsHorizontal  = Objects.equals(maxLineOr.getY1(), maxLineOr.getY2());
        System.out.println("长边信息: " + maxLineOr + "短边信息: " + minLineOr + "长边是否是水平线: " + maxIsHorizontal);

        if (maxIsHorizontal){
            //长边水平
            sb.append("<div style=\"position: absolute; left: ").append(maxLineOr.getMinX()+20).append("px; top: ").append(minLineOr.getMaxY()-30).append("px; width: ").append(30).append("px; height: ").append(15)
                    .append("px; border: 2px solid #499C5C").append(";\">");
        }else {
            //长边是垂直线
//            sb.append("<div style=\"position: absolute; left: ").append(maxLineOr.getMinX()+20).append("px; top: ").append(maxLineOr.getMaxY()-50).append("px; width: ").append(15).append("px; height: ").append(30)
//                    .append("px; border: 2px solid #499C5C").append(";\">");

        }
        sb.append(++count);

        sb.append("</div>");


        return sb.toString();
    }

    private static volatile int count = 0;
}
