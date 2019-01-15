package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class BinarySearchTree<K extends Comparable<K>,V> implements DictionaryADT<K,V>{
	private long modCounter;
	private int currentSize;
	private Node<K,V> root;
	
	public BinarySearchTree (){
		root = null;
		modCounter = 0;
		currentSize = 0;
	}
	
	class Node<K,V> {
		public K key;
		public V value;
		public Node<K,V> leftChild;
		public Node<K,V> rightChild;
		
		public Node(K k, V v) {
			key = k;
			value = v;
			leftChild = rightChild = null;
		}
	}
	
	public boolean contains(K key) {
		if(find(key, root) != null)
			return true;
		return false;
	}
	
	private V find(K key, Node<K,V> n) {
		if(n == null) return null;
		if(((Comparable<K>)key).compareTo(n.key) < 0)
			return find(key, n.leftChild);
		else if(((Comparable<K>)key).compareTo(n.key) > 0)
			return find(key, n.rightChild);
		else
			return (V) n.value;
	}
	
	public boolean add(K key, V value) {
		if (contains(key)) return false;
		if(root == null){
			root = new Node<K,V>(key,value);
		}
		else 
			insert(key,value,root,null,false);
		currentSize++;
		modCounter++;
		return true;
	}
	
	private void insert(K k, V v, Node<K,V> n, Node<K,V> parent, boolean wasLeft) {
		if(n == null) {
			if(wasLeft) parent.leftChild = new Node<K,V>(k,v);
			else parent.rightChild = new Node<K,V>(k,v);
		}
		else if(k.compareTo(n.key) < 0)
			insert(k,v,n.leftChild,n,true);
		else
			insert(k,v,n.rightChild,n,false);
	}
	
	public boolean delete(K key) {
		if (isEmpty()) return false;
		if (!delete(key,root,null,false)) return false;
		currentSize--;
		modCounter++;
		return true;
	}
	
	private boolean delete(K key,Node<K,V> n,Node<K,V> parent, boolean wentLeft){
		if (n.leftChild==null && n.rightChild==null){
			if ((((Comparable<K>)key).compareTo(n.key) == 0)){
				if(parent == null) root = null;
				else if (wentLeft) parent.leftChild = null;
				else parent.rightChild = null;
				return true;
			}
			else return false;
		}
		else if(n.leftChild == null){
			if ((((Comparable<K>)key).compareTo(n.key) == 0)){
				if(parent == null) root = n.rightChild;
				else if (wentLeft) parent.leftChild = n.rightChild;
				else parent.rightChild = n.rightChild;
				return true;
			}
			else if((((Comparable<K>)key).compareTo(n.key) > 0)){
				return(delete(key,n.rightChild,n,false));
			}
			else return false;
		}
		else if(n.rightChild == null){
			if ((((Comparable<K>)key).compareTo(n.key) == 0)){
				if(parent == null) root = n.leftChild;
				else if (wentLeft) parent.leftChild = n.leftChild;
				else parent.rightChild = n.leftChild;
				return true;
			}
			else if((((Comparable<K>)key).compareTo(n.key) < 0)){
				return(delete(key,n.leftChild,n,true));
			}
			else return false;
		}
		else{
			if ((((Comparable<K>)key).compareTo(n.key) == 0)){
				Node<K,V> tmp = n.rightChild; 
				Node<K,V> tmp2 = null;
				if(parent == null){
					if (tmp.leftChild == null){
						tmp.leftChild = n.leftChild;
						root = tmp;
					}
					else {
						while(tmp.leftChild.leftChild != null){
							tmp = tmp.leftChild;
						}
						tmp2 = tmp.leftChild.rightChild;
						tmp.leftChild.leftChild = n.leftChild;
						tmp.leftChild.rightChild = n.rightChild;
						root = tmp.leftChild;
						tmp.leftChild = tmp2;
					}
				}
				else if (wentLeft){
					if (tmp.leftChild == null){
						tmp.leftChild = n.leftChild;
						parent.leftChild = tmp;
					}
					else{
						while(tmp.leftChild.leftChild != null){
							tmp = tmp.leftChild;
						}
						tmp2 = tmp.leftChild.rightChild;
						tmp.leftChild.leftChild = n.leftChild;
						tmp.leftChild.rightChild = n.rightChild;
						parent.leftChild = tmp.leftChild;
						tmp.leftChild = tmp2;
					}
					
				}
				else{
					if (tmp.leftChild == null){
						tmp.leftChild = n.leftChild;
						parent.rightChild = tmp;
					}
					else{
						while(tmp.leftChild.leftChild != null){
							tmp = tmp.leftChild;
						}
						tmp2 = tmp.leftChild.rightChild;
						tmp.leftChild.leftChild = n.leftChild;
						tmp.leftChild.rightChild = n.rightChild;
						parent.rightChild = tmp.leftChild;
						tmp.leftChild = tmp2;
					}
				}
				return true;
			}
			else if((((Comparable<K>)key).compareTo(n.key) < 0)){
				return(delete(key,n.leftChild,n,true));
			}
			else if((((Comparable<K>)key).compareTo(n.key) > 0)){
				return(delete(key,n.rightChild,n,false));
			}
			return false;
		}
	}
	
	public V getValue(K key) {
		return find(key, root);
	}
	
	public K getKey(V value) {
		Node<K,V> tmp = root;
		return findKey(value,tmp);
	}
	
	private K findKey(V value, Node<K,V> n) {
		K tmp;
		if (root == null) return null;
		if (n.leftChild != null){
			tmp = findKey(value,n.leftChild);
			if (tmp != null) return tmp;
		}
		if(((Comparable<V>)value).compareTo(n.value)==0){
			return n.key;
		}
		if (n.rightChild != null){
			tmp = findKey(value, n.rightChild);
			if (tmp != null) return tmp;
		}
		return null;
	}
	
	public int size() {
		return currentSize;
	}
	
	public boolean isFull() {
		return false;
	}
	
	public boolean isEmpty() {
		return (currentSize == 0);
	}
	
	public void clear() {
		root = null;
		currentSize = 0;
		modCounter = 0;
	}
	
	abstract class IteratorHelper<E> implements Iterator<E> {
		protected Node<K,V> [] nodes;
		protected int idx;
		protected long modCheck;
		private int j;
		private Node<K,V> n;
		
		public IteratorHelper() {
			modCheck = modCounter;
			n = root;
			nodes = new Node[currentSize];
			idx = 0;
			j = 0;
			modCheck = modCounter;
			walk(root);
		}
		
		public boolean hasNext() {
			if (modCheck != modCounter) 
				throw new ConcurrentModificationException();
			return idx < currentSize;
		}
		
		public abstract E next();
		
		public void remove(){
			
		}
		
		private void walk(Node n){
			if (root == null) return;
			if (n.leftChild != null) walk(n.leftChild);
			nodes[j++] = n;
			if (n.rightChild != null) walk(n.rightChild);
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
