#!/bin/bash
WORKERS_START=1
WORKERS_END=10
TARGET="$1"


if [ -z $TARGET ]
then
    echo "Pass target Network method"
else
  echo "Analize $TARGET"
  for i in $(seq $WORKERS_START $WORKERS_END)
  do 
    echo -e "\nstart $i worker(s)\n";
    stress-ng --$TARGET $i -t 10s --metrics
  done
fi
