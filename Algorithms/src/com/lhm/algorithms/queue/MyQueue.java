package com.lhm.algorithms.queue;

public interface MyQueue<T> {
	
	void add(T ele);
	
	T peek();
	
	T poll();
	
	void clean();

}
