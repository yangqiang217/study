import bean.Commodity;
import bean.Food;

public class Vendor implements Sell {
    @Override
    public Commodity sell() {
        System.out.println("vendor sell");
        return new Food("real", 12);
    }

    @Override
    public void ad() {
        System.out.println("vendor ad");
    }
}
