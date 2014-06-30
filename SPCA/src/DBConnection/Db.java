package DBConnection;

import spca.datalayer.SpcaDataLayerFactory;

public class Db {

	SpcaDataLayerFactory layerFactory;
		private static Db instance = null;
		   protected Db() {
			   layerFactory = new SpcaDataLayerFactory();
		   }
		   public static Db getInstance() {
		      if(instance == null) {
		         instance = new Db();
		      }
		      return instance;
		   }
		}

