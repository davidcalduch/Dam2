import static java.sql.DriverManager.println;

public class Main {
    public static void main(String[] args) {
        LiquorStore miLiquorStore;
        miLiquorStore= new LiquorStore(8.95, 0.20);

        miLiquorStore.payDrinks(10);
        System.out.printf("Efectivo en la licorería: %.2f€%n", miLiquorStore.getCash());
        Store miStore= new Store(8.95) {
            @Override
            public void welcome() {
                println("Welcome to the store, Our drink price is  "+ getDrinkprice());

            }
        };
    }
}

