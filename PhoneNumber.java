package prog4;

import java.util.Iterator;
import data_structures.*;

public class PhoneNumber implements Comparable<PhoneNumber>{
	String areaCode, prefix, number;
    String phoneNumber;
    
    public PhoneNumber(String n) {
    	phoneNumber = n;
    	verify(phoneNumber);
    }
    
    public int compareTo(PhoneNumber n) {
    	return (toString().compareTo(n.toString()));
    }
    
    public int hashCode() {
    	String s = "";
    	int i = 0;
    	while(i < 11){
    		s = s + toString().charAt(i);
    		if (i == 2 || i == 6) i++;
    		i++;
    	}
    	int j = Integer.parseInt(s);
    	return j;
    }
    
    private void verify(String n) {
    	if(n.length() != 12)
    		throw new IllegalArgumentException();
    	else{
    		if(n.charAt(3)!='-'||n.charAt(7)!='-')
    			throw new IllegalArgumentException();
    		int i = 0;
    		while(i<12){
    			if(i==3 || i==7) i++;
    			if(n.charAt(i)>'9'||n.charAt(i)<'0')
    				throw new IllegalArgumentException();
    			i++;
    		}
    	}
    }
    
    public String getAreaCode() {
    	String[] nums = phoneNumber.split("-");
    	return nums[0];
    }
    
    public String getPrefix() {
    	String[] nums = phoneNumber.split("-");
    	return nums[1];
    }
    
    public String getNumber() {
    	String[] nums = phoneNumber.split("-");
    	return nums[2];
    }
    
    public String toString() {
    	return phoneNumber;
    }


}
