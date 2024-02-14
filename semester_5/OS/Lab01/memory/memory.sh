#!/bin/bash
WORKERS_START=1
WORKERS_END=12
TARGET="$1"


if [ -z $TARGET ]
then
    echo "Pass target IO method"
else
  echo "Analize $TARGET"
  for i in $(seq $WORKERS_START $WORKERS_END)
  do 
    echo -e "\nstart $i worker(s)\n";
    stress-ng --$TARGET $i -t 15s --metrics | sar 1 10 -r
  done
fi
