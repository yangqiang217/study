var num1 = 123;
var num2 = "123";
//==有隐式转换把两种类型转成一种，类似string+number的结果，这里会把num2转换成数字再比较
console.log(num1 == num2);//true
console.log(num1 != num2);//false
//===没有隐式转换
console.log(num1 === num2);//false
console.log(num1 !== num2);//true

var bool1 = true;
var bool2 = "true";
//都转成number，一个是1，一个是NaN
console.log(bool1 == bool2);//false
