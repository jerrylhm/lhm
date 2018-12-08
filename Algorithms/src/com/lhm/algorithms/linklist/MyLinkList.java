package com.lhm.algorithms.linklist;

public interface MyLinkList<T> {

	void add(T ele);
	
	void delete(int index);
	
	void addByIndex(T ele, int index);
	
	void sort();
	
	void print();
	
	boolean isEmpty();
	
	int size();
}
