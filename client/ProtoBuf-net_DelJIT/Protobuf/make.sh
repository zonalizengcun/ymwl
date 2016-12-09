#!/bin/sh

rm *.pb.h *.pb.cc 1>/dev/null 2>&1
for i in *.proto
do
	protogen -I=. -o:$i
	if [ "$?" -eq 0 ]
	then
		echo "已生成$i.pb.h $i.pb.cc"
	else
		exit 1
	fi
done
