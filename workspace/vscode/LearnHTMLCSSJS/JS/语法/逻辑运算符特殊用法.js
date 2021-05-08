var info = {
    name: "yq",

    eating: function () {
        console.log("in eating")
    }
}

//&&
//假设可能info里没有eating函数
console.log(info.eating);
// if (info.eating) {//先判断info里有没有eating函数，没有是undefine的，有就是Function
//     info.eating();
// }
//上面if或者写成：
info.eating && info.eating()


//||
var content = info.age || info.name;//有谁就给我，只要一有就不接着判断后面