package com.lhm.algorithms.linklist;

import com.lhm.algorithms.linklist.MySingleLinkList.SingleLinkListNode;

public class OrderLinkList {
	private OrderLinkListNode head;
	
	public OrderLinkList() {
		head = null;
	}
	
	class OrderLinkListNode {
		private int data;
		private OrderLinkListNode next;
		
		public OrderLinkListNode(int data) {
			this.data = data;
		}
	}
	
	public void add(int data) {
		OrderLinkListNode node = head;
		OrderLinkListNode pre = null;
		OrderLinkListNode current = new OrderLinkListNode(data);
		while(node != null && node.data < data) {
			pre = node;
			node = node.next;
		}
		if(pre == null) {
			head = current;
			head.next = node;
		}else {
			pre.next = current;
			current.next = node;
		}

	}
	
	public void deleteHead() {
		OrderLinkListNode node = head;
		if(head != null) {
			head = node.next;
		}	

	}
	
	public void print() {
		OrderLinkListNode node = head;
		while(node != null) {
			System.out.println(node.data);
			node = node.next;
		}
		
	}
	
	public static void main(String[] args) {
		OrderLinkList ls = new OrderLinkList();
		ls.add(5);
		ls.add(4);
		ls.add(11);
		ls.add(2);
		ls.deleteHead();
		ls.print();
	}
}
