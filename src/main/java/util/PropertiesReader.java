package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    public static final String PROD_URL;
    public static final String CREATE_USER_PATH;
    public static final String AUTHORIZE_USER_PATH;
    public static final String LOGIN_USER_PATH;
    public static final String CREATE_ORDER_PATH;

    static {
        Properties properties = new Properties();
        InputStream propertiesFile = PropertiesReader.class.getClassLoader().getResourceAsStream("api.properties");

        try {
            properties.load(propertiesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PROD_URL = properties.getProperty("domain.production");
        CREATE_USER_PATH = properties.getProperty("path.create.user");
        AUTHORIZE_USER_PATH = properties.getProperty("path.authorize.user");
        LOGIN_USER_PATH = properties.getProperty("path.login.user");
        CREATE_ORDER_PATH = properties.getProperty("path.create.order");
    }
}
