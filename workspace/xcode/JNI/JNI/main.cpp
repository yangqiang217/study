//
//  main.cpp
//  JNI
//
//  Created by YangQiang on 2021/5/8.
//

#include <jni.h>
#include <string>
#include <iostream>

JNIEXPORT void JNICALL
Java_com_my_bbb_MainActivity_stringFromJNI(JNIEnv* env,jobject /* this */) {
    std::string hello = "Hello from C++";
    std::cout << "in native lib";
}

int main(int argc, const char * argv[]) {
    // insert code here...
    std::cout << "Hello, World!\n";
    Java_com_my_bbb_MainActivity_stringFromJNI(NULL, NULL);
    return 0;
}
