#!/bin/bash

#This script runs the framework over all instances.
# - standard output are written in the file standardOutput.txt
# - error output are written in the file errorOutput.txt


rm -f standardOutput.txt
rm -f errorOutput.txt

javac -d ./../bin/ ./../src/rcpsp/*.java -cp ./../lib/plot.jar

for i in ./../instances/*.sm;do
	sleep 1;
	echo "Executing instance ".$i
	java -cp "./../bin/:./../lib/plot.jar" -Djava.library.path=./../lib/ rcpsp.Main -t 60 $i >> standardOutput.txt 2>> errorOutput.txt
done

