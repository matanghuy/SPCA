package spca.datalayer;

public interface DataRow {
	Object getObject(String colName);
	Object getObject(int colIndex);
	String getString(String colName);
	String getString(int colIndex);
}
