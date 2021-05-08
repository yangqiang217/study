//简单数据解构
let[a, b, c] = [1, 2, 3];
console.log(a, b, c);

let arr = [1, 2, 3];
let [a1, b1, c1] = arr;
console.log(a1, b1, c1);
//数量不一致的时候后面几个不对应的就是undefined


//对象解构
let person = {
    name: "yq",
    age: 18
};
let {name, age} = person;
console.log(name, age);
//or
let {name: changedName, age: changedAge} = person;
console.log(changedName, changedAge);