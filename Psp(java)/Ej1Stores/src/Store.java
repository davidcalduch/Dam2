public abstract class Store {
    private double cash;
   private double drinkprice;

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getDrinkprice() {
        return drinkprice;
    }

    public void setDrinkprice(double drinkprice) {
        this.drinkprice = drinkprice;
    }

    public abstract void welcome();
    public Store( double drinkprice) {
        this.cash = 0.0;
        this.drinkprice = drinkprice;
    }
    public void payDrinks(int num0fDrinks)
    {
        double totalCost= drinkprice*num0fDrinks;
        cash+= totalCost;

    }

}
