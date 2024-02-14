#!/bin/bash
WORKERS_START=1
WORKERS_END=10
TARGET=sigpipe


echo "Analize $TARGET"
for i in $(seq $WORKERS_START $WORKERS_END)
do 
  echo -e "\nstart $i worker(s) without flag\n"
  perf stat -e cs stress-ng --$TARGET $i -t 15s --metrics | sar 1 10 -u
  echo -e "\nstart $i worker(s) with flag\n"
  perf stat -e cs stress-ng --$TARGET $i -t 15s --pipeherd-yield --metrics | sar 1 10 -u
done
