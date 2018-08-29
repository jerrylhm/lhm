package com.creator.common.db;

import java.util.List;

/**
 * Excel数据全部处理结束后的结果集
 * @author ilongli
 *
 */
public class ExcelDataResult<T> {
	
	//原始Excel数据
	List<String[]> readExcel;
	//处理结束后的实体集合
	private List<T> entityList;
	//处理过程中产生的错误集合
	private List<ErrorMsgWithPoints> errorList;
	
	public ExcelDataResult() {}

	public ExcelDataResult(List<String[]> readExcel, List<T> entityList, List<ErrorMsgWithPoints> errorList) {
		this.readExcel = readExcel;
		this.entityList = entityList;
		this.errorList = errorList;
	}
	
	public List<String[]> getReadExcel() {
		return readExcel;
	}
	public void setReadExcel(List<String[]> readExcel) {
		this.readExcel = readExcel;
	}
	public List<T> getEntityList() {
		return entityList;
	}
	public void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}
	public List<ErrorMsgWithPoints> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<ErrorMsgWithPoints> errorList) {
		this.errorList = errorList;
	}

	@Override
	public String toString() {
		return "ExcelDataResult [readExcel=" + readExcel + ", entityList=" + entityList + ", errorList=" + errorList
				+ "]";
	}
}
