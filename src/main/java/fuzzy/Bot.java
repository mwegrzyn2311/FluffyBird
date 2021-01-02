package fuzzy;

import game.Bird;
import game.Pipe;
import gui.Game;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

public class Bot {
    private Game game;
    private FIS fis;
    private FuzzyRuleSet fuzzyRuleSet;
    public Bird bird;

    private static String path = "D:\\AAASem5\\PSI\\Lab2[Fuzzy]\\Sterownik\\src\\main\\resources\\fcl\\";

    public Bot(Game game, Bird botBird, String fileName) {
        this.game = game;
        this.bird = botBird;
        fis = FIS.load(path+fileName,false);
        fuzzyRuleSet = fis.getFuzzyRuleSet();
    }

    public void performTurn(double delta) {
        fuzzyRuleSet.setVariable("distY", (bird.y - (game.pipe1.y + Pipe.GAP/2)));
        fuzzyRuleSet.setVariable("spdY", bird.velocity);
        fuzzyRuleSet.setVariable("distFromLast", bird.x - bird.radius - game.pipe2.x);
        fuzzyRuleSet.setVariable("spdX", game.spd);
        fuzzyRuleSet.evaluate();

        double velChange = fuzzyRuleSet.getVariable("speedYChange").defuzzify();

        bird.changeVelocity(velChange, delta);
    }
}
