import bean.Commodity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {

    private Sell object;//real: vendor

    public DynamicProxy(Sell object) {
        this.object = object;
    }

    /**
     * 卖东西的是假的
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("sell")) {
            //fake Commodity
            System.out.println("1 into sell");
            return Proxy.newProxyInstance(Commodity.class.getClassLoader(), new Class[]{Commodity.class}, new DynamicProxy2(object));
        }

        return method.invoke(object, args);
    }


    private static class DynamicProxy2 implements InvocationHandler {

        private Object mBase;

        public DynamicProxy2(Sell realSeller) {
            mBase = realSeller.sell();
        }

        /**
         * 卖的货是假的
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("2 method: " + method.getName());
            if ("getName".equals(method.getName())) {
                return "fake name";
            }
            return method.invoke(mBase, args);
        }
    }
}
