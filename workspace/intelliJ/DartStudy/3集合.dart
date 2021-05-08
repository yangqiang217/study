main(List<String> args) {
  //list
  // var list = new List();
  // list.add(1);
  // list.add('2'); //完全没问题
  // print(list[0]);
  // print(list[1]);
  //method
  // print(list.length);
  // print(list.isEmpty);
  // var newlist = list.reversed.toList();
  // list.add('2');
  // list.addAll(newlist);
  // int a = list.indexOf('shit');
  // list.remove('shit');
  // list.fillRange(1, 3, 'a'); //index是[1, 3)的元素换成a
  // String str = list.join(','); //转换成已逗号分隔的字符串
  // var strList = str.split(','); //字符串转集合

  //set
  // var s = new Set();
  // s.add('a');

  //map
  var m = new Map();
  m['name'] = 'a';
  m.putIfAbsent('age', () => '11');
  // m.remove('age');
  // m.keys.toList();
  // m.values.toList();
  // m.isEmpty;

  //循环（list、map、set通用）
  var list = new List();
  list.add(1);
  list.add(2);
  // for (var item in list) {}
  // list.forEach((element) {
  //   print(element);
  // });

  // m.forEach((key, value) {
  //   print(key + '-' + value); //等同于：
  //   print('$key-$value');
  // });

  // var list2 = list.map((e) => {e + 1});//map用来修改集合中的元素
  // print(list2);

  // var list3 = list.where((element) {//满足条件的
  //   return element % 2 == 0;
  // });
  // print(list3);

  // var bool1 = list.any((element) {//any只要集合里有满足条件的就true
  //   return element > 5 == 0;
  // });
  // print(bool1);

  // var bool1 = list.every((element) {//every集合里所有元素都满足条件就true
  //   return element > 5 == 0;
  // });
  // print(bool1);
}
