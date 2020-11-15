package bean;

public class Food implements Commodity {

    public Food(String name, float price) {
        this.name = name;
        this.price = price;
    }

    private String name;
    private float price;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public float getPrice() {
        return 0;
    }
}
