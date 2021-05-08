//数组支持放不同数据类型，但是不推荐这么做
var a = ["shit", 123, false];

//不存在数组index越界，只会返回undefined，比如
console.log(a[10])


//es5新方法
var arr = [1, 2, 3];
arr.forEach(function (value, index, array) {
    //...
});

//返回新的数组
var filteredArray = arr.filter(function (value, index, array) {
    return value > 20;
});

//是否有满足条件的元素
var boolResSome = arr.some(function (value, index, array) {
    return value > 10;//是否有大于10的元素
    // return value === "shit";
});