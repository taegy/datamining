In a direct marketing campaign, a study had been conducted to determine if a bank client would subscribe to a term deposit.
This program analyse collected data using Naive Bayes algorithm and ID3 algorithm in order to find out if the data attributes have a direct
influence in subscribing a term deposit.
A dataset from the UCI Machine Learning Repository was used.

=========== Steps to run Naive Bayes Classifier ===========
1) Import .java files into a Java project
2) Add testing and training data into project (Must be in .csv format)
3) Edit file source in Main
4) Run and observe console printout

Note:
- Files can taken from "Sampling Data" folder
- To look at misclassified rows, set 2nd parameter of classifyData(String csvFile, boolean writeMisclassify) to true. All misclassified
rows will be written to a .txt file.
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