package com.my.koindemo

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Module 是一个容器，它储存了所有需要注入的对象的实例化方式。换句话说，假如我们想要在某个类中注入 Stove，
 * 那么就必须在 Module 中定义究竟如何取得或创建 Stove 的实例，这一过程本质是将 Service 插入 Module 图中。
 */
val myModule = module {
    /*
    添加 Service 有两个函数分别是 factory 与 single。区别在于前者将在每次被需要时都创建（获取）一个新的实例，
    也就是说后边代码块将被多次运行。而 single 会让 Koin 保留实例用于今后直接返回
     */
    factory {
//    single {
        Stove()
        /*
        bind 是一个中缀函数，可以用于把一个 Service 关联到多个类。
        例如现在有两个接口：Tool, Flammable，Stove 实现了他们。
        显然如果只定义1个 Service 是不能同时注入 Stove 和这两个接口的，act里声明tool和flammable的时候会编译出错
         */
    } bind Tool::class bind Flammable::class

    factory {
        /*
        get 用于最终实现注入，顾名思义就是获得一个实例。在需要获取实例的地方直接填个 get，
        Koin 就会根据数据类型自动从上文 Module 中找到匹配的方法取得实例，即上面Stove的service
         */
        Chef(get())
    }

    /*
    Scope 用于控制对象在 Koin 内的生命周期。事实上，前面所讲的 single 与 factory 都是 scope。
     */
    scope(named("MY_SCOPE")) {
        scoped {
            Stove()
        }
    }
}

val viewModelModule = module {
    viewModel {
        MyViewModel()
    }
}