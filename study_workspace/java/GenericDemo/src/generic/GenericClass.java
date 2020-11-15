package generic;

public class GenericClass<T> {

    public GenericClass() {
    }

    public GenericClass(T key) {
        this.key = key;
    }

    private T key;

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }


}
