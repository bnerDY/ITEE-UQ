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

expected=0

$bindir/serv499 $port Hi 1.deck &
spid=$!
$bindir/client499 A game $port > $res/$name.a.out 2>$res/$name.a.err < amoves &
pid1=$!
$bindir/client499 B game $port > $res/$name.b.out 2>$res/$name.b.err < bmoves &
pid2=$!
$bindir/client499 C game $port > $res/$name.c.out 2>$res/$name.c.err < cmoves &
pid3=$!
$bindir/client499 D game $port > $res/$name.d.out 2>$res/$name.d.err < dmoves &
pid4=$!

wait $pid1 $pid2 $pid3 $pid4
kill $spid 2> /dev/null
wait $spid 2> /dev/null

#if [ $result != $expected ]
#then
#    echo "Expected $expected got $result"
#    exit 1
#fi

for v in a b c d
do

diff -q $res/$name.$v.out $right/$name.$v.out > /dev/null
if [ $? != 0 ]
then
   echo "Stdout mismatch for client $v"
   diff $res/$name.$v.out $right/$name.$v.out
   exit 2
fi

diff -q $res/$name.$v.err $right/$name.$v.err > /dev/null
if [ $? != 0 ]
then
   echo "Stderr mismatch for client $v"
   exit 3
fi

done

echo OK
exit 0

