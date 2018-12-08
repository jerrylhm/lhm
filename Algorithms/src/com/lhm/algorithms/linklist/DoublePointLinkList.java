package com.lhm.algorithms.linklist;

import com.lhm.algorithms.linklist.MySingleLinkList.SingleLinkListNode;

public class DoublePointLinkList {

	class DoublePointLinkListNode {
		private Object data;
		private DoublePointLinkListNode next;
		
		public DoublePointLinkListNode(Object data) {
			this.data = data;
		}
	}
	
	private DoublePointLinkListNode head;
	private DoublePointLinkListNode tail;
	private int size;
	
	public DoublePointLinkList() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}
	
	public void addHead(Object obj){
		if(size == 0) {
			head = new DoublePointLinkListNode(obj);
			tail = head;
			size++;
		}else {
			DoublePointLinkListNode _head = new DoublePointLinkListNode(obj);
			_head.next = head;
			head = _head;
			size++;
		}
	}
	
	public void addTail(Object obj){
		if(size == 0) {
			head = new DoublePointLinkListNode(obj);
			tail = head;
			size++;
		}else {
			DoublePointLinkListNode _tail = new DoublePointLinkListNode(obj);
			tail.next = _tail;
			tail = _tail;
			size++;
		}
	}

	public void deleteHead() {
		if(size == 0) {
			throw new RuntimeException("链表为空，删除表头失败!");
		}else if(size == 1){
			head = null;
			tail = null;
		}else {
			head = head.next;
		}
		size--;
	}
	
	public void deleteTail() {
		if(size == 0) {
			throw new RuntimeException("链表为空，删除表尾失败!");
		}else if(size == 1){
			head = null;
			tail = null;
		}else {
			DoublePointLinkListNode node = head;
			int count = 1;
			while(count++ < size - 1) {
				node = node.next;			
			}
			node.next = null;
			tail = node;
		}
		size--;
	}
	
	public void print() {
		DoublePointLinkListNode node = head;
		while(node != null) {
			System.out.println(node.data);
			node = node.next;
		}
		
	}
	
	public boolean isEmpty() {
		return size <= 0;
	}
	
	public static void main(String[] args) {
		DoublePointLinkList ls = new DoublePointLinkList();
		ls.addHead(1);
		ls.addHead(2);
		ls.addTail(3);
		ls.addTail(4);
//		while(!ls.isEmpty()) {
//			ls.deleteHead();
//		}
		ls.deleteTail();
		ls.print();

	}
}
