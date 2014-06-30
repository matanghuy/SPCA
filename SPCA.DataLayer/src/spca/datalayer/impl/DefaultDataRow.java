package spca.datalayer.impl;

import spca.datalayer.DataRow;

public class DefaultDataRow implements DataRow {

	private final DefaultDataResult parent;
	private final Object[] dataArray;

	public DefaultDataRow(DefaultDataResult parent, Object[] dataArray) {
		this.parent = parent;
		this.dataArray = dataArray;
	}

	@Override
	public Object getObject(String colName) {
		Integer idx = parent.getColIndices().get(colName);

		if (idx == null)
			throw new IllegalArgumentException("Column name does not exist");

		return dataArray[idx];
	}

	@Override
	public Object getObject(int colIndex) {

		if (colIndex < 0 || colIndex >= dataArray.length)
			throw new IllegalArgumentException("Column index does not exist");

		return dataArray[colIndex];
	}

	@Override
	public String getString(String colName) {

		Object obj = getObject(colName);
		return obj != null ? obj.toString() : null;
	}

	@Override
	public String getString(int colIndex) {
		
		Object obj = getObject(colIndex);
		return obj != null ? obj.toString() : null;
	}

}
