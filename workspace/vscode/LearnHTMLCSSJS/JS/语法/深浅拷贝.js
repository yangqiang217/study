//浅拷贝只拷贝一层，更深层次对象级别的只拷贝引用
//深拷贝拷贝多层，每一级都会拷贝
var obj = {
    id: 1,
    name: "andy",
    msg: {
        age: 18
    }
};
var o = {};

//浅拷贝
//for 常规浅拷贝，对象只拷贝地址，指向同一数据
for (var k in obj) {
    o[k] = obj[k];
}
//assign浅拷贝(es6新增)
Object.assign(o, obj);


//深拷贝
function deepCopy(newObj, oldObj) {
    for (var k in oldObj) {
        var item = oldObj[k];
        if (item instanceof Array) {//Array也是object
            newObj[k] = [];
            deepCopy(newObj, item);
        } else if (item instanceof Object) {
            newObj[k] = {};
            deepCopy(newObj[k], item);
        } else {
            newObj[k] = item;
        }
    }
}
deepCopy(o, obj);