package gui;

import fuzzy.Bot;
import game.Base;
import game.Bird;
import game.Pipe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Game extends Canvas implements Runnable {
    Image bgImage;

    private Bird playerBird;
    private List<Bird> birds = new LinkedList<>();

    private List<Bot> bots = new LinkedList<>();

    public Pipe pipe1 = null;
    public Pipe pipe2 = null;
    public Base base1;
    public Base base2;

    public boolean paused = true;
    private boolean started = false;

    private JFrame frame;
    private Thread thread;
    private boolean running = false;

    public static final int TICKSPERS = 60;
    public static final boolean ISFRAMECAPPED = false;
    public int frames;
    public int animationFrame = 0;
    public int lastFrames;
    public int ticks;

    public double spd = 2.0;
    private double baseSpd = 2.0;
    private double spdUpMultiplier = 0.75;
    private boolean spdUped = false;

    private int pointCounter = 0;
    private int speedUpMilestone = 4;

    public Game(JFrame frame) {
        bgImage = getScaledImage("/images/bg.png");

        this.frame = frame;
        Dimension size = new Dimension(bgImage.getWidth(null), bgImage.getHeight(null));
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    public void render() {
        frames++;
        BufferStrategy bs = getBufferStrategy();
        if (bs == null){
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Render background
        g.drawImage(bgImage, 0, 0, this);

        // Render pipes
        g.drawImage(pipe1.image, (int) pipe1.x, pipe1.y + Pipe.GAP, this);
        g.drawImage(pipe1.image, (int) pipe1.x, pipe1.y, pipe1.width, -pipe1.image.getHeight(null), null);
        if(pipe2 != null) {
            g.drawImage(pipe2.image, (int) pipe2.x, pipe2.y + Pipe.GAP, this);
            g.drawImage(pipe2.image, (int) pipe2.x, pipe2.y, pipe2.width, -pipe2.image.getHeight(null), null);
        }

        // Render base after the pipes so that they seem to stick out of the ground
        g.drawImage(base1.image, (int) base1.x, (int) base1.y, this);
        g.drawImage(base2.image, (int) base2.x, (int) base2.y, this);

        // Render bird
        int frame = animationFrame/30;
        birds.forEach(bird -> {
            if (bird.isAlive()) {
                BufferedImage image = bird.getAnimationFrame(frame);
                g.drawImage(getRotatedImage(image, bird.angle).filter(image, null), bird.x- bird.radius, (int) bird.y - bird.radius,this);
            }
        });
        animationFrame = (animationFrame + 1) % 120;

        g.setFont(g.getFont().deriveFont(80.0f));
        String pointsText = String.valueOf(pointCounter);
        g.drawString(pointsText, getWidth()/2 - g.getFontMetrics().stringWidth(pointsText)/2, 70);
        g.setFont(g.getFont().deriveFont(50.0f));

        String nextSpdUpText = "Next spd up in " + (speedUpMilestone - pointCounter) + " points";
        g.drawString(nextSpdUpText, getWidth()/2 - g.getFontMetrics().stringWidth(nextSpdUpText)/2, (int) base1.y + 100);

        g.drawString("Vertical speed =  " + (playerBird.isAlive()? playerBird.velocity : "-"), 10, (int) base1.y + 150);
        g.drawString("Horizontal speed = " + spd, 10, (int) base1.y + 200);

        g.dispose();
        bs.show();
    }

    private boolean allBirdsDead() {
        for(Bird bird : birds) {
            if(bird.isAlive()) {
                return false;
            }
        }
        return true;
    }
    public void tick(double delta) {
        if(!paused) {
            base1.move(spd, delta);
            base2.move(spd, delta);
            pipe1.move(spd, delta);
            if(pipe2 != null) {
                pipe2.move(spd, delta);
            }

            bots.forEach(bot -> {
                if(bot.bird.isAlive()) {
                    bot.performTurn(delta);
                }
            });
            started = true;
            birds.forEach(bird -> {
                if(bird.isAlive()) {
                    bird.move(delta);
                }
            });

            birds.forEach(bird -> {
                if(bird.isAlive()) {
                    if(!bird.GOD_MODE) {
                        if(bird.y + bird.radius > base1.y) {
                            System.out.println(bird + " died by hitting the ground");
                            bird.kill();
                        } else if((bird.x + bird.radius) >= pipe1.x && (bird.x - bird.radius) <= pipe1.x + pipe1.width && ((bird.y + bird.radius) > pipe1.y + Pipe.GAP || (bird.y - bird.radius) < pipe1.y)) {
                            System.out.println(bird + " died by hitting the pipe");
                            bird.kill();
                        }
                    }
                }
                if(bird.isAlive()) {
                    if(bird.x >= pipe1.x + pipe1.width/2) {
                        // spawn a new pipe by changing positions of current pipes
                        pipe2.x = pipe1.x;
                        pipe2.y = pipe1.y;
                        pipe1.x = getWidth();
                        pipe1.y = (int) (Math.random()*(base1.y - Pipe.GAP));
                        ++pointCounter;
                        ++bird.score;
                        if(pointCounter == speedUpMilestone) {
                            speedUpMilestone *= 2;
                            baseSpd += spdUpMultiplier;
                            spd = baseSpd;
                            if(spdUped) {
                                spd *= 2.0;
                            }
                        }
                    }
                }
            });
        } else if(!started) {
            // If game is not paused, but it just haven't started yet, simulate the feeling that bird is flying
            base1.move(spd, delta);
            base2.move(spd, delta);
        }
    }

    @Override
    public void paint(Graphics g) { }


    public synchronized void start(){
        if(running) return;
        running = true;
        thread = new Thread(this, "Thread");
        thread.start();
    }

    public synchronized void stop() {
        if(!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        BufferedImage birdImage1 = imageToBufferedImage(getScaledImage("/images/fluffyBlue1.png"));
        BufferedImage birdImage2 = imageToBufferedImage(getScaledImage("/images/fluffyBlue2.png"));
        BufferedImage birdImage3 = imageToBufferedImage(getScaledImage("/images/fluffyBlue3.png"));
        Bird ultraBotBird = new Bird("Strong bot bird", getHeight()/3, getWidth()/5, birdImage1.getHeight()/2, birdImage1, birdImage2, birdImage3);
        birds.add(ultraBotBird);
        this.bots.add(new Bot(this, ultraBotBird, "strongBot.fcl"));

        birdImage1 = imageToBufferedImage(getScaledImage("/images/fluffyGreen1.png"));
        birdImage2 = imageToBufferedImage(getScaledImage("/images/fluffyGreen2.png"));
        birdImage3 = imageToBufferedImage(getScaledImage("/images/fluffyGreen3.png"));
        Bird dummyBotBird = new Bird("Weak bot bird", getHeight()/3, getWidth()/5, birdImage1.getHeight()/2, birdImage1, birdImage2, birdImage3);
        birds.add(dummyBotBird);
        this.bots.add(new Bot(this, dummyBotBird, "dummyBot.fcl"));

        birdImage1 = imageToBufferedImage(getScaledImage("/images/fluffy1.png"));
        birdImage2 = imageToBufferedImage(getScaledImage("/images/fluffy2.png"));
        birdImage3 = imageToBufferedImage(getScaledImage("/images/fluffy3.png"));
        this.playerBird = new Bird("Player bird", getHeight()/3, getWidth()/5, birdImage1.getHeight()/2, birdImage1, birdImage2, birdImage3);
        birds.add(this.playerBird);

        Image pipeImage = getScaledImage("/images/pipev2.png");
        Image baseImage = getScaledImage("/images/base.png");

        double baseY = bgImage.getHeight(null) - baseImage.getHeight(null);
        int baseLen = baseImage.getWidth(null);

        this.addKeyListener(new MyKeyListener(playerBird, this));
        pipe1 = new Pipe(getWidth() * 2, (int) (Math.random()*(baseY - Pipe.GAP)), pipeImage.getWidth(null), pipeImage);
        pipe2 = new Pipe(-200, 0, pipeImage.getWidth(null), pipeImage);
        base1 = new Base(baseLen, baseY, baseLen, baseImage);
        base2 = new Base(0, baseY, baseLen, baseImage);
    }

    public void reset() {
        birds.forEach(bird -> {
            bird.y = getHeight()/3;
            bird.score = 0;
            bird.revive();
        });
        pipe1.x = getWidth() * 2;
        pipe1.y = (int) (Math.random()*(base1.y - Pipe.GAP));
        pipe2.x = -200;
        base1.x = base1.baseLen;
        base2.x = 0;
        pointCounter = 0;
        started = false;

        spd = 2.0;
        baseSpd = 2.0;
        spdUpMultiplier = 0.75;
        spdUped = false;
        pointCounter = 0;
        speedUpMilestone = 4;
    }

    @Override
    public void run() {
        init();
        //Tick counter variable
        long lastTime = System.nanoTime();
        //Nanoseconds per Tick
        double nsPerTick = 1000000000D/TICKSPERS;
        frames = 0;
        ticks = 0;
        long fpsTimer = System.currentTimeMillis();
        double delta = 0;
        boolean shouldRender;
        while(running){
            shouldRender = !ISFRAMECAPPED;
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            //if it should tick it does this
            while(delta >= 1 ){
                ticks++;
                if(!allBirdsDead()) {
                    tick(delta);
                }
                delta -= 1;
                shouldRender = true;
            }
            if (shouldRender && !allBirdsDead()){
                render();
            }
            if (fpsTimer < System.currentTimeMillis() - 1000){
                System.out.println(ticks +" ticks, "+ frames+ " frames");
                ticks = 0;
                lastFrames = frames;
                frames = 0;
                fpsTimer = System.currentTimeMillis();
            }
        }
    }

    private AffineTransformOp getRotatedImage(BufferedImage image, double angle) {
        double rotationRequired = Math.toRadians (angle);
        double locationX = image.getWidth(null) / 2;
        double locationY = image.getHeight(null) / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op;
    }

    private Image getScaledImage(String path) {
        Image res = null;
        try {
            res = ImageIO.read(getClass().getResource(path));
            res = res.getScaledInstance(res.getWidth(null)*2, res.getHeight(null)*2, Image.SCALE_DEFAULT);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private BufferedImage imageToBufferedImage(Image img) {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public void spdUp() {
        if(!this.spdUped) {
            this.spd *= 2.0;
            this.spdUped = true;
        }
    }
    public void spdBackFromUp() {
        if(this.spdUped) {
            this.spd /= 2.0;
            this.spdUped = false;
        }
    }
}
