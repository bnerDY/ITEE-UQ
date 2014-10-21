#!/bin/bash

if [ $# -lt 3 ]
then
    echo "Usage: $0 testdirectory testlist port [options]" 
    exit 1 
fi



if [ $# == 4 ]
then
    if [ "$4" == "-v" ]
    then
        showcoms=1
    fi
fi

mydir=`pwd`
tdir=$1
list=$2
port=$3

result=$$

mkdir testres.$result

if [ ! -d $tdir ]
then
    echo "Invalid test directory"
    exit 2
fi

cd $tdir

for name in `cat $list`
do
    if [ "`echo $name | cut -c1`" == "#" ]
    then
        continue
    fi 
    echo $name
    if [ "$showcoms" != "" ]
    then
        echo "pushd $tdir;$name $mydir $port $mydir/testres.$result $tdir;popd"
    fi
    $name $mydir $port $mydir/testres.$result $tdir
done
