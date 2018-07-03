import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Tree {
	
	private static String name;
	private static ArrayList<String> nameAttributes;
	private static ArrayList<ArrayList<String>> valuesAttributes;
	private static ArrayList<ArrayList<Integer>> table;
	private static int selection = 0;
	private	static ArrayList<Integer> rows 		= new ArrayList<Integer>();
	private	static ArrayList<Integer> columns 	= new ArrayList<Integer>();
	
	public static void main(String[] args) 
	{
		try 
		{
			readFile();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		selection = nameAttributes.size()-1;
		columns.remove(selection);
		algo(rows, columns,0);
	}

	/**
	 * Asks the user the file's directory, reads the file with a weka format
	 * @throws Exception
	 */
	public static void readFile() throws Exception  
	{
		
		String directory = "";
		System.out.println("Please enter the complete file's directory: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			directory = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedReader reader = null;

		try {
			File file = new File(directory);
			reader = new BufferedReader(new FileReader(file));

			String line;
			line = reader.readLine();
			String[] div = line.split(" ");
			while(!div[0].equals("@relation"))
			{
				line = reader.readLine();
				div = line.split(" ");
			}
			int count = 0;
			name = div[1];
			nameAttributes = new ArrayList<String>();
			valuesAttributes = new ArrayList<ArrayList<String>>();
			line = reader.readLine();
			div = line.split(" ");
			while(!div[0].equals("@attribute"))
			{
				line = reader.readLine();
				div = line.split(" ");
			}
			while(div[0].equals("@attribute"))
			{
				columns.add(count);
				nameAttributes.add(div[1]);
				div = line.split("\\{");
				saveValuesAttributes(count,div[1]);
				line = reader.readLine();
				div = line.split(" ");
				count++;
			}
			while(!div[0].equals("@data"))
			{
				line = reader.readLine();
				div = line.split(" ");
			}
			line = reader.readLine();
			ArrayList<String> temp = new ArrayList<String>();
			while (line != null) 
			{
				temp.add(line);
				line = reader.readLine();
			}
			table = new ArrayList<ArrayList<Integer>>();
			populateTable(temp);
			

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				reader.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Given a line with the format @attribute name {value1, value2, value3, ..., valuen}, saves in valuesAttributes the possible values of the attribute
	 * @param count attribute's position in the list
	 * @param value1, value2, value3, ..., valuen}
	 */
	public static void saveValuesAttributes(int count, String values)
	{
		valuesAttributes.add(new ArrayList<String>());
		
		String[] div = values.split("\\}");
		String[] att = div[0].split(",");
		for (int i = 0; i < att.length; i++) 
		{
			div = att[i].split(" ");
			if (div.length == 1)
			{
				valuesAttributes.get(count).add(div[0]);
			}
			else
			{
				valuesAttributes.get(count).add(div[1]);
			}
			
		}
	}

	/**
	 * Given an array list with all the data (each element of array list is a line of the data), it saves the information in the two dimentional array list valuesAttributes
	 * @param arr Array list with data
	 */
	public static void populateTable(ArrayList<String> arr)
	{
		for (int i = 0; i < valuesAttributes.size(); i++) 
		{
			table.add(new ArrayList<Integer>());
		}
		
		for (int i = 0; i < arr.size(); i++) 
		{
			rows.add(i);
			String [] div = arr.get(i).split(",");
			for (int j = 0; j < div.length; j++) 
			{
				boolean cont = true;
				for (int k = 0; k < valuesAttributes.get(j).size() && cont; k++) {
					if (div[j].equals(valuesAttributes.get(j).get(k)))
					{
						cont = false;
						table.get(j).add(k);
					}
						
				}
			}
		}
	}
	
	/**
	 * 
	 * @param examples
	 * @param attr
	 * @param d
	 */
	public static void algo(ArrayList<Integer> examples, ArrayList<Integer> attr, int d) {
		
		int attribute = importance(examples, attr);
		ArrayList<Integer> nAttr = new ArrayList<Integer>(attr);
		nAttr.remove(nAttr.indexOf(attribute));
		ArrayList<Integer> valuesExist = new ArrayList<Integer>();
		for(int m : examples) {
			if(!valuesExist.contains(table.get(attribute).get(m))) {
				valuesExist.add(table.get(attribute).get(m));
			}
		}
		for(int i : valuesExist) {
			ArrayList<Integer> exs = new ArrayList<Integer>();
			for(int j : examples) {
				if(table.get(attribute).get(j) == i) {
					exs.add(j);
				}
			}
			if(!exs.isEmpty()) {
				for(int k : exs) {
					examples.remove(examples.indexOf(k));
				}
			}
			String x = nameAttributes.get(attribute)+"= " + valuesAttributes.get(attribute).get(i);
			dTL(exs, nAttr, examples, x, d);
		}
	}
	
	/**
	 * Algorithm that creates the decision tree
	 * @param examples
	 * @param attributes
	 * @param pExamples
	 * @param nameP
	 * @param d
	 * @return
	 */
	public static int dTL(ArrayList<Integer> examples, ArrayList<Integer> attributes, ArrayList<Integer> pExamples, String nameP, int d) {
		String h = "";
		for (int i = 0; i < d; i++) 
		{
			h += "     ";
		}
		int[] counter = new int[valuesAttributes.get(selection).size()];
		int highest, result = 0;
		for(int i = 0; i < counter.length; i++) {
			counter[i] = 0;
		}
		if(examples.isEmpty()) {
			for(int j : pExamples) {
				counter[table.get(selection).get(j)] ++;
			}
			highest = counter[result];
			for(int k = 1; k < counter.length; k++) {
				if(counter[k] > highest) {
					result = k;
				}
			}
			System.out.println(h + nameP+": "  + valuesAttributes.get(selection).get(result));
			return result;
		}
	
		else if(check(examples)) {
			//all examples have the same classification then return the classification
			System.out.println(h + nameP +": " + valuesAttributes.get(selection).get(table.get(selection).get(examples.get(0))));
			return table.get(selection).get(examples.get(0));
		}
		
		else if(attributes.isEmpty()) {
			//return examples' plurality value
			for(int j : examples) {
				counter[table.get(selection).get(j)] ++;
			}
			highest = counter[result];
			for(int k = 1; k < counter.length; k++) {
				if(counter[k] > highest) {
					result = k;
				}
			}
			System.out.println(h + nameP + ": " + valuesAttributes.get(selection).get(result));
			return result;
		}
		
		else {
			System.out.println(h + nameP);
			int d1 = d+1;
			algo(examples, attributes, d1);
			return 0;
		}
	}

	/**
	 * Check if all examples have the same classification
	 * @param examples
	 * @return true when all examples have the same classification
	 */
	public static boolean check(ArrayList<Integer> examples) {
		int classification = table.get(selection).get(examples.get(0));
		for(int i : examples) {
			if(table.get(selection).get(i) != classification) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calculates the importance of the remaining attributes and returns the index of the one with the greatest importance
	 * @param examples
	 * @param attributes
	 * @return
	 */
	public static int importance(ArrayList<Integer> examples, ArrayList<Integer> attributes) {
		double	bestGain 	= -1.0;
		double 	gain;
		int		trackID 	= 0;
		double		counter;
		double [] temp	= new double[valuesAttributes.get(selection).size()];
		for(int i : attributes) {
			gain = 1;
			for(int j = 0; j < valuesAttributes.get(i).size(); j++) {
				counter = 0.0;
				for(int n = 0; n < valuesAttributes.get(selection).size(); n++) {
					temp[n] = 0.0;
				}
				for(int k : examples) {
					if(table.get(i).get(k) == j) {
						counter ++;
						temp[table.get(selection).get(k)] += 1;
					}
				}
				double calc1 = counter / examples.size();
				double calc2;
				double totalCalc = 0.0;
				for(int p = 0; p < valuesAttributes.get(selection).size(); p++) {
					calc2 = 0.0;
					if(temp[p] == 0.0) {
						calc2 = 0.0;
					}
					else {
						calc2 = ((temp[p] / counter) * ((Math.log(temp[p] / counter)) / Math.log(2)));
						totalCalc += calc2;
					}
				}
				double minusGain = (calc1 * -(totalCalc));
				gain -= minusGain;
			}
			if(gain > bestGain) {
				bestGain = gain;
				trackID  = i;
			}
		}
		return trackID;
	}
}