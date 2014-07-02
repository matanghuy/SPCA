package spca.datalayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import spca.datalayer.impl.DefaultDataBaseContext;

public class SpcaDataLayerFactory {

//	private final static String LOG4J_CONFIG_FILE = "log4j.xml";
	private final static String PROPERTIES_CONFIG_FILE = "datalayer.properties";
	
	private final static String PROPERTY_URL = "db_url";
	private final static String PROPERTY_USERNAME = "db_username";
	private final static String PROPERTY_PASSWORD = "db_password";

	private static DataContext dataContext = null;

//	static {
//		org.apache.log4j.xml.DOMConfigurator.configure(LOG4J_CONFIG_FILE);
//	}

	private static Properties getProperties() throws IOException {
		Properties props = new Properties();

		try (InputStream is = SpcaDataLayerFactory.class.getResourceAsStream(PROPERTIES_CONFIG_FILE)) {
			props.load(is);
		}
		
//		try (InputStream is = new FileInputStream(new File(PROPERTIES_CONFIG_FILE))) {
//			props.load(is);
//		}
		
		return props;
	}

	public static DataContext getDataContext() throws IOException {

		if (dataContext == null) {
			Properties props = getProperties();
			String url = props.getProperty(PROPERTY_URL);
			String userName = props.getProperty(PROPERTY_USERNAME);
			String password = props.getProperty(PROPERTY_PASSWORD);

			dataContext = new DefaultDataBaseContext(url, userName, password);
		}

		return dataContext;
	}
}