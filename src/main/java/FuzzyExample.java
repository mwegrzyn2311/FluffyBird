import game.Pipe;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

public class FuzzyExample {

    public static void main(String[] args) throws Exception {
        try {
            String fileName = args[0];
            float distY = Float.parseFloat(args[1]);
            float spdY = Float.parseFloat(args[2]);
            float distFromLast = Float.parseFloat(args[3]);
            float gameSpd = Float.parseFloat(args[4]);
            FIS fis = FIS.load(fileName,false);

//wyswietl wykresy funkcji fuzyfikacji i defuzyfikacji
            FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();
            fuzzyRuleSet.chart();

//zadaj wartosci wejsciowe
            fuzzyRuleSet.setVariable("distY", distY);
            fuzzyRuleSet.setVariable("spdY", spdY);
            fuzzyRuleSet.setVariable("distFromLast", distFromLast);
            fuzzyRuleSet.setVariable("spdX", gameSpd);
//logika sterownika
            fuzzyRuleSet.evaluate();

//graficzna prezentacja wyjscia
            fuzzyRuleSet.getVariable("speedYChange").chartDefuzzifier(true);
            System.out.println(fuzzyRuleSet.getVariable("speedYChange").getLatestDefuzzifiedValue());

            System.out.println(fuzzyRuleSet);

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Niepoprawna liczba parametrow. Przyklad: java FuzzyExample string<plik_fcl> int<poziom natezenia> int<pora dnia>");
        } catch (NumberFormatException ex) {
            System.out.println("Niepoprawny parametr. Przyklad: java FuzzyExample string<plik_fcl> int<poziom natezenia> int<pora dnia>");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}