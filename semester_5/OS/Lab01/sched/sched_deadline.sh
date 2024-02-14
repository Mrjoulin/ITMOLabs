#!/bin/bash
WORKERS_START=1
WORKERS_END=4
TARGET=sched-period


echo "Analize $TARGET"
for i in $(seq $WORKERS_START $WORKERS_END)
do 
  echo -e "\nstart $i worker(s)\n"
  for per in 100 10000 1000000
  do 
    echo -e "\nUse period $per ns\n"
    perf stat -e cs stress-ng --yield $i --sched deadline --$TARGET $per -t 10s --metrics | sar 1 10 -u -b
  done
done
