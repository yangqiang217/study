//
//  ViewController.swift
//  SwiftTest
//
//  Created by YangQiang on 2019/2/21.
//  Copyright © 2019 YangQiang. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate, UITextViewDelegate {

    
    @IBOutlet weak var label: UILabel!
    
    @IBAction func onClick(_ sender: Any) {
//        print("click")
        self.label.text = "clicked"
        self.testAlertView()
        
//        test(para1: "shit", para2: 1)
//
//        let bounds = minMax(array: [8, -1, 12, 123])
//        print("min: \(bounds.ret1), max: \(bounds.ret2)")
//
//        if let boundsOptional = minMaxOptional(array: [1, -1, 23]) {
////            print(/...)
//        }
//
//        outName(outArgName: 1)
//
//        changablePara(paras: 1, 3)
//
//        var x = 1, y = 5
//        swap(&x, b: &y)
//        print("x: \(x), y: \(y)")
//
//        var type2: (String, Int) -> Int = funcType
//        type2("a", 2)
//        func funcType2(func1: (String, Int) -> Int, a: Int, b: Int) {//我第一个参数是个函数，你给我传函数进来
//            print("functype2")
//            func1("b", 2)
//        }
//        funcType2(func1: funcType, a: 10, b: 20)
//        funcType2(func1: type2, a: 10, b: 20)
//
//        closure()
    }
    
    //uitextfielddelegate委托协议方法
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        print("textfield get focus, press return")
        textField.resignFirstResponder()// hide softboard
        return true
    }
    //uitextviewdelegate委托协议方法
    func textView(_ textView: UITextView,
                  shouldChangeTextIn range: NSRange,
                  replacementText text: String) -> Bool {
        if (text == "\n") {
            print("textview get focus, press return")
            return false
        }
        return true
    }
    
    func testAlertView() {
        let alertController: UIAlertController = UIAlertController(title: "alert", message: "message", preferredStyle: .alert)
        alertController.addTextField { (textField: UITextField!) -> Void in
            textField.placeholder = "username"
        }
        alertController.addTextField {(textField: UITextField!) -> Void in
            textField.placeholder = "password"
            textField.isSecureTextEntry = true
        }
        
        let noAction = UIAlertAction(title: "no", style: .cancel) {(alertAction) -> Void in
            print("tap no button")
        }
        let yesAction = UIAlertAction(title: "yes", style: .default) {(alertAction) -> Void in
            let username = alertController.textFields!.first!
            let password = alertController.textFields!.last!
            print("tap yes button, username: \(username.text), password: \(password.text)")
        }
        alertController.addAction(noAction)
        alertController.addAction(yesAction)
        
        self.present(alertController, animated: true, completion: nil)
    }

    
    //life cycle--------------------------start
    //similar to onCreate
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }
    //life cycle--------------------------end

    func test(para1: String, para2: Int) -> String {//-> 为返回类型
        var myString = "string"
        
        //optional value--------------1
        var optionalInt: Int? = nil
        optionalInt = 42//-----------mark1
        
        print(optionalInt)//Optional(42) 不要求必须有值，会打印nil
        if optionalInt != nil {
            print(optionalInt!)//42 !必须保证其有值不然会崩
        } else {
            print("is nil")
        }
        
        if let newint = optionalInt {
            //有mark1那一行就走这个
            print("newint: \(newint)")//注意这种写法，用+不行
        } else {
            //没mark1那一行就走这个
            print("no new int")
        }
        
        //optional value--------------2 效果和1一样，再说
//        var optionalInt2: Int!
//        optionalInt2 = 43
//
//        print(optionalInt2!)
//        if optionalInt2 != nil {
//            print(optionalInt2)
//        } else {
//            print("is nil")
//        }
        
        //常量
        let const1 = 11
        let const2:Float = 3.14//带类型标注的常量
//        const1 = 12//报错
        
        //区间
        for i in 1...5 {
            print(i)//1 2 3 4 5
        }
        for i in 1..<5 {//没有1>和>5
            print(i)//1 2 3 4
        }
        
        //循环
        //in
        for i in 1...5{}
        
        var ints:[Int] = [1, 2, 3];
        for i in ints {}
        //for !!!! removed in swift3
//        for var i = 0; i < 3; ++i {
//            print(i)
//        }
        //while
        var index = 15
        repeat {
            print(index)
            index = index + 1
        } while index < 20
        
        //switch 和java不一样，没有break也只走一个，要都走就用fallthrough
        var index2 = 10
        switch index2 {
        case 100:
            print("switch is 100")
        case 10, 15://10 or 15
            print("switch is 10 or 15")
            fallthrough//有了这句会走case 1 的情况，但default不走，只贯穿一个
        case 1:
            print("switch is 1")
        default:
            print("switch default")
        }
        
        //string
        var string1 = "string1"
        var string2 = String("string2")
        
        var string3 = ""
        let string4 = String()
        if (string3.isEmpty) {}
        if (string3.hasPrefix("s")) {}
        if (string3.hasSuffix("g")) {}
        
        var a = Int(string1)
        
        string1 += "append1"
//        string4 += "append4"//error
        string1.count//length
        string1 == string3
        
        //char
        let char1: Character = "a"
//        let char2: Character = ""//不能创建空字符
        for char in string1 {}
        
        //数组
        var array1 = [Int](repeating: 0, count: 3)//var someArray = [SomeType](repeating: InitialValue, count: NumbeOfElements)
        var array2:[Int] = [1, 2, 3]
        var a1 = array2[1]
        
        array1.append(4)
        array1 += [5]
        
        for (i, item) in array1.enumerated() {}//需要index和item的方式
        
        var arrBoth = array1 + array2
        
        //dictionary 如果将一个字典赋值给常量，字典就不可修改，并且字典的大小和内容都不可以修改。
        var dic1 = [Int: String]()
        var dic2:[Int:String] = [1:"1", 2:"2"]
        
        var val1 = dic1[1]//get
        
        dic2[2] = "new2"//update1
        var oldVal = dic2.updateValue("new2", forKey: 2)//update2，返回optional值
        print(oldVal!)
        
        var removedVal = dic2.removeValue(forKey: 2)//remove
        
        for (k, v) in dic2 {}
        for (k, v) in dic2.enumerated() {}
        
        let dicKeys = [Int](dic2.keys)//convert to array
        let dicValues = [String](dic2.values)
        
        dic2.count
        dic2.isEmpty
        
        
        
        return "shit"
    }

    /*
     元组与数组类似，不同的是，元组中的元素可以是任意类型，使用的是圆括号
     你可以用元组（tuple）类型让多个值作为一个复合值从函数中返回。
     */
    func minMax(array: [Int]) -> (ret1: Int, ret2: String) {
        return (1, "3")
    }
    /*
     如果你不确定返回的元组一定不为nil，那么你可以返回一个可选的元组类型。
     可选元组类型如(Int, Int)?与元组包含可选类型如(Int?, Int?)是不同的.可选的元组类型，整个元组是可选的，而不只是元组中的每个元素值。
     */
    func minMaxOptional(array: [Int]) -> (ret1: Int, ret2: String)? {
        if (array.isEmpty) {
            return nil
        }
        return (1, "3")
    }
    
    //outArgName是外面调的时候传的，里面不能用
    func outName(outArgName a: Int) {
        print(a)
    }
    
    func changablePara<N>(paras: N...) {
        //...
    }
    
    /*
     一般默认在函数中定义的参数都是常量参数，也就是这个参数你只可以查询使用，不能改变它的值。
     如果想要声明一个变量参数，可以在参数定义前加 inout 关键字，这样就可以改变这个参数的值了
     _表示忽略外部参数名，比如第一个参数外面就不用指定a:
     */
    func swap(_ a: inout Int, b: inout Int) {
        let temp = a
        a = b
        b = temp
    }
    
    /*类型一致*/
    func funcType(a: String, b: Int) -> Int {
        print("functype")
        return 1
    }
    
    func closure() {
        let studname = {//无参无返回
            print("in closure")
        }
        studname()
        
        let divide = {(val1: Int, val2: Int) -> Int in //有参有返回
            return val1 / val2
        }
        let result = divide(200, 20)
        print(result)
        
        //闭包表达式
        let names = [1, -90, 3, 14]
        // 使用普通函数(或内嵌函数)提供排序功能,闭包函数类型需为(String, String) -> Bool。
        func backwards(s1: Int, s2: Int) -> Bool {
            return s1 > s2
        }
        var reversed = names.sorted(by: backwards)
        print(reversed)
        
        //参数名称缩写,Swift 自动为内联函数提供了参数名称缩写功能，您可以直接通过$0,$1,$2来顺序调用闭包的参数。
        var reversed2 = names.sorted( by: { $0 > $1 } )
        print(reversed2)
        /*String类型定义了关于大于号 (>) 的字符串实现，其作为一个函数接受两个String类型的参数并返回Bool类型的值。
         而这正好与sort(_:)方法的第二个参数需要的函数类型相符合。 因此，您可以简单地传递一个大于号，Swift可以自动推断出您想使用大于号的字符串函数实现*/
        var reversed3 = names.sorted(by: >)
        print(reversed3)
    }
    
    // 定义枚举
    enum DaysofaWeek {
        case Sunday
        case Monday
    }
    
    enum Student{
        case Name(String)
        case Mark(Int,Int,Int)
    }
    var studDetails = Student.Name("Runoob")
    
    //class
    class student{
        var mark: Int
        init(mark: Int) {
            self.mark = mark
        }
    }
//    func equal() {
//        let stu1 = student(mark: 1)//实例化类
//        let stu2 = student(mark: 1)
//        print(stu1.mark)
//        if stu1 === stu2{}//是否同一实例
//    }
    
}

