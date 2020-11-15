public interface Observer<T> {
    void onComplete();
    void onError(Throwable e);
    void onNext(T t);
}
