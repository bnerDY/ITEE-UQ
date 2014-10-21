#!/bin/bash

if [ $# != 4 ]
then
    echo "Usage: $0 binarydir port resultdir correct_dir" 
    exit 1
fi



bindir=$1
port=$2
res=$3
right=$4
name=$0

expected=4


$bindir/serv499 sssss "doom" 1.deck > $res/$name.out 2>$res/$name.err
result=$?

if [ $result != $expected ]
then
    echo "Expected $expected got $result"
    exit 1
fi

diff -q $res/$name.out $right/$name.out > /dev/null
if [ $? != 0 ]
then
   echo "Stdout mismatch"
   diff $res/$name.out $right/$name.out
   exit 2
fi

diff -q $res/$name.err $right/$name.err > /dev/null
if [ $? != 0 ]
then
   echo "Stderr mismatch"
   exit 3
fi

echo OK
exit 0

