package com.lhm.algorithms.stack;

public interface MyStack<T> {

	boolean isFull();
	
	boolean isEmpty();
	
	void push(T ele);
	
	T pop();
	
	T peek();
}
