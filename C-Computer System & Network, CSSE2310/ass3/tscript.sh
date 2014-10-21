#!/bin/bash
#This is the script for the marker

dirn=testres.$$

if [ $# -lt 3 ]
then
    echo "Usage: $0 binname testdir testscript"
    exit 1
fi

BINNAME=$1
TESTD=$2
TESTF=$3


mkdir -p $dirn

touch $dirn/locked
chmod a-w $dirn/locked

if [ ! -e tests ]
then
    ln -s $TESTD tests
fi

RESULT=""

number=0
lnum=0
while read line
do
    let lnum=lnum+1
    if [ "$line" == "" ]
    then
	continue
    fi 
    if [ `echo $line|cut -c1` == '#' ]
    then
	continue
    fi
    let number=number+1
    details=`echo $line | cut -f1,2,3,4,5,6 -d\| --output-delimiter=" "`
    args=`echo $line | cut -f7 -d\| --output-delimiter=" "`
    read retval inp out err f1 f2<<END
$details
END
    comm=$BINNAME
    if [ "`echo $args|cut -c1`" == "!" ]
    then
        comm=""
        args=`echo $args|cut -c2-`
    fi
    #Now we actually run the test
    #$TESTD/tools/tlimit 60 $comm $args < $TESTD/$inp > $dirn/result.$number 2> $dirn/err.$number
    echo "$comm $args < $TESTD/$inp > $dirn/result.$number 2> $dirn/err.$number"
    $comm $args < $TESTD/$inp > $dirn/result.$number 2> $dirn/err.$number    
    result=$?

    if [ $result != $retval ]
    then
        RESULT="$RESULT a 0"
        echo "$number[$lnum] .... expected $retval got $result"
        continue
    else
        if [ ! -e $TESTD/$out ]
        then
            echo "Output file $TESTD/$out is missing"
            continue
        fi
	diff -q $dirn/result.$number $TESTD/$out > /dev/null 2>&1
	if [ $? != 0 ]
	then
                          RESULT="$RESULT b 0"
                          echo "$number[$lnum] .... stdout mismatch"
                          continue
	else
            diff -q $dirn/err.$number $TESTD/$err > /dev/null 2>&1
            if [ $? != 0 ]
            then
                          RESULT="$RESULT c 0"
                          echo "$number[$lnum] .... error mismatch "
                          continue
            else
              if [ -n "$f1" ]
              then
                  diff -q $dirn/$f1 $TESTD/$f1 > /dev/null 2>&1
                  if [ $? != 0 ]
                  then
                          RESULT="$RESULT d 0"
                          echo "$number[$lnum] .... FILE mismatch"
                          continue
                  fi
                  if [ -n  "$f2" ]
                  then
                      diff -q $dirn/$f2 $TESTD/$f2 > /dev/null 2>&1
                      if [ $? != 0 ]
                      then
                          RESULT="$RESULT e 0"
        echo "$number .... FILE mismatch"
                          continue
                      fi
        echo "$number ..... OK"
                      RESULT="$RESULT x 1"         
                      continue 
                  fi
              fi
            fi
	fi 
    fi
    echo "$number .... OK"
    RESULT="$RESULT x 1"
done < $3

