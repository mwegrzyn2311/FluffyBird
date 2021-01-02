package game;

import java.awt.*;

public class Base {

    public double x;
    public double y;
    public int baseLen;
    private double minX;
    public Image image;

    public Base(double x, double y, int baseLen, Image image) {
        this.x = x;
        this.y = y;
        this.baseLen = baseLen;
        this.minX = x - baseLen;
        this.image = image;
    }

    public void move(double spd, double delta) {
        this.x -= spd * delta;
        if(this.x < minX) {
            this.x += baseLen;
        }
    }
}
