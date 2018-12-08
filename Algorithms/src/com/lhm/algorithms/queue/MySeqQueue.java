package com.lhm.algorithms.queue;

public class MySeqQueue<T> implements MyQueue<T>{

	private int font;
	private int read;
	private Object[] data;
	private int size;

	public MySeqQueue(int size) {
		super();
		this.font = 0;
		this.read = 0;
		this.data = new Object[size];
		this.size = 0;
	}

	
	
	public int getFont() {
		return font;
	}



	public void setFont(int font) {
		this.font = font;
	}



	public int getRead() {
		return read;
	}



	public void setRead(int read) {
		this.read = read;
	}



	public Object[] getData() {
		return data;
	}



	public void setData(Object[] data) {
		this.data = data;
	}



	public int getSize() {
		return size;
	}



	public void setSize(int size) {
		this.size = size;
	}



	@Override
	public void add(T ele) {
		if(size < data.length) {
			read = isEmpty()?read:read + 1;
			data[read] = ele;
			size++;
		}else {
			throw new RuntimeException("队列已满，无法添加元素。");
		}
		
	}

	@Override
	public T peek() {
		if(!isEmpty()) {
			return (T) data[font];
		}else {
			throw new RuntimeException("队列为空。");
		}
	}

	@Override
	public T poll() {
		if(!isEmpty()) {
			T result = (T) data[font];
			if(font != read) {
				font = font + 1;
			}
			size--;
			return result;
		}else {
			throw new RuntimeException("队列为空。");
		}
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEmpty() {
		return size == 0;
	}
	
	public static void main(String[] args) {
		MySeqQueue<String> seqQueue = new MySeqQueue<>(10);
		seqQueue.add("A");
		System.out.println(seqQueue.getFont() + "," + seqQueue.getRead() + ":" + seqQueue.getSize());
		System.out.println(seqQueue.peek());
		System.out.println(seqQueue.getFont() + "," + seqQueue.getRead() + ":" + seqQueue.getSize());
		System.out.println(seqQueue.poll());
		System.out.println(seqQueue.getFont() + "," + seqQueue.getRead() + ":" + seqQueue.getSize());
		seqQueue.add("B");
		System.out.println(seqQueue.getFont() + "," + seqQueue.getRead() + ":" + seqQueue.getSize());
		seqQueue.add("C");
		System.out.println(seqQueue.getFont() + "," + seqQueue.getRead() + ":" + seqQueue.getSize());
		System.out.println(seqQueue.peek());
		System.out.println(seqQueue.poll());
		System.out.println(seqQueue.getFont() + "," + seqQueue.getRead() + ":" + seqQueue.getSize());
		seqQueue.add("D");
		seqQueue.add("E");
		seqQueue.add("F");
		seqQueue.add("G");
		seqQueue.add("H");
		seqQueue.add("I");
		seqQueue.add("J");
		seqQueue.add("K");
		System.out.println(seqQueue.peek());
		System.out.println(seqQueue.getFont() + "," + seqQueue.getRead() + ":" + seqQueue.getSize());
		
		while(!seqQueue.isEmpty()) {
			System.out.println(seqQueue.poll());
		}
	}
}
