#!/bin/bash
CACHE_WORKERS_START=1
CACHE_WORKERS_END=5

echo "Analize l1cache-ways"

for w in 1 2 4 8 16
do
    echo -e "\nProcess l1cache-ways $w"
    for i in $(seq $CACHE_WORKERS_START $CACHE_WORKERS_END)
    do
        echo -e "\nStart $i worker(s)\n"
        stress-ng --cache "$i" --l1cache-ways "$w" -t 10s --metrics
    done
done
