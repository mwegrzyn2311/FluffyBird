package gui;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        super();

        Game game = new Game(this);

        this.add(game);

        this.pack();
        setTitle("Fluffy bird");
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        game.start();
    }
}
