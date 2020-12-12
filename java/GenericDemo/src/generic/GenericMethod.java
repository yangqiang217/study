package generic;

import generic.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericMethod<T> {

    public <S> void f1(S t){
    }

    public void f2(T t) {
    }

    public void f3(List<T> list) {

    }

    public <S> void f4(List<S> list) {

    }

    public T f5() {
        return null;
    }

    public <S> S f6() {
        return null;
    }

    public List<T> f7() {
        return null;
    }

    public <S> List<S> f8() {
        return null;
    }

    public static void f9(List<? extends Father> list) {
//        list.add(new Son()); error，<? extends XX>不能添加
//        list.add(new Father());
//        Son s = list.get(0);
        Father f = list.get(0);
    }

    public static void f10(List<? super Father> list) {
        list.add(new Son());
        list.add(new Father());
//        list.add(new Object());

        Object o = list.get(0);
//        Father f = list.get(0);
    }

    public static List<? super Father> f11() {
        List<Object> list = new ArrayList<>();
//        List<Father> list = new ArrayList<>();
//        List<Son> list = new ArrayList<>();
        return list;
    }

    public static void f12(List<?> list) {
//        list.add("");
        String a = (String) list.get(0);
        Object o = list.get(0);
    }

    /**
     * 泛型方法，可以和所属的类完全没有关系,对比getKey()，类的T是声明在类名后面，方法的S声明在方法返回值前面
     * 声明都用<>包住
     *
     * <S>表示声明, <S>后面的S才表示返回类型
     */
    public <S> S genericMethod(GenericClass<S> genericClass) {
        return genericClass.getKey();
    }

    public static void addAll(List<T> list) {

    }

    public List<? super Father> getAll() {
        return null;
    }

    public static void test() {
        List<Son> l = new ArrayList<>();
        addAll(l);
    }
    /**
     * 1. S后面的extends不能是super，因为super只能在?后面
     * @param list
     * @param <S>
     * @return 表示返回list的子类，返回值里面的元素
     */
    public static <S extends Father<? super S>> S max(List<S> list) {
        return null;
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list) {
    }
}
