public class Main {
	
	public static void main(String[] args) {

		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
		
		nbc.trainData("./70-30 Split/set2train.csv");
		nbc.printProbabilities();
		nbc.classifyData("./70-30 Split/set2test.csv", false);
		nbc.printStatistics();
	}
	
}