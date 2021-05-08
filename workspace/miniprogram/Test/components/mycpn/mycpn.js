// components/mycpn/mycpn.js
//自定义组件的使用：1.在xx.json的usingComponents注册，注册的key是用的时候的<xx>里的xx，xx只能是小写字母和-和_的组合；2.在wxml中直接用<xx>。如果多个页面要用到某个自定义组件，就注册在app.json中，也是一样的usingComponents
//自定义组件中也可以使用自定义组件
//自定义组件根目录不能以wx-开头
Component({
    /**
     * Component properties
     */
    properties: {
        //用来让调用组件者动态设置组件的属性
        // title: String//此写法不能设置默认值
        title: {
            type: String,
            value: "组件默认title",
            //此处监听也可以在下面和properties平级的observers里面监听，但就没oldvalue了
            observer: function(newVal, oldVal) {
                console.log(newVal, oldVal)
            }
        }
    },

    //用来让调用组件者动态设置组件的样式
    externalClasses: ["classsetbycaller"],

    /**
     * Component initial data
     */
    data: {
        title: "自定义组件title",
        content: "点击改变外面",

        counterChangedByCaller: 0
    },

    /**
     * Component methods
     * 方法要写在这里面
     */
    methods: {
        handleBtnCpnClick() {
            this.triggerEvent("cpnBtnClick", {name: "yq", age: 18}, {})
        },

        incrementCounter(num) {
            this.setData({
                counterChangedByCaller: this.data.counterChangedByCaller + num
            })
        }
    },

    //监听properties/data的改变
    observers: {
        //此处没有oldvalue
        counterChangedByCaller: function(newVal) {
        }
    },

    //组件生命周期
    //1.监听所在页面的生命周期
    pageLifetimes: {
        show() {

        },
        hide() {

        },
        resize() {

        }
    },
    //2.监听组件生命周期
    lifetimes: {
        created(){},
        attached(){},
        ready(){},
        moved(){},
        detached(){}
    }
})
