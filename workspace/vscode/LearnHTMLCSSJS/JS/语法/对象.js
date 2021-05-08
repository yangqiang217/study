//创建对象方法
//创建对象方法1.{}
var obj = {
    name: "yq",//不需要var
    age: 18,
    sayHi: function () {//注意和普通函数的区别
        console.log("hi")
    }
};
//调用属性
//method 1
console.log(obj.name);
//method 2
console.log(obj['age']);
//删除属性
// delete obj.name;

//调用方法
obj.sayHi();
//添加属性
//method1
obj.height = 188;
//method2 defineProperty(obj, prop, desc)
Object.defineProperty(obj, "location", {
    //四个属性bool的default为false
    value: "NewYork",
    writable: true,//bool，是否可以重写
    enumerable: false,//bool default:false，是否可以被枚举。遍历不出来
    configurable: false//bool，是否可以被删除(delete obj.xxx)或再次修改
});
//修改可重写性。改完obj.name = "xx"是改不了的
//如果像这样调过改value、writable等四个属性中的一个的，别的属性也会被设置为默认值，比如enumerable
Object.defineProperty(obj, "name", {
    writable: false
});



//创建对象方法2，new
var obj2 = new Object();//empty object
obj.name = "yq2";//不用var
obj.age = 18;
obj.sayHi = function () {//类似函数表达式写法
    console.log("hi2");
}
//👆👆👆👆👆上面两种方法都不需要new

//创建对象方法3，构造函数，前面只能一次创建一个对象，即便所有属性方法都相同。方式：
// function 构造函数名() {
//     this.属性 = 值;
//     this.方法 = function () {}
// }
// new 构造函数名();
// 缺点：这种方法比较耗内存，因为比如同一方法等的复杂数据类型会存放很多份，所以原型可以共享函数
function Star(uname, uage) {//首字母大写
    this.name =  uname;//this必须
    this.age = uage;
    this.sing = function (song) {
        console.log(song);
    }
}
var ldh = new Star("刘德华", 18);
ldh.sing("冰雨");
Star.sex = "male";//添加静态成员
console.log(Star.sex);//不能用ldh访问

//原型prototype
/*
* 每个构造函数里都有一个prototype对象，所以可以把不变的方法直接定义在prototype对象上，这样所有对象实例就可以共享这些方法
* 主要用来实现方法共享，不用每个对象各自一个方法空间
* 实例能访问原型是因为实例默认有__proto__属性指向prototype
* 原型链查找：先看对象有没有这个方法，然后在__proto__指向的prototype上找，再在prototype的__proto__指向的prototype上找...比如ldh.toString()
* */
//Star的sing方法就可以定义为：
Star.prototype.sing2 = function(song) {
    console.log("sing in prototype, song: " + song);
}
ldh.sing2("冰雨");
//很多的时候
Star.prototype = {
    constructor: Star,//手动指回原来的构造函数
    sing2: function (song) {
        //...
    },
    act: function (movie) {
        //...
    }
};
/*原型中的this：
function中的this指向对象
原型对象中的this指向调用者，一般是对象
*/
//原型的应用：给系统类添加方法：但这样只能是.xxx不能是{}的方式，不然把别人的方法都覆盖了
Array.prototype.mysum = function() {
    var total = 0;//这里必须初始化
    for (var i = 0; i < this.length; i++) {
        total += this[i];
    }
    return total;
};
var arr = [1, 4];
console.log("Array.prototype.mysum: " + arr.mysum());




//继承
console.log("------继承");
//ex6之前没有extends，需要构造函数+原型对象模拟继承
function Father(uname, age) {
    this.uname = uname;
    this.age = age;
}
Father.prototype.money = function () {
    console.log("Father.prototype.money")
}
function Son(uname, age) {
    //father里面的this是指向father的，不换this uname是没法给子赋值的
    Father.call(this, uname, age);
}
// Son.prototype = Father.prototype;//这样儿子加原型方法就给父也加了
Son.prototype = new Father();
Son.prototype.constructor = Son;
Son.prototype.exam = function () {//这样给父也加了
    console.log("孩子考试")
}
var son = new Son("刘德华", 18);
console.log("继承： " + son.uname);

son.money();



//遍历对象属性
console.log("------遍历对象属性");
for (var k in obj) {
    console.log(k);//属性名
    console.log(obj[k]);//属性值
}



//自带对象
console.log("------自带对象");
//Math
Math.PI;
Math.max(1, 2, 3, 4, 5);
Math.random();//[0,1)，随机整数见mdn搜random上面有写好的

//Date，是new的，和Math不一样
var date = new Date();
console.log(date.getFullYear());
console.log(date.getMonth());//0-11
//时间戳
date.valueOf();
date.getTime();
+new Date();//最常用
Date.now();//h5新增

//Array
console.log("------Array");
//创建method 1
var arr = [13, 4, 77, 1, 7];
var arr2 = new Array(2);//长度为2，空元素
var arr3 = new Array(2, 3);//里面放的2和3
//是否是数组
console.log(arr instanceof Array);
console.log(Array.isArray(arr));//h5新增
//插入删除
var length1 = arr.push(4, 5);//末尾追加4和5
var length2 = arr.unshift(-1, 0);//头部插入-1和0
var removedElement1 = arr.pop();//删除末尾
var removedElement2 = arr.shift();//删除开头元素
//翻转
arr.reverse();
//排序
arr.sort();//冒泡，默认将元素转为字符串的Unicode排序，用的时候：
arr.sort(function (a, b) {
    return a - b;//升序
    // return b - a;//降序
});
console.log(arr);
//indexof
arr.indexOf(13)//只返回第一个满足的，如果没有返回-1
arr.lastIndexOf(13)
//转为字符串
arr.toString();//0,1,4,4,7,13,77
arr.join("-");//参数分隔符



//字符串，js的字符串也是不可变的，所以也少用"+"，所有字符串方法都不会修改本身
var str = "shit";
//有属性是因为把简单数据包装成了复杂，相当于：
// var temp = new String("shit");
// str = temp;
// temp = null;
//string number bool都有包装
str.length;
//根据字符返回位置
var idx1 = str.indexOf("hi");
var idx2 = str.indexOf("hi", 3/*从index3开始*/);
//根据位置返回字符
str.charAt(1);
str.charCodeAt(1);//字符的Ascii码
str[1];//h5新增
//拼接
var str2 = str.concat("fuck");//+更常用
//截取
var sub1 = str.substr(2, 2);
//替换
var rep = str.replace('a', 'b');//a替换为b，只替换第一个
//字符串转为数组
var aarrr = str.split(",");