package fac.factory.abs;

import fac.laptop.Laptop;
import fac.phone.Phone;

public interface ProductFactory {
    Phone createPhone();
    Laptop createLaptop();
}
