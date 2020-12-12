package generic;

public class GenericClass<T> {

    private T key;

    public GenericClass() {
    }

    public GenericClass(T key) {
        this.key = key;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }
}
