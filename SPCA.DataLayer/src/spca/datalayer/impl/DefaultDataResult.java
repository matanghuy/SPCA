package spca.datalayer.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import spca.datalayer.DataResult;
import spca.datalayer.DataRow;

public class DefaultDataResult implements DataResult {

    private final Map<String, Integer> colIndices = new TreeMap<>(
            String.CASE_INSENSITIVE_ORDER);
    private final DataRow[] dataRowList;
    private final Object returnVal;

    public DefaultDataResult(CallableStatement cstat) throws SQLException {
        ArrayList<DataRow> rowList = new ArrayList<>();
        try (ResultSet rs = cstat.getResultSet()) {

            if (rs != null && rs.isClosed() == false) {
                ResultSetMetaData metadata = cstat.getMetaData();

                for (int i = 0; i < metadata.getColumnCount(); i++) {
                    String colName = metadata.getColumnName(i + 1);
                    colIndices.put(colName, i);
                }

                while (rs.next()) {
                    Object[] dataRow = new Object[colIndices.size()];

                    for (int i = 0; i < metadata.getColumnCount(); i++) {
                        dataRow[i] = rs.getObject(i + 1);
                    }

                    rowList.add(new DefaultDataRow(this, dataRow));
                }
            }

            returnVal = cstat.getObject(1);
        }
        
        dataRowList = rowList.toArray(new DataRow[rowList.size()]);
    }

    /* Internal use */
    Map<String, Integer> getColIndices() {
        return colIndices;
    }

    @Override
    public Object getReturnValue() {
        return returnVal;
    }

    @Override
    public DataRow[] getRows() {
        return dataRowList;
    }

    @Override
    public String[] getColumnNames() {
        Set<String> keys = colIndices.keySet();
        return keys.toArray(new String[keys.size()]);
    }
}
