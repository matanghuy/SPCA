package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import spca.datalayer.DataContext;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;

public class SharedMethodes {
	
	public static HashMap<String,Integer> city  = new HashMap<String,Integer>();
	
	public static void intializeCity(){
		
		try {
			
			DataContext layerFactory;
			layerFactory = SpcaDataLayerFactory.getDataContext();
			
			
			DataRow[]  data = layerFactory.getCities().getRows();
			int size = data.length;
			for(int i=0;i<size;i++){
				System.out.println(data[i].getObject("ID"));
				System.out.println(data[i].getObject("Name"));
				
				String cityName = (String)data[i].getObject("Name");
				Integer id = (Integer)data[i].getObject("ID");
				
				city.put(cityName, id);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isOk(Object obj,int id,String howToHandle){
		return false;
	}

}
