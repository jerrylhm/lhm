package com.lhm.algorithms.linklist;

public class MySingleLinkList<T> implements MyLinkList<T>{

	private SingleLinkListNode head;
	
	public MySingleLinkList() {
		head = new SingleLinkListNode(null);
	}
	
	class SingleLinkListNode {
		private Object data;
		private SingleLinkListNode next;
		
		public SingleLinkListNode(Object data) {
			this.data = data;
		}
	}
	
	@Override
	public void add(T ele) {
		SingleLinkListNode endNode = head;
		while(endNode.next != null) {
			endNode = endNode.next;
		}
		SingleLinkListNode newNode = new SingleLinkListNode(ele);
		endNode.next = newNode;
	}

	@Override
	public void delete(int index) {
		int current_index = 0;
		int length = size();
		SingleLinkListNode temp = head;
		while(index >= 0 && index < length) {
			if(index == current_index) {
				temp.next = temp.next.next;
				break;
			}
			current_index++;
			temp = temp.next;
		}
		
	}

	@Override
	public void addByIndex(T ele, int index) {
		int current_index = 0;
		int length = size();
		SingleLinkListNode temp = head;
		while(index >= 0 && index <= length) {
			if(index == current_index) {
				SingleLinkListNode node = new SingleLinkListNode(ele);
				node.next = temp.next;
				temp.next = node;
				break;
			}
			current_index++;
			temp = temp.next;
		}
	}

	@Override
	public void print() {
		SingleLinkListNode node = head.next;
		while(node != null) {
			System.out.println(node.data);
			node = node.next;
		}
		
	}

	@Override
	public boolean isEmpty() {
		return head.next == null;
	}

	@Override
	public int size() {
		int size = 0;
		SingleLinkListNode temp = head;
		while(temp.next != null) {
			size++;
			temp = temp.next;
		}
		return size;
	}

	@Override
	public void sort() {
		for(int i=0;i<size();i++) {
			SingleLinkListNode temp = head;		
			while(temp.next != null) {
				SingleLinkListNode next = new SingleLinkListNode(temp.next.data);
				next.next = temp.next.next;
				
				if(next.next != null && (int)next.data > (int)next.next.data) {
					SingleLinkListNode next_next = new SingleLinkListNode(next.next.data);	
					next_next.next = next.next.next;
					
					next.next = next_next.next;
					temp.next = next_next;
					next_next.next = next;
				}
				temp = temp.next;
			}
		}
		
	}
	
	public static void main(String[] args) {
		MySingleLinkList<Integer> ls = new MySingleLinkList<>();
		ls.add(1);
		ls.add(3);
		ls.add(1);
		ls.addByIndex(5, 0);
		ls.addByIndex(1, 4);
		ls.sort();
		ls.print();
	}

}
