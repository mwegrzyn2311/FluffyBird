package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Bird {
    public String name;

    public double y;
    public int x;
    public int radius;

    public double preJumpY;
    public double velocity = 0;
    public double angle = 0.0;
    private final double MIN_SPD = -20;
    private final double MAX_SPD = 20;

    private final double FLY_CONST = 0.25;

    public boolean flyUp = false;
    public boolean flyDown = false;


    public List<BufferedImage> animationFrames = new ArrayList<>(4);

    private final static int MAX_ROTATION = 90;
    private final static int MIN_ROTATION = -90;

    private boolean alive = true;

    public int score = 0;

    public boolean GOD_MODE = false;

    public Bird(String name, int startY, int startX, int radius, BufferedImage image1, BufferedImage image2, BufferedImage image3) {
        this.name = name;
        this.y = startY;
        this.x = startX;
        this.radius = radius;
        this.animationFrames.add(image2);
        this.animationFrames.add(image1);
        this.animationFrames.add(image2);
        this.animationFrames.add(image3);
    }

    public void move(double delta) {
        if(flyUp) {
            flyUp(delta);
        }
        if(flyDown) {
            flyDown(delta);
        }

        this.y += velocity;
    }

    public void jump() {
        this.velocity = MIN_SPD;
        preJumpY = this.y;
    }

    public void changeVelocity(double velChange, double delta) {
        velocity = Math.max(MIN_SPD, Math.min(velocity + velChange * delta, MAX_SPD));
        angle = Math.max(MIN_ROTATION, Math.min(angle + velChange/MAX_SPD * delta * MAX_ROTATION, MAX_ROTATION));
    }

    public void flyUp(double delta) {
        velocity = Math.max(MIN_SPD, velocity - FLY_CONST * delta);
        angle = velocity / MAX_SPD * MAX_ROTATION;
    }


    public void flyDown(double delta) {
        velocity = Math.min(MAX_SPD, velocity + FLY_CONST * delta);
        angle = velocity / MAX_SPD * MAX_ROTATION;
    }

    public void kill() {
        this.alive = false;
    }
    public void revive() {
        this.alive = true;
        this.angle = 0;
        this.velocity = 0;
    }
    public boolean isAlive() {
        return this.alive;
    }

    public BufferedImage getAnimationFrame(int i) {
        return this.animationFrames.get(i);
    }

    @Override
    public String toString() {
        return name;
    }
}
