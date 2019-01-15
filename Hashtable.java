package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class Hashtable<K extends Comparable<K>,V> implements DictionaryADT<K,V> {
	private long modCounter;
	private int currentSize, maxSize, tableSize;
	private LinearListADT<DictionaryNode<K,V>> [] list;
	
	public Hashtable (int size){
		modCounter = 0;
		maxSize = size;
		currentSize = 0;
		tableSize = (int) (maxSize*1.3f);
		list = new LinearList[tableSize];
		for (int i = 0; i < tableSize; i++){
			list[i] = new LinearList<DictionaryNode<K,V>>();
		}	
	}
	
	class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>> {
		K key;
		V value;
		DictionaryNode (K key, V value) {
			this.key = key;
			this.value = value;
		}
		public int compareTo(DictionaryNode<K,V> Node){
			return ((Comparable<K>)key).compareTo((K)Node.key);
		}
	}
	
	private int getIndex(K key) {
		return (key.hashCode() & 0x7FFFFFFF % tableSize);
	}
	
	public boolean contains(K key) {
		return list[getIndex(key)].contains(new DictionaryNode<K,V>(key,null));
	}
	
	public boolean add(K key, V value) {
		if(isFull()) return false;
		if(contains(key)) return false;
		DictionaryNode<K,V> newNode = new DictionaryNode<K,V>(key,value);
		list[getIndex(key)].addLast(newNode);
		currentSize++;
		modCounter++;
		return true;
	}
	
	public boolean delete(K key) {
		if(!contains(key)) return false;
		list[getIndex(key)].remove(new DictionaryNode<K,V>(key,null));
		currentSize--;
		modCounter++;
		return true;
	}
	
	public V getValue(K key) {
		DictionaryNode<K,V> tmp = list[getIndex(key)].find(new DictionaryNode<K,V>(key,null));
		if (tmp == null) return null;
		return tmp.value;
	}
	
	public K getKey(V value) {
		for(int i = 0; i<tableSize; i++)
			for(DictionaryNode<K,V> n : list[i]){
				if (((Comparable<V>)value).compareTo((V)n.value) == 0) 
					return (K)n.key;
			}
		return null;
	}
	
	public int size() {
		return currentSize;
	}
	
	public boolean isFull() {
		return (currentSize == maxSize);
	}
	
	public boolean isEmpty() {
		return (currentSize == 0);
	}
	
	public void clear() {
		currentSize = 0;
		modCounter = 0;
		for (int i=0; i < tableSize; i++)
			list[i].clear();
	}
	
	abstract class IteratorHelper<E> implements Iterator<E> {
		protected DictionaryNode<K,V> [] nodes;
		protected int idx;
		protected long modCheck;
		
		public IteratorHelper() {
			nodes = new DictionaryNode[currentSize];
			idx = 0;
			int j = 0;
			modCheck = modCounter;
			for(int i = 0; i<tableSize; i++)
				for (DictionaryNode n : list[i]){
					nodes[j++] = n;
				}
			quicksort(nodes, 0, currentSize-1);
		}
		
		public boolean hasNext() {
			if (modCheck != modCounter) 
				throw new ConcurrentModificationException();
			return idx < currentSize;
		}
		
		public abstract E next();
		
		public void remove(){
			//throw new UnsuppotedOperationException();
		}
		
		public void quicksort(DictionaryNode<K,V> [] nodes, int low, int high){
			if(low >= high) return;
			int i = low;
			int j = 0;
			int k = low;
			DictionaryNode<K,V> pivot = nodes[high];
			DictionaryNode<K,V> comp = nodes[k];
			DictionaryNode<K,V> tmp;
			while(k<high){
				comp = nodes[k];
				if (((Comparable<K>)comp.key).compareTo(pivot.key) < 0){
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
	
	public Iterator keys() {
		class KeyIteratorHelper<K> extends IteratorHelper<K> {
			public KeyIteratorHelper(){
				super();
			}
			public K next() {
				if(!hasNext()) throw new NoSuchElementException();
				return (K) nodes[idx++].key;
			}
		}
		return new KeyIteratorHelper();
	}
	
	public Iterator values() {
		class ValueIteratorHelper<K> extends IteratorHelper<K> {
			public ValueIteratorHelper(){
				super();
			}
			public K next() {
				if(!hasNext()) throw new NoSuchElementException();
				return (K) nodes[idx++].value;
			}
		}
		return new ValueIteratorHelper();
	}

}
