LIB_PATH=.
for i in ../lib/*.jar; do
LIB_PATH=${LIB_PATH},$i
done
LIB_PATH=${LIB_PATH:0,2}
#echo $LIB_PATH
/usr/local/spark/bin/spark-submit --class com.wyd.BigData.App \
    --files /usr/local/spark/conf/hive-site.xml --jars $LIB_PATH \
    --master yarn \
    --deploy-mode cluster \
    --driver-memory 512m \
    --executor-memory 512m \
    --executor-cores 1 \
    --queue default \
    ./BigData.jar > stdout.log 2>&1 &

echo $! > bigdata.pid
