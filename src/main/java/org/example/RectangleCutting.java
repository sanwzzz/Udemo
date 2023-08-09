import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Line {
    Point start, end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
}

public class RectangleCutting {
    public static Point getIntersection(Point p1, Point p2, Point p3, Point p4) {
        // 计算线段 (p1, p2) 和 (p3, p4) 的交点
        // 返回交点的坐标，如果不存在交点则返回 null
        return null;
    }

    public static void main(String[] args) {
        // 底板矩形
        Point bottomLeft = new Point(0, 0);
        Point topRight = new Point(800, 600);

        // 切割线列表
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(new Point(550, 0), new Point(550, 600)));
        lines.add(new Line(new Point(0, 500), new Point(550, 500)));
        lines.add(new Line(new Point(0, 450), new Point(550, 450)));
        lines.add(new Line(new Point(450, 0), new Point(450, 450)));

        // 添加底板矩形的四个角点
        List<Point> points = new ArrayList<>();
        points.add(bottomLeft);
        points.add(new Point(topRight.x, bottomLeft.y));
        points.add(topRight);
        points.add(new Point(bottomLeft.x, topRight.y));

        // 计算切割线与底板矩形的交点
        for (Line line : lines) {
            for (Point point : points) {
                Point intersection = getIntersection(line.start, line.end, point, points.get((points.indexOf(point) + 1) % 4));
                if (intersection != null) {
                    points.add(intersection);
                }
            }
        }

        // 对交点进行排序，按照 x 坐标从小到大
        Collections.sort(points, Comparator.comparingInt(p -> p.x));

        // 划分图形并计算每个图形的顶点坐标
        for (int i = 0; i < points.size() - 1; i++) {
            System.out.println("图形 " + (i + 1) + " 的顶点坐标：" + points.get(i).x + "," + points.get(i).y + " ; " + points.get(i + 1).x + "," + points.get(i + 1).y);
        }
    }
}
