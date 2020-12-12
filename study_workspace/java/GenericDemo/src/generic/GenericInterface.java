package generic;

public interface GenericInterface<T, S> {
    //名字不能相同，因为擦除就区分不开了
    T call1(S s);
    T call2(T t);
}
