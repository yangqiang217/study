package com.study.regex

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangqiang on 16-9-9.
 */
public class PatternTest {

    //匹配语句
    private static String patternStr;
    //待配串
    private static String str;
    //想运行第几个
    private static int which = 35;

    public static void main(String[] args) {
        //1  ^ 匹配输入字符串开始的位置。如果设置了 RegExp 对象的 Multiline 属性，^ 还会与"\n"或"\r"之后的位置匹配。
        if (which == 1) {
            patternStr = "^b.*";
            str = "bdd";
        }

        //2  $ 匹配输入字符串结尾的位置。如果设置了 RegExp 对象的 Multiline 属性，$ 还会与"\n"或"\r"之前的位置匹配。
        if (which == 2) {
            patternStr = ".*a$";
            str = "cca";
        }

        //3  * 零次或多次匹配前面的字符或子表达式。例如，zo* 匹配"z"和"zoo"。* 等效于 {0,}。
        if (which == 3) {
            patternStr = "d*";
            str = "";
        }

        //4  + 一次或多次匹配前面的字符或子表达式。例如，"zo+"与"zo"和"zoo"匹配，但与"z"不匹配。+ 等效于 {1,}。
        if (which == 4) {
            patternStr = "a+";
            str = "aaa";
        }

        //5  ? 零次或一次匹配前面的字符或子表达式。例如，"do(es)?"匹配"do"或"does"中的"do"。? 等效于 {0,1}。
        if (which == 5) {
            patternStr = "a?";
            str = "";
        }

        //6  {n} n 是非负整数。正好匹配 n 次。例如，"o{2}"与"Bob"中的"o"不匹配，但与"food"中的两个"o"匹配。
        if (which == 6) {
            patternStr = "a{2}";//就两个a
            str = "aa";
        }

        //7  {n,} n 是非负整数。至少匹配 n 次。例如，"o{2,}"不匹配"Bob"中的"o"，而匹配"foooood"中的所有 o。"o{1,}"等效于"o+"。"o{0,}"等效于"o*"。
        if (which == 7) {
            patternStr = "a{2,}";//>=2个a
            str = "aa";
        }

        //8  {n,m} M 和 n 是非负整数，其中 n <= m。匹配至少 n 次，至多 m 次。例如，"o{1,3}"匹配"fooooood"中的头三个 o。'o{0,1}' 等效于 'o?'。注意：您不能将空格插入逗号和数字之间。
        if (which == 8) {
            patternStr = "a{2,4}";//两个或三个或四个a
            str = "aaaa";
        }

        //9  ? 当此字符紧随任何其他限定符（*、+、?、{n}、{n,}、{n,m}）之后时，匹配模式是"非贪心的"。"非贪心的"模式匹配搜索到的、尽可能短的字符串，而默认的"贪心的"模式匹配搜索到的、尽可能长的字符串。例如，在字符串"oooo"中，"o+?"只匹配单个"o"，而"o+"匹配所有"o"。
        if (which == 9) {
            patternStr = "a+?";//匹配到第一个马上返回，不再继续进行查找，百度懒惰匹配
            str = "aaaa";
        }

        //10  . 匹配除"\r\n"之外的任何单个字符。若要匹配包括"\r\n"在内的任意字符，请使用诸如"[\s\S]"之类的模式。
        if (which == 10) {
            patternStr = "a+?";
            str = "aaaa";
        }

        //11  (pattern) 匹配 pattern 并捕获该匹配的子表达式。可以使用 $0…$9 属性从结果"匹配"集合中检索捕获的匹配。若要匹配括号字符 ( )，请使用"\("或者"\)"。
        //12  (?:pattern) 匹配 pattern 但不捕获该匹配的子表达式，即它是一个非捕获匹配，不存储供以后使用的匹配。这对于用"or"字符 (|) 组合模式部件的情况很有用。例如，'industr(?:y|ies) 是比 'industry|industries' 更经济的表达式。
        //13  (?=pattern) 执行正向预测先行搜索的子表达式，该表达式匹配处于匹配 pattern 的字符串的起始点的字符串。它是一个非捕获匹配，即不能捕获供以后使用的匹配。例如，'Windows (?=95|98|NT|2000)' 匹配"Windows 2000"中的"Windows"，但不匹配"Windows 3.1"中的"Windows"。预测先行不占用字符，即发生匹配后，下一匹配的搜索紧随上一匹配之后，而不是在组成预测先行的字符后。
        //14  (?!pattern) 执行反向预测先行搜索的子表达式，该表达式匹配不处于匹配 pattern 的字符串的起始点的搜索字符串。它是一个非捕获匹配，即不能捕获供以后使用的匹配。例如，'Windows (?!95|98|NT|2000)' 匹配"Windows 3.1"中的 "Windows"，但不匹配"Windows 2000"中的"Windows"。预测先行不占用字符，即发生匹配后，下一匹配的搜索紧随上一匹配之后，而不是在组成预测先行的字符后。

        //15  x|y 匹配 x 或 y。例如，'z|food' 匹配"z"或"food"。'(z|f)ood' 匹配"zood"或"food"。
        if (which == 15) {
            patternStr = "a|book";
            str = "book";
        }

        //16  [xyz] 字符集。匹配包含的任一字符。例如，"[abc]"匹配"plain"中的"a"。
        if (which == 16) {
            patternStr = "[abc]";
            str = "c";
        }

        //17  [^xyz] 反向字符集。匹配未包含的任何字符。例如，"[^abc]"匹配"plain"中"p"，"l"，"i"，"n"。
        if (which == 17) {
            patternStr = "[^abc]";
            str = "d";
        }

        //18  [a-z] 字符范围。匹配指定范围内的任何字符。例如，"[a-z]"匹配"a"到"z"范围内的任何小写字母。
        if (which == 18) {
            patternStr = "[a-c]";
            str = "c";
        }

        //19  [^a-z] 反向范围字符。匹配不在指定的范围内的任何字符。例如，"[^a-z]"匹配任何不在"a"到"z"范围内的任何字符。
        if (which == 19) {
            patternStr = "[^a-c]";
            str = "d";
        }

        //20  \b 匹配一个字边界，即字与空格间的位置。例如，"er\b"匹配"never"中的"er"，但不匹配"verb"中的"er"。
        if (which == 20) {
            patternStr = "never\\b,";
            str = "never,";
        }

        //21  \B 不是字边界匹配。"er\B"匹配"verb"中的"er"，但不匹配"never"中的"er"。
        if (which == 21) {
            patternStr = "never\\Bd";
            str = "neverd";
        }

        //22  \cx 匹配 x 指示的控制字符。例如，\cM 匹配 Control-M 或回车符。x 的值必须在 A-Z 或 a-z 之间。如果不是这样，则假定 c 就是"c"字符本身。

        //23  \d 数字字符匹配。等效于 [0-9]。
        if (which == 23) {
            patternStr = "\\d{3}";
            str = "123";
        }

        //24  \D 非数字字符匹配。等效于 [^0-9]。
        if (which == 24) {
            patternStr = "\\D{3}";
            str = "abc";
        }

        //25  \f 换页符匹配。等效于 \x0c 和 \cL。
        //26  \n 换行符匹配。等效于 \x0a 和 \cJ。
        //27  \r 匹配一个回车符。等效于 \x0d 和 \cM。

        //28  \s 匹配任何空白字符，包括空格、制表符、换页符等。与 [ \f\n\r\t\v] 等效。
        if (which == 28) {
            patternStr = "ab\\s*c";
            str = "ab  c";
        }

        //29  \S 匹配任何非空白字符。与 [^ \f\n\r\t\v] 等效。
        if (which == 29) {
            patternStr = "ab\\S*c";
            str = "ab__c";
        }

        //30  \t 制表符匹配。与 \x09 和 \cI 等效。
        //31  \v 垂直制表符匹配。与 \x0b 和 \cK 等效。

        //32  \w 匹配任何字类字符，包括下划线。与"[A-Za-z0-9_]"等效。
        if (which == 32) {
            patternStr = "ab\\w*c";
            str = "ab__c";
        }

        //33  \W 与任何非单词字符匹配。与"[^A-Za-z0-9_]"等效。
        if (which == 33) {
            patternStr = "ab\\W*c";
            str = "ab&&c";
        }

        //34  \xn 匹配 n，此处的 n 是一个十六进制转义码。十六进制转义码必须正好是两位数长。例如，"\x41"匹配"A"。"\x041"与"\x04"&"1"等效。允许在正则表达式中使用 ASCII 代码。
        //35  \num 匹配 num，此处的 num 是一个正整数。到捕获匹配的反向引用。例如，"(.)\1"匹配两个连续的相同字符。
        if (which == 35) {//http://zhidao.baidu.com/question/981985866163210579.html
            patternStr = "([a-z])([0-9])\\2\\1";
            str = "a77a";
        }

        //36  \n 标识一个八进制转义码或反向引用。如果 \n 前面至少有 n 个捕获子表达式，那么 n 是反向引用。否则，如果 n 是八进制数 (0-7)，那么 n 是八进制转义码。
        //37  \nm 标识一个八进制转义码或反向引用。如果 \nm 前面至少有 nm 个捕获子表达式，那么 nm 是反向引用。如果 \nm 前面至少有 n 个捕获，则 n 是反向引用，后面跟有字符 m。如果两种前面的情况都不存在，则 \nm 匹配八进制值 nm，其中 n 和 m 是八进制数字 (0-7)。
        //38  \nml 当 n 是八进制数 (0-3)，m 和 l 是八进制数 (0-7) 时，匹配八进制转义码 nml。
        //39  \\un 匹配 n，其中 n 是以四位十六进制数表示的 Unicode 字符。例如，\u00A9 匹配版权符号 (©)。

        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);
        System.out.println(matcher.matches());
    }
}
