import static java.sql.DriverManager.println;

public class CardClass {
    private CardSymbol symbol;
    private CardSuit suit;
    public CardClass(CardSymbol symbol, CardSuit suit){
        this.symbol = symbol;
        this.suit = suit;
    }
    public CardSymbol getSymbol() {
        return symbol;
    }
    public CardSuit getSuit() {
        return suit;
    }
    public String toString(){
        return symbol.toString() + suit.toString();
    }
    public String StringCardRepresentation(){
        return symbol.name()+""+ symbol.name();

    }
    public double NumericValue() {
        if (symbol == CardSymbol.SJ || symbol == CardSymbol.SA || symbol == CardSymbol.SQ || symbol== CardSymbol.SK) {
            return 0.5;

        }else{
            return symbol.getValue();
        }

    }
}
