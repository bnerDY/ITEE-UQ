#!/bin/bash


dirn=res.$$

if [ $# -lt 3 ]
then
    echo "Usage: $0 binname testdir testscript [detail]"
    exit 1
fi

BINNAME=$1
TESTD=$2
TESTF=$3
if [ $# -gt 3 ]
then
    if [ "$4" == "detail" ]
    then
       DETAIL=1   
    fi
fi


mkdir $dirn

if [ ! -e tests ]
then
    ln -s $TESTD tests
fi

if [ ! -x $BINNAME ]
then
    echo "Can't find an executable called $BINNAME"
    exit 1
fi

echo "Test results are in res.$$"

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
    #Now we actually run the test
    if [ "$DETAIL" == "1" ]
    then
        echo "$BINNAME $args < $TESTD/$inp > $dirn/result.$number 2> $dirn/err.$number"
    fi
    $BINNAME $args < $TESTD/$inp > $dirn/result.$number 2> $dirn/err.$number
    result=$?
    if [ $result != $retval ]
    then
	echo "$number (line $lnum) ... Expected $retval got $result"
    else
	diff -q $dirn/result.$number $TESTD/$out > /dev/null 2>&1
	if [ $? != 0 ]
	then
	    echo "$number (line $lnum) ... Std Output mismatch"
            if [ "$DETAIL" == "1" ]
            then
               echo "diff $dirn/result.$number $TESTD/$out"
            fi
	else
            diff -q $dirn/err.$number $TESTD/$err > /dev/null 2>&1
            if [ $? != 0 ]
            then
              echo "$number (line $lnum) ... Std Error mismatch"
              if [ "$DETAIL" == "1" ]
              then
                 echo "diff $dirn/err.$number $TESTD/$err"
              fi
            else
              if [ -n "$f1" ]
              then
                  diff -q $dirn/$f1 $TESTD/$f1 > /dev/null 2>&1
                  if [ $? != 0 ]
                  then
                      echo "$number (line $lnum) ... $f1 does not match"
                      continue
                  fi
                  if [ -n  "$f2" ]
                  then
                      diff -q $dirn/$f2 $TESTD/$f2 > /dev/null 2>&1
                      if [ $? != 0 ]
                      then
                          echo "$number (line $lnum) ... $f2 does not match"
                          continue
                      fi
                  fi
              fi
	      echo "$number (line $lnum) ... Ok"
            fi
	fi 
    fi
done < $3

#mv $dirn res.$$
echo "Test results are in res.$$"
