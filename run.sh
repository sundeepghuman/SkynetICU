frameworkDir="SkynetFramework"
userDir="AfibExtractor"
extractorClass="edu.skynet.featureextraction.AfibExtractor"
sampleParserClass="edu.skynet.afib.parsers.AfibSampleParser"
annotationParserClass="edu.skynet.afib.parsers.AfibAnnotationParser"
exporterClass="edu.skynet.afib.export.AfibDataExporter"
sampleRate="250"
minSlice="1000"
maxSlice="1500"


#clear hdfs
echo "----Clearing HDFS"
hadoop dfs -rmr .

#copy over data to hdfs
echo "----Copying files to HDFS"
hadoop dfs -mkdir input
hadoop dfs -copyFromLocal afib-data/*.* input/

#run job
echo "----Running job"
hadoop jar $frameworkDir/skynet.jar edu.skynet.hadoop.MapRedDriver \
 -D minSlice=$minSlice -D maxSlice=$maxSlice -D exporter.class=$exporterClass \
 -libjars $userDir/afib-extractor.jar \
 input/ $extractorClass $sampleParserClass $annotationParserClass $sampleRate

#copy results from HDFS to local machine
echo "----Copying results to local machine"
rm -r job-output/
hadoop dfs -copyToLocal extractedData/ job-output/

 
