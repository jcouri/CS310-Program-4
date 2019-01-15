package prog4;

import data_structures.*;
import java.util.Iterator;
import java.io.*;

public class PhoneBook {
	private DictionaryADT<PhoneNumber,String> dictionary;

    public PhoneBook(int maxSize) {
    	dictionary = new Hashtable<PhoneNumber,String>(maxSize);
    	//dictionary = new BinarySearchTree<PhoneNumber,String>();
    	//dictionary = new BalancedTree<PhoneNumber,String>();
    	
    }
    
    public void load(String filename) {
    	String line;
    	try{
    		BufferedReader in = new BufferedReader(new FileReader(filename));
    		while((line = in.readLine()) != null){
    			PhoneNumber tmp = new PhoneNumber(line.substring(0, 12));
    			String value = line.substring(13);
    			dictionary.add(tmp,value);
    		}
    		in.close();
    	}
    	catch(IOException e){
    		System.out.println("Error loading file");
    	}
    }
           
    public String numberLookup(PhoneNumber number) {
    	String s = (String) dictionary.getValue(number);
    	return s;
    }
       
    public PhoneNumber nameLookup(String name) {
    	return dictionary.getKey(name);
    }
       
    public boolean addEntry(PhoneNumber number, String name) {
    	if(dictionary.add(number, name)) return true;
    	return false;
    }
       
    public boolean deleteEntry(PhoneNumber number) {
    	//System.out.println(dictionary.size());
    	if(dictionary.delete(number)) return true;
    	return false;
    }
       
    public void printAll() {
        Iterator<PhoneNumber> keys = dictionary.keys();
        PhoneNumber p;
        String s;
        Iterator<String> values = dictionary.values();
        while(keys.hasNext()) {
        	p = keys.next();
        	s = values.next();
            System.out.print(p);
            System.out.print("   " + s);
            System.out.println();
        }    
    }
    
    public void printByAreaCode(String code) {
    	Iterator<PhoneNumber> keys = dictionary.keys();
    	PhoneNumber p;
    	while(keys.hasNext()) {
    		p = keys.next();
    		if (p.getAreaCode().equals(code)) System.out.println(p + " " + dictionary.getValue(p));
    	}
    }
        
    public void printNames() {
    	Iterator<String> values = dictionary.values();
    	int i = 0;
    	String [] nodes = new String[dictionary.size()];
    	while(values.hasNext()) {
            nodes[i] = values.next();
            i++;
        }
    	quicksort(nodes,0,dictionary.size()-1);
    	i = 0;
    	while(i<dictionary.size()) {
    		System.out.println(nodes[i]);
    		i++;
    	}
    }
    
    private void quicksort(String [] nodes, int low, int high){
		if(low >= high) return;
		int i = low;
		int j = 0;
		int k = low;
		String pivot = nodes[high];
		String comp = nodes[k];
		String tmp;
		while(k<high){
			comp = nodes[k];
			if (((Comparable<String>)comp).compareTo(pivot) < 0){
				j++;
				tmp = nodes[i];
				nodes[i] = nodes[k];
				nodes[k] = tmp;
				i++;
			}
			k++;
		}
		tmp = nodes[low+j];
		nodes[low+j] = pivot;
		nodes[high]=tmp;
		quicksort(nodes, low, low+j-1);
		quicksort(nodes, low+j+1, high);
	}


}
