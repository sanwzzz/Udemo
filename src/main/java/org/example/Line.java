package org.example;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class Line implements Serializable,Cloneable {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    public Line(int x1, int y1, int x2, int y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;

        minX = Math.min(x1, x2);
        maxX = Math.max(x1, x2);
        minY = Math.min(y1, y2);
        maxY = Math.max(y1, y2);
    }

    public Line(Line line){
        this.x1=line.x1;
        this.y1=line.y1;
        this.x2=line.x2;
        this.y2=line.y2;

        minX = line.minX;
        maxX = line.maxX;
        minY = line.minY;
        maxY = line.maxY;
    }

    public Line clone() throws CloneNotSupportedException {
        return (Line) super.clone();
    }

    @Override
    public String toString() {
        return "Line{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return x1 == line.x1 && y1 == line.y1 && x2 == line.x2 && y2 == line.y2 && maxX == line.maxX && minX == line.minX && maxY == line.maxY && minY == line.minY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, y1, x2, y2, maxX, minX, maxY, minY);
    }


    public void setX1(int x1) {
        this.x1 = x1;
        this.maxX = Math.max(x1, x2);
    }

    public void setY1(int y1) {
        this.y1 = y1;
        this.maxY = Math.max(y1, y2);
    }

    public void setX2(int x2) {
        this.x2 = x2;
        this.maxX = Math.max(x1, x2);
    }

    public void setY2(int y2) {
        this.y2 = y2;
        this.maxY = Math.max(y1, y2);
    }
}
