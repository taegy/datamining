import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Model {
	
	protected ArrayList<String> attNameList; //List of all attribute names
	protected HashMap<String, ArrayList<String>> attNameValueMap; //List of all attribute names and their possible attribute values 
	
	protected HashMap<String, Integer> labelCount; //Counts of class labels ie. Number of yes, number of no
	protected HashMap<ArrayList<String>, Integer> attValueCount; //Counts of occurrence of each attribute value with each label value. Key = {attribute, label, attribute value}, Value = count
	
	public Model() {
		attNameList = new ArrayList<String>();
		attNameValueMap = new HashMap<String, ArrayList<String>>();
		labelCount = new HashMap<String, Integer>();
		attValueCount = new HashMap<ArrayList<String>, Integer>();
	}
	
	/**
	 * Populate list of attribute names
	 * @param category
	 */
	public void addAttName(String[] category) {
		
		for(int i=0; i<category.length; i++) {
			
			if(!attNameList.contains(category[i]))
				attNameList.add(category[i]);
		}
		
		initAttNameValueMap();
	}
	
	/**
	 * Initialise the attribute name/value hashmap
	 */
	public void initAttNameValueMap() {
		for(int i=0; i<attNameList.size(); i++) {
			attNameValueMap.put(attNameList.get(i), new ArrayList<String>());
		}
	}
	
	/**
	 * add attribute value to attribute/attribute value hashmap
	 * @param attribute
	 * @param attributeValue
	 */
	public void addAttributeValuetoMap(int attribute, String attributeValue) {
		
		ArrayList<String> tempList = attNameValueMap.get(attNameList.get(attribute));
		
		if(!tempList.contains(attributeValue)) {
			tempList.add(attributeValue);
		}
	}
	
	/**
	 * Loop through all attribute values in a record and update the count of each {label,attributeValue} pair
	 * @param row refers to a row of data in the .csv file
	 */
	public void trainClassifier(String[] row) {
		String labelName = row[row.length-1];
		//update attribute name/value mapping for labels
		addAttributeValuetoMap(row.length-1, labelName);
		
		for(int i=0; i<row.length-1; i++) {
			
			ArrayList<String> tempKey = new ArrayList<String>();
			tempKey.add(attNameList.get(i));
			tempKey.add(labelName);
			tempKey.add(row[i]);
			
			//update attribute name/value mapping for attribute values
			addAttributeValuetoMap(i, row[i]);
			
			//update attribute value count
			if(attValueCount.containsKey(tempKey)) {
				int count = attValueCount.get(tempKey);
				attValueCount.put(tempKey, count+1);
			}
			else {
				attValueCount.put(tempKey, 1);
			}
		}
		//update label count
		if(labelCount.containsKey(labelName)) {
			int count = labelCount.get(labelName);
			labelCount.put(labelName, count+1);
		}
		else {
			labelCount.put(labelName, 1);
		}
	}
	
	/**
	 * Given a row of data, classify the row
	 * @param row refers to a row of data in the .csv file
	 * @return the class label it is classified to
	 */
	public String classifyRow(String[] row) {
		
		ArrayList<String> tempKey = new ArrayList<String>();
		HashMap<String, Float> recordProbability =  new HashMap<String, Float>();
		//get total count of class labels
		int totalClassLabels = sumOfValuesInHashMap(labelCount);
		Iterator<String> keySetIterator = labelCount.keySet().iterator();
		//Using an iterator, initialise record probability to P(Label Class)
		while(keySetIterator.hasNext()){
		  String key = keySetIterator.next();
		  float labelProb = (float)labelCount.get(key)/totalClassLabels;
		  recordProbability.put(key, labelProb);
		}
		
		//loop through all attributes in a row
		for(int i=0; i<row.length-1; i++) {
			String attValue = row[i];
			
			Iterator<String> labelIterator = labelCount.keySet().iterator();
			//loop through all possible labels for each attribute value
			while(labelIterator.hasNext()) {
				String label = labelIterator.next();
				tempKey.add(attNameList.get(i));
				tempKey.add(label);
				tempKey.add(attValue);
				
				//Calculate probability of each attribute value given label P(Attribute Value|Label Class)
				float featureProbability = 0.0f;
				if(attValueCount.get(tempKey) == null)
					featureProbability = 0;
				else
					featureProbability = (float)attValueCount.get(tempKey)/labelCount.get(label);
				
				//Update probability of record given label. P(Record|Label Class)
				float currProb = recordProbability.get(label);
				recordProbability.put(label, currProb*featureProbability);				
				tempKey.clear();
			}
		}
		//obtain key with max probability
		return getMaxValueInHashMap(recordProbability);
	}
	
	/**
	 * Returns the key of the max value in a HashMap. Used to determine highest probability
	 * @param hashMap
	 * @return key of maxEntry
	 */
	private String getMaxValueInHashMap(HashMap<String, Float> hashMap) {
		
		Map.Entry<String, Float> maxEntry = null;

		for (Map.Entry<String, Float> entry : hashMap.entrySet())
		{
			//System.out.println("Classified as = " + entry.getKey() + "probability = " + entry.getValue());
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		    {
		        maxEntry = entry;
		    }
		}
		
		return maxEntry.getKey();
	}
	
	/**
	 * Sums up all integers in the hashmap. Used to calculate the total count of labels in the trained data
	 * @param hashMap
	 * @return
	 */
	private int sumOfValuesInHashMap(HashMap<String, Integer> hashMap) {
		
		int sum = 0;
		
		for(float value : hashMap.values()) 
		{
			sum += value;
		}
		
		return sum;
	}
	
	/**
	 * Print the independent probabilities of each attribute
	 */
	public void printProbabilities() {
		Iterator<String> keySetIterator = attNameValueMap.keySet().iterator();
		
		System.out.println("\n");

		//Loop through the attribute value map
		while(keySetIterator.hasNext()){
		  String key = keySetIterator.next();
		  System.out.println("CATEGORY: " + key + "\nFEATURES: " + attNameValueMap.get(key));
		  
		  //for each attribute in the attribute value map, print out the probabilities of each class label
		  for(String attVal: attNameValueMap.get(key)) {
				
			  Iterator<String> labelCountIterator = labelCount.keySet().iterator();
				ArrayList<String> temp = new ArrayList<String>();
				//Loop through all label counts and print out the probabilities
				while(labelCountIterator.hasNext()) {
					String labelCountKey = labelCountIterator.next();
					temp.add(key);
					temp.add(labelCountKey);
					temp.add(attVal);
					System.out.println("P(" + attVal + "|" + labelCountKey + ") = " + attValueCount.get(temp) + "/" + labelCount.get(labelCountKey));
					temp.clear();
				}
				System.out.println();
		  }
		}
	}
	
	/**
	 * Print the name of all attributes
	 */
	public void printAttributeNameList() {
		
		for(int i=0; i<attNameList.size(); i++) {
			System.out.println(attNameList.get(i) + "\n");
		}
	}
	
	/**
	 * Print the count of each class label
	 */
	public void printLabelCount() {
		Iterator<String> keySetIterator = labelCount.keySet().iterator();

		System.out.println("\n");
		
		while(keySetIterator.hasNext()){
		  String key = keySetIterator.next();
		  System.out.println("label: " + key + " value: " + labelCount.get(key));
		}
	}
	
	/**
	 * Print all attributes and their possible attribute values
	 */
	public void printAttributeNameValueMap() {
		Iterator<String> keySetIterator = attNameValueMap.keySet().iterator();
		
		System.out.println("\n");

		while(keySetIterator.hasNext()){
		  String key = keySetIterator.next();
		  System.out.println("category: " + key + " features: " + attNameValueMap.get(key));
		}
	}
}
