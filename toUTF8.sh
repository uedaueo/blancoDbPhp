#!/bin/sh

#files=`ls *.java`
files=`find . -type f -name '*.java'`

for myfile in $files 
do
	echo "$myfile"
	sjis="$myfile.sjis"
	echo "mv $myfile $sjis"
	mv $myfile $sjis
	echo "nkf -Lu -w < $sjis > $myfile"
	nkf -Lu -w < $sjis > $myfile
done
