import java.lang.annotation.*;

/**
 * @Retention 保留规则。
 * 在@Retention注解中使用枚举RetentionPolicy来表示注解保留时期
 * Retention(RetentionPolicy.SOURCE)，注解仅存在于源码中，在class字节码文件中不包含
 * Retention(RetentionPolicy.CLASS)， 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得
 * Retention(RetentionPolicy.RUNTIME)， 注解会在class字节码文件中存在，在运行时可以通过反射获取到
 * 如果我们是自定义注解，则通过前面分析，我们自定义注解如果只存着源码中或者字节码文件中就无法发挥作用，
 * 而在运行期间能获取到注解才能实现我们目的，所以自定义注解中肯定是使用 @Retention(RetentionPolicy.RUNTIME)
 *
 * @Target
 * 表示我们的注解作用的范围就比较具体了，可以是类，方法，方法参数变量等，同样也是通过枚举类ElementType表达作用类型
 * Target(ElementType.TYPE) 作用接口、类、枚举、注解
 * Target(ElementType.FIELD) 作用属性字段、枚举的常量
 * Target(ElementType.METHOD) 作用方法
 * Target(ElementType.PARAMETER) 作用方法参数
 * Target(ElementType.CONSTRUCTOR) 作用构造函数
 * Target(ElementType.LOCAL_VARIABLE)作用局部变量
 * Target(ElementType.ANNOTATION_TYPE)作用于注解（@Retention注解中就使用该属性）
 * Target(ElementType.PACKAGE) 作用于包
 * Target(ElementType.TYPE_PARAMETER) 作用于类型泛型，即泛型方法、泛型类、泛型接口 （jdk1.8加入）
 * Target(ElementType.TYPE_USE) 类型使用.可以用于标注任意类型除了 class （jdk1.8加入）
 * 一般比较常用的是ElementType.TYPE类型
 *
 * @Documented
 * Document的英文意思是文档。它的作用是能够将注解中的元素包含到 Javadoc 中去。
 *
 * @Inherited
 * 和我们平时理解的继承大同小异，一个被@Inherited注解了的注解修饰了一个父类，
 * 如果他的子类没有被其他注解修饰，则它的子类也继承了父类的注解。
 *
 * @Repeatable
 * 说明被这个元注解修饰的注解可以同时作用一个对象多次，但是每次作用注解又可以代表不同的含义。
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyAnnotation {
    /*
    注解属性类型可以有以下列出的类型
    1.基本数据类型
    2.String
    3.枚举类型
    4.注解类型
    5.Class类型
    6.以上类型的一维数组类型
     */
    String name() default "";
    int age() default 0;
}
