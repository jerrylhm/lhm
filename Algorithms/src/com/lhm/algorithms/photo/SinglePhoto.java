package com.lhm.algorithms.photo;

public class SinglePhoto {

	private static int targetCount = 0;
	
	class currentItem {
		int i;
		int j;
		String value;
		
		public currentItem(int i, int j, String value) {
			this.i = i;
			this.j = j;
			this.value = value;
		}

		@Override
		public String toString() {
			return "currentItem [i=" + i + ", j=" + j + ", value=" + value + "]";
		}
		
		
	}
	
	public static String SingleLink(String[][] data, String[] target) {
		int[][] _data = initDataBak(data);
		int[][] _head = initDataBak(data);
		
		
		currentItem item = null;
		for (int i=0;i<_head.length;i++) {
			for (int j=0;j<_head[i].length;j++) {
				if(item == null) {
					item = doLink(data, _data, i, j, target);			
				}
				if(item != null) {
					_head[i][j] = 0;
					_data[i][j] = 0;
					targetCount = targetCount ++;
					break;
				}
			}
			if(item != null) {
				break;
			}
		}
		
		String result = autoBackHandle(item, data, _data, target);
		
		for (int i=0;i<_data.length;i++) {
			for (int j=0;j<_data[i].length;j++) {
				System.out.print(_data[i][j] + " ");
			}
			System.out.println("");
		}
		return result;
	}
	
	public static String autoBackHandle(currentItem item, String[][] data, int[][] _data, String[] target) {
		String result = "";
		while(item != null) {
			result += item.value;
			item = doLink(data, _data, item.i, item.j, target);
		}
		
		if(targetCount < target.length) {
			if(targetCount > 0) {
				targetCount--;
				//TODO
			}
		}
		
		return result;
	}
	
	public static currentItem doLink(String[][] data, int[][] _data, int i, int j, String[] target) {
		for(int k=0;k<=4;k++) {
			String linkItem = null;
			int currentI = i;
			int currentJ = j;
			switch (k) {
			case 0:
				break;
			case 1:
				currentI = i;
				currentJ = j-1;
				break;
			case 2:
				currentI = i-1;
				currentJ = j;
				break;
			case 3:
				currentI = i;
				currentJ = j+1;
				break;
			case 4:
				currentI = i+1;
				currentJ = j;
				break;
			}	
			linkItem = linkHandle(data, _data, currentI, currentJ, target, k);
			if(linkItem != null) {
//				System.out.println(data[currentI][currentJ] + "," + _data[currentI][currentJ] + "," + currentI + "," + currentJ + "," + targetCount);
				return new SinglePhoto().new currentItem(currentI, currentJ, linkItem);
			}	
		}
		return null;
	}
	
	public static boolean hasNeighbor(int[][] _data, int i, int j) {
		for(int k=1;k<=4;k++) {
			switch (k) {
			case 1:
				if(validRange(i, j-1, _data) && _data[i][j-1] != -1) {
					return true;
				}
				break;
			case 2:
				if(validRange(i-1, j, _data) &&_data[i-1][j] != -1) {
					return true;
				}
				break;
			case 3:
				if(validRange(i, j+1, _data) &&_data[i][j+1] != -1) {
					return true;
				}
				break;
			case 4:
				if(validRange(i+1, j, _data) &&_data[i+1][j] != -1) {
					return true;
				}
				break;
			}	
		}
		return false;
	}
	
	public static boolean validRange(int i, int j, int[][] data) {
		if(i < 0 || j < 0 || i >= data.length || j >= data[0].length) {
			return false;
		}else {
			return true;
		}
	}
	
	public static String linkHandle(String[][] data, int[][] _data, int i, int j, String[] target, int bakCount) {
		String linkItem = null;
		if(targetCount >= target.length || i < 0 || j < 0 || i >= data.length || j >= data[0].length) {
			return null;
		}
		linkItem = data[i][j];
		if(linkItem == target[targetCount] && _data[i][j] == -1) {
			_data[i][j] = bakCount;
			if(targetCount < target.length) {
				targetCount++;
			}
			return linkItem;
		}
		
		return null;
	}
	
	public static int[][] initDataBak(String[][] data) {
		int[][] _data = new int[data.length][data[0].length];
		for (int i=0;i<data.length;i++) {
			for (int j=0;j<data[i].length;j++) {
				_data[i][j] = -1;
			}
		}
		return _data;
	}
	
	public static void main(String[] args) {
		String[][] data = {{"A","B","C","E"},{"F","C","B","D"},{"A","B","C","E"}};
		String[] target = {"A","B","C","B","C","E"};
		int[][] _data = {{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
//		SingleLink(data, target);
		
		System.out.println(SingleLink(data, target));
		
	}
}
