package data_structures;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class BalancedTree<K extends Comparable<K>,V> implements DictionaryADT<K,V>{
	private TreeMap<K,V> rbt;
	
	public BalancedTree(){
		rbt = new TreeMap<K,V>();
	}
	
	public boolean contains(K key) {
		return rbt.containsKey(key);
	}
	
	public boolean add(K key, V value) {
		if (rbt.containsKey(key)) return false;
		rbt.put(key,value);
		return true;
	}
	
	public boolean delete(K key) {
		if (rbt.remove(key) != null){
			return true;
		}
		return false;
	}
	
	public V getValue(K key) {
		return rbt.get(key);
	}
	
	public K getKey(V value) {
		for (Map.Entry<K, V> entry : rbt.entrySet()) {
			if(((Comparable<V>)value).compareTo(entry.getValue()) == 0) return entry.getKey();
		}
		return null;
	}
	
	public int size() {
		return rbt.size();
	}
	
	public boolean isFull() {
		return false;
	}
	
	public boolean isEmpty() {
		if (rbt.size() == 0) return true;
		return false;
	}
	
	public void clear() {
		rbt.clear();
	}
	
	public Iterator keys() {
		return rbt.keySet().iterator();
	}
	
	public Iterator values() {
		return  rbt.values().iterator();
	}

}
