
import generic.GenericClass;
import generic.GenericInterface;
import generic.GenericMethod;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws ClassNotFoundException {
        List<String> list = new ArrayList<>();

        //1.generic class
        GenericClass<String> genericClass = new GenericClass<>();
        genericClass.setKey("a");
        System.out.println(genericClass.getKey());

        //2.generic interface
        ChildGeneic<String> childGeneic = new ChildGeneic<>();
        String res = childGeneic.call("a");

        //3.wildcard
        GenericClass<Integer> genericClass1 = new GenericClass<>();
        genericClass1.setKey(111111);
        /*
        find that although Integer is sub class of Number, but can't be set here
        that because GenericClass<Integer> is not the sub class of GenericClass<Number>
         */
//        printKey1(genericClass1);
        printKey2(genericClass1);

        //4.generic method
        GenericMethod<String> genericMethodClass = new GenericMethod<>();
        int methodres = genericMethodClass.genericMethod(new GenericClass<>(123465));
        System.out.println(methodres);

        //5.generic extend
        GenericClass<Integer> a = new GenericClass<>();
        bound(a);
//        GenericClass<String> b = new GenericClass<>();
//        bound(b);


    }

    //2
    static class ChildGeneic<T> implements GenericInterface<T> {

        @Override
        public T call(T t) {
            return t;
        }
    }

    //3
    static void printKey1(GenericClass<Number> clazz) {
        System.out.println(clazz.getKey());
    }
    //3.what the difference between <?> and nothing,其实应该是没区别，？的主要作用在于extend和super，如果单独用？还不如不用
    static void printKey2(GenericClass clazz) {
        System.out.println(clazz.getKey());
    }
    //5
    static void bound(GenericClass<? extends Number> num) {

    }
}