package fac.factory;

import fac.phone.Iphone;
import fac.phone.Phone;

public class IphoneFactory implements PhoneFactory {
    @Override
    public Phone createPhone() {
        System.out.println("create apple phone");
        return new Iphone();
    }
}
