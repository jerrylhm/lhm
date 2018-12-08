package com.lhm.algorithms.stack;

public class MySeqStack<T> implements MyStack<T>{

	private Object[] data;
	private int len;
	private int index;
	
	public MySeqStack(int len) {
		super();
		this.data = new Object[len];
		this.len = len;
		this.index = -1;
	}

	@Override
	public boolean isFull() {
		return index == len - 1;
	}

	@Override
	public boolean isEmpty() {
		return index < 0;
	}

	@Override
	public void push(T ele) {
		if(!isFull()) {
			data[++index] = ele;
		}else {
			throw new RuntimeException("栈已满");
		}
	}

	@Override
	public T pop() {
		if(!isEmpty()) {
			return (T) data[index--];
		}else {
			throw new RuntimeException("栈为空");
		}
	}
	
	@Override
	public T peek() {
		if(!isEmpty()) {
			return (T) data[index];
		}else {
			throw new RuntimeException("栈为空");
		}
	}
	
	public static void main(String[] args) {
		MySeqStack<String> stack = new MySeqStack<>(10);
		stack.push("A");
		stack.push("B");
		stack.push("C");
		stack.push("D");
		stack.push("E");
		stack.push("F");
		stack.push("G");
		stack.push("H");
		stack.push("I");
		stack.push("J");
		
		while(!stack.isEmpty()) {
			System.out.println(stack.pop());
		}
	}


}
