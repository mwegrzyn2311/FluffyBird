package game;

import java.awt.*;

public class Pipe {
    public static int GAP = 200;
    public int width;
    public double x;
    public int y;
    public Image image;

    public Pipe(int x, int y, int width, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.image = image;
    }

    public void move(double spd, double delta) {
        this.x -= spd * delta;
    }
}
