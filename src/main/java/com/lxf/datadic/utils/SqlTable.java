package com.lxf.datadic.utils;

import java.util.ArrayList;
import java.util.List;

public class SqlTable {

	private String title;// 表名称

	private String tableName;// 数据库表名


	private String[] fieldValues = null;// 表字段值
	private List<String[]> fieldList = new ArrayList<String[]>();

	public SqlTable(String title) {
		this.title = title;
	}

	private Integer rows;

	private Integer cols;

	private String[] values;

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }


	// 添加一行字段的内容
	public void addRowField(String[] rowField) {
		fieldList.add(rowField);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String[] getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(String[] fieldValues) {
		this.fieldValues = fieldValues;
	}

	public List<String[]> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<String[]> fieldList) {
		this.fieldList = fieldList;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


}
