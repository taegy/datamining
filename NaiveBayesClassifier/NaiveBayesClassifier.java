import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class NaiveBayesClassifier {

	private String separator =",";
	private BufferedReader br;
	private Model model;
	
	//Statistics
	int totalInstances;
	int misclassify;
	
	boolean isRowUnclassified = false;
	
	public NaiveBayesClassifier() {
		model = new Model();
	}
	
	/**
	 * Given a .csv file, parse the file and train the model
	 * @param csvFile
	 */
	public void trainData(String csvFile) { 
		boolean isHeader = true;
		String line = "";
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while((line = br.readLine()) != null) {
				//retrieve header
				if(isHeader) {
					String[] header = line.split(separator);
					isHeader = false;
					//handle header row
					model.addAttName(header);
				}
				else {
					String[] row = line.split(separator);
					//handle data row
					model.trainClassifier(row);
				}
			}
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Given a .csv file, parse the file and classify the file based on the model trained
	 * @param csvFile
	 * @param writeMisclassify. set to true to write misclassified rows to .txtfile
	 */
	public void classifyData(String csvFile, boolean writeMisclassify) {
		boolean isHeader = true;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while((line = br.readLine()) != null) {
				String[] row = line.split(separator);
				//retrieve header
				if(isHeader) {
					if(row.length < model.attNameValueMap.keySet().size())
						isRowUnclassified = true;
					else
						isRowUnclassified = false;
					isHeader = false;
				}
				else {
					totalInstances++;
					//handle data row
					String classLabel = model.classifyRow(row);
					//If row is not classified, print row and classified label
					if(isRowUnclassified == true) {
						//Print each row and its classified label
						for(int i=0; i<row.length-1; i++) {
							System.out.print(row[i] + ", ");
						}
						System.out.println(" CLASSIFIED AS: " + classLabel);
					}
					//If row is misclassified, write the misclassified rows to a txtfile
					else if(isRowUnclassified == false && !row[row.length-1].equals(classLabel)) {
						String result = "";
						for(int i=0; i<row.length; i++) {
							result += row[i] + ", ";
						}
						result += "Classified as: " + classLabel;
						misclassify++;
						if(writeMisclassify)
							writeToFile("misclassify.txt", result);
					}
				}
			}
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println();
	}
	
	/**
	 * Helper class to write misclassified rows to .txt file
	 * @param filename
	 * @param content
	 * @throws IOException
	 */
	public void writeToFile(String filename, String content) throws IOException{
		File file = new File(filename);
		file.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
		bw.append(content);
		bw.newLine();
		bw.close();
	}
	
	public void printProbabilities() {
		model.printProbabilities();
		//model.printLabelCount();
		//model.printAttributeNameList();
		//model.printAttributeNameValueMap();
	}
	
	/**
	 * Calculate and print out the accuracy of the trained data and the test data if test data has been classified.
	 * Otherwise only print out total instances
	 */
	public void printStatistics() {
		if(isRowUnclassified == false) {
			float correctPercentage = (float)(totalInstances-misclassify)/totalInstances * 100;
			float incorrectPercentage = (float)(misclassify)/totalInstances * 100;
			System.out.println("Correctly Classified Instances: \t" + (totalInstances-misclassify) + "\t" + correctPercentage + "%");
			System.out.println("Incorrectly Classified instances: \t" + misclassify + "\t" + incorrectPercentage + "%");
			System.out.println("Total Number of Instances: " + totalInstances);
		}
		else {
			System.out.println("Correctly Classified Instances: \t" + " - ");
			System.out.println("Incorrectly Classified instances: \t" + " -");
			System.out.println("Total Number of Instances: " + totalInstances);
		}
	}
}
