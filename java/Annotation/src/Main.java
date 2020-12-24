import java.lang.reflect.Field;
import java.lang.reflect.Method;

@MyAnnotation(name = "yq")
public class Main {

    @FieldAnnotation(val = 11)
    private int field = 0;

    public static void main(String[] args) {
        //获取类注解
        Class<Main> mainClass = Main.class;
        boolean present = mainClass.isAnnotationPresent(MyAnnotation.class);
        if (present) {
            MyAnnotation myAnnotation = mainClass.getAnnotation(MyAnnotation.class);
            System.out.println(myAnnotation.name());
            System.out.println(myAnnotation.age());
        }

        //获取Field注解
        try {
            Field f = mainClass.getDeclaredField("field");
            boolean has = f.isAnnotationPresent(FieldAnnotation.class);
            if (has) {
                FieldAnnotation ann = f.getAnnotation(FieldAnnotation.class);
                System.out.println(ann.val());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        //获取方法注解
        try {
            Method fun = mainClass.getDeclaredMethod("fun");
            if (fun != null) {
                MethodAnnotation methodAnnotation = fun.getAnnotation(MethodAnnotation.class);
                String[] params = methodAnnotation.param();
                for (String p : params) {
                    System.out.println(p);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @MethodAnnotation(param = {"a", "b"})
    private void fun() {

    }
}
