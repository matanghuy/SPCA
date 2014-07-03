package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import beans.Animal;
import beans.Contact;
import beans.TransactionType;
import org.apache.log4j.Logger;
import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;

import static spca.datalayer.SpcaDataLayerFactory.getDataContext;

public class CommonUtils {
    private static final Logger logger = Logger.getLogger(CommonUtils.class);
    public static Contact contact;
    public static Animal animal;
	public static HashMap<String,Integer> citiesMap = new HashMap<String,Integer>();
    private static DataContext dataContext;
    static {
        try {
            dataContext = getDataContext();
        } catch(IOException e) {
            logger.error("Cannot get data context", e);
        }
    }
	public static void initializeCity(){
		try {

			DataRow[]  data = dataContext.getCities().getRows();
			int size = data.length;
			for(int i=0;i<size;i++){
				String cityName = (String)data[i].getObject("Name");
				Integer id = (Integer)data[i].getObject("ID");
				citiesMap.put(cityName, id);
			}
            logger.info("Cities map has benn loaded");
		} catch (SQLException e) {
            logger.error("Failed to initiate cities map", e);
        }
	}
    public static List<TransactionType> getTransactionTypes() {
        ArrayList<TransactionType> types = new ArrayList<TransactionType>();
        try {
            DataResult result = dataContext.getTransactionType();
            DataRow[] rows = result.getRows();

            for (int i = 0; i < rows.length; i++) {
                types.add(new TransactionType(rows[i]));
            }
            logger.info("Transaction types has been loaded");
            return types;
        } catch (SQLException e) {
            logger.error("failed to get transaction types from database", e);
        }
        return null;
    }

}
