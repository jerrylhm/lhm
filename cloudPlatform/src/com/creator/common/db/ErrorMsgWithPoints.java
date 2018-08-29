package com.creator.common.db;

/**
 * 用户导入的错误信息与出错行列坐标的封装类
 * @author ilongli
 *
 */
public class ErrorMsgWithPoints {

	private String errorMsg;	//错误信息
		
	private int row;			//行
	
	private int col;			//列

	public ErrorMsgWithPoints() {}

	public ErrorMsgWithPoints(String errorMsg, int row, int col) {
		this.errorMsg = errorMsg;
		this.row = row;
		this.col = col;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public String toString() {
		return "ErrorMsgWithPoints [errorMsg=" + errorMsg + ", row=" + row + ", col=" + col + "]";
	}
}
