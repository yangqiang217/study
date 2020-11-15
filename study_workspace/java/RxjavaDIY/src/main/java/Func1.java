import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * for example:
 * @param <T> 由于call方法中T为参数，所以new Func1时传入第一个泛型必须是T的父类（即? super T, 称为ST），这样才能保证别人调用call的时候传进来的实参是ST的子类
 * @param <R> 同理由于call方法中R为返回类型，所以new Func1时传入第二个泛型必须是R的子类
 *
 * 其实只需要看map调用的地方：看call方法，返回必须是小类，参数必须是大类，所以new Func的地方需要前大后小
 *  map(new Func1<大, 小>() {
 *      @Override
 *      public 小 call(大 list) {
 *          return null;
 *      }
    };
 */
public interface Func1<T, R> {
    /**
     *
     * @param t can use T's subclass
     * @return R can use R's
     */
    R call(T t);

    /**
     * T和R的参数在map中是Func1<? super T, ? extends R>举例
     * ------也就是说b方法的参数只能传List的子类，而返回类型只能为b的父类------
     */
    class Example {
        void a() {
            Collection collection = null;
            ArrayList arrayList = null;
//            b(collection); //can't use
            b(arrayList); //only List's subclass allowed

            //return type
            Collection c = b(arrayList);
//            ArrayList a = b(arrayList);
        }

        List b(List list) {
            return null;
        }
    }
}