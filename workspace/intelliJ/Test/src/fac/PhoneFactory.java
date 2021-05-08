package fac;

import fac.phone.Iphone;
import fac.phone.Phone;
import fac.phone.XiaoMi;

public class PhoneFactory {
    public static Phone createPhone(String type) {
        switch (type) {
            case "iphne":
                return new Iphone();
            case "mi":
                return new XiaoMi();
        }
        return null;
    }

    public static <T extends Phone> T create(Class<T> phone) {
        T p = null;
        try {
            p = (T) Class.forName(phone.getName()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return p;
    }
}
