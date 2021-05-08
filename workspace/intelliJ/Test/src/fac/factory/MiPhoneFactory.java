package fac.factory;

import fac.phone.Phone;
import fac.phone.XiaoMi;

public class MiPhoneFactory implements PhoneFactory {
    @Override
    public Phone createPhone() {
        System.out.println("create mi phone");
        return new XiaoMi();
    }
}
