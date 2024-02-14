WORKERS_START=1
WORKERS_END=6
TARGET="$1"

if [ -z $TARGET ]
then
    echo "Pass target cpu method"
else
    echo "Analize $TARGET"
    for i in $(seq $WORKERS_START $WORKERS_END)
    do echo -e "\nstart $i worker(s)\n";
       stress-ng --cpu $i --cpu-method $TARGET -t 15s --metrics | sar 1 10 -u
    done
fi
