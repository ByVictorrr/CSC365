#!bin/bash
rm *.class
javac *.java
java Main $1 $2 >> $3

