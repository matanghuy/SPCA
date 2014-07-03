package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import beans.Animal;
import beans.Contact;
import beans.TransactionType;
import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;

import static spca.datalayer.SpcaDataLayerFactory.getDataContext;

public class CommonUtils {

    public static Contact contact;
    public static Animal animal;
	public static HashMap<String,Integer> citiesMap = new HashMap<String,Integer>();
    private static DataContext dataContext;
    static {
        try {
            dataContext = getDataContext();
        } catch(IOException e) {
            System.out.println("Cannot get data context");
        }
    }
	public static void initializeCity(){
		try {

			DataRow[]  data = dataContext.getCities().getRows();
			int size = data.length;
			for(int i=0;i<size;i++){
				System.out.println(data[i].getObject("ID"));
				System.out.println(data[i].getObject("Name"));

				String cityName = (String)data[i].getObject("Name");
				Integer id = (Integer)data[i].getObject("ID");

				citiesMap.put(cityName, id);
			}
		} catch (SQLException e) {
            e.printStackTrace();
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
            return types;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void initTypes() {
//        try {
//            DataResult result = dataContext.getTransactionType();
//            result.
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
