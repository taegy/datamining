=========Group Names and Group ID==========================
GroupID: her001
GroupMembers:
1) Chng Jun Er Carolyn - jchng001 (U1220913G)
2) Er Hui Yuan - her001 (U1221832G)
3) Hoe Jia Chang Kenny - jhoe002 (U1221549L)
4) Hwang Taegyeong - n1403661l (N1403661L)
5) Leong Pui Cheng - pleong001 (U1222662L)
6) Low Kuan Wei - klow001 (U1221220C)
7) Tan Teck Siong, Leonard - ltan007 (U1221567B)


=========== Steps to run Naive Bayes Classifier ===========
1) Import .java files into a Java project
2) Add testing and training data into project (Must be in .csv format)
3) Edit file source in Main
4) Run and observe console printout

Note:
- Files can taken from "Sampling Data" folder
- To look at misclassified rows, set 2nd parameter of classifyData(String csvFile, boolean writeMisclassify) to true. All misclassified rows will be written to a .txt file.
- Calculation of accuracy and misclassification only works if test data has already been classified.

=============== Steps to run ID3 ==========================
1) Run cmd.exe
2) Change directory to where the Tree.jar file is saved
3) Type "java -jar Tree.jar" and hit enter
4) The program will prompts for the complete directory of the ARFF format file only
5) Type in the ARFF file directory, for example "C:\Users\DataMining\OriginalFullData-45k.arff
6) The program will automatically generate the decision tree 

Note:
- Files can be taken from "Sampling Data" folder