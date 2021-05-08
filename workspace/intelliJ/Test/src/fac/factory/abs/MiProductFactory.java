package fac.factory.abs;

import fac.laptop.Laptop;
import fac.laptop.MiLaptop;
import fac.phone.Phone;
import fac.phone.XiaoMi;

public class MiProductFactory implements ProductFactory{
    @Override
    public Phone createPhone() {
        return new XiaoMi();
    }

    @Override
    public Laptop createLaptop() {
        return new MiLaptop();
    }
}
