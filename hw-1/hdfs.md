## Часть 1

1. hdfs dfs -mkdir /test_dir
2. hdfs dfs -mkdir /test_dir/nested_dir
3. Для обеспечение сохранности данных, по дефолту, происходит не удаление, а перемещение в папку trash.
Со временем содержимое этой папки очищается. Для "настоящего" удаления нужно испльзовать флаг -skipTrash
4. hdfs dfs -touch /test_dir/nested_dir/random_file
5. hdfs dfs -rm -skipTrash /test_dir/nested_dir/random_file
6. hdfs dfs -rm -r -skipTrash /test_dir

## Часть 2

1. mkdir local_dir
hdfs dfs -put local_dir/ /
2. echo "Transfered from local to hdfs" > local_file.txt
hdfs dfs -put local_file.txt /
hdfs dfs -cat /local_file.txt
3. hdfs dfs -tail /local_file.txt
4. hdfs dfs -head /local_file.txt
5. hdfs dfs -mkdir /new_location
hdfs dfs -cp /local_file.txt /new_location

## Часть 3

1. hdfs dfs -setrep -w 1 /local_file.txt
hdfs dfs -setrep -w 3 /local_file.txt
Уменьшение фактора репликации занимает больше времени, даже варнинг выскакивает
2. hdfs fsck /local_file.txt -files -blocks -locations
3. Из предыдущей команды получили:
"0. BP-683595759-172.19.0.2-1632881558737:blk_1073741843_1019 len=30 Live_repl=3"
[DatanodeInfoWithStorage[172.19.0.3:9866,DS-62c1b255-d2f6-4036-9904-087c51fefa58,DISK], 
DatanodeInfoWithStorage[172.19.0.8:9866,DS-ff908ab1-ff76-4a4c-96e3-d4aef8e13542,DISK], 
DatanodeInfoWithStorage[172.19.0.6:9866,DS-15664713-b9f4-4cf2-b687-f007edff0295,DISK]]
blockID with GS - blk_1073741843_1019 и отрезали GS (цифры после последнего "_")
hdfs fsck -blockId blk_1073741843