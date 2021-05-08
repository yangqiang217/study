//...拆分
let arr = [1, 2, 3];
//...arr: 拆分成以, 分割的序列
console.log(...arr);//没打印出, 因为被log方法当成参数分割符了
//应用
//1.合并数组
let arr1 = [1, 2, 3];
let arr2 = [4, 5, 6];
let arr3 = [...arr1, ...arr2];
//or
arr1.push(...arr2);
//2.伪数组改为数组
// var divs = document.getElementsByTagName("div");
// var arr4 = [...divs];


//Array的
//Array.from()：伪数组转为数组
var arrayLike = {
    "0": "zhansan",
    "1": "lisi",
    "2": "wangwu",
    "length": 3
};
var array5 = Array.from(arrayLike);


//Array.find()
let arr6 = [{
    id: 1,
    name: "yq1"
}, {
    id: 2,
    name: "yq2"
}];
let target = arr6.find((item, index) => item.id === 2);

//Array.findIndex()
let index = arr.findIndex((value, index) => value > 2);

//Array.includes()
let include = arr.includes(2);//return bool




//String的
//模板字符串(键盘左上角的`)
//特点
//1.可以解析变量
let name = "yq";
console.log(`my name is ${name}`);
//同样可以拼接到html
let html = `
    <div>
        <span>${name}</span>
    </div>
`;
//还可以调用函数
const say = function () {
    return "hi";
};
console.log(`${say()}`);

//startsWith()/endsWith();
//...

//repeat
"x".repeat(3);//"xxx"