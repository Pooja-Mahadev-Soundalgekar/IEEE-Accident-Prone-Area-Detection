from pyspark import SparkConf, SparkContext
import collections
import operator

conf = SparkConf().setMaster("local").setAppName("AccidentCity")
sc = SparkContext(conf = conf)


def parseLine(line):
    fields = line.split('\t')
    cityState = fields[1]
    cityStateArray = cityState.split(',')
    if len(cityStateArray) == 2 and cityStateArray[0] != "\"N/A" and cityStateArray[0] != "\"NA":
        return (cityStateArray[0])
    else:
        return

lines = sc.textFile("file:///SparkCourse/allDataTsv.txt")
rdd = lines.map(parseLine)
result = rdd.countByValue()

sortedResults = collections.OrderedDict(sorted(result.items(), key=operator.itemgetter(1), reverse=True)[:11])
for key, value in sortedResults.iteritems():
    if key != None:
        print "%s %i" % (key, value)
    

