//1. other -> number, maybe NaN
Number("123");
Number("abc");//NaN
Number(true);//1
Number(false);//0
Number(undefined);//NaN
Number(null);//0

// string -> int/float
parseInt("123");
parseInt("123.34");//123
parseInt("abc");//NaN
parseInt("abc123");//NaN
parseInt("123abc");//123
parseInt("10", 2);//2，第二个参数是进制

var f1 = parseFloat("123.45");
typeof f1;//number，和int一样都是number
parseFloat("123.34.23");//123.34

//2. other -> string
// way1
"123".toString();
true.toString();
var n1 = 123;
n1.toString();
// way2
String(true);
String(123);
// way3, same as java, auto change to string
123 + "123";

//3. other -> bool
//转换成false的5种情况："", 0/-0, undefined, null, NaN
//其它情况都是true
Boolean("shit")



console.log(Boolean("shit"))