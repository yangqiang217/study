package generic;

public class GenericMethod<T> {

    private T key;

    public T getKey() {
        return key;
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
}
