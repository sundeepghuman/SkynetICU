frameworkDir="SkynetFramework"
userDir="AfibExtractor"
extractorClass="edu.skynet.featureextraction.AfibExtractor"
sampleParserClass="edu.skynet.afib.parsers.AfibSampleParser"
annotationParserClass="edu.skynet.afib.parsers.AfibAnnotationParser"
exporterClass="edu.skynet.afib.export.AfibDataExporter"
sampleRate="250"
minSlice="1000"
maxSlice="1500"


#create framework jar
echo "----Creating framework jar"
rm $frameworkDir/skynet.jar
jar cf $frameworkDir/skynet.jar -C $frameworkDir/bin edu

#create user code jar
echo "----Creating user code jar"
rm $userDir/afib-extractor.jar
jar cf $userDir/afib-extractor.jar -C $userDir/bin edu

#clear hdfs
echo "----Clearing HDFS"
hadoop dfs -rmr .

#copy over data
echo "----Copying files to HDFS"
hadoop dfs -mkdir input
hadoop dfs -copyFromLocal afib-data/*.* input/

#run jar
echo "----Running job"
hadoop jar $frameworkDir/skynet.jar edu.skynet.hadoop.MapRedDriver \
 -D minSlice=$minSlice -D maxSlice=$maxSlice -D exporter.class=$exporterClass \
 -libjars $userDir/afib-extractor.jar \
 input/ $extractorClass $sampleParserClass $annotationParserClass $sampleRate

#copy results
echo "----Copying results to local machine"
rm -r job-output/
hadoop dfs -copyToLocal extractedData/ job-output/

 
