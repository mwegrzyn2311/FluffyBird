package gui;

import game.Bird;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {
    private Bird bird;
    private Game game;

    public MyKeyListener(Bird bird, Game game) {
        this.bird = bird;
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if((int) e.getKeyChar() == 27) {
            game.paused = true;
        }
        switch(e.getKeyChar()) {
            case ' ' -> {
                game.paused = false;
                bird.jump();
            }
            case 'w' -> {
                game.paused = false;
                bird.flyUp = true;
            }
            case 's' -> {
                game.paused = false;
                bird.flyDown = true;
            }
            case 'd' -> {
                game.spdUp();
                game.paused = false;
            }
            case 'r' -> {
                game.reset();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyChar()) {
            case 'w' -> {
                bird.flyUp = false;
            }
            case 's' -> {
                bird.flyDown = false;
            }
            case 'd' -> {
                game.spdBackFromUp();
            }
        }
    }
}
