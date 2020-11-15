#!/bin/bash

temp_jar_name="temp.jar"

cp ./out/artifacts/ProguardTest_jar/ProguardTest.jar ./${temp_jar_name}
java -jar proguard.jar -injars ${temp_jar_name} -outjars ./useProguardLib/libs/yq.jar @proguard-android.txt -verbose
rm ${temp_jar_name}