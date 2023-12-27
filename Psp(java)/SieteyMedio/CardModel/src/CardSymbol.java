
public enum CardSymbol {

    SA('A'),S2('2'),S3('3'),S4('4'),S5('5'),S6('6'),S7('7'),SJ('J'),SQ('Q'),SK('K');
    private char symbol;
    private char value;
    CardSymbol(char symbol){
        this.symbol = symbol;
        this.value= value;
    }

    public char getSymbol() {
        return symbol;
    }

    public char getValue() {
        return value;
    }
}

