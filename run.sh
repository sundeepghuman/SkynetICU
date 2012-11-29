#create jar
rm extract.jar
jar cf extract.jar -C bin edu

#clear hdfs
hadoop dfs -rmr .

#copy over data
hadoop dfs -put test-afib-data/small_data.txt data1.txt
hadoop dfs -put test-afib-data/small_data2.txt data2.txt

#run jar
hadoop jar extract.jar edu.skynet.hadoop.MapRedDriver . output/

#copy results
rm -r job-output/
hadoop dfs -copyToLocal output/ job-output/

 
