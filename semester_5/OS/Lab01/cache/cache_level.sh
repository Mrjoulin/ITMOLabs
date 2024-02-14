#!/bin/bash
CACHE_LEVEL_START=1
CACHE_LEVEL_END=3
CACHE_WORKERS_START=1
CACHE_WORKERS_END=5

echo "Analize cache-levels"

for lvl in $(seq $CACHE_LEVEL_START $CACHE_LEVEL_END)
do
    echo -e "\nProcess cache-level $lvl"
    for i in $(seq $CACHE_WORKERS_START $CACHE_WORKERS_END)
    do
        echo -e "\nStart $i worker(s)\n"
        stress-ng --cache "$i" --cache-level "$lvl" -t 10s --metrics
    done
done
