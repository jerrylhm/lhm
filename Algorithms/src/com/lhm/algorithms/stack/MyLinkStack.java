package com.lhm.algorithms.stack;

public class MyLinkStack<T> implements MyStack<T>{

	private LinkStackNode node;
	private int size;
	
	public MyLinkStack() {
		node = null;
		size = 0;
	}
	
	class LinkStackNode {
		private T data;
		private LinkStackNode next;
	}
	
	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return node == null;
	}

	@Override
	public void push(T ele) {
		LinkStackNode newNode = new LinkStackNode();
		newNode.data = ele;
		newNode.next = node;
		node = newNode;
		size++;
	}

	@Override
	public T pop() {
		if(!isEmpty()) {
			Object result = node.data;
			node = node.next;
			return (T) result;
		}else {
			throw new RuntimeException("栈为空！");
		}

	}
	
	@Override
	public T peek() {
		if(!isEmpty()) {
			Object result = node.data;
			return (T) result;
		}else {
			throw new RuntimeException("栈为空！");
		}
	}
	
	public static void main(String[] args) {
		MyLinkStack<String> stack = new MyLinkStack<>();
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
