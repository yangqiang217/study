import bean.Commodity;

import java.lang.reflect.Proxy;

public class Main {

    public static void main(String[] args) {
        DynamicProxy inter = new DynamicProxy(new Vendor());

        //加上这句将会在项目根目录产生一个$Proxy0.class文件，这个文件即为动态生成的代理类文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        /*
         * 这里产生的sell其实并不是Vendor，而是个java运行时动态实现接口Sell在内存中构造了一个.class(一般是com.sun.proxy.Proxy0)，而将DynamicProxy传进去作为变量h
         * 当下面调用.sell()时，其实掉的是变量h的invoke方法
         */
        Sell sell = (Sell) Proxy.newProxyInstance(Sell.class.getClassLoader(), new Class[]{Sell.class}, inter);

        Commodity res = sell.sell();
        System.out.println("res: name: " + res.getName() + ", price: " + res.getPrice());
        sell.ad();
    }
}
