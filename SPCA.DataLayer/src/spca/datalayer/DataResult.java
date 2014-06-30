package spca.datalayer;

public interface DataResult {
	Object getReturnValue();
	DataRow[] getRows();
	String[] getColumnNames();
}
