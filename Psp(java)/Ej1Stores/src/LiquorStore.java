import static java.sql.DriverManager.println;
public class LiquorStore extends Store {
    private double tax;
    public LiquorStore(double v, double drinkPrice)  {
        super(drinkPrice);
        this.tax = tax;
    }
    @Override
    public void payDrinks(int num0fDrinks)
    {
        super.payDrinks(num0fDrinks);
        double taxprice= getDrinkprice()*num0fDrinks*tax;

    }
    @Override
    public void welcome()
    {
        println("Bienvenido a la licoreria");
    }
}
