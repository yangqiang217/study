//箭头函数
//普通：
function old() {

}
/*特点：
    如果只有一个参数，()可以省
    如果只有一个return，{}可以省
*/
let a = function (a, b) {

};
//可以写为：
let a2 = (a, b) => {
    console.log("a2");
};

let arr = [];
arr.sort(function (a, b) {
    return a - b;
});
//可以写成：
arr.sort((a, b) => a - b);

//参数只有一个，括号可以省略
let show = function (a) {
    return a + 1;
};
//可以写成：
let show2 = a => {
    return a + 1;
};
//或：
let show3 = a => a + 1;


//this指向函数定义位置的上下文，不是普通函数指向调用者
function fn() {
    console.log(this);//此处this被call改成了指向obj
    return () => {
        //此处this指向同fn中的this，即指向obj
        console.log(this);
    }
}
const obj = {name: "yq"};
const  resFn = fn.call(obj);
//or
var obj2 = {
    age2: 20,
    say: () => {
        console.log(this);
        console.log(this.age2);
    }
};
obj2.say();


//剩余参数(箭头函数不能用arguments)
function sum(...args) {
    return 10;
}
sum(1, 2, 3);
//和解构配合
let stu = ['a', 'b', 'c'];
let [...stus] =  stu;//or
let [s1, ...stus] = stu;
console.log(stus);