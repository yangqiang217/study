
import generic.GenericClass;
import generic.GenericInterface;
import generic.GenericMethod;
import generic.util.Comp;
import generic.util.Father;
import generic.util.Son;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws ClassNotFoundException {
        List<String> list = new ArrayList<>();
        List<Comp> l2 = new ArrayList<>();
        l2.add(new Comp());
        GenericMethod.sort(l2);

        List<Integer[]> a = new ArrayList<>();
//
//        //1.generic class
//        GenericClass<String> genericClass = new GenericClass<>();
//        genericClass.setKey("a");
//        System.out.println(genericClass.getKey());
//
//        //2.generic interface
//        GenericInterfaceImpl genericInterfaceImpl = new GenericInterfaceImpl();
//        String res = genericInterfaceImpl.call1(1);
//        String res1 = genericInterfaceImpl.call2("1");
//
//        GenericInterfaceImpl genericInterface = new GenericInterfaceImpl();
//
//        //3.wildcard
//        GenericClass<Integer> genericClass1 = new GenericClass<>();
//        genericClass1.setKey(111111);
//        /*
//        find that although Integer is sub class of Number, but can't be set here
//        that because GenericClass<Integer> is not the sub class of GenericClass<Number>
//         */
////        printKey1(genericClass1);
//        printKey2(genericClass1);
//
//        //4.generic method
//        GenericMethod<String> genericMethodClass = new GenericMethod<>();
//        int methodres = genericMethodClass.genericMethod(new GenericClass<>(123465));
//        System.out.println(methodres);
//
//        //5.generic extend
//        GenericClass<Integer> a = new GenericClass<>();
//        bound(a);
////        GenericClass<String> b = new GenericClass<>();
////        bound(b);
//
//        List<Son> list1 = new ArrayList<>();
//        genericMethodClass.addAll(list1);
//        List<? super Father> list2 = genericMethodClass.getAll();
//
//        List<String> l = new ArrayList<>();
//        genericMethodClass.f4(l);
    }

    //2

    /**
     * GenericInterfaceImpl后面的<T, S>是声明，必须要，虽然接口里面有自己的<T, S>声明
     * 或者GenericInterface的<T, S>都别要了大家都用Object然后报警告
     * 此时两个call方法的参数、返回值都是Object
     */
    static class GenericInterfaceImpl<T, S> implements GenericInterface<T, S>  {


        @Override
        public T call1(S s) {
            return null;
        }

        @Override
        public T call2(T t) {
            return null;
        }
    }

    static class GenericInterfaceImplSon extends GenericInterfaceImpl {

    }

    static class GenericInterfaceImpl2<T extends String, S extends Number> implements GenericInterface<String, Integer>  {


        @Override
        public String call1(Integer integer) {
            return null;
        }

        @Override
        public String call2(String string) {
            return null;
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