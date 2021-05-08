class Star {
    constructor(uname) {
        this.name = uname;
    }
    sing(song) {//不需要function
        //这里name必须加this，调用本对象的属性、函数都必须加this
        // console.log(this.name + " sing " + song);
    }
}
var star = new Star('yq');
star.sing("冰雨");


var that;
class Father {
    constructor(x, y) {
        this.x = x;
        this.y = y;

        //A
        // this.btn = document.querySelector("button");
        // this.btn.onclick = this.sum;//不要()
        //B
        that = this;

    }
    sum() {
        console.log(this.x + this.y);
    }
}
class Son extends Father {
    // constructor(x, y) {
    //     super(x, y);
    //     this.x = x;
    //     this.y = y;
    // }
}
var son = new Son(1, 2);
son.sum();//打印NaN

//关于this：
//constructor里的this表示当前对象，方法中的this指向调用者，如
//经过A地方后，sum里面的this指向的就是button了，所以里面的this.x是没有值的，
//如果还是再sum里用Father对象，就用B处赋好值的that



//类的本质是构造函数
//所以es6中的类就是个语法糖
console.log(typeof Star);//function
//特点和构造函数是一致的:
/*
* 1.有prototype
* 2.prototype里有constructor指向构造函数本身
* 3.可以通过原型对象添加方法
* 4.对象有__proto__指向构造函数的原型对象
*/
console.log(Star.prototype);
console.log(Star.prototype.constructor);
Star.prototype.sing3 = function () {

}
console.log(son.__proto__ === Star.prototype);