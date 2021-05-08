package fac.factory.abs;

import fac.laptop.Laptop;
import fac.laptop.Mac;
import fac.phone.Iphone;
import fac.phone.Phone;

public class AppleProductFactory implements ProductFactory{
    @Override
    public Phone createPhone() {
        return new Iphone();
    }

    @Override
    public Laptop createLaptop() {
        return new Mac();
    }
}
